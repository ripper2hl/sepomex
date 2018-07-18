package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/estado/")
public class EstadoController implements ControllerGeneric<Estado, Integer>{
    
    @Autowired
    private EstadoService estadoService;
    
    @GetMapping("/{id}")
    public Estado buscarPorId(Integer id) {
        return estadoService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Estado> buscarTodos(int page, int size) {
        return estadoService.buscarTodos(page,size);
    }

    @PostMapping
    public Estado guardar(Estado entity) {
        return estadoService.guardar(entity);
    }

    @PutMapping
    public Estado actualizar(Estado entity) {
        return estadoService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public Estado borrar(@PathVariable Integer id) {
        return estadoService.borrar(id);
    }
}
