package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoService implements ServiceGeneric<Estado, Integer> {

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Transactional(readOnly = true)
    public Estado buscarPorId(Integer id) {
        return estadoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Estado> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return estadoRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public Estado guardar(Estado entity) {
        return estadoRepository.save(entity);
    }
    
    @Transactional
    public Estado actualizar(Estado entity) {
        return estadoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Estado borrar(Integer id) {
        Estado estado = estadoRepository.findById(id).get();
        estadoRepository.delete(estado);
        return estado;
    }
    
    @Transactional(readOnly = true)
    public Estado findByInegiClave(String inegiClave) {
        return estadoRepository.findFirstByInegiClave(inegiClave);
    }
}
