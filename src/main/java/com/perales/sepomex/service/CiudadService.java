package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CiudadService  implements ServiceGeneric<Ciudad, Integer> {

    @Autowired
    private CiudadRepository ciudadRepository;

    public Ciudad buscarPorId(Integer id) {
        return ciudadRepository.findById(id).get();
    }

    public Page<Ciudad> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return ciudadRepository.findAll( PageRequest.of(firstResult, size ) );
    }

    public Ciudad guardar(Ciudad entity) {
        return ciudadRepository.save(entity);
    }

    public Ciudad actualizar(Ciudad entity) {
        return ciudadRepository.saveAndFlush(entity);
    }

    public Ciudad borrar(Integer id) {
        return null;
    }

    public Ciudad findFirstByNombreAndEstadoId(String nombre, Integer estadoId) {
        return ciudadRepository.findFirstByNombreAndEstadoId(nombre, estadoId);
    }
}
