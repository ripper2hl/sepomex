package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Municipio;
import com.perales.sepomex.repository.MunicipioRepository;
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
public class MunicipioService implements ServiceGeneric<Municipio, Integer> {

    @Autowired
    private MunicipioRepository municipioRepository;
    
    @Autowired
    private EntityManagerFactory emf;
    
    @Transactional(readOnly = true)
    public Municipio buscarPorId(Integer id) {
        return municipioRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Municipio> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return municipioRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public Municipio guardar(Municipio entity) {
        return municipioRepository.save(entity);
    }
    
    @Transactional
    public Municipio actualizar(Municipio entity) {
        return municipioRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Municipio borrar(Integer id) {
        Municipio municipio = municipioRepository.findById(id).get();
        municipio.getColonias().forEach(colonia -> colonia.setMunicipio(null));
        municipio.getCodigosPostales().forEach(codigoPostal -> codigoPostal.setMunicipio(null));
        municipioRepository.delete(municipio);
        return municipio;
    }
    
    @Transactional(readOnly = true)
    public Municipio findFirstByNombreAndEstadoId(String nombre, Integer estadoId) {
        return municipioRepository.findFirstByNombreAndEstadoId(nombre,estadoId);
    }
    
    @Transactional(readOnly = true)
    public Page<Municipio> findByEstadoId(Integer estadoId, int page, int size){
        int firstResult = page * size;
        return municipioRepository.findByEstadoIdOrderByNombre(estadoId, PageRequest.of(firstResult, size ));
    }
    
    @Transactional(readOnly = true)
    public List<Municipio> searchByName(String name){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( emf.createEntityManager() );
        
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Municipio.class)
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
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Municipio.class);
        
        return jpaQuery.getResultList();
    }
}
