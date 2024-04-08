package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.InegiClaveCiudad;
import com.perales.sepomex.repository.InegiClaveCiudadRepository;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InegiClaveCiudadService implements ServiceGeneric<InegiClaveCiudad, Integer> {

    @Autowired
    private InegiClaveCiudadRepository inegiClaveCiudadRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public InegiClaveCiudad buscarPorId(Integer id) {
        return inegiClaveCiudadRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<InegiClaveCiudad> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return inegiClaveCiudadRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public InegiClaveCiudad guardar(InegiClaveCiudad entity) {
        return inegiClaveCiudadRepository.save(entity);
    }
    
    @Transactional
    public InegiClaveCiudad actualizar(InegiClaveCiudad entity) {
        return inegiClaveCiudadRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public InegiClaveCiudad borrar(Integer id) {
        InegiClaveCiudad inegiClaveCiudad = inegiClaveCiudadRepository.findById(id).get();
        inegiClaveCiudad.getColonias().forEach(colonia -> colonia.setInegiClaveCiudad(null));
        inegiClaveCiudadRepository.delete(inegiClaveCiudad);
        return inegiClaveCiudad;
    }
    
    @Transactional(readOnly = true)
    public InegiClaveCiudad findFirstByNombre(String nombre) {
        return inegiClaveCiudadRepository.findFirstByNombre(nombre);
    }
}
