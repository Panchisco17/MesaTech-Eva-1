package cl.duoc.ms_pedidos360_orders.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String descripcion;
    private String categoria;
    private String prioridad;
    private String usuarioSolicitante; 
    private String estado; 
    
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}