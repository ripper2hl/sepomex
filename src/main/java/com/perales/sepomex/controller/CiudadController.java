package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.service.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("v1/ciudad/")
public class CiudadController implements ControllerGeneric<Ciudad, Integer> {
    
    @Autowired
    private CiudadService ciudadService;
    
    @GetMapping("/{id}")
    public Ciudad buscarPorId(Integer id) {
        return ciudadService.buscarPorId(id);
    }
    
    @GetMapping("/")
    public Page<Ciudad> buscarTodos(int page, int size) {
        return ciudadService.buscarTodos(page, size);
    }
    
    @PostMapping
    public Ciudad guardar(Ciudad entity) {
        return ciudadService.guardar(entity);
    }
    
    @PutMapping("/")
    public Ciudad actualizar(Ciudad entity) {
        return ciudadService.actualizar(entity);
    }
    
    @DeleteMapping("/")
    public Ciudad borrar(Integer id) {
        return ciudadService.borrar(id);
    }
}
