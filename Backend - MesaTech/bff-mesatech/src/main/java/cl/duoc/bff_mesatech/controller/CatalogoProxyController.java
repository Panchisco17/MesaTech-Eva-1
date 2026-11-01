package cl.duoc.bff_mesatech.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api") // Cambiamos la ruta base para permitir múltiples versiones
public class CatalogoProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String msCatalogUrl = "http://localhost:8082"; // Puerto correcto de tu Catálogo

    // PROXY VERSIÓN 1: GET
    @GetMapping("/v1/productos")
    public ResponseEntity<String> obtenerProductosProxyV1() {
        String url = msCatalogUrl + "/api/v1/productos";
        return restTemplate.getForEntity(url, String.class);
    }

    // PROXY VERSIÓN 1: POST
    @PostMapping("/v1/productos")
    public ResponseEntity<String> crearProductoProxyV1(@RequestBody String productoJson) {
        String url = msCatalogUrl + "/api/v1/productos";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(productoJson, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    // PROXY VERSIÓN 2: GET (Con cálculo de IVA)
    @GetMapping("/v2/productos")
    public ResponseEntity<String> obtenerProductosProxyV2() {
        String url = msCatalogUrl + "/api/v2/productos";
        return restTemplate.getForEntity(url, String.class);
    }
}