package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.AsentamientoTipo;
import com.perales.sepomex.repository.AsentamientoTipoRepository;
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
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public AsentamientoTipo buscarPorId(Integer id) {
        return asentamientoTipoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<AsentamientoTipo> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return asentamientoTipoRepository.findAll( PageRequest.of( firstResult, size) );
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
        asentamientoTipo.getColonias().forEach( colonia -> colonia.setAsentamientoTipo(null) );
        asentamientoTipoRepository.deleteById(id);
        return asentamientoTipo;
    }
    
    @Transactional(readOnly = true)
    public AsentamientoTipo findBySepomexClave(String sepomexClave) {
        return asentamientoTipoRepository.findFirstBySepomexClave(sepomexClave);
    }
    
    public List<AsentamientoTipo> searchByName(String nombre){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(AsentamientoTipo.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( nombre)
                .createQuery();
    
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, AsentamientoTipo.class);
        
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
}
