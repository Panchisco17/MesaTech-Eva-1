package cl.duoc.ms_pedidos360_orders.service;

import cl.duoc.ms_pedidos360_orders.model.Solicitud;
import cl.duoc.ms_pedidos360_orders.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
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
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        // Aseguramos el inicio del flujo según la rúbrica
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

        // Regla de negocio obligatoria: No pasar a RESUELTA si no está EN_PROCESO
        if ("RESUELTA".equals(estadoLimpio) && !"EN_PROCESO".equals(estadoActual)) {
            throw new IllegalStateException("Regla de negocio: Una solicitud no puede pasar a RESUELTA si antes no se encuentra EN_PROCESO.");
        }

        existente.setEstado(estadoLimpio); 
        return repository.save(existente);
    }

    public List<Solicitud> obtenerPorUsuario(String usuarioSolicitante) {
        return repository.findByUsuarioSolicitante(usuarioSolicitante);
    }
}