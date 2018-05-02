package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.service.ColoniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("v1/colonia/")
public class ColoniaController implements ControllerGeneric<Colonia, Integer>{

    @Autowired
    private ColoniaService coloniaService;

    @GetMapping("/")
    public Colonia buscarPorId(Integer id) {
        return coloniaService.buscarPorId(id);
    }

    public List<Colonia> buscarTodos(int page, int size) {
        return null;
    }

    public Colonia guardar(Colonia entity) {
        return null;
    }

    public Colonia actualizar(Colonia entity) {
        return null;
    }

    public Colonia borrar(Integer id) {
        return null;
    }
    
    @GetMapping("/carga")
    public boolean cargaMasiva() throws IOException {
        return coloniaService.cargaMasiva("/tmp/sepomex.txt");
    }
}
