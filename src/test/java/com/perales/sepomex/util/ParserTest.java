package com.perales.sepomex.util;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
class ParserTest extends TestCase{

    private final Logger logger = Logger.getGlobal();

    private static final String FILE_NAME = "sepomex.txt";
    BufferedReader br = null;

    @Autowired
    private Parser parser;
    
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        br = new BufferedReader( new InputStreamReader(new FileInputStream( "src/test/resources/" + FILE_NAME ), StandardCharsets.UTF_8) );
    }
    
    @Test
    void convertirListaColonia()  {
        AtomicInteger count = new AtomicInteger();
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    Colonia colonia = parser.convertirListaColonia(list);
                    assertNotNull("Deberia tener una colonia",colonia);
                    count.incrementAndGet();
                });
        logger.info(count.toString());
    }

    @Test
    void obtenerAsentamientoTipo() {
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    AsentamientoTipo asentamientoTipo = parser.obtenerAsentamientoTipo(list);
                    assertNotNull("Deberia obtener un tipo de asentamineto", asentamientoTipo);
                });
    }

    @Test
    void obtenerCiudad() {
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    Ciudad ciudad = parser.obtenerCiudad(list);
                    assertNotNull("Deberia obtener una ciudad", ciudad);
                    assertNotNull("La cidudad deberia tener un nombre", ciudad.getNombre());
                });
    }

    @Test
    void obtenerMunicipio() {
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    Municipio municipio = parser.obtenerMunicipio(list);
                    assertNotNull("Deberia obtener un municipio", municipio);
                    assertNotNull("El municipio deberia tener un nombre", municipio.getNombre());
                });
    }

    @Test
    void obtenerEstado() {
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    Estado estado = parser.obtenerEstado(list);
                    assertNotNull("Deberia obtener un estado", estado);
                    assertNotNull("El estado deberia tener un nombre", estado.getNombre());
                });
    }

    @Test
    void obtenerZonaTipo() {
        br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .forEach( list -> {
                    ZonaTipo zonaTipo = parser.obtenerZonaTipo(list);
                    assertNotNull("Deberia obtener un tipo de zona", zonaTipo);
                    assertNotNull("El tipo de zona deberia tener un nombre", zonaTipo.getNombre());
                });
    }
    
    @Test
    void guardarArchivoEntidadesParseadas() throws IOException, ClassNotFoundException {
        String archivoSepomexNombre = "src/test/resources/" + FILE_NAME;
        String archivoParseadoNombre = "/tmp/sepomex-parseado.dat";
        parser.guardarArchivoEntidadesParseadas(archivoSepomexNombre, archivoParseadoNombre);
        File archivoParseadoComprobacion = new File(archivoParseadoNombre);
        assertTrue("Deberia existir el archivo parseado", archivoParseadoComprobacion.exists());
        FileInputStream fileInputStream = new FileInputStream(archivoParseadoNombre);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        List<Colonia> colonias = (List<Colonia>) objectInputStream.readObject();
        List<Colonia> coloniasComparar = br.lines().parallel()
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                .map( line -> Arrays.asList(line.split("\\|")) )
                .map( list -> {
                    Colonia colonia = parser.convertirListaColonia(list);
                    return colonia;
                }).collect( Collectors.toList() );
        for(int i = 0; i < colonias.size(); i++){
            assertEquals("Deberian ser la misma colonia", coloniasComparar.get(i), colonias.get(i));
        }
        archivoParseadoComprobacion.delete();
    }
}
