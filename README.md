# Evaluación Parcial 2: Gestión de ventas online
**Asignatura:** DESARROLLO FULLSTACK I_007D 

## Descripción del Proyecto
El sistema esta estructurado bajo el patrón **CSR (Controller-Service-Repository)**.
Está compuesto por 10 microservicios integrados mediante comunicación remota (**Feign Client/WebClient**).

## Integrantes
* **[Sebastian Navarro]** - [Estudiante]
* **[Jose Calderon]** - [Estudiante]

---

## Funcionalidades Implementadas
El sistema está compuesto por 10 microservicios independientes, comunicados entre sí y estructurados bajo el patrón CSR (Controller-Service-Repository):

1. **Auth:** Autenticación de usuarios y generación de JWT.
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
* **Migraciones de Base de Datos:** Scripts SQL en `db/migration`
* **Seguridad:** Spring Security con JWT (JSON Web Tokens)
* **Comunicación Interna:** OpenFeign / WebClient
* **Validaciones:** Bean Validation y `@ControllerAdvice` para manejo de errores centralizado.

---

### Ejecución en IDE
1. **Base de Datos:** Inicia MySLQ localmente. Las credenciales deben coincidir con los archivos `application.properties/yml`.
2. **Importar:** Abre la carpeta raíz en tu IDE para cargar todos los módulos.
3. **Arrancar Servicios:**
                           Primero: auth (Puerto sugerido: 8080)
                           Segundo: category, product, inventory
                           Tercero: cart, order, payment, shipping
                           Cuarto: review, notification
