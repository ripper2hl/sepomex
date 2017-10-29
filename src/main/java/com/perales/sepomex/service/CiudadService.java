package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Ciudad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CiudadService  implements ServiceGeneric<Ciudad, Integer> {
    public Ciudad buscarPorId(Integer id) {
        return null;
    }

    public List<Ciudad> buscarTodos(int page, int size) {
        return null;
    }

    public Ciudad guardar(Ciudad entity) {
        return null;
    }

    public Ciudad actualizar(Ciudad entity) {
        return null;
    }

    public Ciudad borrar(Integer id) {
        return null;
    }
}
