# Evaluación Parcial 3: Gestión de ventas online
**Asignatura:** DESARROLLO FULLSTACK I_007D 

## Descripción del Proyecto
El caso consiste en una plataforma de gestión de ventas online, que simula el funcionamiento de un sistema de comercio electrónico, que comprende la gestión de usuarios, productos, ordenes, pagos, envíos, entre otros.
Está compuesto por 10 microservicios integrados mediante comunicación remota (**Feign Client/WebClient**), un API-Gateway y un servicio de descubrimiento.
Cada microservicio esta estructurado bajo el patrón **CSR (Controller-Service-Repository)**.

## Integrantes
* **Sebastian Navarro**
* **José Calderón**

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

Además cuenta con:

11. **API-Gateway:** Puerta de enlace centralizada para acceder a todos los servicios.
12. **Eureka-server:** Servicio de descubrimiento, le entrega la dirección de cada servicio al API-gateway.

---

## Tecnologías Utilizadas
* **Lenguaje & Framework:** Java 17+, Spring Boot
* **Persistencia:** MySQL, Spring Data JPA, Hibernate
* **Migraciones de Base de Datos:** Flyway con Scripts SQL en `db/migration`
* **Seguridad:** Spring Security con JWT (JSON Web Token)
* **Comunicación Interna:** OpenFeign / WebClient
* **Validaciones:** Bean Validation y `@ControllerAdvice` para manejo de errores centralizado.
* **Documentación:** Swagger y API-docs
* **Pruebas:** Mockito y JUnit
* **Despliegue:** Docker
* **Descubrimiento:** Eureka
* **Salud:** Actuator

---

### Ejecución
1. Una vez clonado el repositorio, habilite Docker/DockerDesktop y corra el archivo compose.yaml con `docker compose up -d` dentro del directorio raíz.
2. Si quiere correr las pruebas unitarias, puede acceder a la carpeta del servicio (ej: /inventory) y correr `./mvnw test`. Tambien puede usar el archivo batch compilar-servicios.bat para compilar con pruebas. En ambos casos, debe contar con una base de datos MySQL local en el puerto 3306 sin contraseña root para que las pruebas unitarias corran con éxito, puede usar Laragon/MySQL para esto.
3. Para acceder a los servicios, puede hacerlo mediante un programa de testeo de APIs como Postman o ThunderClient por el puerto `8080` (Gateway), o mediante la interfaz de Swagger:

   ```http://localhost:8080/swagger-ui.html``` o
   ```http://localhost:8080/swagger-ui/index.html```

   - Pero para utilizar los endpoints de cada servicio, debe iniciar sesión y obtener un JWT válido desde el servicio auth, por lo que debe acceder primero al endpoint con POST:

     ```http
     http://localhost:8080/api/v1/auth
     ```
   - En la interfaz de swagger, debe cambiar en `Select a definition` a **Autenticación y Usuarios** y dirijase a Control de Autenticación -> Iniciar sesión.

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
   - Ó ingresar el token mediante el botón `Authorize` en la interfaz de Swagger de cada servicio.

   - Todos los endpoints de la plataforma (que no son swagger), excepto `/api/v1/auth`, requieren autenticación mediante este token.

   - Puede acceder a los servicios tanto en swagger como postman con el API Gateway o directamente a los servicios, para lo último éstos son los puertos de cada servicio:
      - `category` (Puerto: `8086`)
      - `product` (Puerto: `8085`)
      - `inventory` (Puerto: `8083`)
      - `cart` (Puerto: `8082`)
      - `order` (Puerto: `8084`)
      - `payment` (Puerto: `8089`)
      - `shipping`(Puerto: `8081`)
      - `review` (Puerto: `8087`)
      - `notification` (Puerto: `8088`)

## Endpoints de cada microservicio

### 1. Auth Service 
Maneja la seguridad, generación de JWT y la gestión del ciclo de vida de los usuarios.
**Rutas Base:** `api/v1/auth` y `api/v1/usuarios`

* **Autenticación:**
  * `POST api/v1/auth`: Inicia sesión. Recibe credenciales (`LoginRequest`) y devuelve un token JWT válido si el usuario existe. Es la única ruta pública por defecto.
* **Usuarios:**
  * `GET api/v1/usuarios`: Obtiene una lista con todos los usuarios registrados.
  * `GET api/v1/usuarios/{id}`: Obtiene el detalle de un usuario específico según su ID.
  * `GET api/v1/usuarios/username/{username}`: Busca y retorna un usuario específico mediante su nombre de usuario.
  * `POST api/v1/usuarios`: Registra un nuevo usuario en la base de datos.
  * `PUT api/v1/usuarios/{id}`: Actualiza los datos de un usuario existente.
  * `DELETE api/v1/usuarios/{id}`: Elimina un usuario por completo según su ID.

### 2. Cart Service
Gestiona los carritos de compra de los clientes.
**Ruta Base:** `api/v1/carts`

* `GET api/v1/carts`: Retorna la lista de todos los carritos activos en el sistema.
* `GET api/v1/carts/{userId}`: Obtiene el carrito específico de un usuario mediante su ID.
* `POST api/v1/carts`: Añade un producto al carrito. Requiere autenticación (extrae el ID de usuario del JWT).
* `PUT api/v1/carts/{userId}`: Actualiza el carrito completo de un usuario. Requiere autenticación para validar la propiedad.
* `DELETE api/v1/carts/{userId}/items/{productId}`: Elimina un producto específico del carrito. Requiere autenticación.
* `DELETE api/v1/carts/{userId}`: Vacía/elimina el carrito completo de un usuario. Requiere autenticación.

### 3. Category Service
Administra las categorías a las que pertenecen los productos del sistema.
**Ruta Base:** `api/v1/category`

