package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.CodigoPostal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
public class CodigoPostalServiceTest {
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    CodigoPostalService codigoPostalService;
    
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
        int codigoPostalId = 1;
        CodigoPostal codigoPostal = codigoPostalService.buscarPorId( codigoPostalId );
        assertThat("Deberian ser las mismas", codigoPostalId , is( codigoPostal.getId() ) );
    }
    
    @Test
    public void buscarTodos() {
        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setNombre("Tipo de asentamiento");
        CodigoPostal codigoPostalGuardada = codigoPostalService.guardar(codigoPostal);
        int page = 0;
        int size = 20;
        Page<CodigoPostal> codigoPostals = codigoPostalService.buscarTodos(page, size);
        assertThat("Tener codigoPostals igual o  menos de 20 codigoPostals", codigoPostals.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener codigoPostals", codigoPostals.getContent().size(), is(  greaterThan( 0 ) ) );
        codigoPostalService.borrar(codigoPostalGuardada.getId());
    }
    
    @Test
    public void guardar() {
        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setNombre("codigoPostal");
        CodigoPostal codigoPostalGuardado = codigoPostalService.guardar(codigoPostal);
    }
    
    @Test
    public void actualizar() {
        String nombreCodigoPostal = "cambiando nombre sepomex";
        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setNombre("codigoPostal2");
        CodigoPostal codigoPostalGuardado = codigoPostalService.guardar(codigoPostal);
        codigoPostalGuardado.setNombre( nombreCodigoPostal );
        codigoPostalService.actualizar(codigoPostalGuardado);
        CodigoPostal codigoPostalEncontrado = codigoPostalService.buscarPorId( codigoPostal.getId() );
        assertThat("Deberia tener el nombre igual", nombreCodigoPostal, is( equalTo( codigoPostalEncontrado.getNombre() ) ) );
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
        codigoPostalService.borrar(id);
        exception.expect(NoSuchElementException.class);
        codigoPostalService.buscarPorId(id);
    }
}