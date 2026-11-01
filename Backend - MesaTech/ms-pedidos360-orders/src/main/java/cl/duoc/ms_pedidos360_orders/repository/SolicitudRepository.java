package cl.duoc.ms_pedidos360_orders.repository;

import cl.duoc.ms_pedidos360_orders.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByUsuarioSolicitante(String usuarioSolicitante);
}