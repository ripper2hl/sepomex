package com.perales.sepomex.service;

import com.google.common.collect.Iterables;
import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.repository.*;
import com.perales.sepomex.util.Parser;
import lombok.extern.log4j.Log4j2;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ColoniaService implements ServiceGeneric<Colonia, Long> {
    
    private final Logger logger = Logger.getGlobal();

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ColoniaRepository coloniaRepository;

    @Transactional(readOnly = true)
    public Colonia buscarPorId(Long id) {
        Colonia colonia = coloniaRepository.findOneById(id);
        if(colonia == null){
            throw new NoSuchElementException();
        }
        return colonia;
    }
    
    @Transactional(readOnly = true)
    public Page<Colonia> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return coloniaRepository.findAll( PageRequest.of(firstResult, size ) );
    }

    @Transactional(readOnly = true)
    public List<Colonia> buscarColoniasPorCodigoPostal(String codigoPostal) {
        return coloniaRepository.findByCodigoPostal_Nombre(codigoPostal);
    }
    
    @Transactional
    public Colonia guardar(Colonia entity) {
        return coloniaRepository.save(entity);
    }
    
    @Transactional
    public Colonia actualizar(Colonia entity) {
        Colonia colonia = coloniaRepository.getOne( entity.getId() );
        colonia.setNombre(entity.getNombre());
        return colonia;
    }
    
    @Transactional
    public Colonia borrar(Long id) {
        Colonia colonia = coloniaRepository.findById(id).get();
        coloniaRepository.delete(colonia);
        return colonia;
    }

    public Page<Colonia> findByMunicipioId( Integer id, Integer page , Integer size ){
        int firstResult = page * size;
        return coloniaRepository.findByMunicipioId(id, PageRequest.of(firstResult, size ));
    }

    public List<Colonia> search(Colonia colonia){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( em );
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Colonia.class)
                .get();
    
        Query fuzzyQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onField("nombre")
                .matching( colonia.getNombre())
                .createQuery();
        
        org.hibernate.search.jpa.FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Colonia.class);
        
        jpaQuery.setCriteriaQuery( createCriteriaSearch(colonia) );
        jpaQuery.setMaxResults(100);
        jpaQuery.limitExecutionTimeTo(1l, TimeUnit.SECONDS);
        List lista = jpaQuery.getResultList();
        fullTextEntityManager.close();
        em.close();
        return lista;
    }
    
    @SuppressWarnings("deprecated")
    private Criteria createCriteriaSearch(Colonia colonia){
        Session session = (Session) em.getDelegate();
        session.setDefaultReadOnly(true);
        Criteria criteria = session.createCriteria(Colonia.class);
        criteria.setFetchMode("estado", FetchMode.JOIN);
        criteria.setFetchMode("municipio", FetchMode.JOIN);
        criteria.setFetchMode("codigoPostal", FetchMode.JOIN);
        
        if(colonia.getEstado() != null ){
            criteria.add( Restrictions.eq( "estado.id", colonia.getEstado().getId() ) );
        }
        if(colonia.getMunicipio() != null ){
            criteria.add( Restrictions.eq( "municipio.id", colonia.getMunicipio().getId() ) );
        }
        return criteria;
    }
    
    public int createCriteriaSearchCount(Colonia colonia){
        Criteria criteria = createCriteriaSearch(colonia);
        criteria.setProjection(Projections.rowCount());
        Long count = (Long)criteria.uniqueResult();
        return count.intValue();
    }
}
