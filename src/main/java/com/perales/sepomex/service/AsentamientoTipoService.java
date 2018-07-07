package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.repository.AsentamientoTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;
    
    @Transactional(readOnly = true)
    public AsentamientoTipo buscarPorId(Integer id) {
        return asentamientoTipoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<AsentamientoTipo> buscarTodos(int page, int size) {
        Pageable pageable = new PageRequest(page, size);
        return asentamientoTipoRepository.findAll(pageable);
    }
    
    @Transactional
    public AsentamientoTipo guardar(AsentamientoTipo entity) {
        return asentamientoTipoRepository.save(entity);
    }
    
    @Transactional
    public AsentamientoTipo actualizar(AsentamientoTipo entity) {
        return asentamientoTipoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public AsentamientoTipo borrar(Integer id) {
        AsentamientoTipo asentamientoTipo = asentamientoTipoRepository.findById(id).get();
            asentamientoTipoRepository.deleteById(id);
        return asentamientoTipo;
    }
    
    @Transactional(readOnly = true)
    public AsentamientoTipo findBySepomexClave(String sepomexClave) {
        return asentamientoTipoRepository.findFirstBySepomexClave(sepomexClave);
    }
}
