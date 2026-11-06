package cl.duoc.ms_pedidos360_catalog.publisher;

import cl.duoc.ms_pedidos360_catalog.entity.Categoria;
import cl.duoc.ms_pedidos360_catalog.dto.CategoriaEventoDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class CategoriaPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public CategoriaPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarCategoria(Categoria categoria) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        CategoriaEventoDTO evento = new CategoriaEventoDTO();
        evento.setEventId(correlationData.getId());
        evento.setTipoEvento("CATEGORIA_CREADA");
        evento.setFechaEvento(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        evento.setCategoriaId(categoria.getId());
        evento.setNombre(categoria.getNombre());

        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                evento,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setMessageId(correlationData.getId());
                    return message;
                },
                correlationData
        );

        try {
            CorrelationData.Confirm confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);
            if (confirm != null && !confirm.ack()) {
                throw new AmqpException("RabbitMQ no confirmó la publicación: " + confirm.reason());
            }
            if (correlationData.getReturned() != null) {
                throw new AmqpException("RabbitMQ no pudo dirigir el mensaje a la cola");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AmqpException("Se interrumpió la espera de confirmación", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new AmqpException("No se pudo confirmar la publicación en RabbitMQ", e);
        }
    }
}