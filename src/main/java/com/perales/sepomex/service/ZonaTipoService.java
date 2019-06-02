package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.ZonaTipo;
import com.perales.sepomex.repository.ZonaTipoRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class ZonaTipoService implements ServiceGeneric<ZonaTipo, Integer> {

    @Autowired
    private ZonaTipoRepository zonaTipoRepository;
    
    @Autowired
    private EntityManagerFactory emf;
    
    @Transactional(readOnly = true)
    public ZonaTipo buscarPorId(Integer id) {
        return zonaTipoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<ZonaTipo> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return zonaTipoRepository.findAll(PageRequest.of(firstResult , size) );
    }
    
    @Transactional
    public ZonaTipo guardar(ZonaTipo entity) {
        return zonaTipoRepository.save(entity);
    }
    
    @Transactional
    public ZonaTipo actualizar(ZonaTipo entity) {
        return zonaTipoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public ZonaTipo borrar(Integer id) {
        ZonaTipo zonaTipo = zonaTipoRepository.findById(id).get();
        zonaTipo.getColonias().forEach(colonia -> colonia.setZonaTipo(null));
        zonaTipoRepository.delete(zonaTipo);
        return zonaTipo;
    }
    
    @Transactional(readOnly = true)
    public ZonaTipo findByNombre(String nombre) {
        return zonaTipoRepository.findFirstByNombre(nombre);
    }
    
    @Transactional(readOnly = true)
    public List<ZonaTipo> searchByName(String name){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( emf.createEntityManager() );
        
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(ZonaTipo.class)
                .get();
        
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(2)
                .withPrefixLength(0)
                .onField("nombre")
                .matching(name)
                .createQuery();
        
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, ZonaTipo.class);
        
        return jpaQuery.getResultList();
    }
}
