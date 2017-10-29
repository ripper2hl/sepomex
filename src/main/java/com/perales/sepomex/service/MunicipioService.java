package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Municipio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService implements ServiceGeneric<Municipio, Integer> {
    public Municipio buscarPorId(Integer id) {
        return null;
    }

    public List<Municipio> buscarTodos(int page, int size) {
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
