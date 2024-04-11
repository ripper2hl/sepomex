package com.perales.sepomex.service;

import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.model.Municipio;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@ActiveProfiles({ "test" })
@Log4j2
class ColoniaServiceTest {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ColoniaService coloniaService;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void buscarPorId() {
        Colonia colonia = generadorColonia();
        Colonia coloniaEncontrada = coloniaService.buscarPorId( colonia.getId() );
        assertThat("Deberian ser las mismas", colonia.getId() , is( coloniaEncontrada.getId() ) );
    }

    @Test
    void buscarTodos() {
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
    void borrar() {
        Colonia colonia = generadorColonia();
        coloniaService.borrar( colonia.getId() );
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () ->coloniaService.buscarPorId( colonia.getId() ));
        assertThat("Debe lanzar la un NoSuchElementException ", exception, is( notNullValue() ) );
    }
    
    @Test
    void guardar() {
        Colonia colonia = new Colonia();
        colonia.setNombre("Cañada blanca");
        colonia.setIdentificadorMunicipal("identificadorMunicipal");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        assertThat("Deberia tener un id", coloniaGuardada.getId(), is( notNullValue() ) );
    }
    
    @Test
    void actualizar() {
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
    void findByMunicipioId() {
        int id = 1;
        Page<Colonia> colonias = coloniaService.findByMunicipioId( id , 0, 10);
        assertThat("Deberia encontrar colonias por municipio", colonias , is( notNullValue() ) );
    }
    
    private Colonia generadorColonia(){
        Colonia colonia = new Colonia();
        colonia.setNombre("borrar");
        colonia.setIdentificadorMunicipal("identificadorMunicipal");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        return  coloniaGuardada;
    }
}