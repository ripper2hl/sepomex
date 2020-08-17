package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.CodigoPostal;
import com.perales.sepomex.service.CodigoPostalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/codigopostal/")
public class CodigoPostalController implements ControllerGeneric<CodigoPostal, Integer>{

    private final CodigoPostalService codigoPostalService;
    
    @Autowired
    public CodigoPostalController(CodigoPostalService codigoPostalService) {
        this.codigoPostalService = codigoPostalService;
    }
    
    @GetMapping("/{id}")
    public CodigoPostal buscarPorId(@PathVariable Integer id) {
        return codigoPostalService.buscarPorId(id.longValue());
    }
    
    @GetMapping(params = {"page", "size"})
    public Page<CodigoPostal> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return codigoPostalService.buscarTodos(page, size) ;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CodigoPostal guardar(@Validated @RequestBody CodigoPostal entity) {
        return codigoPostalService.guardar(entity);
    }

    @PutMapping
    public CodigoPostal actualizar(@Validated @RequestBody CodigoPostal entity) {
        return codigoPostalService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public CodigoPostal borrar(@PathVariable Integer id) {
        return codigoPostalService.borrar(id.longValue());
    }
    
    @GetMapping(value = "/name/{name}")
    public List<CodigoPostal> searchByName(@PathVariable String name) {
        return codigoPostalService.searchByName(name);
    }
}
