package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    public AsentamientoTipo buscarPorId(Integer id) {
        return null;
    }

    public List<AsentamientoTipo> buscarTodos(int page, int size) {
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
