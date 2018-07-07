package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.repository.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CiudadService  implements ServiceGeneric<Ciudad, Integer> {

    @Autowired
    private CiudadRepository ciudadRepository;
    
    @Transactional(readOnly = true)
    public Ciudad buscarPorId(Integer id) {
        return ciudadRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Ciudad> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return ciudadRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public Ciudad guardar(Ciudad entity) {
        return ciudadRepository.save(entity);
    }
    
    @Transactional
    public Ciudad actualizar(Ciudad entity) {
        return ciudadRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Ciudad borrar(Integer id) {
        Ciudad ciudad = ciudadRepository.findById(id).get();
        ciudadRepository.deleteById(id);
        return ciudad;
    }
    
    @Transactional(readOnly = true)
    public Ciudad findFirstByNombreAndEstadoId(String nombre, Integer estadoId) {
        return ciudadRepository.findFirstByNombreAndEstadoId(nombre, estadoId);
    }
}
