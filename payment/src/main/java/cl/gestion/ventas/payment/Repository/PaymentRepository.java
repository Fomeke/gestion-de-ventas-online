package cl.gestion.ventas.payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.payment.model.Payment;


public interface PaymentRepository extends JpaRepository<Payment,Long>{

}
