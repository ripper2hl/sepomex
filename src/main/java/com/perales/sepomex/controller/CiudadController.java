package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.service.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/ciudad/")
public class CiudadController implements ControllerGeneric<Ciudad, Integer> {
    
    @Autowired
    private CiudadService ciudadService;
    
    @GetMapping("/{id}")
    public Ciudad buscarPorId(@PathVariable Integer id) {
        return ciudadService.buscarPorId(id);
    }
    
    @GetMapping(params = {"page", "size"})
    public Page<Ciudad> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return ciudadService.buscarTodos(page, size);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ciudad guardar(@Validated @RequestBody Ciudad entity) {
        return ciudadService.guardar(entity);
    }
    
    @PutMapping
    public Ciudad actualizar(@Validated @RequestBody Ciudad entity) {
        return ciudadService.actualizar(entity);
    }
    
    @DeleteMapping("/{id}")
    public Ciudad borrar(@PathVariable Integer id) {
        return ciudadService.borrar(id);
    }
}
