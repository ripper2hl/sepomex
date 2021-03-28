package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.InegiClaveCiudad;
import org.junit.jupiter.api.Assertions;
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

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
class InegiClaveCiudadServiceTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private InegiClaveCiudadService inegiClaveCiudadService;
    
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
    void buscarPorId() {
        int inegiClaveCiudadId = 100;
        InegiClaveCiudad inegiClaveCiudad = inegiClaveCiudadService.buscarPorId( inegiClaveCiudadId );
        assertThat("Deberian ser las mismas", inegiClaveCiudadId , is( inegiClaveCiudad.getId() ) );
    }
    
    @Test
    void buscarTodos() {
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setNombre("Tipo de asentamiento");
        InegiClaveCiudad inegiClaveCiudadGuardada = inegiClaveCiudadService.guardar(inegiClaveCiudad);
        int page = 0;
        int size = 20;
        Page<InegiClaveCiudad> inegiClaveCiudads = inegiClaveCiudadService.buscarTodos(page, size);
        assertThat("Tener inegiClaveCiudads igual o  menos de 20 inegiClaveCiudads", inegiClaveCiudads.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener inegiClaveCiudads", inegiClaveCiudads.getContent().size(), is(  greaterThan( 0 ) ) );
        inegiClaveCiudadService.borrar(inegiClaveCiudadGuardada.getId());
    }
    
    @Test
    void guardar() {
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setNombre("inegiClaveCiudad");
        InegiClaveCiudad inegiClaveCiudadGuardado = inegiClaveCiudadService.guardar(inegiClaveCiudad);
        assertThat("Deberia tener un id", inegiClaveCiudadGuardado.getId(), is( notNullValue() ) );
    }
    
    @Test
    void actualizar() {
        String nombreInegiClaveCiudad = "cambiando nombre sepomex";
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setNombre("inegiClaveCiudad2");
        InegiClaveCiudad inegiClaveCiudadGuardado = inegiClaveCiudadService.guardar(inegiClaveCiudad);
        inegiClaveCiudadGuardado.setNombre( nombreInegiClaveCiudad );
        inegiClaveCiudadService.actualizar(inegiClaveCiudadGuardado);
        InegiClaveCiudad inegiClaveCiudadEncontrado = inegiClaveCiudadService.buscarPorId( inegiClaveCiudad.getId() );
        assertThat("Deberia tener el nombre igual", nombreInegiClaveCiudad, is( equalTo( inegiClaveCiudadEncontrado.getNombre() ) ) );
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
    void borrar() {
        int id = 1;
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> inegiClaveCiudadService.buscarPorId(id));
        assertThat("Debe lanzar la un NoSuchElementException ", exception, is( notNullValue() ) );
    }
}