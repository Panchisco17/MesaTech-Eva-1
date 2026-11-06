package cl.duoc.ms_pedidos360_orders.listener;

import cl.duoc.ms_pedidos360_orders.dto.CategoriaEventoDTO;
import cl.duoc.ms_pedidos360_orders.model.CategoriaSync;
import cl.duoc.ms_pedidos360_orders.repository.CategoriaSyncRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CategoriaListener {

    private final CategoriaSyncRepository categoriaSyncRepository;

    public CategoriaListener(CategoriaSyncRepository categoriaSyncRepository) {
        this.categoriaSyncRepository = categoriaSyncRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void recibirCategoria(CategoriaEventoDTO evento) {
        try {
            System.out.println(" [x] Evento recibido en Solicitudes: " + evento.getTipoEvento() + " - ID: " + evento.getEventId());

            if ("CATEGORIA_CREADA".equals(evento.getTipoEvento())) {

                CategoriaSync categoriaLocal = new CategoriaSync();
                categoriaLocal.setId(evento.getCategoriaId());
                categoriaLocal.setNombre(evento.getNombre());

                categoriaSyncRepository.save(categoriaLocal);

                System.out.println(" [✔] Copia de Categoría guardada localmente: " + categoriaLocal.getNombre());
            }

        } catch (Exception e) {
            System.err.println(" [!] Error al procesar el mensaje de RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}