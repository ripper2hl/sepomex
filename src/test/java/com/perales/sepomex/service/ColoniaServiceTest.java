package com.perales.sepomex.service;

import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Colonia;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppTestConfig.class)
public class ColoniaServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ColoniaService coloniaService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void buscarPorId() {
        Colonia colonia = coloniaService.guardar(new Colonia());
        Colonia coloniaEncontrada = coloniaService.buscarPorId(colonia.getId());
        assertThat("Deberian ser las mismas",colonia.getId() , is( coloniaEncontrada.getId() ) );
    }

    @Test
    public void cargaMasiva() throws Exception {
        assertThat("Deberia obtener verdadero",true,  is(coloniaService.cargaMasiva())  );
    }

    @Test
    public void buscarTodos() {
    }

    @Test
    public void guardar() {
    }

    @Test
    public void actualizar() {
    }

    @Test
    public void borrar() {
    }

}