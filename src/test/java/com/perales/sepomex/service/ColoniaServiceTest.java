package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Colonia;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
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
public class ColoniaServiceTest {
    
    private static final String FILE_NAME = "sepomex.txt";
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ColoniaService coloniaService;

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
        int coloniaId = 1;
        Colonia coloniaEncontrada = coloniaService.buscarPorId( coloniaId );
        assertThat("Deberian ser las mismas", coloniaId , is( coloniaEncontrada.getId() ) );
    }

    @Test
    public void cargaMasiva() throws Exception {
        String archivoSepomexNombre = "src/test/resources/" + FILE_NAME;
        assertThat("Deberia obtener verdadero",true,  is(coloniaService.cargaMasiva(archivoSepomexNombre))  );
    }

    @Test
    public void buscarTodos() {
        Colonia colonia = new Colonia();
        colonia.setNombre("Cañada blanca");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        int page = 0;
        int size = 20;
        Page<Colonia> colonias = coloniaService.buscarTodos(page, size);
        assertThat("Tener colonias igual o  menos de 20 colonias", colonias.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener colonias", colonias.getContent().size(), is(  greaterThan( 0 ) ) );
        coloniaService.borrar(coloniaGuardada.getId());
    }

    @Test
    public void guardar() {
        Colonia colonia = new Colonia();
        colonia.setNombre("Cañada blanca");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        assertThat("Deberia tener un id", coloniaGuardada.getId(), is( notNullValue() ) );
    }

    @Test
    public void actualizar() {
        String nombreColonia = "cañada blanca";
        Colonia colonia = new Colonia();
        colonia.setNombre( "canada blanca" );
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
    
    public void borrar() {
        coloniaService.borrar( 1 );
        exception.expect(NoSuchElementException.class);
        Colonia coloniaEncontrada = coloniaService.buscarPorId( 1 );
    }

}