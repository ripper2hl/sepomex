package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.ZonaTipo;
import com.perales.sepomex.repository.ZonaTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ZonaTipoService implements ServiceGeneric<ZonaTipo, Integer> {

    @Autowired
    private ZonaTipoRepository zonaTipoRepository;

    public ZonaTipo buscarPorId(Integer id) {
        return null;
    }

    public Page<ZonaTipo> buscarTodos(int page, int size) {
        return null;
    }

    public ZonaTipo guardar(ZonaTipo entity) {
        return zonaTipoRepository.save(entity);
    }

    public ZonaTipo actualizar(ZonaTipo entity) {
        return null;
    }

    public ZonaTipo borrar(Integer id) {
        return null;
    }

    public ZonaTipo findByNombre(String nombre) {
        return zonaTipoRepository.findFirstByNombre(nombre);
    }
}
