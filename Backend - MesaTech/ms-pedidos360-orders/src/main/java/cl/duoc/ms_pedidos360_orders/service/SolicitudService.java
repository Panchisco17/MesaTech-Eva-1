package cl.duoc.ms_pedidos360_orders.service;

import cl.duoc.ms_pedidos360_orders.model.Solicitud;
import cl.duoc.ms_pedidos360_orders.repository.SolicitudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository repository;

    public SolicitudService(SolicitudRepository repository) {
        this.repository = repository;
    }

    public List<Solicitud> obtenerTodas() {
        return (List<Solicitud>) repository.findAll();
    }

    public Solicitud obtenerPorId(Long id) {
        // Lanzar un 404 explícito si el ID no existe, evitando el error 500.
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada")
        );
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        if (solicitud.getEstado() == null || solicitud.getEstado().isEmpty() || "PENDIENTE".equalsIgnoreCase(solicitud.getEstado())) {
            solicitud.setEstado("CREADA");
        }
        return repository.save(solicitud);
    }

    public Solicitud actualizarSolicitudCompleta(Long id, Solicitud solicitudActualizada) {
        Solicitud existente = obtenerPorId(id);
        solicitudActualizada.setId(existente.getId());
        return repository.save(solicitudActualizada);
    }

    public void eliminarSolicitud(Long id) {
        repository.deleteById(id);
    }

    public Solicitud actualizarEstado(Long id, String nuevoEstado) {
        Solicitud existente = obtenerPorId(id);
        String estadoActual = existente.getEstado();
        String estadoLimpio = nuevoEstado.trim().toUpperCase();

        // Regla de negocio estricta solo para "RESUELTA"
        if ("RESUELTA".equals(estadoLimpio) && !"EN_PROCESO".equals(estadoActual)) {
            // Lanzamos un 400 Bad Request que Postman interpretará limpiamente
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Regla de negocio: Una solicitud no puede pasar a RESUELTA si antes no se encuentra EN_PROCESO.");
        }

        existente.setEstado(estadoLimpio);
        return repository.save(existente);
    }

    public List<Solicitud> obtenerPorUsuario(String usuarioSolicitante) {
        return repository.findByUsuarioSolicitante(usuarioSolicitante);
    }
}