package cl.duoc.ms_pedidos360_orders.dto;

import lombok.Data;

@Data
public class CategoriaEventoDTO {
    private String eventId;
    private String tipoEvento;
    private String fechaEvento;
    private Long categoriaId;
    private String nombre;
}