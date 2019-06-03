package com.perales.sepomex.service;

import com.google.common.collect.Iterables;
import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.repository.ColoniaRepository;
import com.perales.sepomex.util.Parser;
import org.apache.lucene.search.Query;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ColoniaService implements ServiceGeneric<Colonia, Integer> {
    
    private final Logger logger = Logger.getGlobal();
    
    private List<Estado> estados = new ArrayList<>();
    private List<Ciudad> ciudades = new ArrayList<>();
    private List<Municipio> municipios = new ArrayList<>();
    private List<CodigoPostal> codigosPostales = new ArrayList<>();
    private List<InegiClaveCiudad> inegiClaveCiudades = new ArrayList<>();
    private List<InegiClaveMunicipio> inegiClavesMunicipios = new ArrayList<>();
    private List<AsentamientoTipo> asentamientosTipos = new ArrayList<>();
    private List<ZonaTipo> zonaTipos = new ArrayList<>();
    
    private static final int POSICIONES_MAXIMAS_SEPARADOR = 15;
    @Autowired
    private ColoniaRepository coloniaRepository;

    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private Parser parser;
    
    @Transactional(readOnly = true)
    public Colonia buscarPorId(Integer id) {
        return coloniaRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Colonia> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return coloniaRepository.findAll( PageRequest.of(firstResult, size ) );
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
    public Colonia borrar(Integer id) {
        Colonia colonia = coloniaRepository.findById(id).get();
        coloniaRepository.delete(colonia);
        return colonia;
    }
    
    public Boolean cargaMasiva(MultipartFile file) throws IOException {
        try (BufferedReader br = new BufferedReader( new InputStreamReader( file.getInputStream() , "UTF-8") )) {
            List<Colonia> colonias = br.lines().parallel()
                    .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                    .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                    .map( line -> {
                        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(line);
                        return line;
                    })
                    .map( line -> Arrays.asList(line.split("\\|")) )
                    .map( list -> {
                        Colonia colonia = parser.convertirListaColonia(list);
                        if(colonia != null){
                            logger.info( colonia.toString() );
                        }else{
                            logger.severe("No fue posible guardar el siguiente registro: " + list);
                        }
                        return colonia;
                    }).filter( colonia -> colonia != null )
                    .collect( Collectors.toList() );
    
            Iterables.partition(colonias, 1000).forEach( coloniasBatch -> {
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                for(Colonia colonia : coloniasBatch){
                    try {
                        revisarColonia(colonia, em);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                em.getTransaction().commit();
                em.close();
            });
        }
        return true;
    }
    
    private void revisarColonia(Colonia colonia, EntityManager em) {
        CodigoPostal codigoPostal = colonia.getCodigoPostal();
        
        if( codigosPostales.contains( codigoPostal ) ) {
            codigoPostal =  codigosPostales.get( codigosPostales.indexOf(codigoPostal) ) ;
            colonia.setCodigoPostal(codigoPostal);
        }else{
            em.persist( colonia.getCodigoPostal() );
            codigoPostal = colonia.getCodigoPostal();
            codigosPostales.add(codigoPostal);
        }
        
        CodigoPostal codigoPostalAdministracionAsentamiento = colonia.getCodigoPostalAdministracionAsentamiento();
        if( codigosPostales.contains( codigoPostalAdministracionAsentamiento ) ){
            codigoPostalAdministracionAsentamiento = codigosPostales.get( codigosPostales.indexOf( codigoPostalAdministracionAsentamiento ) );
            colonia.setCodigoPostalAdministracionAsentamiento(codigoPostalAdministracionAsentamiento);
        } else {
            em.persist(colonia.getCodigoPostalAdministracionAsentamiento());
            codigoPostalAdministracionAsentamiento = colonia.getCodigoPostalAdministracionAsentamiento();
            codigosPostales.add( codigoPostalAdministracionAsentamiento );
        }
    
        CodigoPostal codigoPostalAdministracionAsentamientoOficina = colonia.getCodigoPostalAdministracionAsentamientoOficina();
        if ( codigosPostales.contains(codigoPostalAdministracionAsentamientoOficina) ){
            codigoPostalAdministracionAsentamientoOficina = codigosPostales.get( codigosPostales.indexOf( codigoPostalAdministracionAsentamientoOficina ) );
            colonia.setCodigoPostalAdministracionAsentamientoOficina(codigoPostalAdministracionAsentamientoOficina);
        }else{
            em.persist(colonia.getCodigoPostalAdministracionAsentamientoOficina());
            codigoPostalAdministracionAsentamientoOficina = colonia.getCodigoPostalAdministracionAsentamientoOficina();
            codigosPostales.add(codigoPostalAdministracionAsentamientoOficina);
        }
        
        if(colonia.getInegiClaveCiudad() != null){
            InegiClaveCiudad inegiClaveCiudad = colonia.getInegiClaveCiudad();
            if ( inegiClaveCiudades.contains( inegiClaveCiudad ) ) {
                colonia.setInegiClaveCiudad( inegiClaveCiudades.get( inegiClaveCiudades.indexOf(inegiClaveCiudad) ) );
            }else{
                em.persist(colonia.getInegiClaveCiudad() );
                inegiClaveCiudades.add( colonia.getInegiClaveCiudad() );
            }
        }
        
        InegiClaveMunicipio inegiClaveMunicipio = colonia.getInegiClaveMunicipio();
        if ( inegiClavesMunicipios.contains( inegiClaveMunicipio ) ) {
            colonia.setInegiClaveMunicipio( inegiClavesMunicipios.get( inegiClavesMunicipios.indexOf(inegiClaveMunicipio) ) );
        }else{
            em.persist( colonia.getInegiClaveMunicipio() );
            inegiClavesMunicipios.add(colonia.getInegiClaveMunicipio());
        }
        
        AsentamientoTipo asentamientoTipo = colonia.getAsentamientoTipo();
        if ( asentamientosTipos.contains( asentamientoTipo ) ) {
            asentamientoTipo = asentamientosTipos.get( asentamientosTipos.indexOf( asentamientoTipo ) );
            colonia.setAsentamientoTipo(asentamientoTipo);
        }else{
            em.persist(colonia.getAsentamientoTipo());
            asentamientosTipos.add( colonia.getAsentamientoTipo() );
        }
        
        Estado estado = colonia.getEstado();
        if ( estados.contains( estado ) ){
            estado = estados.get( estados.indexOf( estado ) );
            colonia.setEstado(estado);
            codigoPostal.setEstado(estado);
            codigoPostalAdministracionAsentamiento.setEstado(estado);
            codigoPostalAdministracionAsentamientoOficina.setEstado(estado);
        }else{
            em.persist( colonia.getEstado() );
            estados.add( colonia.getEstado() );
            codigoPostal.setEstado(estado);
            codigoPostalAdministracionAsentamiento.setEstado( estado );
            codigoPostalAdministracionAsentamientoOficina.setEstado( estado );
        }

        if(colonia.getCiudad() != null && estado != null){
            Ciudad ciudad = colonia.getCiudad();
            if( ciudades.contains( ciudad ) ) {
                ciudad = ciudades.get( ciudades.indexOf( ciudad ) );
                colonia.setCiudad(ciudad);
                codigoPostal.setCiudad( ciudad);
                codigoPostalAdministracionAsentamiento.setCiudad( ciudad );
                codigoPostalAdministracionAsentamientoOficina.setCiudad( ciudad );
            }else {
                colonia.getCiudad().setEstado(estado);
                em.persist(colonia.getCiudad());
                ciudades.add( colonia.getCiudad() );
                codigoPostal.setCiudad(colonia.getCiudad());
                codigoPostalAdministracionAsentamiento.setCiudad( colonia.getCiudad() );
                codigoPostalAdministracionAsentamientoOficina.setCiudad( colonia.getCiudad() );
            }
        }
        
        Municipio municipio = colonia.getMunicipio();
        if( municipios.contains( municipio ) ){
            municipio = municipios.get( municipios.indexOf( municipio ) );
            colonia.setMunicipio(municipio);
            codigoPostal.setMunicipio( municipio );
            codigoPostalAdministracionAsentamiento.setMunicipio( municipio );
            codigoPostalAdministracionAsentamientoOficina.setMunicipio( municipio );
        }else {
            colonia.getMunicipio().setEstado(estado);
            em.persist(colonia.getMunicipio());
            municipios.add( colonia.getMunicipio() );
            codigoPostal.setMunicipio( colonia.getMunicipio() );
            codigoPostalAdministracionAsentamiento.setMunicipio( colonia.getMunicipio() );
            codigoPostalAdministracionAsentamientoOficina.setMunicipio( colonia.getMunicipio() );
        }
        
        ZonaTipo zonaTipo = colonia.getZonaTipo();
        if (zonaTipos.contains( zonaTipo ) ) {
            colonia.setZonaTipo( zonaTipos.get( zonaTipos.indexOf( zonaTipo ) ) );
        }else{
            em.persist(colonia.getZonaTipo());
            zonaTipos.add( colonia.getZonaTipo() );
        }
        em.persist(colonia);
    }
    
    public Page<Colonia> findByMunicipioId( Integer id, Integer page , Integer size ){
        int firstResult = page * size;
        return coloniaRepository.findByMunicipioId(id, PageRequest.of(firstResult, size ));
    }
    
    public void indexDb() throws InterruptedException {
    
        
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(emf.createEntityManager());
        fullTextEntityManager.createIndexer().startAndWait();
    }
    
    @Transactional(readOnly = true)
    public List<Colonia> searchByName(String name){
        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager( emf.createEntityManager() );
    
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Colonia.class)
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
                = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Colonia.class);
                
        return jpaQuery.getResultList();
    }
    
}