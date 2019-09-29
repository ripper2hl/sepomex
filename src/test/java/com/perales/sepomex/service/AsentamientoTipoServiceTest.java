package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.AsentamientoTipo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AppTestConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
public class AsentamientoTipoServiceTest {
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    AsentamientoTipoService asentamientoTipoService;
    
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
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
        
    })
    public void buscarPorId() {
        int asentamientoTipoId = 1;
        AsentamientoTipo asentamientoTipo = asentamientoTipoService.buscarPorId( asentamientoTipoId );
        assertThat("Deberian ser las mismas", asentamientoTipoId , is( asentamientoTipo.getId() ) );
    }
    
    @Test
    public void buscarTodos() {
        AsentamientoTipo asentamientoTipo = new AsentamientoTipo();
        asentamientoTipo.setNombre("Tipo de asentamiento");
        asentamientoTipo.setSepomexClave("Tipo de asentamiento");
        AsentamientoTipo asentamientoTipoGuardada = asentamientoTipoService.guardar(asentamientoTipo);
        int page = 0;
        int size = 20;
        Page<AsentamientoTipo> asentamientoTipos = asentamientoTipoService.buscarTodos(page, size);
        assertThat("Tener asentamientoTipos igual o  menos de 20 asentamientoTipos", asentamientoTipos.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener asentamientoTipos", asentamientoTipos.getContent().size(), is(  greaterThan( 0 ) ) );
        asentamientoTipoService.borrar(asentamientoTipoGuardada.getId());
    }
    
    @Test
    public void guardar() {
        AsentamientoTipo asentamientoTipo = new AsentamientoTipo();
        asentamientoTipo.setNombre("asentamientoTipo");
        asentamientoTipo.setSepomexClave("sepomexClave");
        AsentamientoTipo asentamientoTipoGuardado = asentamientoTipoService.guardar(asentamientoTipo);
    }
    
    @Test
    public void actualizar() {
        String nombreAsentamientoTipo = "cambiando nombre sepomex";
        AsentamientoTipo asentamientoTipo = new AsentamientoTipo();
        asentamientoTipo.setNombre("asentamientoTipo2");
        asentamientoTipo.setSepomexClave("sepomexClave2");
        AsentamientoTipo asentamientoTipoGuardado = asentamientoTipoService.guardar(asentamientoTipo);
        asentamientoTipoGuardado.setNombre( nombreAsentamientoTipo );
        asentamientoTipoService.actualizar(asentamientoTipoGuardado);
        AsentamientoTipo asentamientoTipoEncontrado = asentamientoTipoService.buscarPorId( asentamientoTipo.getId() );
        assertThat("Deberia tener el nombre igual", nombreAsentamientoTipo, is( equalTo( asentamientoTipoEncontrado.getNombre() ) ) );
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
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
    
    })
    public void borrar() {
        int id = 1;
        asentamientoTipoService.borrar(id);
        exception.expect(NoSuchElementException.class);
        asentamientoTipoService.buscarPorId(id);
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
                    value = "classpath:sample-data/zona-tipo.xml",
                    type = DatabaseOperation.REFRESH),
            @DatabaseSetup(
                    value = "classpath:sample-data/colonia.xml",
                    type = DatabaseOperation.REFRESH)
        
    })
    public void findBySepomexClave() {
        AsentamientoTipo asentamientoTipo = asentamientoTipoService.findBySepomexClave("sepomexClave");
        assertThat("Debria no ser nulo", asentamientoTipo, is (notNullValue( ) ) );
    }
}