* `GET api/v1/category`: Obtiene el catálogo completo de categorías.
* `GET api/v1/category/porid/{id}`: Obtiene los detalles de una categoría específica por su ID.
* `POST api/v1/category`: Crea una nueva categoría.
* `PUT api/v1/category/{id}`: Modifica los datos de una categoría existente.
* `DELETE api/v1/category/{id}`: Elimina una categoría del sistema.

### 4. Inventory Service
Controla el stock y existencias físicas de los productos.
**Ruta Base:** `api/v1/inventories`

* `GET api/v1/inventories`: Obtiene un listado completo del stock de todos los productos.
* `GET api/v1/inventories/{productId}`: Consulta el inventario/stock disponible para un producto específico.
* `POST api/v1/inventories`: Crea un nuevo registro de inventario inicial para un producto.
* `PUT api/v1/inventories/{productId}?quantity={X}`: Actualiza el stock de un producto específico. El parámetro `quantity` define la cantidad a sumar o restar.
* `PUT api/v1/inventories/bulk-update`: Actualiza el inventario de múltiples productos a la vez de forma masiva (recibe una lista de items).
* `DELETE api/v1/inventories/{productId}`: Elimina el registro de inventario de un producto.

### 5. Notification Service
Maneja las alertas y mensajes enviados al usuario (ej. confirmación de órdenes).
**Ruta Base:** `api/v1/notification`

* `GET api/v1/notification`: Lista todas las notificaciones emitidas por el sistema.
* `POST api/v1/notification`: Crea y emite una nueva notificación. Requiere pasar el JWT en el Header (`Authorization`).
* `PUT api/v1/notification/{id}`: Actualiza el estado o contenido de una notificación. Requiere pasar el JWT en el Header.
* `DELETE api/v1/notification/{id}`: Elimina una notificación por su ID.

### 6. Order Service
Orquesta el proceso de compra, conectando carrito, inventario y posterior pago/envío.
**Ruta Base:** `api/v1/orders`

* `GET api/v1/orders`: Obtiene el historial completo de todas las órdenes del sistema.
* `GET api/v1/orders/{id}`: Obtiene el detalle completo de una orden específica, incluyendo sus items.
* `GET api/v1/orders/noitems/{id}`: Retorna una versión resumida de la orden (sin el detalle de los productos/items). Útil para vistas generales.
* `GET api/v1/orders/user/{userId}`: Obtiene el historial de órdenes de un usuario específico.
* `GET api/v1/orders/ship/{id}`: Retorna una versión de la orden formateada y adaptada específicamente para el servicio de envíos (Shipping).
* `POST api/v1/orders`: Genera una nueva orden de compra a partir de una solicitud (`OrderRequest`).
* `PUT api/v1/orders/{orderId}`: Actualiza el estado logístico/transaccional de una orden (ej. de PENDIENTE a PAGADO).
* `DELETE api/v1/orders/{id}`: Elimina o anula una orden por su ID.

### 7. Payment Service (Pagos)
Procesa y registra las transacciones económicas asociadas a las órdenes.
**Ruta Base:** `api/v1/payment`

* `GET /v1/payment`: Obtiene un listado general de todos los pagos registrados.
* `GET /v1/payment/{id}`: Obtiene el detalle de una transacción de pago específica.
* `POST /v1/payment`: Procesa un nuevo pago. Exige el token en el Header (`Authorization`) para validar seguridad cruzada.
* `PUT /v1/payment/{id}`: Modifica el estado o datos de un pago existente. Requiere token en el Header.
* `DELETE /v1/payment/{id}`: Elimina/anula un registro de pago. Requiere token en el Header.

### 8. Product Service
Catálogo central de los artículos disponibles para la venta.
**Ruta Base:** `api/v1/product`

* `GET api/v1/product`: Retorna el catálogo completo de productos.
* `GET api/v1/product/{id}`: Muestra el detalle de un producto específico.
* `POST api/v1/product`: Agrega un nuevo producto al catálogo. Requiere token en el Header (`Authorization`).
* `PUT api/v1/product/{id}`: Actualiza la información de un producto existente. Requiere token en el Header.
* `DELETE api/v1/product/{id}`: Elimina un producto del catálogo.

### 9. Review Service
Permite a los usuarios calificar y dejar comentarios sobre los productos adquiridos.
**Ruta Base:** `api/v1/review`

* `GET api/v1/review`: Lista todas las reseñas escritas en la plataforma.
* `GET api/v1/review/{id}`: Obtiene el detalle de una reseña puntual por su ID.
* `POST api/v1/review`: Crea una nueva reseña para un producto. Exige pasar el JWT en el Header (`Authorization`).
* `PUT api/v1/review/{id}`: Permite modificar el contenido o calificación de una reseña existente. Exige token en el Header.
* `DELETE api/v1/review/{id}`: Elimina una reseña del sistema por su ID.

### 10. Shipping Service
Gestiona la logística, despachos y números de seguimiento de las compras.
**Ruta Base:** `api/v1/shipments`

* `GET api/v1/shipments`: Lista todos los envíos registrados.
* `GET api/v1/shipments/shipmentById/{id}`: Busca un envío utilizando su ID interno de base de datos.
* `GET api/v1/shipments/{trackingNum}`: Busca y hace seguimiento a un envío utilizando su código alfanumérico (`trackingNum`).
* `POST api/v1/shipments`: Crea la logística de un nuevo envío.
* `PUT api/v1/shipments/{trackingNum}`: Actualiza el estado o información de un envío utilizando su número de seguimiento.
* `DELETE api/v1/shipments/{trackingNum}`: Elimina o cancela un envío utilizando su número de seguimiento.

### 11. Eureka Server
* Puede acceder a la interfaz de eureka mediante el puerto `8761`, ej: `localhost:8761`
