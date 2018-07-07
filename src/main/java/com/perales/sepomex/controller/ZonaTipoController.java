package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.ZonaTipo;
import com.perales.sepomex.service.ZonaTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/zonatipo/")
public class ZonaTipoController implements ControllerGeneric<ZonaTipo, Integer>{
    
    @Autowired
    private ZonaTipoService zonaTipoService;
    
    public ZonaTipo buscarPorId(Integer id) {
        return null;
    }

    public Page<ZonaTipo> buscarTodos(int page, int size) {
        return null;
    }

    public ZonaTipo guardar(ZonaTipo entity) {
        return null;
    }

    public ZonaTipo actualizar(ZonaTipo entity) {
        return null;
    }

    public ZonaTipo borrar(Integer id) {
        return null;
    }
}
