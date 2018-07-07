package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.service.ColoniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("v1/colonia/")
public class ColoniaController implements ControllerGeneric<Colonia, Integer>{

    @Autowired
    private ColoniaService coloniaService;

    @GetMapping("/{id}")
    public Colonia buscarPorId(@PathVariable Integer id) {
        return coloniaService.buscarPorId(id);
    }
    
    @GetMapping("/")
    public Page<Colonia> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return coloniaService.buscarTodos(page, size) ;
    }

    @PostMapping
    public Colonia guardar(@RequestBody Colonia entity) {
        return coloniaService.guardar(entity);
    }

    @PutMapping("/")
    public Colonia actualizar(@RequestBody Colonia entity) {
        return coloniaService.actualizar(entity);
    }

    @DeleteMapping("/")
    public Colonia borrar(Integer id) {
        return coloniaService.borrar(id);
    }
    
    @GetMapping("/carga")
    public boolean cargaMasiva() throws IOException {
        return coloniaService.cargaMasiva("/tmp/sepomex.txt");
    }
}
