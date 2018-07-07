package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.service.AsentamientoTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/asentamientotipo/")
public class AsentamientoTipoController implements ControllerGeneric<AsentamientoTipo, Integer> {
    
    @Autowired
    private AsentamientoTipoService asentamientoTipoService;
    
    public AsentamientoTipo buscarPorId(Integer id) {
        return null;
    }

    public Page<AsentamientoTipo> buscarTodos(int page, int size) {
        return null;
    }

    public AsentamientoTipo guardar(AsentamientoTipo entity) {
        return null;
    }

    public AsentamientoTipo actualizar(AsentamientoTipo entity) {
        return null;
    }

    public AsentamientoTipo borrar(Integer id) {
        return null;
    }
}
