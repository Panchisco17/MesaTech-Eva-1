package cl.duoc.ms_pedidos360_catalog.service;

import cl.duoc.ms_pedidos360_catalog.entity.Categoria;
import cl.duoc.ms_pedidos360_catalog.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> obtenerTodas() {
        return repository.findAll();
    }

    public Categoria crearCategoria(Categoria categoria) {
        return repository.save(categoria);
    }
}