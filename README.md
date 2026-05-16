# Evaluación Parcial 2: Gestión de ventas online
**Asignatura:** DESARROLLO FULLSTACK I_007D 

## Descripción del Proyecto
El caso consiste en una plataforma de gestión de ventas online, que simula el funcionamiento de un sistema de comercio electrónico, que comprende la gestión de usuarios, productos, ordenes, pagos, envíos, entre otros.
Está compuesto por 10 microservicios integrados mediante comunicación remota (**Feign Client/WebClient**).
Cada microservicio esta estructurado bajo el patrón **CSR (Controller-Service-Repository)**.

## Integrantes
* **[Sebastian Navarro]** - [Estudiante]
* **[Jose Calderon]** - [Estudiante]

---

## Funcionalidades Implementadas
El sistema está compuesto por 10 microservicios independientes, comunicados entre sí y estructurados bajo el patrón CSR (Controller-Service-Repository):

1. **Auth:** Autenticación de usuarios mediante JWT y gestión de los mismos.
2. **Category:** Mantenimiento de categorías.
3. **Product:** Catálogo de artículos.
4. **Inventory:** Control de stock físico disponible.
5. **Cart:** Carrito de compras temporal por usuario.
6. **Order:** Orquestador de pedidos y control de estados (PENDING, PAID, etc.).
7. **Payment:** Simulación de pagos.
8. **Shipping:** Logística de despachos.
9. **Review:** Calificaciones de productos.
10. **Notification:** Sistema de alertas.

---

## Tecnologías Utilizadas
* **Lenguaje & Framework:** Java 17+, Spring Boot
* **Persistencia:** MySQL, Spring Data JPA, Hibernate
* **Migraciones de Base de Datos:** Flyway con Scripts SQL en `db/migration`
* **Seguridad:** Spring Security con JWT (JSON Web Token)
* **Comunicación Interna:** OpenFeign / WebClient
* **Validaciones:** Bean Validation y `@ControllerAdvice` para manejo de errores centralizado.

---

### Ejecución
1. **Bases de Datos:** Puede utilizar Laragon para crear bases de datos MySQL. Los nombres y credenciales de las bases de datos de cada servicio deben coincidir con lo especificado en cada `application.properties/yml`.
2. **Importar:** Abre la carpeta raíz en tu IDE para cargar todos los servicios, para iniciar uno debe ejecutar el archivo en:
   - `gestion-de-ventas-online\<nombre de servicio>\src\main\java\cl\gestion\ventas\<nombre de servicio>\<NombreDeServicio>Application.java`.
4. **Arrancar Servicios:**  

   #### Primero: `auth` (Puerto: `8080`)

   - Inicie la aplicación y accede al endpoint con POST:

     ```http
     http://localhost:8080/api/v1/auth
     ```

   - Utiliza uno de los usuarios predeterminados mediante el siguiente formato JSON en el Body de la solicitud:

     ```json
     {
       "username": "",
       "password": ""
     }
     ```

   - Credenciales disponibles:

     1. `username: admin`  
        `password: contrasenasegura123`

     2. `username: noob`  
        `password: contrasenasegura456`

     3. `username: pro`  
        `password: contrasenasegura789`

   - El endpoint devolverá un token JWT con duración de 1 hora.

   - Debes adjuntar ese token en cada solicitud de Postman utilizando:

     - Header: `Authorization`
     - Auth Type: `Bearer Token`

   - Todos los endpoints de la plataforma, excepto `/api/v1/auth`, requieren autenticación mediante este token.

   #### Segundo:

   - `category`
   - `product`
   - `inventory`

   #### Tercero:

   - `cart`
   - `order`
   - `payment`
   - `shipping`

   #### Cuarto:

   - `review`
   - `notification`
