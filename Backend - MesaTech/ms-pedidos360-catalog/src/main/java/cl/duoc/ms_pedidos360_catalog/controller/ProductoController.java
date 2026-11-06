package cl.duoc.ms_pedidos360_catalog.controller;

import cl.duoc.ms_pedidos360_catalog.model.Producto;
import cl.duoc.ms_pedidos360_catalog.publisher.ProductoPublisher;
import cl.duoc.ms_pedidos360_catalog.service.ProductoService;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private final ProductoService service;
    private final ProductoPublisher publisher;

    public ProductoController(ProductoService service, ProductoPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @GetMapping("/v1/productos")
    public List<Producto> obtenerTodosV1() {
        return service.obtenerTodos();
    }

    @PostMapping("/v1/productos")
    public Producto crearProductoV1(@RequestBody Producto producto) {
        Producto productoGuardado;
        try {
            productoGuardado = service.crearProducto(producto);
            publisher.publicarProducto(productoGuardado);
        } catch (AmqpException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Se guardó en MySQL, pero falló la publicación en RabbitMQ", e
            );
        }
        return productoGuardado;
    }

    @GetMapping("/v2/productos")
    public List obtenerTodosV2() {
        List<Producto> productos = service.obtenerTodos();

        return productos.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("nombre", p.getNombre());
            map.put("precio_neto", p.getPrecio());
            map.put("moneda", "CLP");
            map.put("precio_iva", Math.round(p.getPrecio() * 1.19));
            return map;
        }).collect(Collectors.toList());
    }
}