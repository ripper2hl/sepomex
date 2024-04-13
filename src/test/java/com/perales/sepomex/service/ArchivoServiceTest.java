package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.model.Municipio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
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
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = AppTestConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
@ActiveProfiles({ "test" })
class ArchivoServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArchivoService archivoService;

    @Autowired
    private ColoniaService coloniaService;

    @Autowired
    private MunicipioService municipioService;

    private static final String SEPOMEX_URL_FILE="https://raw.githubusercontent.com/ripper2hl/sepomex/actualizacion-automatica/src/test/resources/sepomex-iso-encoding.txt";

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

    private static final String FILE_NAME = "sepomex.txt";

    private  static final String SEPOMEX_TEXT_REPETIDOS = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
            "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_estado|d_ciudad|d_CP|c_estado|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
            "||||||||||||||\n" +
            "67117|Cañada Blanca|Unidad habitacional|Guadalupe|Nuevo León|Guadalupe|67121|19|67121||31|026|1551|Urbano|04\n" +
            "32765|Cerros Colorados|Ranchería|Guadalupe|Chihuahua||32001|08|32001||29|028|1316|Rural|\n" +
            "74895|Barranca el Brasil|Ranchería|Guadalupe|Puebla||74801|21|74801||29|066|7805|Rural|";

    private static String SEPOMEX_TEXT_ACTUALIZACION = "El Catálogo Nacional de Códigos Postales, es elaborado por Correos de México y se proporciona en forma gratuita para uso particular, no estando permitida su comercialización, total o parcial, ni su distribución a terceros bajo ningún concepto.\n" +
            "d_codigo|d_asenta|d_tipo_asenta|D_mnpio|d_estado|d_ciudad|d_CP|c_estado|c_oficina|c_CP|c_tipo_asenta|c_mnpio|id_asenta_cpcons|d_zona|c_cve_ciudad\n" +
            "||||||||||||||\n" +
            "01010|Los Alpes|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0005|Urbano|01\n" +
            "01063|Altavistaaa|Colonia|Álvaro Obregón|Ciudad de México|Ciudad de México|01001|09|01001||09|010|0018|Urbano|01\n" +
            "00000|Cañada Blancaa|Unidad habitacionall|Guadalupee|Nuevo Leónn|Guadalupee|67121|19|67121||31|026|1551|Urbano|04" ;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void buscarPorId() {
        Archivo archivoGuardar = new Archivo();
        archivoGuardar.setContenido(SEPOMEX_TEXT);
        archivoGuardar = archivoService.guardar(archivoGuardar);
        Archivo archivo = archivoService.buscarPorId( archivoGuardar.getId() );
        assertThat("Deberian ser las mismas", archivoGuardar.getId() , is( archivo.getId() ) );
    }

    @Test
    void buscarTodos() {
        Archivo archivo = new Archivo();
        archivo.setContenido(SEPOMEX_TEXT);
        Archivo archivoGuardada = archivoService.guardar(archivo);
        int page = 0;
        int size = 20;
        Page<Archivo> archivos = archivoService.buscarTodos(page, size);
        assertThat("Tener archivos igual o  menos de 20 archivos", archivos.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener archivos", archivos.getContent().size(), is(  greaterThan( 0 ) ) );
        archivoService.borrar(archivoGuardada.getId());
    }

    @Test
    void guardar() {
        Archivo archivo = new Archivo();
        archivo.setContenido(SEPOMEX_TEXT);
        Archivo archivoGuardado = archivoService.guardar(archivo);
        assertThat("Deberia tener un id", archivoGuardado.getId(), is( notNullValue() ) );
    }

    @Test
    void actualizar() {
        String contenidoArchivo = "cambiando cotenido";
        Archivo archivo = new Archivo();
        archivo.setContenido(SEPOMEX_TEXT);
        Archivo archivoGuardado = archivoService.guardar(archivo);
        archivoGuardado.setContenido(contenidoArchivo);
        archivoService.actualizar(archivoGuardado);
        Archivo archivoEncontrado = archivoService.buscarPorId( archivo.getId() );
        assertThat("Deberia tener el nombre igual", contenidoArchivo, is( equalTo( archivoEncontrado.getContenido() ) ) );
    }

    @Test
    void borrar() {
        Archivo archivoGuardar = new Archivo();
        archivoGuardar.setContenido(SEPOMEX_TEXT);
        archivoGuardar = archivoService.guardar(archivoGuardar);
        Integer archivoId = archivoService.buscarPorId( archivoGuardar.getId() ).getId();

        archivoService.borrar(archivoId);

        NoSuchElementException exception = Assertions.assertThrows( NoSuchElementException.class, () -> archivoService.buscarPorId( archivoId ) );
        assertThat("Debe lanzar la un NoSuchElementException ", exception, is( notNullValue() ) );
    }

    @Test
    void procesarArchivo() {
        boolean result = archivoService.procesarArchivo(SEPOMEX_URL_FILE);
        assertTrue(result);
    }

    @Test
    void cargaMasivaMunicipiosRepetidos() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT_REPETIDOS.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(archivoService.cargaMasiva( file ) )  );
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

    @Test
    void cargaMasiva() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(archivoService.cargaMasiva( file ) )  );
        Page<Colonia> colonias = coloniaService.buscarTodos(0, 9);
        assertThat( "Debe ser un numero total de ", 9, is( colonias.getContent().size() )  );
    }
    @Test
    void actualizacionMasiva() throws Exception{
        cargaMasiva();
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain", SEPOMEX_TEXT_ACTUALIZACION.getBytes());
        assertThat("Deberia obtener verdadero",true,  is(archivoService.actualizacionMasiva( file ) )  );
        Page<Colonia> colonias = coloniaService.buscarTodos(0, 1000);
        // Verifica si existe una colonia llamada "colonia prueba" en la lista
        boolean coloniaPruebaExists = false;
        for (Colonia colonia : colonias.getContent()) {
            if (colonia.getNombre().equals("Altavistaaa")) {
                coloniaPruebaExists = true;
                break;
            }
        }

        // Verifica si la colonia "colonia prueba" existe después de la actualización masiva
        assertThat("Debería existir una colonia llamada 'colonia Altavistaaa'", coloniaPruebaExists, is(true));
    }

}