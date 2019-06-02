package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/estado/")
public class EstadoController implements ControllerGeneric<Estado, Integer>{
    
    @Autowired
    private EstadoService estadoService;
    
    @GetMapping("/{id}")
    public Estado buscarPorId(@PathVariable Integer id) {
        return estadoService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Estado> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return estadoService.buscarTodos(page,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado guardar(@Validated @RequestBody Estado entity) {
        return estadoService.guardar(entity);
    }

    @PutMapping
    public Estado actualizar(@Validated @RequestBody Estado entity) {
        return estadoService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public Estado borrar(@PathVariable Integer id) {
        return estadoService.borrar(id);
    }
    
    @GetMapping(value = "/name/{name}")
    public List<Estado> searchByName(@PathVariable String name) {
        return estadoService.searchByName(name);
    }
}
