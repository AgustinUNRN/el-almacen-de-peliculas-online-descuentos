package unrn.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange descuentosExchange(@Value("${rabbitmq.descuentos.exchange}") String name) {
        return new TopicExchange(name, true, false);
    }

    @Bean
    public Queue validarCuponQueue(@Value("${rabbitmq.descuentos.cupon.validar.queue}") String name) {
        return QueueBuilder.durable(name).build();
    }

    @Bean
    public Binding validarCuponBinding(
            Queue validarCuponQueue,
            TopicExchange descuentosExchange,
            @Value("${rabbitmq.descuentos.cupon.validar.routing-key}") String routingKey) {
        return BindingBuilder.bind(validarCuponQueue).to(descuentosExchange).with(routingKey);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.findAndRegisterModules();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        // Para permitir deserializar los DTOs del package unrn.dto...
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("unrn.dto", "java.util", "java.lang");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }
}