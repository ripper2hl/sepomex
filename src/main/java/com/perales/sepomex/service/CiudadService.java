package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.repository.CiudadRepository;
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
public class CiudadService  implements ServiceGeneric<Ciudad, Integer> {

    @Autowired
    private CiudadRepository ciudadRepository;
    
    @PersistenceContext
    private EntityManager em;
    
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
        ciudad.getColonias().forEach( colonia -> colonia.setCiudad(null));
        ciudad.getMunicipios().forEach(municipio -> municipio.setCiudad(null));
        ciudad.getCodigosPostales().forEach(codigoPostal -> codigoPostal.setCiudad(null));
        ciudadRepository.deleteById(id);
        return ciudad;
    }
    
    @Transactional(readOnly = true)
    public Ciudad findFirstByNombreAndEstadoId(String nombre, Integer estadoId) {
        return ciudadRepository.findFirstByNombreAndEstadoId(nombre, estadoId);
    }
    
    @Transactional(readOnly = true)
    public List<Ciudad> searchByName(String nombre){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Ciudad.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( nombre)
                .createQuery();
    
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Ciudad.class);
    
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
}
