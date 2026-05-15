# Gestión de Ventas Online

## Descripción del Proyecto
Este proyecto es una solución distribuida basada en microservicios para la gestión integral de un sistema de ventas online y control de stock.
## Integrantes
* **[Sebastian Navarro]** - [Backend Developer]
* **[Jose Calderon]** - [Backend Developer]

---

## Funcionalidades Implementadas
El sistema está compuesto por 10 microservicios independientes, comunicados entre sí y estructurados bajo el patrón CSR (Controller-Service-Repository):

1. **Auth Service (`auth`):** Gestión de usuarios, autenticación y generación de tokens JWT.
2. **Category Service (`category`):** Administración de las categorías del catálogo.
3. **Product Service (`product`):** Gestión del catálogo principal de artículos.
4. **Inventory Service (`inventory`):** Control del stock y validaciones de disponibilidad.
5. **Cart Service (`cart`):** Almacenamiento de las intenciones de compra.
6. **Order Service (`order`):** Transforma carritos en pedidos y gestiona sus estados.
7. **Payment Service (`payment`):** Procesa las transacciones asociadas a las órdenes.
8. **Shipping Service (`shipping`):** Seguimiento de despachos de órdenes.
9. **Review Service (`review`):** Comentarios de usuarios sobre los productos.
10. **Notification Service (`notification`):** Avisa sobre el estado de las órdenes.

---

## Tecnologías Utilizadas
* **Lenguaje & Framework:** Java 17+, Spring Boot
* **Persistencia:** MySQL, Spring Data JPA, Hibernate
* **Migraciones de Base de Datos:** Scripts SQL en `db/migration`
* **Seguridad:** Spring Security con JWT (JSON Web Tokens)
* **Comunicación Interna:** OpenFeign / WebClient
* **Validaciones:** Bean Validation (JSR 380)

---

## Pasos para Ejecutar el Proyecto

### Requisitos Previos
* Instalar **Java 17** o superior.
* Instalar **MySQL** y ejecutándose en el puerto `3306`.
* IDE: IntelliJ IDEA o VSCode.

### Configuración de la Base de Datos
1. Inicia tu servidor MySQL.
2. Cada microservicio creará sus tablas e insertará sus datos iniciales automáticamente al arrancar, gracias a los scripts de migración configurados en la carpeta `resources/db/migration`.
3. Asegúrate de que las credenciales (usuario y contraseña) de MySQL coincidan con las configuradas en el archivo `application.properties` o `application.yaml` de cada microservicio.

### Ejecución de los Microservicios
Debes levantar los microservicios en el siguiente orden lógico para evitar errores de comunicación:

Primero: auth (Puerto sugerido: 8080)
Segundo: category, product, inventory
Tercero: cart, order, payment, shipping
Cuarto: review, notification

1. Abre una terminal en la carpeta raíz de cada microservicio.
2. Ejecuta el siguiente comando (usando el wrapper de Maven incluido):
   ```bash
   # En Windows
   mvnw.cmd spring-boot:run

   # En Linux/Mac
   ./mvnw spring-boot:run
