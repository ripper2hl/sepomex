package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Estado;
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
public class EstadoServiceTest {
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    EstadoService estadoService;
    
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
        int estadoId = 100;
        Estado estado = estadoService.buscarPorId( estadoId );
        assertThat("Deberian ser las mismas", estadoId , is( estado.getId() ) );
    }
    
    @Test
    public void buscarTodos() {
        Estado estado = new Estado();
        estado.setNombre("Tipo de asentamiento");
        estado.setInegiClave("inegiClave");
        Estado estadoGuardada = estadoService.guardar(estado);
        int page = 0;
        int size = 20;
        Page<Estado> estados = estadoService.buscarTodos(page, size);
        assertThat("Tener estados igual o  menos de 20 estados", estados.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener estados", estados.getContent().size(), is(  greaterThan( 0 ) ) );
        estadoService.borrar(estadoGuardada.getId());
    }
    
    @Test
    public void guardar() {
        Estado estado = new Estado();
        estado.setNombre("estado");
        estado.setInegiClave("inegiClave");
        Estado estadoGuardado = estadoService.guardar(estado);
    }
    
    @Test
    public void actualizar() {
        String nombreEstado = "cambiando nombre sepomex";
        Estado estado = new Estado();
        estado.setNombre("estado2");
        estado.setInegiClave("inegiClave");
        Estado estadoGuardado = estadoService.guardar(estado);
        estadoGuardado.setNombre( nombreEstado );
        estadoService.actualizar(estadoGuardado);
        Estado estadoEncontrado = estadoService.buscarPorId( estado.getId() );
        assertThat("Deberia tener el nombre igual", nombreEstado, is( equalTo( estadoEncontrado.getNombre() ) ) );
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
        int id = 100;
        estadoService.borrar(id);
        exception.expect(NoSuchElementException.class);
        estadoService.buscarPorId(id);
    }
}