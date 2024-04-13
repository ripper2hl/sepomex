package com.perales.sepomex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.service.ArchivoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.logging.Logger;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@ActiveProfiles({ "test" })
public class ArchivoControllerTest {
    
    private static final String API_URL = "/v1/archivo/";
    
    private final Logger logger = Logger.getGlobal();
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private ArchivoService archivoService;
    
    private String sepomexText;
    
    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sepomexText = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
                "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_archivo|d_ciudad|d_CP|c_archivo|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
                "01000|San Ángel|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0001|Urbano|01\n" +
                "01010|Los Alpes|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0005|Urbano|01\n" +
                "01020|Guadalupe Inn|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0006|Urbano|01\n" +
                "01030|Axotla|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0009|Urbano|01\n" +
                "01030|Florida|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0010|Urbano|01\n" +
                "01040|Campestre|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0012|Urbano|01\n" +
                "01048|Las Águilas|Unidad habitacional|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||31|010|0013|Urbano|01\n" +
                "01049|Tlacopac|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0014|Urbano|01\n" +
                "01050|Ex-Hacienda de Guadalupe Chimalistac|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0016|Urbano|01\n" +
                "01060|Altavista|Archivo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0017|Urbano|01";
    }
    
    @Test
    public void buscarPorId() throws Exception {
        Archivo archivo = generadorArchivo();
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append(archivo.getId());
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(sb.toString()));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( content().contentType(MediaType.APPLICATION_JSON) )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is (  archivo.getId().intValue() ) ) )
                .andExpect(jsonPath("$.contenido", is (  archivo.getContenido() ) ) );
    
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
        Archivo archivo = new Archivo();
        archivo.setContenido("Archivo");
        StringBuilder sb = new StringBuilder(API_URL);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString( archivo );
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.post(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( json ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().isCreated() );
    }
    
    @Test
    public void actualizar() throws Exception {
        Archivo archivo = generadorArchivo();
        StringBuilder sb = new StringBuilder(API_URL);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString( archivo );
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.put(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( json ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }
    
    @Test
    public void borrar() throws Exception {
        Archivo archivo = generadorArchivo();
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append(archivo.getId());
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.delete(sb.toString())
                        .contentType( MediaType.APPLICATION_JSON ));
        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }
    
    @Test
    public void cargaMasiva() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", sepomexText.getBytes());
    
        StringBuilder sb = new StringBuilder(API_URL);
        sb.append("carga");
        ResultActions response = mockMvc.perform(fileUpload(sb.toString())
                .file(multipartFile));

        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }

    @Test
    public void actualizacionMasiva() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", sepomexText.getBytes());

        StringBuilder sb = new StringBuilder(API_URL);
        sb.append("actualizacion");
        ResultActions response = mockMvc.perform(fileUpload(sb.toString())
                .file(multipartFile));

        logger.info( response.andReturn().getResponse().getContentAsString() );
        response.andExpect( status().is2xxSuccessful() );
    }
    
    private Archivo generadorArchivo(){
        Archivo archivo = new Archivo();
        archivo.setContenido("borrar");
        Archivo archivoGuardada = archivoService.guardar(archivo);
        return  archivoGuardada;
    }
}