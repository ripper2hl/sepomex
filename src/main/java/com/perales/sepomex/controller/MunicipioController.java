package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/municipio/")
public class MunicipioController implements ControllerGeneric<Municipio, Integer>{
    @Autowired
    private MunicipioService municipioService;
    public Municipio buscarPorId(Integer id) {
        return null;
    }

    public Page<Municipio> buscarTodos(int page, int size) {
        return null;
    }

    public Municipio guardar(Municipio entity) {
        return null;
    }

    public Municipio actualizar(Municipio entity) {
        return null;
    }

    public Municipio borrar(Integer id) {
        return null;
    }
}
