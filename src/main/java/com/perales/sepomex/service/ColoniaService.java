package com.perales.sepomex.service;

import com.google.common.collect.Iterables;
import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.repository.ColoniaRepository;
import com.perales.sepomex.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.*;
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
    
    private static final int POSICIONES_MAXIMAS_SEPARADOR = 15;
    @Autowired
    private ColoniaRepository coloniaRepository;
    @Autowired
    private CodigoPostalService codigoPostalService;
    @Autowired
    private MunicipioService municipioService;
    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private EstadoService estadoService;
    @Autowired
    private AsentamientoTipoService asentamientoTipoService;
    @Autowired
    private ZonaTipoService zonaTipoService;
    @Autowired
    private InegiClaveCiudadService inegiClaveCiudadService;
    @Autowired
    private InegiClaveMunicipioService inegiClaveMunicipioService;
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
                        logger.info( colonia.toString() );
                        return colonia;
                    }).collect( Collectors.toList() );
    
            Iterables.partition(colonias, 1000).forEach( coloniasBatch -> {
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                for(Colonia colonia : coloniasBatch){
                    revisarColonia(colonia, em);
                }
                em.getTransaction().commit();
            });
        }
        return true;
    }
    
    private void revisarColonia(Colonia colonia, EntityManager em) {
        CodigoPostal codigoPostal = codigoPostalService.findByNombre(colonia.getCodigoPostal().getNombre());
        if (codigoPostal != null) {
            colonia.setCodigoPostal(codigoPostal);
        }else if( codigosPostales.contains( codigoPostal ) ) {
            colonia.setCodigoPostal(codigoPostal);
        }else{
            em.persist( colonia.getCodigoPostal() );
            codigoPostal = colonia.getCodigoPostal();
            codigosPostales.add(codigoPostal);
        }
        
        CodigoPostal codigoPostalAdministracionAsentamiento = codigoPostalService
                .findByNombre(colonia.getCodigoPostalAdministracionAsentamiento().getNombre());
        if (codigoPostalAdministracionAsentamiento != null) {
            colonia.setCodigoPostalAdministracionAsentamiento(codigoPostalAdministracionAsentamiento);
        }else if( codigosPostales.contains( codigoPostalAdministracionAsentamiento ) ){
            colonia.setCodigoPostalAdministracionAsentamiento(codigoPostalAdministracionAsentamiento);
        } else {
            em.persist(colonia.getCodigoPostalAdministracionAsentamiento());
            codigoPostalAdministracionAsentamiento = colonia.getCodigoPostalAdministracionAsentamiento();
            codigosPostales.add(codigoPostal);
        }
        
        CodigoPostal codigoPostalAdministracionAsentamientoOficina = codigoPostalService
                .findByNombre(colonia.getCodigoPostalAdministracionAsentamientoOficina().getNombre());
        if (codigoPostalAdministracionAsentamientoOficina != null) {
            colonia.setCodigoPostalAdministracionAsentamientoOficina(codigoPostalAdministracionAsentamientoOficina);
        }else if ( codigosPostales.contains(codigoPostalAdministracionAsentamientoOficina) ){
            colonia.setCodigoPostalAdministracionAsentamientoOficina(codigoPostalAdministracionAsentamientoOficina);
        }else{
            em.persist(colonia.getCodigoPostalAdministracionAsentamientoOficina());
            codigoPostalAdministracionAsentamientoOficina = colonia.getCodigoPostalAdministracionAsentamientoOficina();
            codigosPostales.add(codigoPostal);
        }
        
        if(colonia.getInegiClaveCiudad() != null){
            InegiClaveCiudad inegiClaveCiudad = inegiClaveCiudadService.findFirstByNombre(colonia.getInegiClaveCiudad().getNombre());
            if (inegiClaveCiudad != null) {
                colonia.setInegiClaveCiudad(inegiClaveCiudad);
            }else{
                em.persist(colonia.getInegiClaveCiudad() );
                inegiClaveCiudad = colonia.getInegiClaveCiudad();
            }
        }
        
        InegiClaveMunicipio inegiClaveMunicipio = inegiClaveMunicipioService.findFirstByNombre(colonia.getInegiClaveMunicipio().getNombre());
        if (inegiClaveMunicipio != null) {
            colonia.setInegiClaveMunicipio(inegiClaveMunicipio);
        }else{
            em.persist( colonia.getInegiClaveMunicipio() );
            inegiClaveMunicipio = colonia.getInegiClaveMunicipio();
        }
        
        AsentamientoTipo asentamientoTipo = asentamientoTipoService
                .findBySepomexClave(colonia.getAsentamientoTipo().getSepomexClave());
        if (asentamientoTipo != null) {
            colonia.setAsentamientoTipo(asentamientoTipo);
        }else{
            em.persist(colonia.getAsentamientoTipo());
            asentamientoTipo = colonia.getAsentamientoTipo();
        }
        
        Estado estado = estadoService.findByInegiClave(colonia.getEstado().getInegiClave());
        if (estado != null) {
            colonia.setEstado(estado);
        }else if ( estados.contains( estado ) ){
            colonia.setEstado(estado);
        }else{
            estado = estadoService.guardar(colonia.getEstado());
            estado = colonia.getEstado();
            estados.add(estado);
        }

        
        if(colonia.getCiudad() != null && estado != null){
            Ciudad ciudad = ciudadService.findFirstByNombreAndEstadoId(colonia.getCiudad().getNombre(), estado.getId());
            if (ciudad != null) {
                colonia.setCiudad(ciudad);
            }else if( ciudades.contains( ciudad ) ) {
                colonia.setCiudad(ciudad);
            }else {
                colonia.getCiudad().setEstado(estado);
                em.persist(colonia.getCiudad());
                ciudad = colonia.getCiudad();
                ciudades.add(ciudad);
            }
        }
        
        Municipio municipio = municipioService.findFirstByNombreAndEstadoId(colonia.getMunicipio().getNombre(), estado.getId());
        if (municipio != null) {
            colonia.setMunicipio(municipio);
        }else if( municipios.contains( municipio ) ){
            colonia.setMunicipio(municipio);
        }else {
            colonia.getMunicipio().setEstado(estado);
            em.persist(colonia.getMunicipio());
            municipio = colonia.getMunicipio();
            municipios.add(municipio);
        }
        
        ZonaTipo zonaTipo = zonaTipoService.findByNombre(colonia.getZonaTipo().getNombre());
        if (zonaTipo != null) {
            colonia.setZonaTipo(zonaTipo);
        }else{
            em.persist(colonia.getZonaTipo());
            zonaTipo = colonia.getZonaTipo();
        }
        em.persist(colonia);
    }
    
}