package cl.duoc.bff_mesatech.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class SolicitudProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String msOrdersUrl = "http://localhost:8081";

    @GetMapping("/api/v1/solicitudes")
    public ResponseEntity<String> listarSolicitudesProxy() {
        String url = msOrdersUrl + "/v1/solicitudes";
        return restTemplate.getForEntity(url, String.class);
    }

    @GetMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> obtenerSolicitudPorIdProxy(@PathVariable Long id) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        return restTemplate.getForEntity(url, String.class);
    }

    // --- ¡NUEVO MÉTODO AGREGADO! ---
    // Este puente permite que React pregunte por las solicitudes de un usuario específico
    @GetMapping("/api/v1/solicitudes/usuario/{email}")
    public ResponseEntity<String> listarSolicitudesPorUsuarioProxy(@PathVariable String email) {
        String url = msOrdersUrl + "/v1/solicitudes/usuario/" + email;
        return restTemplate.getForEntity(url, String.class);
    }
    // -------------------------------

    @PostMapping("/api/v1/solicitudes")
    public ResponseEntity<String> crearSolicitudProxy(@RequestBody String solicitudJson) {
        String url = msOrdersUrl + "/v1/solicitudes";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(solicitudJson, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @PutMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> actualizarSolicitudCompletaProxy(@PathVariable Long id, @RequestBody String solicitudJson) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(solicitudJson, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }

    @DeleteMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> eliminarSolicitudProxy(@PathVariable Long id) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }

    @PutMapping("/api/v1/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoV1Proxy(@PathVariable Long id, @RequestBody String estado) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id + "/estado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(estado, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }

    @PutMapping("/api/v2/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoV2Proxy(@PathVariable Long id, @RequestBody String dto) {
        String url = msOrdersUrl + "/v2/solicitudes/" + id + "/estado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(dto, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
    }
}