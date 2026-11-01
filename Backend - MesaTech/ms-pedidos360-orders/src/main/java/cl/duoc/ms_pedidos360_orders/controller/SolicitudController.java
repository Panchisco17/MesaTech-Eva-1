package cl.duoc.ms_pedidos360_orders.controller;

import cl.duoc.ms_pedidos360_orders.model.Solicitud;
import cl.duoc.ms_pedidos360_orders.service.SolicitudService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class SolicitudController {
    
    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @GetMapping("/v1/solicitudes")
    public List<Solicitud> listarSolicitudes() {
        return service.obtenerTodas();
    }

    @GetMapping("/v1/solicitudes/{id}")
    public Solicitud obtenerSolicitudPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/v1/solicitudes/usuario/{email}")
    public List<Solicitud> listarSolicitudesPorUsuario(@PathVariable String email) {
        return service.obtenerPorUsuario(email);
    }

    @PostMapping("/v1/solicitudes")
    public Solicitud crearSolicitud(@RequestBody Solicitud nuevaSolicitud) {
        return service.crearSolicitud(nuevaSolicitud);
    }

    @PutMapping("/v1/solicitudes/{id}")
    public Solicitud actualizarSolicitud(@PathVariable Long id, @RequestBody Solicitud solicitud) {
        return service.actualizarSolicitudCompleta(id, solicitud);
    }

    @DeleteMapping("/v1/solicitudes/{id}")
    public void eliminarSolicitud(@PathVariable Long id) {
        service.eliminarSolicitud(id);
    }

    @PutMapping("/v1/solicitudes/{id}/estado")
    public Solicitud actualizarEstadoV1(@PathVariable Long id, @RequestBody String estado) {
        return service.actualizarEstado(id, estado);
    }

    @PutMapping("/v2/solicitudes/{id}/estado")
    public Solicitud actualizarEstadoV2(@PathVariable Long id, @RequestBody EstadoDTO dto) {
        return service.actualizarEstado(id, dto.getEstado());
    }
    
    public static class EstadoDTO {
        private String estado;
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}