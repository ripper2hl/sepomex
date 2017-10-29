package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.ZonaTipo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZonaTipoService implements ServiceGeneric<ZonaTipo, Integer> {
    public ZonaTipo buscarPorId(Integer id) {
        return null;
    }

    public List<ZonaTipo> buscarTodos(int page, int size) {
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
