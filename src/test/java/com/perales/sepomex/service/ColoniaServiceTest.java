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
    
    private static final String FILE_NAME = "sepomex.txt";
    private  static final String SEPOMEX_TEXT = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
            "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_estado|d_ciudad|d_CP|c_estado|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
            "||||||||||||||\n" +
            "01010|Los Alpes|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0005|Urbano|01\n" +
            "01020|Guadalupe Inn|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0006|Urbano|01\n" +
            "01030|Axotla|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0009|Urbano|01\n" +
            "01030|Florida|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0010|Urbano|01\n" +
            "01040|Campestre|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0012|Urbano|01\n" +
            "01048|Las Águilas|Unidad habitacional|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||31|010|0013|Urbano|01\n" +
            "01049|Tlacopac|Pueblo|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||28|010|0014|Urbano|01\n" +
            "01050|Ex-Hacienda de Guadalupe Chimalistac|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0016|Urbano|01\n" +
            "01060|Altavista|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0017|Urbano|01";
    
    private  static final String SEPOMEX_TEXT_REPETIDOS = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
            "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_estado|d_ciudad|d_CP|c_estado|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
            "||||||||||||||\n" +
            "67117|Cañada Blanca|Unidad habitacional|Guadalupe|Nuevo León|Guadalupe|67121|19|67121||31|026|1551|Urbano|04\n" +
            "32765|Cerros Colorados|Ranchería|Guadalupe|Chihuahua||32001|08|32001||29|028|1316|Rural|\n" +
            "74895|Barranca el Brasil|Ranchería|Guadalupe|Puebla||74801|21|74801||29|066|7805|Rural|";
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ColoniaService coloniaService;
    
    @Autowired
    private MunicipioService municipioService;

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
    void cargaMasiva() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(coloniaService.cargaMasiva( file ) )  );
        Page<Colonia> colonias = coloniaService.buscarTodos(0, 9);
        assertThat( "Debe ser un numero total de ", 9, is( colonias.getContent().size() )  );
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
    
    @Test
    void cargaMasivaMunicipiosRepetidos() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT_REPETIDOS.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(coloniaService.cargaMasiva( file ) )  );
        Page<Colonia> colonias = coloniaService.buscarTodos(0, 3);
        assertThat( "Debe ser un numero total de ", 3, is( colonias.getContent().size() )  );
    
        Page<Municipio> municipios = municipioService.buscarTodos(0, 100);
        List<Municipio> municipiosList = municipios.getContent()
                .stream()
                .filter(municipio -> municipio.getNombre().equals("Guadalupe"))
                .collect(Collectors.toList());
        assertThat( "Debe ser un numero total de ", 3, is( municipiosList.size() )  );
        municipiosList.forEach( municipio -> {
            assertThat( "Debe llamarse ", "Guadalupe", is( municipio.getNombre() )  );
        } );
        
    }
    
    private Colonia generadorColonia(){
        Colonia colonia = new Colonia();
        colonia.setNombre("borrar");
        colonia.setIdentificadorMunicipal("identificadorMunicipal");
        Colonia coloniaGuardada = coloniaService.guardar(colonia);
        return  coloniaGuardada;
    }
}