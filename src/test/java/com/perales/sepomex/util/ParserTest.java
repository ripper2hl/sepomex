package com.perales.sepomex.util;

import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppTestConfig.class)
public class ParserTest extends TestCase{

    private final Logger logger = Logger.getGlobal();

    private static final String FILE_NAME = "sepomex.txt";
    private static final int FIRST_NUMBER_OF_ITEMS= 145250;
    BufferedReader br = null;

    @Autowired
    private Parser parser;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        br = new BufferedReader(new FileReader("src/test/resources/" + FILE_NAME));
    }
    
    @Test
    public void convertirListaColonia() throws IOException {
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
        assertTrue("Deberia ser mayor o igual", count.get() >= FIRST_NUMBER_OF_ITEMS );
        logger.info(count.toString());
    }

    @Test
    public void obtenerAsentamientoTipo() {
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
    public void obtenerCiudad() {
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
    public void obtenerMunicipio() {
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
    public void obtenerEstado() {
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
    public void obtenerZonaTipo() {
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
    public void guardarArchivoEntidadesParseadas() throws IOException, ClassNotFoundException {
        String archivoSepomexNombre = "src/test/resources/" + FILE_NAME;
        String archivoParseadoNombre = "/home/perales/sepomex-parseado.ser";
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
