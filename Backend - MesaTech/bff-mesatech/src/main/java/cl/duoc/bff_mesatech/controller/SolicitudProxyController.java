package cl.duoc.bff_mesatech.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException; // <-- Importante para atrapar el 400
import org.springframework.web.client.RestTemplate;

@RestController
public class SolicitudProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${url.microservicio.solicitudes}")
    private String msOrdersUrl;

    @GetMapping("/api/v1/solicitudes")
    public ResponseEntity<String> listarSolicitudesProxy() {
        String url = msOrdersUrl + "/v1/solicitudes";
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> obtenerSolicitudPorIdProxy(@PathVariable Long id) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/api/v1/solicitudes/usuario/{email}")
    public ResponseEntity<String> listarSolicitudesPorUsuarioProxy(@PathVariable String email) {
        String url = msOrdersUrl + "/v1/solicitudes/usuario/" + email;
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/api/v1/solicitudes")
    public ResponseEntity<String> crearSolicitudProxy(@RequestBody String solicitudJson) {
        String url = msOrdersUrl + "/v1/solicitudes";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(solicitudJson, headers);
        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PutMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> actualizarSolicitudCompletaProxy(@PathVariable Long id, @RequestBody String solicitudJson) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(solicitudJson, headers);
        try {
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @DeleteMapping("/api/v1/solicitudes/{id}")
    public ResponseEntity<String> eliminarSolicitudProxy(@PathVariable Long id) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id;
        try {
            return restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PutMapping("/api/v1/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoV1Proxy(@PathVariable Long id, @RequestBody String estado) {
        String url = msOrdersUrl + "/v1/solicitudes/" + id + "/estado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(estado, headers);

        try {
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            // El BFF atrapa el 400 Bad Request del microservicio y lo envía limpio a Postman
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PutMapping("/api/v2/solicitudes/{id}/estado")
    public ResponseEntity<String> actualizarEstadoV2Proxy(@PathVariable Long id, @RequestBody String dto) {
        String url = msOrdersUrl + "/v2/solicitudes/" + id + "/estado";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(dto, headers);

        try {
            return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}