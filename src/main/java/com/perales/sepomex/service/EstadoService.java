package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Estado;
import com.perales.sepomex.repository.EstadoRepository;
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
public class EstadoService implements ServiceGeneric<Estado, Integer> {

    @Autowired
    private EstadoRepository estadoRepository;
    
    @PersistenceContext
    private EntityManager em;
    
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
        estado.getColonias().forEach(colonia -> colonia.setEstado(null));
        estado.getMunicipios().forEach(municipio -> municipio.setEstado(null));
        estado.getCiudades().forEach( ciudad -> ciudad.setEstado(null) );
        estado.getCodigosPostales().forEach(codigoPostal -> codigoPostal.setEstado(null));
        estadoRepository.delete(estado);
        return estado;
    }
    
    @Transactional(readOnly = true)
    public Estado findByInegiClave(String inegiClave) {
        return estadoRepository.findFirstByInegiClave(inegiClave);
    }
    
    public List<Estado> searchByName(String nombre){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Estado.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( nombre)
                .createQuery();
    
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Estado.class);
    
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
}
