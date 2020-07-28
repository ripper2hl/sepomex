package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.model.ZonaTipo;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AppTestConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
@Log4j2
public class ColoniaServiceTest {
    
    private static final String FILE_NAME = "sepomex.txt";
    private  static final String SEPOMEX_TEXT = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
            "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_estado|d_ciudad|d_CP|c_estado|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
            "||||||||||||||\n" +
            "01010|Los Alpes|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0005|Urbano|01\n" +
            "01020|Guadalupe Inn|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0006|Urbano|01\n" +
            "01030|Axotla|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0009|Urbano|01\n" +
            "01030|Florida|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0010|Urbano|01\n" +
            "01040|Campestre|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0012|Urbano|01\n" +
            "01048|Las Águilas|Unidad habitacional|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||31|010|0013|Urbano|01\n" +
            "01049|Tlacopac|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0014|Urbano|01\n" +
            "01050|Ex-Hacienda de Guadalupe Chimalistac|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0016|Urbano|01\n" +
            "01060|Altavista|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0017|Urbano|01";
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ColoniaService coloniaService;
    
    @Autowired
    private ZonaTipoService zonaTipoService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DatabaseSetups({
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/codigo-postal.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/asentamiento-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/estado.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
            
    })
    public void buscarPorId() {
        int coloniaId = 100;
        Colonia coloniaEncontrada = coloniaService.buscarPorId( coloniaId );
        assertThat("Deberian ser las mismas", coloniaId , is( coloniaEncontrada.getId() ) );
    }
    
    @Test
    @DatabaseSetups({
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/codigo-postal.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/asentamiento-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/estado.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/zona-tipo-carga-masiva.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
        
    })
    public void cargaMasiva() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(coloniaService.cargaMasiva( file ) )  );
        Page<Colonia> colonias = coloniaService.buscarTodos(0, 9);
        assertThat( "Debe ser un numero total de ", 9, is( colonias.getContent().size() )  );
    }

    @Test
    public void buscarTodos() {
        Colonia colonia = new Colonia();
        colonia.setNombre("Cañada blanca");
        colonia.setIdentificadorMunicipal("identificador");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        int page = 0;
        int size = 20;
        Page<Colonia> colonias = coloniaService.buscarTodos(page, size);
        assertThat("Tener colonias igual o  menos de 20 colonias", colonias.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener colonias", colonias.getContent().size(), is(  greaterThan( 0 ) ) );
        coloniaService.borrar(coloniaGuardada.getId());
    }
    
    @Test
    @DatabaseSetups({
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/codigo-postal.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/asentamiento-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/estado.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
        
    })
    public void borrar() {
        coloniaService.borrar( 1 );
        exception.expect(NoSuchElementException.class);
        Colonia coloniaEncontrada = coloniaService.buscarPorId( 1 );
    }
    
    @Test
    public void guardar() {
        Colonia colonia = new Colonia();
        colonia.setNombre("Cañada blanca");
        colonia.setIdentificadorMunicipal("identificadorMunicipal");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        assertThat("Deberia tener un id", coloniaGuardada.getId(), is( notNullValue() ) );
    }
    
    @Test
    public void actualizar() {
        String nombreColonia = "cañada blanca";
        Colonia colonia = new Colonia();
        colonia.setNombre( "canada blanca" );
        colonia.setIdentificadorMunicipal("identificador");
        colonia = coloniaService.guardar( colonia );
        colonia.setNombre( nombreColonia );
        coloniaService.actualizar( colonia );
        Colonia coloniaEncontrada = coloniaService.buscarPorId( colonia.getId() );
        assertThat("Deberia tener el nombre corregido", nombreColonia, is( equalTo( coloniaEncontrada.getNombre() ) ) );
    }
    
    @Test
    @DatabaseSetups({
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/inegi-clave-municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/codigo-postal.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/asentamiento-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/estado.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/ciudad.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/municipio.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
        
    })
    public void findByMunicipioId() {
        int id = 1;
        Page<Colonia> colonias = coloniaService.findByMunicipioId( id , 0, 10);
        assertThat("Deberia encontrar colonias por municipio", colonias , is( notNullValue() ) );
    }

}