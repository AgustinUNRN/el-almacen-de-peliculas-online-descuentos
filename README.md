# 🎟️ Microservicio de Descuentos - El Almacén de Películas Online

Microservicio REST dedicado a la gestión y validación de cupones de descuento para la plataforma de alquiler de películas online.

## 📋 Descripción

Este vertical forma parte de una arquitectura de microservicios y se encarga exclusivamente de:
- Validar cupones de descuento por código
- Gestionar la creación de nuevos cupones
- Listar cupones disponibles
- Verificar vigencia temporal de descuentos

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una **arquitectura hexagonal** (puertos y adaptadores) organizada en capas:

```
src/main/java/unrn/
│
├── api/                           # 🌐 Capa de presentación (REST Controllers)
│   └── DescuentoController.java   # Endpoints HTTP
│
├── service/                       # 💼 Capa de lógica de negocio
│   └── CuponService.java          # Validación y gestión de cupones
│
├── dto/                           # 📦 Objetos de transferencia de datos
│   └── CuponDTO.java              # DTO para comunicación API
│
├── model/                         # 🎯 Modelos de dominio
│   └── Cupon.java                 # Entidad de negocio
│
├── infra/persistence/             # 🗄️ Capa de infraestructura (Persistencia)
│   ├── CuponEntity.java           # Entidad JPA
│   ├── CuponRepository.java       # Repositorio personalizado
│   └── CuponJpaRepository.java    # Interfaz Spring Data JPA
│
└── app/                           # 🚀 Punto de entrada
    └── ElAlmacenDePeliculasOnlineDescuentosApplication.java
```

## 🛠️ Stack Tecnológico

### Framework Principal
- **Spring Boot 4.0.3** - Framework de aplicaciones Java
- **Java 17** - Versión LTS del lenguaje

### Dependencias Core
| Tecnología | Propósito |
|-----------|-----------|
| **Spring Web MVC** | API REST y controladores HTTP |
| **Spring Data JPA** | Persistencia y acceso a datos |
| **Spring Security OAuth2 Resource Server** | Autenticación JWT con Keycloak |
| **Spring AMQP** | Mensajería asíncrona con RabbitMQ |
| **Spring Cloud Netflix Eureka Client** | Registro y descubrimiento de servicios |
| **Spring Boot Actuator** | Monitoreo y métricas de salud |

### Base de Datos
- **MySQL 8** - Sistema de gestión de bases de datos relacional
- **MySQL Connector/J** - Driver JDBC para MySQL

### Herramientas de Desarrollo
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias y construcción del proyecto

## 🔐 Seguridad

El microservicio está protegido con **OAuth 2.0 + JWT**:

- **Servidor de autenticación:** Keycloak
- **Realm:** `videoclub`
- **Issuer URI:** `http://localhost:9090/realms/videoclub`
- **Tipo de token:** Bearer JWT

Todos los endpoints (excepto `/actuator`) requieren un token válido en el header:
```bash
Authorization: Bearer <access_token>
```

## 🗄️ Base de Datos

**Nombre de la BD:** `db_descuentos`

### Esquema de la tabla `cupon`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | INT (PK, Auto) | Identificador único |
| `nombre` | VARCHAR(15) | Código del cupón |
| `monto` | FLOAT | Descuento en valor fijo |
| `porcentaje` | INT | Descuento en porcentaje |
| `fecha_inicio` | DATE | Inicio de vigencia |
| `fecha_fin` | DATE | Fin de vigencia |

## 🚀 Ejecución del Proyecto

### Prerrequisitos

1. **Java 17** instalado
2. **MySQL 8** ejecutándose en `localhost:3306`
3. **Keycloak** ejecutándose en `localhost:9090` con el realm `videoclub` configurado
4. Base de datos `db_descuentos` creada

### Crear la base de datos

```sql
CREATE DATABASE db_descuentos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE db_descuentos;

CREATE TABLE cupon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(15) NOT NULL,
    monto FLOAT,
    porcentaje INT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL
);

-- Datos de ejemplo
INSERT INTO cupon (nombre, monto, porcentaje, fecha_inicio, fecha_fin) 
VALUES ('VERANO2026', 500.0, NULL, '2026-01-01', '2026-03-31');

INSERT INTO cupon (nombre, monto, porcentaje, fecha_inicio, fecha_fin) 
VALUES ('PROMO10', NULL, 10, '2026-02-01', '2026-12-31');
```

