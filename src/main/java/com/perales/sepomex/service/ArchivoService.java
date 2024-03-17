package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.repository.ArchivoRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ArchivoService implements ServiceGeneric<Archivo, Integer> {

    @Autowired
    private ArchivoRepository ArchivoRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public Archivo buscarPorId(Integer id) {
        return ArchivoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Archivo> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return ArchivoRepository.findAll( PageRequest.of( firstResult, size) );
    }
    
    @Transactional
    public Archivo guardar(Archivo entity) {
        return ArchivoRepository.save(entity);
    }
    
    @Transactional
    public Archivo actualizar(Archivo entity) {
        return ArchivoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Archivo borrar(Integer id) {
        Archivo Archivo = ArchivoRepository.findById(id).get();
        ArchivoRepository.deleteById(id);
        return Archivo;
    }
}
