package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.CodigoPostal;
import com.perales.sepomex.repository.CodigoPostalRepository;
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
public class CodigoPostalService implements ServiceGeneric<CodigoPostal, Long> {

    @Autowired
    private CodigoPostalRepository codigoPostalRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public CodigoPostal buscarPorId(Long id) {
        return codigoPostalRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<CodigoPostal> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return codigoPostalRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public CodigoPostal guardar(CodigoPostal entity) {
        return codigoPostalRepository.save(entity);
    }
    
    @Transactional
    public CodigoPostal actualizar(CodigoPostal entity) {
        return codigoPostalRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public CodigoPostal borrar(Long id) {
        CodigoPostal codigoPostal = codigoPostalRepository.findById(id).get();
        codigoPostal.getColonias().forEach( colonia -> colonia.setCodigoPostal(null) );
        
        codigoPostal.getColoniasCodigoPostalAdministracionAsentamiento()
                .forEach(colonia -> colonia.setCodigoPostalAdministracionAsentamiento(null));
        
        codigoPostal.getColoniasCodigoPostalAdministracionAsentamientoOficina()
                .forEach(colonia -> colonia.setCodigoPostalAdministracionAsentamientoOficina(null));
        codigoPostalRepository.delete(codigoPostal);
        return codigoPostal;
    }
    
    @Transactional(readOnly = true)
    public CodigoPostal findByNombre(String nombre) {
        return codigoPostalRepository.findFirstByNombre(nombre);
    }
    
    public List<CodigoPostal> searchByName(String nombre){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(CodigoPostal.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( nombre)
                .createQuery();
    
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, CodigoPostal.class);
    
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
}