package cl.gestion.ventas.shipping.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="shipment")
@Builder
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_id",nullable=false)
    private Long orderId;

    @Column(name="tracking_number",nullable=false,unique=true,length=200)
    private String trackingNumber;

    @Column(nullable=false,length=100)
    private String carrier;

    @Column(name="shipping_address",nullable=false,length=300)
    private String shippingAddress;

    @Builder.Default
    @Column(name="est_delivery_date")
    private LocalDate estimatedDeliveryDate = LocalDate.now().plusDays(30);

}
