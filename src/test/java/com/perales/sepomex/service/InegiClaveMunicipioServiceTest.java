package com.perales.sepomex.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.perales.sepomex.configuration.AppTestConfig;
import com.perales.sepomex.model.InegiClaveMunicipio;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AppTestConfig.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,DbUnitTestExecutionListener.class })
public class InegiClaveMunicipioServiceTest {
    
    private MockMvc mockMvc;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    InegiClaveMunicipioService inegiClaveMunicipioService;
    
    @Before
    public void setUp() throws Exception {
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
    public void buscarPorId() {
        int inegiClaveMunicipioId = 1;
        InegiClaveMunicipio inegiClaveMunicipio = inegiClaveMunicipioService.buscarPorId( inegiClaveMunicipioId );
        assertThat("Deberian ser las mismas", inegiClaveMunicipioId , is( inegiClaveMunicipio.getId() ) );
    }
    
    @Test
    public void buscarTodos() {
        InegiClaveMunicipio inegiClaveMunicipio = new InegiClaveMunicipio();
        inegiClaveMunicipio.setNombre("Tipo de asentamiento");
        InegiClaveMunicipio inegiClaveMunicipioGuardada = inegiClaveMunicipioService.guardar(inegiClaveMunicipio);
        int page = 0;
        int size = 20;
        Page<InegiClaveMunicipio> inegiClaveMunicipios = inegiClaveMunicipioService.buscarTodos(page, size);
        assertThat("Tener inegiClaveMunicipios igual o  menos de 20 inegiClaveMunicipios", inegiClaveMunicipios.getContent().size(), is(  lessThanOrEqualTo( size ) ) );
        assertThat("Tener inegiClaveMunicipios", inegiClaveMunicipios.getContent().size(), is(  greaterThan( 0 ) ) );
        inegiClaveMunicipioService.borrar(inegiClaveMunicipioGuardada.getId());
    }
    
    @Test
    public void guardar() {
        InegiClaveMunicipio inegiClaveMunicipio = new InegiClaveMunicipio();
        inegiClaveMunicipio.setNombre("inegiClaveMunicipio");
        InegiClaveMunicipio inegiClaveMunicipioGuardado = inegiClaveMunicipioService.guardar(inegiClaveMunicipio);
    }
    
    @Test
    public void actualizar() {
        String nombreInegiClaveMunicipio = "cambiando nombre sepomex";
        InegiClaveMunicipio inegiClaveMunicipio = new InegiClaveMunicipio();
        inegiClaveMunicipio.setNombre("inegiClaveMunicipio2");
        InegiClaveMunicipio inegiClaveMunicipioGuardado = inegiClaveMunicipioService.guardar(inegiClaveMunicipio);
        inegiClaveMunicipioGuardado.setNombre( nombreInegiClaveMunicipio );
        inegiClaveMunicipioService.actualizar(inegiClaveMunicipioGuardado);
        InegiClaveMunicipio inegiClaveMunicipioEncontrado = inegiClaveMunicipioService.buscarPorId( inegiClaveMunicipio.getId() );
        assertThat("Deberia tener el nombre igual", nombreInegiClaveMunicipio, is( equalTo( inegiClaveMunicipioEncontrado.getNombre() ) ) );
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
    public void borrar() {
        int id = 1;
        inegiClaveMunicipioService.borrar(id);
        exception.expect(NoSuchElementException.class);
        inegiClaveMunicipioService.buscarPorId(id);
    }
}