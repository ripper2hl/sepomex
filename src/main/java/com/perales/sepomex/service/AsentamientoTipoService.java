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

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class AsentamientoTipoService implements ServiceGeneric<AsentamientoTipo, Integer> {

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;
    
    @Autowired
    private EntityManagerFactory emf;
    
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
    
    @Transactional(readOnly = true)
    public List<AsentamientoTipo> searchByName(String name){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( emf.createEntityManager() );
        
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(AsentamientoTipo.class)
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
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, AsentamientoTipo.class);
        
        return jpaQuery.getResultList();
    }
}
