package com.perales.sepomex.service;

import com.google.common.collect.Iterables;
import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.repository.*;
import com.perales.sepomex.util.Parser;
import io.undertow.server.handlers.form.FormData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItem;
@Service
@Log4j2
public class ArchivoService implements ServiceGeneric<Archivo, Integer> {

    private final Logger logger = Logger.getGlobal();

    private List<Estado> estados = new ArrayList<>();
    private List<Ciudad> ciudades = new ArrayList<>();

    private List<Colonia> colonias = new ArrayList<>();
    private List<Municipio> municipios = new ArrayList<>();
    private List<CodigoPostal> codigosPostales = new ArrayList<>();
    private List<InegiClaveCiudad> inegiClaveCiudades = new ArrayList<>();
    private List<InegiClaveMunicipio> inegiClavesMunicipios = new ArrayList<>();
    private List<AsentamientoTipo> asentamientosTipos = new ArrayList<>();
    private List<ZonaTipo> zonaTipos = new ArrayList<>();

    private static final int POSICIONES_MAXIMAS_SEPARADOR = 15;

    @PersistenceContext
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private Parser parser;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private ColoniaRepository coloniaRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CodigoPostalRepository codigoPostalRepository;

    @Autowired
    private InegiClaveCiudadRepository inegiClaveCiudadRepository;

    @Autowired
    private InegiClaveMunicipioRepository inegiClaveMunicipioRepository;

    @Autowired
    private AsentamientoTipoRepository asentamientoTipoRepository;

    @Autowired
    private ZonaTipoRepository zonaTipoRepository;

    @Autowired
    private ArchivoRepository ArchivoRepository;

    @Autowired
    private ColoniaService coloniaService;
    
    @Transactional(readOnly = true)
    public Archivo buscarPorId(Integer id) {
        return ArchivoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Archivo> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return ArchivoRepository.findAll( PageRequest.of( firstResult, size) );
    }
    
    @Transactional
    public Archivo guardar(Archivo entity) {
        return ArchivoRepository.save(entity);
    }
    
