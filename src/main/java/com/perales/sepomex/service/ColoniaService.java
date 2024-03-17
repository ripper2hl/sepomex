package com.perales.sepomex.service;

import com.google.common.collect.Iterables;
import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.repository.ColoniaRepository;
import com.perales.sepomex.repository.EstadoRepository;
import com.perales.sepomex.repository.MunicipioRepository;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ColoniaService implements ServiceGeneric<Colonia, Long> {
    
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
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;
    
    @PersistenceContext
    private EntityManager em;
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Autowired
    private Parser parser;
    
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

    @Autowired
    private ArchivoService archivoService;

    public Boolean cargaMasiva(MultipartFile file) throws IOException {
        EntityManager em = emf.createEntityManager();
        try (BufferedReader br = new BufferedReader( new InputStreamReader( file.getInputStream() , "UTF-8") )) {
            List<Colonia> colonias = leerColoniaDesdeArchivo(br);
    
            Iterables.partition(colonias, 10000).forEach( coloniasBatch -> {
                em.getTransaction().begin();
                for(Colonia colonia : coloniasBatch){
                    try {
                        revisarColonia(colonia, em);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                em.getTransaction().commit();
            });
            em.close();
        }
        return true;
    }

    public Boolean actualizacionMasiva(MultipartFile file) throws IOException {
        EntityManager em = emf.createEntityManager();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            List<Colonia> colonias = leerColoniaDesdeArchivo(br);
            Iterables.partition(colonias, 10000).forEach(coloniasBatch -> {
                em.getTransaction().begin();
                coloniasBatch.forEach(colonia -> {
                    try {
                        // Verificar si la colonia ya existe en la base de datos
                        Colonia existingColonia = buscarColonia(colonia);
                        if (existingColonia == null) {
                            log.info("La colonia no existe y es necesario crearla: " + colonia.toString());
                            revisarColonia(colonia, em);
                        }else{
                            log.info("La colonia ya existe y no se guardara: " + colonia.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                em.getTransaction().commit();
            });
        } finally {
            em.close();
        }
        return true;
    }

    private Colonia buscarColonia(Colonia colonia) {
        // Buscar el estado por nombre
        Estado estado = estadoRepository.findFirstByNombre(colonia.getEstado().getNombre());
        if (estado != null) {
            colonia.setEstado(estado);
        } else {
            logger.warning("No se encontró el estado con nombre: " + colonia.getEstado().getNombre());
            return null;
        }

        // Buscar el municipio por nombre y estado
        Municipio municipio = municipioRepository.findFirstByNombreAndIdEstado(colonia.getMunicipio().getNombre(),estado.getId());
        if (municipio != null) {
            Integer estadoId = municipio.getEstado().getId();
            colonia.setMunicipio(municipio);
        } else {
            logger.warning("No se encontró el municipio con nombre: " + colonia.getMunicipio().getNombre() + " y estado ID: " + estado.getId());
            return null;
        }

        // Verificar si la colonia ya existe en la base de datos
        List<Colonia> coloniasEncontradas = coloniaRepository.findByNombreAndMunicipioIdAndEstadoId(
                colonia.getNombre(), colonia.getMunicipio().getId(), colonia.getEstado().getId());

        return !coloniasEncontradas.isEmpty() ? coloniasEncontradas.get(0) : null;
    }

    private List<Colonia> leerColoniaDesdeArchivo(BufferedReader br) {
        StringBuilder contenidoArchivo = new StringBuilder();
        List<Colonia> colonias = br.lines()
                .parallel()
                .peek( line -> contenidoArchivo.append(line))
                .filter(line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE))
                .filter(line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION))
                .map(line -> Arrays.asList(line.split("\\|")))
                .map(list -> {
                    Colonia colonia = parser.convertirListaColonia(list);
                    if (colonia != null) {
                        logger.info(colonia.toString());
                    } else {
                        logger.severe("No fue posible guardar el siguiente registro: " + list);
                    }
                    return colonia;
                })
                .filter(colonia -> colonia != null)
                .collect(Collectors.toList());

        Archivo archivo = new Archivo();
        archivo.setFechaCarga(LocalDateTime.now());
        archivo.setContenido(contenidoArchivo.toString().replaceAll("\u0000", ""));
        archivoService.guardar(archivo);
        return colonias;
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
                = Search.getFullTextEntityManager(em);
        fullTextEntityManager.createIndexer().startAndWait();
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
