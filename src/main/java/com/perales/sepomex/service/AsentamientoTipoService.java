package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.repository.AsentamientoTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;

    public AsentamientoTipo buscarPorId(Integer id) {
        return null;
    }

    public List<AsentamientoTipo> buscarTodos(int page, int size) {
        return null;
    }

    public AsentamientoTipo guardar(AsentamientoTipo entity) {
        return asentamientoTipoRepository.save(entity);
    }

    public AsentamientoTipo actualizar(AsentamientoTipo entity) {
        return null;
    }

    public AsentamientoTipo borrar(Integer id) {
        return null;
    }

    public AsentamientoTipo findBySepomexClave(String sepomexClave) {
        return asentamientoTipoRepository.findFirstBySepomexClave(sepomexClave);
    }
}
