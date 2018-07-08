package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/municipio/")
public class MunicipioController implements ControllerGeneric<Municipio, Integer>{
    @Autowired
    private MunicipioService municipioService;
    
    @GetMapping("/{id}")
    public Municipio buscarPorId(Integer id) {
        return municipioService.buscarPorId(id);
    }

    @GetMapping("/")
    public Page<Municipio> buscarTodos(int page, int size) {
        return municipioService.buscarTodos(page,size);
    }

    @PostMapping("/")
    public Municipio guardar(Municipio entity) {
        return municipioService.guardar(entity);
    }

    @PutMapping("/")
    public Municipio actualizar(Municipio entity) {
        return municipioService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public Municipio borrar( @PathVariable Integer id) {
        return municipioService.borrar(id);
    }
}
