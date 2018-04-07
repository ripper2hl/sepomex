package com.perales.util;

import com.perales.sepomex.configuration.AppConfig;
import com.perales.sepomex.model.Colonia;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.apache.logging.log4j.core.impl.ThrowableFormatOptions.FILE_NAME;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Parser.class)
public class ParserTest extends TestCase{

    private final Logger logger = Logger.getGlobal();

    private static final String FILE_NAME = "sepomex.txt";
    private static final int FIRST_NUMBER_OF_ITEMS= 145251;

    @Autowired
    private Parser parser;

    @Test
    public void convertirListaColonia() throws IOException {
        Integer count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/" + FILE_NAME))) {
            for (String line; (line = br.readLine()) != null;) {
                List<String> list = Arrays.asList(line.split("\\|"));
                if( list.size() == 1){
                    continue;
                }
                Colonia colonia = parser.convertirListaColonia(list);
                assertNotNull("Deberia tener una colonia",colonia);
                count++;
            }
        }
        assertTrue("Deberia ser mayor o igual", count >= FIRST_NUMBER_OF_ITEMS );
        logger.info(count.toString());
    }

    @Test
    public void obtenerAsentamientoTipo() {
    }

    @Test
    public void obtenerCiudad() {
    }

    @Test
    public void obtenerMunicipio() {
    }

    @Test
    public void obtenerEstado() {
    }

    @Test
    public void obtenerZonaTipo() {
    }
}
