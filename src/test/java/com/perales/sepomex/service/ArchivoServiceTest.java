package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.Archivo;
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
class ArchivoServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArchivoService archivoService;

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
}