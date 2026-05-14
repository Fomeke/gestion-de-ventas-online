package cl.gestion.ventas.payment.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{

}
