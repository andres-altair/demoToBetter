# 🚀 demoToBetter

API REST construida con Spring Boot 3.5.10 que implementa autenticación segura basada en JWT, autorización por roles y gestión completa de usuarios.

---

## 📌 Características Principales

### 🔐 Autenticación JWT sin estado
- Access Token (15 min)
- Refresh Token (7 días)
- Persistencia de refresh tokens en base de datos
- Rotación automática para prevenir ataques de repetición

### 👤 Gestión de Usuarios (CRUD completo)
- Crear, consultar, actualizar y eliminar usuarios
- Paginación y ordenamiento
- Filtros dinámicos

### 🛡 Autorización basada en roles
- Rol `USER` por defecto
- Integración con Spring Security

### 🔑 Seguridad de contraseñas
- Hashing con BCrypt

### 🗄 Migraciones de base de datos
- Versionado automático con Flyway

### 🧪 Testing y cobertura
- Unit tests con JUnit 5
- Mocks con Mockito
- Reportes de cobertura con JaCoCo

### 🧩 Manejo global de excepciones
- Respuestas estandarizadas mediante `ErrorDTO`

---

## 🏗 Arquitectura

El proyecto sigue una arquitectura por capas:

```bash
Controller → Service → Repository → Database
```

### Principios aplicados

- Separación clara de responsabilidades
- Seguridad aplicada antes de los controladores (JwtAuthenticationFilter)
- Manejo centralizado de errores (GlobalExceptionHandler)
- Abstracción de base de datos mediante Spring Data JPA
- Migraciones gestionadas por Flyway (no por Hibernate)
- DTOs + MapStruct para mapeo tipado seguro

---

## 📦 Organización de Módulos


```bash
com.andres.demotobetter
├── common
├── modules
│   ├── security
│   └── users
```


---

## 🔐 Flujo de Autenticación

1. Cliente envía credenciales → `/api/auth/login`
2. Se genera:
   - Access Token (15 min)
   - Refresh Token (7 días)
   - Refresh Token persistido en base de datos
3. Cada request protegida:
   - Pasa por JwtAuthenticationFilter
   - Se valida firma, expiración y estado de cuenta
4. Renovación en `/api/auth/refresh`
   - Se rota automáticamente el refresh token

---

## 📚 Endpoints Principales

### 🔐 Autenticación

| Método | Endpoint            | Descripción                  |
|--------|--------------------|------------------------------|
| POST   | `/api/auth/login`   | Login y generación de tokens |
| POST   | `/api/auth/refresh` | Renovación de access token   |

### 👤 Usuarios

| Método | Endpoint                    | Descripción        |
|--------|-----------------------------|--------------------|
| POST   | `/api/users`                | Crear usuario      |
| GET    | `/api/users/{id}`           | Obtener usuario    |
| GET    | `/api/users?page=0&size=10` | Listado paginado   |
| PUT    | `/api/users/{id}`           | Actualizar usuario |
| DELETE | `/api/users/{id}`           | Eliminar usuario   |

### Validaciones

- Tamaño máximo de página: 50
- Campos ordenables: `id`, `firstName`, `lastName`, `phone`
- Filtros: `firstName`, `lastName`, `phone` (case-insensitive)

---

## 🗄 Modelo de Datos

Relaciones principales:

- UserProfile → 1:1 → UserSecurity
- UserSecurity → M:N → Role
- RefreshToken → M:1 → UserSecurity

---

## 🧰 Stack Tecnológico
| Tecnología         | Versión            |
| ------------------ | ------------------ |
| ☕ Java             | 21                 |
| 🌱 Spring Boot     | 3.5.10             |
| 🔐 Spring Security | 3.5.10             |
| 🗄 Spring Data JPA | 3.5.10             |
| 🐘 PostgreSQL      | Runtime            |
| 🧪 H2              | Runtime (dev/test) |
| 🔄 Flyway          | Migraciones        |
| 🧩 MapStruct       | 1.6.3              |
| 📦 Maven           | Build Tool         |
| 🪶 Lombok          | 1.18.x             |
| 🔑 JJWT            | 0.13.0             |
| 🧪 JUnit 5         | Testing            |
| 🎭 Mockito         | Testing            |
| 📊 JaCoCo          | Coverage           |


---

## ⚙️ Configuración

### Base de datos
- Desarrollo / Test → H2
- Producción → PostgreSQL

### application.yaml

Configuraciones clave:

- JWT secret
- Expiración de tokens
- Flyway habilitado
- Consola H2 en `/h2-console`
- `ddl-auto: none` (esquema gestionado por Flyway)

---

### 🔒 Buenas Prácticas de Seguridad Aplicadas
 - Arquitectura completamente stateless
 - Tokens de corta duración
 - Rotación automática de refresh tokens
 - Validación en múltiples capas
 - Cuentas deshabilitadas bloqueadas a nivel de filtro
 - BCrypt para contraseñas
 - Manejo estandarizado de errores
