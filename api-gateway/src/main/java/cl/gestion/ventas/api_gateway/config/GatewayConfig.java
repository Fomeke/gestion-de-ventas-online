package cl.gestion.ventas.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    private RouterFunction<ServerResponse> serviceRoute(String routeId, String serviceName, String... paths){
        var builder = GatewayRouterFunctions.route(routeId);

        for (String path : paths){
            builder = builder.route(RequestPredicates.path(path), HandlerFunctions.http());
        }

        return builder.filter(LoadBalancerFilterFunctions.lb(serviceName))
                      .filter(FilterFunctions.addRequestHeader("X-Gateway-Source", "api-gateway"))
                      .build();
    }

    private RouterFunction<ServerResponse> docsRoute(String serviceName){

        return GatewayRouterFunctions.route(
                    "ms-"+serviceName+"-docs-route")
                .route(RequestPredicates.path("/"+serviceName+"/api/v3/api-docs/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/"+serviceName+"/api/swagger-ui/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/"+serviceName+"/api/swagger-ui.html"), HandlerFunctions.http())
                .route(RequestPredicates.path("/"+serviceName+"/api/actuator/health"), HandlerFunctions.http())
                .filter(FilterFunctions.stripPrefix(1))
                .filter(LoadBalancerFilterFunctions.lb(serviceName))
                .filter(FilterFunctions.addRequestHeader("X-Gateway-Source", "api-gateway"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes(){

        return serviceRoute("ms-auth-route", "auth","/api/v1/auth/**","/api/v1/usuarios/**")
                .and(docsRoute("auth"))
                .and(serviceRoute("ms-cart-route", "cart", "/api/v1/carts/**"))
                .and(docsRoute("cart"))
                .and(serviceRoute("ms-category-service-route", "category", "/api/v1/category/**"))
                .and(docsRoute("category"))
                .and(serviceRoute("ms-inventory-service-route", "inventory", "/api/v1/inventories/**"))
                .and(docsRoute("inventory"))
                .and(serviceRoute("ms-notification-service-route", "notification", "/api/v1/notification/**"))
                .and(docsRoute("notification"))
                .and(serviceRoute("ms-order-service-route", "order", "/api/v1/orders/**"))
                .and(docsRoute("order"))
                .and(serviceRoute("ms-payment-service-route", "payment", "/api/v1/payment/**"))
                .and(docsRoute("payment"))
                .and(serviceRoute("ms-product-service-route", "product", "/api/v1/product/**"))
                .and(docsRoute("product"))
                .and(serviceRoute("ms-review-service-route", "review", "/api/v1/review/**"))
                .and(docsRoute("review"))
                .and(serviceRoute("ms-shipping-service-route", "shipping", "/api/v1/shipments/**"))
                .and(docsRoute("shipping"));
    }

}
