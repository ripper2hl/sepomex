package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.repository.AsentamientoTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;

    public AsentamientoTipo buscarPorId(Integer id) {
        return asentamientoTipoRepository.findById(id).get();
    }

    public Page<AsentamientoTipo> buscarTodos(int page, int size) {
        Pageable pageable = new PageRequest(page, size);
        return asentamientoTipoRepository.findAll(pageable);
    }

    public AsentamientoTipo guardar(AsentamientoTipo entity) {
        return asentamientoTipoRepository.save(entity);
    }

    public AsentamientoTipo actualizar(AsentamientoTipo entity) {
        return asentamientoTipoRepository.saveAndFlush(entity);
    }

    public AsentamientoTipo borrar(Integer id) {
        AsentamientoTipo asentamientoTipo = asentamientoTipoRepository.findById(id).get();
            asentamientoTipoRepository.deleteById(id);
        return asentamientoTipo;
    }

    public AsentamientoTipo findBySepomexClave(String sepomexClave) {
        return asentamientoTipoRepository.findFirstBySepomexClave(sepomexClave);
    }
}
