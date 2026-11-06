package cl.duoc.bff_mesatech.controller;

import org.springframework.beans.factory.annotation.Value; // IMPORTANTE: Agregar este import
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class CatalogoProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    // ¡AQUÍ ESTÁ EL CAMBIO! Ahora lee la variable del application.properties
    @Value("${url.microservicio.catalogo}")
    private String msCatalogUrl;

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

    // --- NUEVAS RUTAS PARA CATEGORÍAS (EP2) ---

    @PostMapping("/v1/categorias")
    public ResponseEntity<String> crearCategoriaProxy(@RequestBody String categoriaJson) {
        String url = msCatalogUrl + "/api/v1/categorias";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(categoriaJson, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
}