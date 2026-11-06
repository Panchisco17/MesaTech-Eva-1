package cl.duoc.ms_pedidos360_orders.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CategoriaSync {
    // Usamos el ID original que viene desde el microservicio Catálogo
    @Id
    private Long id;
    private String nombre;
}