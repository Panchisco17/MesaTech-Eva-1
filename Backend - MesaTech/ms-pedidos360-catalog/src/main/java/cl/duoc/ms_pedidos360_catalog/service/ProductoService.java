package cl.duoc.ms_pedidos360_catalog.service;

import cl.duoc.ms_pedidos360_catalog.model.Producto;
import cl.duoc.ms_pedidos360_catalog.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> obtenerTodos() {
        return repository.findAll();
    }

    public Producto crearProducto(Producto producto) {
        return repository.save(producto);
    }
}