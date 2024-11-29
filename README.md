# Caso de Uso: Gestión Segura de Tokens y Cuentas Bancarias con Spring WebFlux y Spring Security

## Introducción
Este caso de uso tiene como objetivo desarrollar una *API Reactiva* que combine la gestión de autenticación y la administración de cuentas bancarias en un contexto del sector financiero. La solución implementará seguridad mediante *Spring Security* para proteger los endpoints y garantizar el acceso únicamente a usuarios autenticados y autorizados, validando sus tokens de sesión en *DynamoDB. Además, se utilizará **RDS PostgreSQL* para gestionar auditorías y operaciones de cuentas bancarias.

---

## Objetivos del Caso de Uso

1. *Gestión de autenticación y sesiones*:
    - Utilizar DynamoDB con *Time to Live (TTL)* para almacenar y eliminar automáticamente tokens de sesión expirados.
    - Implementar un sistema de validación de tokens para garantizar la seguridad de las solicitudes.

2. *Gestión de cuentas bancarias protegida*:
    - Implementar un CRUD completo para administrar cuentas bancarias en PostgreSQL.
    - Asegurar que cada operación esté protegida mediante validación de token y permisos de acceso.

3. *Auditoría centralizada*:
    - Registrar eventos como inicios de sesión, validaciones de tokens y operaciones en las cuentas bancarias en PostgreSQL.

4. *Seguridad robusta*:
    - Proteger todos los endpoints mediante *Spring Security*, asegurando autenticación y autorización basadas en roles.

---

## Requisitos del Sistema

### Funcionales
1. Generar tokens únicos para sesiones autenticadas y almacenarlos en DynamoDB con TTL.
2. Validar tokens enviados en cada solicitud para garantizar autenticidad.
3. Implementar operaciones CRUD sobre cuentas bancarias:
    - Crear nuevas cuentas.
    - Consultar cuentas específicas o todas las cuentas.
    - Actualizar información de cuentas existentes.
    - Eliminar cuentas bancarias.
4. Registrar en PostgreSQL las acciones realizadas en el sistema, como inicios de sesión, operaciones exitosas y fallidas.
5. Asignar roles (e.g., ADMIN, USER) para determinar el nivel de acceso a los endpoints.

### No Funcionales
1. Diseñar una API reactiva y eficiente usando Spring WebFlux.
2. Integrar DynamoDB y PostgreSQL para almacenamiento distribuido y estructurado.
3. Garantizar la seguridad de los datos con Spring Security.
4. Implementar manejo de errores claro y completo para responder adecuadamente a los usuarios.

---

## Flujo del Sistema

### 1. Autenticación y Validación
- *Inicio de Sesión*:
    - El cliente envía credenciales al endpoint /auth/login.
    - Si las credenciales son correctas, se genera un token único y se almacena en DynamoDB con un TTL de 15 minutos.
    - Se registra el inicio de sesión en la tabla de auditoría de PostgreSQL.
    - El sistema devuelve el token generado al cliente.
- *Validación de Token*:
    - Cada solicitud a los endpoints protegidos incluye el token como encabezado.
    - El token es validado en DynamoDB antes de procesar la solicitud.
    - Si el token no es válido o expiró, el sistema rechaza la solicitud.

### 2. Gestión de Cuentas Bancarias
- CRUD protegido en /accounts:
    - *Crear cuenta*:
        - Sólo usuarios con rol ADMIN pueden crear cuentas.
    - *Consultar cuentas*:
        - Los usuarios con rol USER pueden consultar sus propias cuentas.
        - Los administradores (ADMIN) pueden consultar todas las cuentas.
    - *Actualizar cuenta*:
        - Sólo administradores pueden modificar cuentas.
    - *Eliminar cuenta*:
        - Sólo administradores pueden eliminar cuentas.

### 3. Auditoría
- Todas las acciones (e.g., inicio de sesión, validaciones de token, operaciones CRUD) se registran en la tabla de auditoría en PostgreSQL.

---

## Esquema de Base de Datos

### DynamoDB: Tabla SessionTokens
| Atributo      | Tipo   | Descripción                              |
|---------------|--------|------------------------------------------|
| token       | String | Identificador único del token.           |
| userId      | String | ID del usuario autenticado.              |
| createdAt   | Number | Timestamp de creación del token.         |
| ttl         | Number | Timestamp para la eliminación automática.|

### PostgreSQL: Tabla AuditLog
| Columna       | Tipo      | Descripción                            |
|---------------|-----------|----------------------------------------|
| id          | UUID      | Identificador único.                   |
| userId      | String    | ID del usuario.                        |
| action      | String    | Acción realizada (e.g., login, validación, CRUD). |
| ipAddress   | String    | Dirección IP del cliente.              |
| timestamp   | Timestamp | Fecha y hora de la acción.             |

