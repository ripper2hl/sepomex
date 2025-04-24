package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.service.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Hidden;

import java.io.IOException;
import java.util.List;

@Hidden
@RestController
@RequestMapping("v1/archivo/")
public class ArchivoController implements ControllerGeneric<Archivo, Integer>{
    
    @Autowired
    private ArchivoService archivoService;
    
    @GetMapping("/{id}")
    public Archivo buscarPorId(@PathVariable Integer id) {
        return archivoService.buscarPorId(id);
    }

    @GetMapping(params = {"page", "size"})
    public Page<Archivo> buscarTodos(@RequestParam int page, @RequestParam int size) {
        return archivoService.buscarTodos(page,size);
    }

    @PostMapping
    @Hidden
    @ResponseStatus(HttpStatus.CREATED)
    public Archivo guardar(@Validated @RequestBody Archivo entity) {
        return archivoService.guardar(entity);
    }

    @Hidden
    @PutMapping
    public Archivo actualizar(@Validated @RequestBody Archivo entity) {
        return archivoService.actualizar(entity);
    }

    @Hidden
    @DeleteMapping("/{id}")
    public Archivo borrar(@PathVariable Integer id) {
        return archivoService.borrar(id);
    }

    @Hidden
    @PostMapping(value = "/carga", produces = "application/json; charset=UTF-8", consumes = "multipart/form-data;charset=UTF-8")
    public boolean cargaMasiva( @RequestPart("file") MultipartFile file ) throws IOException {
        return archivoService.cargaMasiva( file );
    }

    @Hidden
    @PostMapping(value = "/actualizacion", produces = "application/json; charset=UTF-8", consumes = "multipart/form-data;charset=UTF-8")
    public boolean actualizacionMasiva( @RequestPart("file") MultipartFile file ) throws IOException {
        return archivoService.actualizacionMasiva( file );
    }
    @GetMapping(value = "/index")
    public Boolean index() throws InterruptedException {
        archivoService.indexDb();
        return true;
    }
}
