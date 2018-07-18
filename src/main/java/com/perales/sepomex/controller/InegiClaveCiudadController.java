package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.InegiClaveCiudad;
import com.perales.sepomex.service.InegiClaveCiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/inegiclaveciudad/")
public class InegiClaveCiudadController implements ControllerGeneric<InegiClaveCiudad, Integer>{
    
    @Autowired
    private InegiClaveCiudadService inegiClaveCiudadService;
    
    @GetMapping("/{id}")
    public InegiClaveCiudad buscarPorId(@PathVariable Integer id) {
        return inegiClaveCiudadService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<InegiClaveCiudad> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return inegiClaveCiudadService.buscarTodos(page,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InegiClaveCiudad guardar(@Validated @RequestBody InegiClaveCiudad entity) {
        return inegiClaveCiudadService.guardar(entity);
    }

    @PutMapping
    public InegiClaveCiudad actualizar(@Validated @RequestBody InegiClaveCiudad entity) {
        return inegiClaveCiudadService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public InegiClaveCiudad borrar(@PathVariable Integer id) {
        return inegiClaveCiudadService.borrar(id);
    }
}
