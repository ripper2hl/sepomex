package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/estado/")
public class EstadoController implements ControllerGeneric<Estado, Integer>{
    
    @Autowired
    private EstadoService estadoService;
    
    public Estado buscarPorId(Integer id) {
        return null;
    }

    public Page<Estado> buscarTodos(int page, int size) {
        return null;
    }

    public Estado guardar(Estado entity) {
        return null;
    }

    public Estado actualizar(Estado entity) {
        return null;
    }

    public Estado borrar(Integer id) {
        return null;
    }
}
