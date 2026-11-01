package cl.duoc.ms_pedidos360_catalog.controller;

import cl.duoc.ms_pedidos360_catalog.model.Producto;
import cl.duoc.ms_pedidos360_catalog.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api") // Modificamos la ruta base para permitir versiones
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // VERSIÓN 1: Mantiene el contrato original (GET)
    @GetMapping("/v1/productos")
    public List<Producto> obtenerTodosV1() {
        return service.obtenerTodos();
    }

    // VERSIÓN 1: Mantiene el contrato original (POST)
    @PostMapping("/v1/productos")
    public Producto crearProductoV1(@RequestBody Producto producto) {
        return service.crearProducto(producto);
    }

    // VERSIÓN 2: Extiende el contrato agregando el cálculo de IVA y moneda
    @GetMapping("/v2/productos")
    public List<Map<String, Object>> obtenerTodosV2() {
        List<Producto> productos = service.obtenerTodos();
        
        return productos.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("nombre", p.getNombre());
            map.put("precio_neto", p.getPrecio());
            map.put("moneda", "CLP");
            map.put("precio_iva", Math.round(p.getPrecio() * 1.19)); // Regla de negocio adicional
            return map;
        }).collect(Collectors.toList());
    }
}