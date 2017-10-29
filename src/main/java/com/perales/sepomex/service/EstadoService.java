package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Estado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService implements ServiceGeneric<Estado, Integer> {
    public Estado buscarPorId(Integer id) {
        return null;
    }

    public List<Estado> buscarTodos(int page, int size) {
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