    @Transactional
    public Archivo actualizar(Archivo entity) {
        return ArchivoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Archivo borrar(Integer id) {
        Archivo Archivo = ArchivoRepository.findById(id).get();
        ArchivoRepository.deleteById(id);
        return Archivo;
    }

    public void indexDb() throws InterruptedException {


        FullTextEntityManager fullTextEntityManager
                = Search.getFullTextEntityManager(em);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    public Boolean cargaMasiva(MultipartFile file) throws IOException {
        EntityManager em = emf.createEntityManager();
        AtomicInteger coloniasProcesadas = new AtomicInteger(0);
        try (BufferedReader br = new BufferedReader( new InputStreamReader( file.getInputStream() , "UTF-8") )) {
            List<Colonia> colonias = leerColoniaDesdeArchivo(br);
            int totalColonias = colonias.size();

            Iterables.partition(colonias, 10000).forEach(coloniasBatch -> {
                em.getTransaction().begin();
                for(Colonia colonia : coloniasBatch){
                    try {
                        revisarColonia(colonia, em);
                        coloniasProcesadas.incrementAndGet();
                        double porcentaje = ((double) coloniasProcesadas.get() / totalColonias) * 100;
                        log.info("Porcentaje de colonias procesadas: " + porcentaje + "%");
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
            int totalColonias = colonias.size();
            AtomicInteger coloniasProcesadas = new AtomicInteger(0);
            this.asentamientosTipos =  asentamientoTipoRepository.findAll();
            this.ciudades = ciudadRepository.findAll();
            this.codigosPostales = codigoPostalRepository.findAll();
            this.colonias = coloniaRepository.findAllColoniasWithEstadoAndMunicipio();
            this.estados = estadoRepository.findAll();
            this.inegiClaveCiudades = inegiClaveCiudadRepository.findAll();
            this.inegiClavesMunicipios = inegiClaveMunicipioRepository.findAll();
            this.municipios = municipioRepository.findAllMunicipiosWithEstado();
            this.zonaTipos = zonaTipoRepository.findAll();
            Iterables.partition(colonias, 10000).forEach(coloniasBatch -> {
                em.getTransaction().begin();
                coloniasBatch.forEach(colonia -> {
                    try {
                        // Verificar si la colonia ya existe en la base de datos
                        Colonia existingColonia = buscarColonia(colonia);
                        if (existingColonia == null) {
                            log.debug("La colonia no existe y es necesario crearla: " + colonia.toString());
                            revisarColonia(colonia, em);
                        }else{
                            log.debug("La colonia ya existe y no se guardara: " + colonia.toString());
                        }
                        coloniasProcesadas.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                em.getTransaction().commit();
                double porcentaje = ((double) coloniasProcesadas.get() / totalColonias) * 100;
                log.info("Porcentaje de colonias procesadas: " + porcentaje + "%");
            });
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            em.close();
            this.asentamientosTipos = null;
            this.ciudades = null;
            this.codigosPostales = null;
            this.colonias = null;
            this.estados = null;
            this.inegiClaveCiudades = null;
            this.inegiClavesMunicipios = null;
            this.municipios = null;
            this.zonaTipos = null;
        }
        return true;
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
        this.guardar(archivo);
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
                colonia.getCiudad().setMunicipio(municipio);
                em.persist(colonia.getCiudad());
                ciudades.add( colonia.getCiudad() );
                codigoPostal.setCiudad(colonia.getCiudad());
                codigoPostalAdministracionAsentamiento.setCiudad( colonia.getCiudad() );
                codigoPostalAdministracionAsentamientoOficina.setCiudad( colonia.getCiudad() );
            }
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

    private Colonia buscarColonia(Colonia colonia) {
        // Buscar el estado por nombre
        Estado estado = buscarEstado(colonia.getEstado().getNombre());
        if (estado != null) {
            colonia.setEstado(estado);
        } else {
            logger.warning("No se encontró el estado con nombre: " + colonia.getEstado().getNombre());
            return null;
        }

        // Buscar el municipio por nombre y estado
        Municipio municipio = buscarMunicipio(colonia.getMunicipio().getNombre(),estado);
        if (municipio != null) {
            Integer estadoId = municipio.getEstado().getId();
            colonia.setMunicipio(municipio);
        } else {
            logger.warning("No se encontró el municipio con nombre: " + colonia.getMunicipio().getNombre() + " y estado ID: " + estado.getId());
            return null;
        }

        Colonia foundColonia = null;
        for (Colonia existingColonia : colonias) {
            if (existingColonia.getNombre().equals(colonia.getNombre()) &&
                    existingColonia.getMunicipio().getId().equals(colonia.getMunicipio().getId()) &&
                    existingColonia.getEstado().getId().equals(colonia.getEstado().getId())) {
                foundColonia = existingColonia;
                break;
            }
        }

        return foundColonia;
    }

    // Método para buscar estados y municipios pre-cargados
    private Estado buscarEstado(String nombre) {
        return estados.stream()
                .filter(estado -> estado.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    private Municipio buscarMunicipio(String nombre, Estado estado) {
        return municipios.stream()
                .filter(municipio -> municipio.getNombre().equalsIgnoreCase(nombre) && municipio.getEstado().equals(estado))
                .findFirst()
                .orElse(null);
    }

    private void descargarArchivo(String fileUrl, String destinationPath) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public Boolean procesarArchivo(String fileUrl) {
        try {
            String tempFilePath = "/tmp/tempFile" + System.currentTimeMillis() + ".txt";
            descargarArchivo(fileUrl, tempFilePath);
            cambiarEncoding(tempFilePath, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8);
            File file = new File(tempFilePath);
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()),
                    false, file.getName(), (int) file.length(), file.getParentFile());

            // Leer el contenido del archivo y copiarlo al FileItem
            try (InputStream input = new FileInputStream(file);
                 OutputStream outputStream = fileItem.getOutputStream()) {
                IOUtils.copy(input, outputStream);
            }

            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            return actualizacionMasiva(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void cambiarEncoding(String filePath, Charset origen, Charset destino) throws IOException {
        File file = new File(filePath);
        String contenido;
        try (InputStream inputStream = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(inputStream, origen);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            contenido = builder.toString();
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), destino)) {
            writer.write(contenido);
        }
    }

}
