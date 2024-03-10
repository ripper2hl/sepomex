package com.perales.sepomex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.InegiClaveCiudad;
import com.perales.sepomex.model.InegiClaveMunicipio;
import com.perales.sepomex.service.InegiClaveCiudadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
public class InegiClaveCiudadControllerTest {
    
    private static final String API_URL = "/v1/inegiclaveciudad/";
    
    private final Logger logger = Logger.getGlobal();
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InegiClaveCiudadService inegiClaveCiudadService;
    
    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void buscarPorId() throws Exception {
        InegiClaveCiudad guardado = generadorInegiClaveCiudad();
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append(guardado.getId());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(sb.toString()));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is (  guardado.getId() ) ) )
                .andExpect(jsonPath("$.nombre", is (  guardado.getNombre() ) ) );
        
    }
    
    @Test
    public void buscarTodos() throws Exception {
        StringBuilder sb = new StringBuilder(API_URL);
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.get(sb.toString())
                        .param("page", "0")
                        .param("size", "10"));
        logger.info (response.andReturn().getResponse().getContentAsString() );
        response
                .andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect( jsonPath("$", hasKey("content") ) )
                .andExpect( jsonPath("$", hasKey("pageable") ) )
                .andExpect( jsonPath("$", hasKey("totalPages") ) )
                .andExpect( jsonPath("$", hasKey("totalElements") ) )
                .andExpect( jsonPath("$", hasKey("numberOfElements") ) )
                .andExpect( jsonPath("$", hasKey("size") ) );
    }
    
    @Test
    public void guardar() throws Exception {
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setNombre("inegiClaveCiudad");
        StringBuilder sb = new StringBuilder(API_URL);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString( inegiClaveCiudad );
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.post(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( json ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().isCreated() );
    }
    
    @Test
    public void actualizar() throws Exception {
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setId(1);
        inegiClaveCiudad.setNombre("actualizar");
        StringBuilder sb = new StringBuilder(API_URL);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString( inegiClaveCiudad );
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.put(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( json ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }
    
    @Test
    public void borrar() throws Exception {
        InegiClaveCiudad guardado = generadorInegiClaveCiudad();
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append(guardado.getId());
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.delete(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }

    private InegiClaveCiudad generadorInegiClaveCiudad(){
        InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
        inegiClaveCiudad.setNombre(UUID.randomUUID().toString());
        return inegiClaveCiudadService.guardar(inegiClaveCiudad);
    }
}