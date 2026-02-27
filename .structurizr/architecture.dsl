workspace "El Almacén de Películas Online - Microservicio Descuentos" "Arquitectura C4 completa del vertical de descuentos con integración Keycloak y RabbitMQ" {

    model {
        # ========================================
        # ACTORES DEL SISTEMA
        # ========================================
        cliente = person "Cliente" "Usuario final que consulta películas y aplica cupones de descuento en sus compras. Solo puede ver descuentos vigentes." {
            tags "Cliente"
        }

        admin = person "Administrador" "Usuario con privilegios elevados que gestiona el catálogo completo de cupones: crear, modificar, listar todos (vigentes y expirados)." {
            tags "Admin"
        }

        # ========================================
        # SISTEMAS EXTERNOS
        # ========================================
        keycloak = softwareSystem "Keycloak" "Sistema de Identity and Access Management (IAM) que gestiona autenticación OAuth2/OIDC y autorización basada en roles (ADMIN, CLIENTE)." {
            tags "External System" "Security"
        }

        frontend = softwareSystem "Frontend React Vite" "Single Page Application (SPA) desarrollada en React con Vite que proporciona la interfaz de usuario para gestionar y consultar descuentos." {
            tags "External System" "Frontend"
        }

        apiGateway = softwareSystem "API Gateway" "Spring Cloud Gateway que actúa como punto de entrada único al ecosistema de microservicios. Valida tokens JWT con Keycloak y enruta peticiones a los servicios correspondientes." {
            tags "External System" "Gateway"
        }

        ventasSystem = softwareSystem "Microservicio Ventas" "Vertical de negocio que gestiona el carrito de compras, órdenes de compra y transacciones. Solicita validación de cupones vía RabbitMQ RPC." {
            tags "External System" "Microservice"
        }

        # ========================================
        # NUESTRO SISTEMA: MICROSERVICIO DESCUENTOS
        # ========================================
        descuentosSystem = softwareSystem "Microservicio Descuentos" "Sistema responsable de la gestión completa del ciclo de vida de cupones de descuento: creación, validación, consulta y auditoría." {
            tags "Internal System"

            # ----------------------------------------
            # INFRAESTRUCTURA
            # ----------------------------------------
            baseDatos = container "Base de Datos MySQL" "Base de datos relacional que persiste cupones con sus atributos (nombre, porcentaje, fechas de vigencia) y auditoría de validaciones." "MySQL 8.0" {
                tags "Database"
            }

            rabbitMQ = container "RabbitMQ Message Broker" "Sistema de mensajería que facilita comunicación asíncrona RPC entre el microservicio de Ventas y Descuentos." "RabbitMQ 3.x con plugin Management" {
                tags "Message Broker"
            }

            # ----------------------------------------
            # APLICACIÓN SPRING BOOT
            # ----------------------------------------
            springApp = container "Descuentos Application" "Aplicación Spring Boot que expone API REST y consume mensajes RabbitMQ. Contiene toda la lógica de negocio del dominio de descuentos." "Java 17, Spring Boot 3.4.2, Spring Data JPA, Spring AMQP" {
                tags "Spring Boot"

                # === CAPA DE SEGURIDAD ===
                securityConfig = component "SecurityConfig" "Configuración de Spring Security que establece filtros OAuth2 Resource Server, valida tokens JWT contra Keycloak y define reglas de autorización por rol." "Spring Security 6, OAuth2 Resource Server" {
                    tags "Security Component"
                }

                jwtDecoder = component "JwtDecoder" "Bean que decodifica y valida la firma de tokens JWT usando las claves públicas de Keycloak (JWK Set)." "Spring Security OAuth2" {
                    tags "Security Component"
                }

                # === CAPA DE API REST ===
                descuentoController = component "DescuentoController" "REST Controller que expone endpoints HTTP para gestión de cupones. Protegido con anotaciones @PreAuthorize según roles." "Spring MVC @RestController" {
                    tags "Controller"
                    description "GET /descuentos -> hasAnyRole('ADMIN','CLIENTE') | POST /descuentos -> hasRole('ADMIN')"
                }

                # === CAPA DE SERVICIO (LÓGICA DE NEGOCIO) ===
                cuponService = component "CuponService" "Servicio que implementa reglas de negocio: validación de vigencia por fechas, cálculo de porcentajes, verificación de existencia de cupones." "Spring @Service" {
                    tags "Service"
                }

                # === CAPA DE DOMINIO ===
                cuponModel = component "Cupon (Domain Model)" "Clase de dominio que encapsula la entidad de negocio con métodos de validación (estaVigente, esValido)." "Java Record/POJO" {
                    tags "Domain Model"
                }

                # === CAPA DE PERSISTENCIA ===
                cuponRepository = component "CuponRepository" "Repositorio Spring Data JPA que proporciona operaciones CRUD y queries personalizadas (findByNombre, findByFechaFinAfter)." "Spring Data JPA Repository" {
                    tags "Repository"
                }

                cuponEntity = component "CuponEntity" "Entidad JPA que mapea la tabla 'cupon' en MySQL. Contiene anotaciones @Entity, @Table, @Id, @Column, validaciones JSR-303." "JPA @Entity" {
                    tags "Entity"
                }

                # === CAPA DE DTOs ===
                cuponDTO = component "CuponDTO" "Data Transfer Object para exponer cupones en la API REST sin exponer detalles internos de la entidad." "Java Record" {
                    tags "DTO"
                }

                validarCuponRequest = component "ValidarCuponRequest" "DTO de entrada para validación de cupones vía RabbitMQ. Contiene el código del cupón a validar." "Java Record" {
                    tags "DTO"
                }

                validarCuponResponse = component "ValidarCuponResponse" "DTO de respuesta para validación RPC. Indica si el cupón es válido, el porcentaje de descuento y mensaje descriptivo." "Java Record" {
                    tags "DTO"
                }

                # === CAPA DE EVENTOS (RabbitMQ) ===
                validarCuponRpcListener = component "ValidarCuponRpcListener" "Listener anotado con @RabbitListener que consume mensajes RPC de la cola 'validar-cupon-queue'. Procesa solicitudes de validación desde Ventas y responde sincrónicamente." "Spring AMQP @RabbitListener" {
                    tags "Event Listener"
                }

                rabbitConfig = component "RabbitMQConfig" "Configuración de colas, exchanges y bindings de RabbitMQ. Define la topología de mensajería (DirectExchange, Queue, RoutingKey)." "Spring AMQP @Configuration" {
                    tags "Configuration"
                }

                # === CAPA DE CONFIGURACIÓN ===
                applicationConfig = component "ApplicationConfig" "Configuración general de la aplicación: DataSource, EntityManagerFactory, TransactionManager, properties de conexión a MySQL." "Spring @Configuration" {
                    tags "Configuration"
                }

                # ----------------------------------------
                # RELACIONES ENTRE COMPONENTES
                # ----------------------------------------

                # Flujo de petición REST
                descuentoController -> securityConfig "Protegido por"
                descuentoController -> jwtDecoder "Valida token con"
                descuentoController -> cuponService "Delega lógica de negocio"
                descuentoController -> cuponDTO "Serializa respuesta con"

                # Lógica de negocio
                cuponService -> cuponModel "Trabaja con"
                cuponService -> cuponRepository "Consulta datos con"
                cuponService -> cuponDTO "Convierte a"

                # Persistencia
                cuponRepository -> cuponEntity "Mapea"
                cuponRepository -> baseDatos "Lee/Escribe con JDBC"
                cuponEntity -> baseDatos "Persiste en"

                # Flujo RabbitMQ
                validarCuponRpcListener -> rabbitMQ "Consume mensajes de"
                validarCuponRpcListener -> validarCuponRequest "Deserializa mensaje a"
                validarCuponRpcListener -> cuponService "Valida cupón con"
                validarCuponRpcListener -> validarCuponResponse "Serializa respuesta a"
                validarCuponRpcListener -> rabbitMQ "Publica respuesta RPC a"

                # Configuración
                rabbitConfig -> rabbitMQ "Configura topología en"
                securityConfig -> keycloak "Valida tokens contra"
                applicationConfig -> baseDatos "Configura conexión a"
            }
        }

        # ========================================
        # RELACIONES ENTRE SISTEMAS
        # ========================================

        # Flujo de autenticación
        cliente -> frontend "Accede a través del navegador"
        admin -> frontend "Administra cupones desde"
        frontend -> keycloak "1. Autentica usuario (POST /auth/realms/almacen/protocol/openid-connect/token)"
        keycloak -> frontend "2. Retorna access_token JWT con roles"

        # Flujo de peticiones REST
        frontend -> apiGateway "3. Envía peticiones HTTP con Bearer token"
        apiGateway -> keycloak "4. Valida token JWT y verifica firma"
        apiGateway -> springApp "5. Enruta a /api/descuentos/** si autorizado"
        springApp -> baseDatos "6. Persiste/consulta datos"

        # Flujo RabbitMQ RPC
        ventasSystem -> rabbitMQ "Publica mensaje RPC a 'validar-cupon-queue'"
        rabbitMQ -> springApp "Entrega mensaje al listener"
        springApp -> rabbitMQ "Publica respuesta a 'replyTo' queue"
        rabbitMQ -> ventasSystem "Entrega respuesta RPC"
    }

    # ========================================
    # VISTAS DEL MODELO C4
    # ========================================
    views {
        # ----------------------------------------
        # NIVEL 1: CONTEXTO DEL SISTEMA
        # ----------------------------------------
        # Muestra el sistema como "caja negra" y sus relaciones externas
        systemContext descuentosSystem "SystemContext" "Vista de contexto: El microservicio de Descuentos como caja negra interactuando con usuarios (Cliente, Admin) y sistemas externos (Keycloak, API Gateway, Frontend, Ventas)." {
            include *
            # Excluimos los contenedores internos para mantener la abstracción
            exclude "relationship.tag==Internal"
            autoLayout tb 150 150
        }

        # ----------------------------------------
        # NIVEL 2: CONTENEDORES
        # ----------------------------------------
        # Desglosa el sistema en aplicaciones/procesos independientes
        # Útil para DevOps: muestra qué se despliega, dónde y cómo se comunican
        container descuentosSystem "Containers" "Vista de contenedores: Desglosa el microservicio de Descuentos en aplicaciones independientes desplegables. Muestra la aplicación Spring Boot, la base de datos MySQL y el broker RabbitMQ, incluyendo sistemas externos relevantes (Keycloak para seguridad, Ventas para RPC)." {
            include *
            # Incluimos usuarios y sistemas externos clave para entender el contexto
            include cliente admin keycloak apiGateway ventasSystem
            autoLayout tb 150 150
        }

        # ----------------------------------------
        # NIVEL 3: COMPONENTES
        # ----------------------------------------
        # Dentro de un contenedor específico, muestra cómo se organizan los paquetes/módulos
        # Útil para desarrolladores: arquitectura interna de código
        component springApp "Components" "Vista de componentes: Arquitectura interna de la aplicación Spring Boot organizada en capas. Muestra cómo se estructuran los paquetes: Controller (API REST), Service (lógica de negocio), Repository (acceso a datos), Entity (modelo de persistencia), DTO (transferencia de datos), Event Listener (consumidores RabbitMQ) y Configuration (seguridad y beans)." {
            include *
            # Incluimos la base de datos y RabbitMQ para ver las conexiones salientes
            include baseDatos rabbitMQ keycloak
            autoLayout tb 200 150
        }

        # ----------------------------------------
        # VISTAS DINÁMICAS (SECUENCIAS)
        # ----------------------------------------

        # Flujo 1: Cliente consulta descuentos vigentes
        dynamic descuentosSystem "ConsultaDescuentosVigentes" "Secuencia de consulta de descuentos por un cliente autenticado" {
            cliente -> frontend "1. Accede a /descuentos"
            frontend -> keycloak "2. Solicita token (credenciales)"
            keycloak -> frontend "3. Retorna JWT (rol: CLIENTE)"
            frontend -> apiGateway "4. GET /api/descuentos [Authorization: Bearer {token}]"
            apiGateway -> keycloak "5. Valida token JWT"
            apiGateway -> springApp "6. Proxy a /descuentos"
            springApp -> descuentoController "7. Procesa petición (verifica rol)"
            descuentoController -> cuponService "8. listarCuponesVigentes()"
            cuponService -> cuponRepository "9. findByFechaFinAfter(LocalDate.now())"
            cuponRepository -> baseDatos "10. SELECT * FROM cupon WHERE fecha_fin >= CURDATE()"
            baseDatos -> cuponRepository "11. Retorna [CuponEntity]"
            cuponRepository -> cuponService "12. Retorna lista de entidades"
            cuponService -> cuponDTO "13. Mapea a List<CuponDTO>"
            cuponService -> descuentoController "14. Retorna DTOs"
            descuentoController -> springApp "15. ResponseEntity<List<CuponDTO>>"
            springApp -> apiGateway "16. HTTP 200 OK + JSON"
            apiGateway -> frontend "17. Retorna respuesta"
            frontend -> cliente "18. Muestra cupones aplicables"
            autoLayout lr
        }

        # Flujo 2: Admin crea nuevo cupón
        dynamic descuentosSystem "CreacionCupon" "Secuencia de creación de un cupón por un administrador" {
            admin -> frontend "1. Rellena formulario de cupón"
            frontend -> keycloak "2. Solicita token (credenciales admin)"
            keycloak -> frontend "3. Retorna JWT (rol: ADMIN)"
            frontend -> apiGateway "4. POST /api/descuentos [Bearer token + JSON body]"
            apiGateway -> keycloak "5. Valida token y verifica rol ADMIN"
            apiGateway -> springApp "6. Proxy a POST /descuentos"
            springApp -> descuentoController "7. @PreAuthorize('ADMIN') crearCupon(dto)"
            descuentoController -> cuponService "8. crearCupon(dto)"
            cuponService -> cuponModel "9. Valida reglas de negocio (fechas, porcentaje)"
            cuponService -> cuponRepository "10. save(cuponEntity)"
            cuponRepository -> baseDatos "11. INSERT INTO cupon VALUES(...)"
            baseDatos -> cuponRepository "12. Retorna ID generado"
            cuponRepository -> cuponService "13. Retorna entidad persistida"
            cuponService -> cuponDTO "14. Convierte a DTO"
            cuponService -> descuentoController "15. Retorna DTO"
            descuentoController -> springApp "16. HTTP 201 Created + Location header"
            springApp -> apiGateway "17. Respuesta exitosa"
            apiGateway -> frontend "18. JSON del cupón creado"
            frontend -> admin "19. Confirmación visual"
            autoLayout lr
        }

        # Flujo 3: Validación RPC desde Ventas
        dynamic descuentosSystem "ValidacionRPC" "Secuencia de validación de cupón solicitada por el microservicio de Ventas vía RabbitMQ RPC" {
            ventasSystem -> rabbitMQ "1. Publica mensaje RPC [queue: validar-cupon-queue, replyTo: amq.rabbitmq.reply-to]"
            rabbitMQ -> springApp "2. Entrega mensaje al @RabbitListener"
            springApp -> validarCuponRpcListener "3. Deserializa ValidarCuponRequest"
            validarCuponRpcListener -> cuponService "4. validarCupon(codigoCupon)"
            cuponService -> cuponRepository "5. findByNombre(codigo)"
            cuponRepository -> baseDatos "6. SELECT * FROM cupon WHERE nombre = ?"
            baseDatos -> cuponRepository "7. Retorna CuponEntity | null"
            cuponRepository -> cuponService "8. Optional<CuponEntity>"
            cuponService -> cuponModel "9. cupon.estaVigente(LocalDate.now())"
            cuponModel -> cuponService "10. Retorna booleano + porcentaje"
            cuponService -> validarCuponRpcListener "11. Retorna resultado de validación"
            validarCuponRpcListener -> validarCuponResponse "12. Construye DTO de respuesta"
            validarCuponRpcListener -> springApp "13. Retorna response"
            springApp -> rabbitMQ "14. Publica a replyTo queue [correlationId]"
            rabbitMQ -> ventasSystem "15. Entrega respuesta RPC"
            ventasSystem -> ventasSystem "16. Aplica descuento al carrito si válido"
            autoLayout lr
        }

        # ----------------------------------------
        # ESTILOS VISUALES
        # ----------------------------------------
        styles {
            element "Element" {
                shape RoundedBox
                background #dddddd
                color #000000
            }

            element "Person" {
                shape Person
                background #08427b
                color #ffffff
            }

            element "Cliente" {
                background #3498db
                color #ffffff
            }

            element "Admin" {
                background #e67e22
                color #ffffff
            }

            element "Software System" {
                background #1168bd
                color #ffffff
            }

            element "External System" {
                background #999999
                color #ffffff
            }

            element "Security" {
                background #2ecc71
                color #ffffff
            }

            element "Frontend" {
                background #9b59b6
                color #ffffff
            }

            element "Gateway" {
                background #34495e
                color #ffffff
            }

            element "Microservice" {
                background #16a085
                color #ffffff
            }

            element "Internal System" {
                background #2980b9
                color #ffffff
            }

            element "Container" {
                background #438dd5
                color #ffffff
            }

            element "Database" {
                shape Cylinder
                background #f39c12
                color #000000
            }

            element "Message Broker" {
                shape Pipe
                background #e74c3c
                color #ffffff
            }

            element "Spring Boot" {
                background #6db33f
                color #ffffff
            }

            element "Component" {
                background #85bbf0
                color #000000
            }

            element "Controller" {
                background #3498db
                color #ffffff
            }

            element "Service" {
                background #9b59b6
                color #ffffff
            }

            element "Repository" {
                background #e67e22
                color #ffffff
            }

            element "Entity" {
                background #e74c3c
                color #ffffff
            }

            element "Domain Model" {
                background #1abc9c
                color #ffffff
            }

            element "DTO" {
                background #95a5a6
                color #ffffff
            }

            element "Event Listener" {
                background #f39c12
                color #000000
            }

            element "Configuration" {
                background #34495e
                color #ffffff
            }

            element "Security Component" {
                background #27ae60
                color #ffffff
            }

            relationship "Relationship" {
                color #707070
                style solid
                thickness 2
            }
        }

        # ----------------------------------------
        # TERMINOLOGÍA PERSONALIZADA
        # ----------------------------------------
        terminology {
            person "Actor"
            softwareSystem "Sistema"
            container "Contenedor"
            component "Componente"
            deploymentNode "Nodo"
            infrastructureNode "Infraestructura"
            relationship "Relación"
        }
    }
}