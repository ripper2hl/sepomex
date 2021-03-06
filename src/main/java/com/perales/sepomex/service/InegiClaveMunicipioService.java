package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.InegiClaveMunicipio;
import com.perales.sepomex.repository.InegiClaveMunicipioRepository;
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
public class InegiClaveMunicipioService implements ServiceGeneric<InegiClaveMunicipio, Integer> {

    @Autowired
    private InegiClaveMunicipioRepository inegiClaveMunicipioRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public InegiClaveMunicipio buscarPorId(Integer id) {
        return inegiClaveMunicipioRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<InegiClaveMunicipio> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return inegiClaveMunicipioRepository.findAll( PageRequest.of(firstResult, size ) );
    }
    
    @Transactional
    public InegiClaveMunicipio guardar(InegiClaveMunicipio entity) {
        return inegiClaveMunicipioRepository.save(entity);
    }
    
    @Transactional
    public InegiClaveMunicipio actualizar(InegiClaveMunicipio entity) {
        return inegiClaveMunicipioRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public InegiClaveMunicipio borrar(Integer id) {
        InegiClaveMunicipio inegiClaveMunicipio = inegiClaveMunicipioRepository.findById(id).get();
        inegiClaveMunicipio.getColonias().forEach(colonia -> colonia.setInegiClaveMunicipio(null));
        inegiClaveMunicipioRepository.delete(inegiClaveMunicipio);
        return inegiClaveMunicipio;
    }
    
    @Transactional(readOnly = true)
    public InegiClaveMunicipio findFirstByNombre(String nombre) {
        return inegiClaveMunicipioRepository.findFirstByNombre(nombre);
    }
    
    public List<InegiClaveMunicipio> searchByName(String nombre){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(InegiClaveMunicipio.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( nombre)
                .createQuery();
    
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, InegiClaveMunicipio.class);
    
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
}
