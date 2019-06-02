package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.InegiClaveMunicipio;
import com.perales.sepomex.service.InegiClaveMunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/inegiclavemunicipio/")
public class InegiClaveMunicipioController implements ControllerGeneric<InegiClaveMunicipio, Integer>{
    
    @Autowired
    private InegiClaveMunicipioService inegiClaveMunicipioService;
    
    @GetMapping("/{id}")
    public InegiClaveMunicipio buscarPorId(@PathVariable Integer id) {
        return inegiClaveMunicipioService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<InegiClaveMunicipio> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return inegiClaveMunicipioService.buscarTodos(page,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InegiClaveMunicipio guardar(@Validated @RequestBody InegiClaveMunicipio entity) {
        return inegiClaveMunicipioService.guardar(entity);
    }

    @PutMapping
    public InegiClaveMunicipio actualizar(@Validated @RequestBody InegiClaveMunicipio entity) {
        return inegiClaveMunicipioService.actualizar(entity);
    }

    @DeleteMapping("/{id}")
    public InegiClaveMunicipio borrar(@PathVariable Integer id) {
        return inegiClaveMunicipioService.borrar(id);
    }
    
    @GetMapping(value = "/name/{name}")
    public List<InegiClaveMunicipio> searchByName(@PathVariable String name) {
        return inegiClaveMunicipioService.searchByName(name);
    }
}
