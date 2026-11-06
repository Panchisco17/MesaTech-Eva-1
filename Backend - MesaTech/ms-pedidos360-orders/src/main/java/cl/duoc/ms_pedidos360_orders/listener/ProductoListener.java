package cl.duoc.ms_pedidos360_orders.listener;

import cl.duoc.ms_pedidos360_orders.model.Solicitud;
import cl.duoc.ms_pedidos360_orders.service.SolicitudService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductoListener {

    private final SolicitudService solicitudService;

    public ProductoListener(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    //@RabbitListener(queues = "${app.rabbitmq.queue}")//
    public void recibirProducto(Map<String, Object> mensajeProducto) {
        try {
            System.out.println(" [x] Producto recibido desde RabbitMQ: " + mensajeProducto);

            // Extraemos los datos enviados por el microservicio de Catálogo
            String nombreProducto = (String) mensajeProducto.get("nombre");
            Object precioObj = mensajeProducto.get("precio");
            Double precio = precioObj != null ? Double.valueOf(precioObj.toString()) : 0.0;

            // Creamos una Solicitud automática basada en el producto recibido
            Solicitud nuevaSolicitud = new Solicitud();
            nuevaSolicitud.setTitulo("Registro automático de producto: " + nombreProducto);
            nuevaSolicitud.setDescripcion("Producto sincronizado desde Catálogo. Precio neto: $" + precio);
            nuevaSolicitud.setCategoria("INVENTARIO");
            nuevaSolicitud.setPrioridad("MEDIA");
            nuevaSolicitud.setUsuarioSolicitante("sistema-catalogo@mesatech.cl");
            nuevaSolicitud.setEstado("CREADA");

            // Guardamos usando el servicio existente
            solicitudService.crearSolicitud(nuevaSolicitud);
            System.out.println(" [✔] Solicitud creada exitosamente para el producto: " + nombreProducto);

        } catch (Exception e) {
            System.err.println(" [!] Error al procesar el mensaje de RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}