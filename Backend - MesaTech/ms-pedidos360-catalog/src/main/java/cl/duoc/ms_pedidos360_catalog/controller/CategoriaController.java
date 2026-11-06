package cl.duoc.ms_pedidos360_catalog.controller;

import cl.duoc.ms_pedidos360_catalog.entity.Categoria;
import cl.duoc.ms_pedidos360_catalog.publisher.CategoriaPublisher;
import cl.duoc.ms_pedidos360_catalog.service.CategoriaService;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriaController {

    private final CategoriaService service;
    private final CategoriaPublisher publisher;

    public CategoriaController(CategoriaService service, CategoriaPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @GetMapping("/v1/categorias")
    public List<Categoria> obtenerTodas() {
        return service.obtenerTodas();
    }

    @PostMapping("/v1/categorias")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        Categoria categoriaGuardada;
        try {
            categoriaGuardada = service.crearCategoria(categoria);
            publisher.publicarCategoria(categoriaGuardada);
        } catch (AmqpException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Se guardó en MySQL, pero falló la publicación en RabbitMQ", e
            );
        }
        return categoriaGuardada;
    }
}