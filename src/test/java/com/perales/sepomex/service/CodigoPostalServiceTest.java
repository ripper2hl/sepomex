package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.CodigoPostal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
class CodigoPostalServiceTest {
    
    private MockMvc mockMvc;
 
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    CodigoPostalService codigoPostalService;
    
    @BeforeEach
    void setUp() throws Exception {
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
    void buscarPorId() {
        long codigoPostalId = 1l;
        CodigoPostal codigoPostal = codigoPostalService.buscarPorId( codigoPostalId );
        assertThat("Deberian ser las mismas", codigoPostalId , is( codigoPostal.getId() ) );
    }
    
    @Test
    void buscarTodos() {
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
    void guardar() {
        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setNombre("codigoPostal");
        CodigoPostal codigoPostalGuardado = codigoPostalService.guardar(codigoPostal);
        assertThat("Deberia tener un id", codigoPostalGuardado.getId(), is( notNullValue() ) );
    }
    
    @Test
    void actualizar() {
        String nombreCodigoPostal = "cambiando nombre sepomex";
        CodigoPostal codigoPostal = new CodigoPostal();
        codigoPostal.setNombre("codigoPostal2");
        CodigoPostal codigoPostalGuardado = codigoPostalService.guardar(codigoPostal);
        codigoPostalGuardado.setNombre( nombreCodigoPostal );
        codigoPostalService.actualizar(codigoPostalGuardado);
        CodigoPostal codigoPostalEncontrado = codigoPostalService.buscarPorId( codigoPostal.getId() );
        assertThat("Deberia tener el nombre igual", nombreCodigoPostal, is( equalTo( codigoPostalEncontrado.getNombre() ) ) );
    }
    
    
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
    void borrar() {
        long id = 1;
        codigoPostalService.borrar(id);
        codigoPostalService.buscarPorId(id);
    }
}