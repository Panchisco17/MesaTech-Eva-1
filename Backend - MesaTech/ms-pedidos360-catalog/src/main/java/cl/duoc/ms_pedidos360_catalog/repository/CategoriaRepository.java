package cl.duoc.ms_pedidos360_catalog.repository;

import cl.duoc.ms_pedidos360_catalog.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}