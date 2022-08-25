package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.ZonaTipo;
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
class ZonaTipoServiceTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    ZonaTipoService zonaTipoService;
    
    @BeforeEach
    void setUp(){
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
        int zonaTipoId = 100;
        ZonaTipo zonaTipo = zonaTipoService.buscarPorId( zonaTipoId );
        assertThat("Deberian ser las mismas", zonaTipoId , is( zonaTipo.getId() ) );
    }
    
    @Test
    void buscarTodos() {
        ZonaTipo zonaTipo = new ZonaTipo();
        zonaTipo.setNombre("Tipo de asentamiento");
        ZonaTipo zonaTipoGuardada = zonaTipoService.guardar(zonaTipo);
        int page = 0;
        int size = 20;
        Page<ZonaTipo> zonaTipos = zonaTipoService.buscarTodos(page, size);
        assertThat("Tener zonaTipos igual o  menos de 20 zonaTipos", zonaTipos.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener zonaTipos", zonaTipos.getContent().size(), is(  greaterThan( 0 ) ) );
        zonaTipoService.borrar(zonaTipoGuardada.getId());
    }
    
    @Test
    void guardar() {
        ZonaTipo zonaTipo = new ZonaTipo();
        zonaTipo.setNombre("zonaTipo");
        ZonaTipo zonaTipoGuardado = zonaTipoService.guardar(zonaTipo);
        assertThat("Deberia tener un id", zonaTipoGuardado.getId(), is( notNullValue() ) );
    }
    
    @Test
    void actualizar() {
        String nombreZonaTipo = "cambiando nombre sepomex";
        ZonaTipo zonaTipo = new ZonaTipo();
        zonaTipo.setNombre("zonaTipo2");
        ZonaTipo zonaTipoGuardado = zonaTipoService.guardar(zonaTipo);
        zonaTipoGuardado.setNombre( nombreZonaTipo );
        zonaTipoService.actualizar(zonaTipoGuardado);
        ZonaTipo zonaTipoEncontrado = zonaTipoService.buscarPorId( zonaTipo.getId() );
        assertThat("Deberia tener el nombre igual", nombreZonaTipo, is( equalTo( zonaTipoEncontrado.getNombre() ) ) );
    }
    
    @Test
    void borrar() {
        ZonaTipo zonaTipo = new ZonaTipo();
        zonaTipo.setNombre("zonaTipoBorrar");
        Integer zonaTipoId = zonaTipoService.guardar(zonaTipo).getId();
        zonaTipoService.borrar(zonaTipoId);
        NoSuchElementException exception = Assertions.assertThrows( NoSuchElementException.class, () -> zonaTipoService.buscarPorId(zonaTipoId) );
        assertThat("Debe lanzar la un NoSuchElementException ", exception, is( notNullValue() ) );
    }
}