### PostgreSQL: Tabla BankAccount
| Columna        | Tipo      | Descripción                            |
|----------------|-----------|----------------------------------------|
| id           | UUID      | Identificador único de la cuenta.      |
| accountNumber| String    | Número de cuenta único.                |
| accountHolder| String    | Nombre del titular de la cuenta.       |
| balance      | Decimal   | Saldo actual de la cuenta.             |
| createdAt    | Timestamp | Fecha de creación de la cuenta.        |
| updatedAt    | Timestamp | Fecha de última modificación.          |

---

## Endpoints

### Autenticación
- *POST* /auth/login
    - Genera un token para el usuario autenticado. El curl es el siguiente:
  ```bash
        curl --location 'http://localhost:8080/api/v1/auth/login' \
        --header 'Accept: application/json' \
        --header 'Content-Type: application/json' \
        --data '{
          "username": "andres97",
          "password": "123"
          }'
  ``` 
- *GET* /auth/validate-token
    - Valida un token enviado.

### Gestión de Cuentas
- *POST* /accounts
    - Crear una nueva cuenta bancaria (rol ADMIN requerido).
- *GET* /accounts
    - Consultar todas las cuentas (rol ADMIN requerido).
    - Consultar las cuentas del usuario autenticado (rol USER).
- *GET* /accounts/{id}
    - Consultar una cuenta bancaria específica por ID (acceso según permisos).
- *PUT* /accounts/{id}
    - Actualizar una cuenta bancaria existente (rol ADMIN requerido).
- *DELETE* /accounts/{id}
    - Eliminar una cuenta bancaria específica (rol ADMIN requerido).

---

## Roles y Seguridad

### Roles del Sistema
1. *ADMIN*: Acceso total a la gestión de cuentas y auditorías.
2. *USER*: Acceso limitado a consultar y validar su propia información.

### Spring Security
- Se implementará un filtro de autenticación que valide los tokens enviados en cada solicitud.
- Se configurarán reglas de autorización para restringir los endpoints según el rol del usuario.

---

### Configuracion despliegue locaL
 * 1. Ejecutar Docker Compose
    
        Dirigirse a la carpeta deployment dentro del proyecto y ejecutar el siguiente comando:
        ```bash
        docker-compose up
        ```
        Este comando desplegara una instancia de DynamoDB y PostgreSQL en contenedores Docker, necesarios para la
        ejecución de la aplicación.

        **IMPORTANTE**: Los siguentes comandos tambien se pueden encontrar en la carpeta deployment del proyecto.


 * 2. **Crear Tabla dynamoDB y configurar TTL**
    
        Para crear la tabla en DynamoDB, se debe ejecutar el siguiente comando:
        ```bash 
      aws dynamodb create-table --table-name SessionTokens --attribute-definitions AttributeName=userId,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 --endpoint-url http://localhost:8010
        ```
        Luego, se debe configurar el TTL en la tabla creada:
        ```bash
      aws dynamodb update-time-to-live --table-name SessionTokens --time-to-live-specification "Enabled=true, AttributeName=ttl" --endpoint-url http://localhost:8010
        ```
      Para validar la correctar crear la tabla, se puede ejecutar el siguiente comando:
        ```bash
      aws dynamodb list-tables --endpoint-url http://localhost:8010 
        ```
 * 3. **Crear Tablas en PostgreSQL**
    
        Para crear las tablas en PostgreSQL, se debe ejecutar el siguiente comando desde algun gestor de base de datos:
        ```bash
        CREATE TABLE AuditLog (
        id UUID PRIMARY KEY,
        userId VARCHAR(255) NOT NULL,
        action VARCHAR(255) NOT NULL,
        ipAddress VARCHAR(45) NOT NULL,
        dateLog TIMESTAMP NOT NULL
        );
        ```
      Las credenciales de conexion a la base de datos son las siguientes:
        ```bash
        url: localhost:5544
        username: admin
        password: admin123
        database: accounts
        ```
 * 4. **IMPORTANTE**

        Para poder ejecutar la aplicación, se debe configurar las credenciales de AWS en el archivo application.properties
        ubicado en la carpeta resources del proyecto. Se debe agregar las siguientes propiedades:
        ```properties
        aws:
          accessKey: any
          secretKey: any
        adapter:
          aws:
            dynamodb:
               endpoint: http://localhost:8010
               role-arn: dummy
               role-session-name: dummy
               region: us-east-1
               host: localhost
               protocol: http
               port: 8010
        ```
        Además, se debe configurar las credenciales de PostgreSQL en el mismo archivo application.properties:
        ```properties
          r2dbc:
            url: r2dbc:postgresql://localhost:5544/accounts
            username: admin
            password: admin123
        ```
        **Nota:** Se debe tener en cuenta la configuracion de los logs de la aplicacion, por defecto se encuntra 
        en modo DEBUG pero si se requiere se puede pasar a INFO o ERROR.
        
        ```properties
      logging:
         level:
          root: DEBUG
          org.springframework.web: DEBUG
          co.com.bancolombia: DEBUG
