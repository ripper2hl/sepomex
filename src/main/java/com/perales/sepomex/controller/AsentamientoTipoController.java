package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.service.AsentamientoTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/asentamientotipo/")
public class AsentamientoTipoController implements ControllerGeneric<AsentamientoTipo, Integer> {
    
    @Autowired
    private AsentamientoTipoService asentamientoTipoService;
    
    @GetMapping("/{id}")
    public AsentamientoTipo buscarPorId(@PathVariable Integer id) {
        return asentamientoTipoService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<AsentamientoTipo> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return asentamientoTipoService.buscarTodos(page,size);
    }

    @PostMapping
    public AsentamientoTipo guardar( @RequestBody AsentamientoTipo entity) {
        return asentamientoTipoService.guardar(entity);
    }

    @PutMapping
    public AsentamientoTipo actualizar( @RequestBody AsentamientoTipo entity) {
        return asentamientoTipoService.actualizar(entity);
    }
    
    @DeleteMapping("/{id}")
    public AsentamientoTipo borrar( @PathVariable Integer id) {
        return asentamientoTipoService.borrar(id);
    }
}
