package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.ZonaTipo;
import com.perales.sepomex.service.ZonaTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/zonatipo/")
public class ZonaTipoController implements ControllerGeneric<ZonaTipo, Integer>{
    
    @Autowired
    private ZonaTipoService zonaTipoService;
    
    @GetMapping("/{id}")
    public ZonaTipo buscarPorId( @PathVariable Integer id) {
        return zonaTipoService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<ZonaTipo> buscarTodos( @RequestParam int page, @RequestParam int size) {
        return zonaTipoService.buscarTodos(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaTipo guardar(@Validated @RequestBody ZonaTipo entity) {
        return zonaTipoService.guardar(entity);
    }

    @PutMapping
    public ZonaTipo actualizar(@Validated @RequestBody ZonaTipo entity) {
        return zonaTipoService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public ZonaTipo borrar(@PathVariable Integer id) {
        return zonaTipoService.borrar(id);
    }
}
