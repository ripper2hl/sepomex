package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Ciudad;
import com.perales.sepomex.model.Colonia;
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
class CiudadServiceTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    CiudadService ciudadService;
    
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    void buscarPorId() {
        Ciudad ciudad = generadorCiudad();
        Ciudad ciudadEncontrada = ciudadService.buscarPorId( ciudad.getId() );
        assertThat("Deberian ser las mismas", ciudad.getId() , is( ciudadEncontrada.getId() ) );
    }
    
    @Test
    void buscarTodos() {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Tipo de asentamiento");
        Ciudad ciudadGuardada = ciudadService.guardar(ciudad);
        int page = 0;
        int size = 20;
        Page<Ciudad> ciudads = ciudadService.buscarTodos(page, size);
        assertThat("Tener ciudads igual o  menos de 20 ciudads", ciudads.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener ciudads", ciudads.getContent().size(), is(  greaterThan( 0 ) ) );
        ciudadService.borrar(ciudadGuardada.getId());
    }
    
    @Test
    void guardar() {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("ciudad");
        Ciudad ciudadGuardado = ciudadService.guardar(ciudad);
        assertThat("Deberia tener un id", ciudadGuardado.getId(), is( notNullValue() ) );
    }
    
    @Test
    void actualizar() {
        String nombreCiudad = "cambiando nombre sepomex";
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("ciudad2");
        Ciudad ciudadGuardado = ciudadService.guardar(ciudad);
        ciudadGuardado.setNombre( nombreCiudad );
        ciudadService.actualizar(ciudadGuardado);
        Ciudad ciudadEncontrado = ciudadService.buscarPorId( ciudad.getId() );
        assertThat("Deberia tener el nombre igual", nombreCiudad, is( equalTo( ciudadEncontrado.getNombre() ) ) );
    }
    
    @Test
    void borrar() {
        Integer ciudadGuardadoId = generadorCiudad().getId();
        ciudadService.borrar(ciudadGuardadoId);
        NoSuchElementException exception = Assertions.assertThrows( NoSuchElementException.class, () -> ciudadService.buscarPorId(ciudadGuardadoId) );
        assertThat("Debe lanzar la un NoSuchElementException ", exception, is( notNullValue() ) );
    }
    private Ciudad generadorCiudad(){
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("borrar");
        Ciudad ciudadGuardada = ciudadService.guardar(ciudad);
        return  ciudadGuardada;
    }
}
