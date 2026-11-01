package cl.duoc.ms_pedidos360_catalog.repository;

import cl.duoc.ms_pedidos360_catalog.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}