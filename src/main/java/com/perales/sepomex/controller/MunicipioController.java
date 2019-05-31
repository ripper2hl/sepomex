package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/municipio/")
public class MunicipioController implements ControllerGeneric<Municipio, Integer>{
    @Autowired
    private MunicipioService municipioService;
    
    @GetMapping("/{id}")
    public Municipio buscarPorId( @PathVariable Integer id) {
        return municipioService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Municipio> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return municipioService.buscarTodos(page,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Municipio guardar(@Validated @RequestBody Municipio entity) {
        return municipioService.guardar(entity);
    }

    @PutMapping
    public Municipio actualizar(@Validated @RequestBody Municipio entity) {
        return municipioService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public Municipio borrar( @PathVariable Integer id) {
        return municipioService.borrar(id);
    }
    
    @GetMapping(value = "/estado/{id}", params = {"page", "size"})
    public Page<Municipio> findByEstadoId( @PathVariable Integer id, @RequestParam int page, @RequestParam int size) {
        return municipioService.findByEstadoId(id, page, size);
    }
}