### Compilar y ejecutar

```bash
# Compilar el proyecto
./mvnw clean install

# Ejecutar la aplicación
./mvnw spring-boot:run
```

El servicio estará disponible en: **http://localhost:8084**

## 📡 API Endpoints

### 🔹 Health Check
```http
GET /descuentos/test
```
**Respuesta:**
```json
{
  "status": "OK",
  "message": "Servicio de Descuentos operando en el puerto 8084"
}
```

### 🔹 Validar Cupón
```http
GET /descuentos/validar?codigo=VERANO2026
Authorization: Bearer <token>
```
**Respuesta exitosa (200):**
```json
{
  "id": 1,
  "nombre": "VERANO2026",
  "monto": 500.0,
  "porcentaje": null,
  "fechaInicio": "2026-01-01",
  "fechaFin": "2026-03-31"
}
```
**Respuesta sin cupón válido (404):**
```
Not Found
```

### 🔹 Listar Cupones
```http
GET /descuentos/listar
Authorization: Bearer <token>
```
**Respuesta (200):**
```json
[
  {
    "id": 1,
    "nombre": "VERANO2026",
    "monto": 500.0,
    "porcentaje": null,
    "fechaInicio": "2026-01-01",
    "fechaFin": "2026-03-31"
  },
  {
    "id": 2,
    "nombre": "PROMO10",
    "monto": null,
    "porcentaje": 10,
    "fechaInicio": "2026-02-01",
    "fechaFin": "2026-12-31"
  }
]
```

### 🔹 Crear Cupón
```http
POST /descuentos/crear
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "NAVIDAD2026",
  "monto": 1000.0,
  "porcentaje": null,
  "fechaInicio": "2026-12-01",
  "fechaFin": "2026-12-31"
}
```
**Respuesta (200):**
```json
{
  "id": 3,
  "nombre": "NAVIDAD2026",
  "monto": 1000.0,
  "porcentaje": null,
  "fechaInicio": "2026-12-01",
  "fechaFin": "2026-12-31"
}
```

## 🔑 Autenticación con Keycloak

### Obtener un token de acceso

```bash
curl -X POST "http://localhost:9090/realms/videoclub/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=web" \
  -d "username=<tu_usuario>" \
  -d "password=<tu_password>"
```

### Usar el token en las peticiones

```bash
curl -H "Authorization: Bearer <access_token>" \
  "http://localhost:8084/descuentos/validar?codigo=VERANO2026"
```

## 📊 Monitoreo

Spring Boot Actuator expone endpoints de monitoreo en:

```
http://localhost:8084/actuator/health
http://localhost:8084/actuator/info
```

## 🧪 Testing

Ejecutar los tests:
```bash
./mvnw test
```

## 📝 Configuración

La configuración principal se encuentra en `src/main/resources/application.yml`:

```yaml
server:
  port: 8085                        # Puerto del microservicio

spring:
  application:
    name: descuentos-service        # Nombre del servicio
  datasource:
    url: jdbc:mysql://localhost:3306/db_descuentos
    username: root
    password: 
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9090/realms/videoclub

eureka:
  client:
    enabled: false                  # Deshabilitado por defecto
```

## 🐛 Logging

El proyecto incluye logging detallado de Hibernate para facilitar el debug:

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## 🔄 Integración con Otros Servicios

Este microservicio está diseñado para integrarse con:
- **Servicio de Ventas/Compras** - Para aplicar descuentos en transacciones
- **API Gateway** - Como punto de entrada centralizado
- **Eureka Server** - Para registro y descubrimiento (cuando esté habilitado)
- **RabbitMQ** - Para eventos asíncronos relacionados con descuentos

## 👨‍💻 Desarrollador

**Agustín Fernández**

## 📄 Licencia

Este proyecto es parte de un sistema académico/empresarial privado.

---

⭐ **¡Gracias por usar el Microservicio de Descuentos!** ⭐

