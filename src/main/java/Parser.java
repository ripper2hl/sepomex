import com.perales.model.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by perales on 21/04/17.
 */
public class Parser {

    private static final String FILE_NAME = "/tmp/test.txt";
    private static final int CODIGO_POSTAL_POSICION = 0;
    private static final int ASENTAMIENTO_NOMBRE_POSICION = 1;
    private static final int ASENTAMIENTO_TIPO_POSICION = 2;
    private static final int MUNICIPIO_NOMBRE_POSICION = 3;
    private static final int ESTADO_NOMBRE_POSICION = 4;
    private static final int CIUDAD_NOMBRE_POSICION = 5;
    private static final int CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_POSICION = 6;
    private static final int ESTADO_INEGI_CLAVE_POSICION = 7;
    private static final int CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_OFICINA_POSICION = 8;
    private static final int CAMPO_VACIO_POSICION = 9;
    private static final int TIPO_ASENTAMIENTO_CLAVE_SEPOMEX_POSICION = 10;
    private static final int MUNICIPIO_CLAVE_INEGI_POSICION = 11;
    private static final int ASENTAMIENTO_IDENTIFICADOR_MUNICIPAL_POSICION = 12;
    private static final int ZONA_UBICACION_ASENTAMIENTO_POSICION = 13;
    private static final int CIUDAD_CLAVE_POSICION = 14;

    public static void main(String args[]) throws IOException {
        List<String> strings = Files.readAllLines(Paths.get(FILE_NAME), Charset.defaultCharset());
        boolean first = true;
        for(String s : strings){
            if(first){
                first = false;
            }else{
                List<String> list = Arrays.asList(s.split("\\|"));
                Parser parser = new Parser();
                Colonia colonia = parser.convertirListaColonia(list);
                System.out.println(colonia);
            }
        }
    }

    private Colonia convertirListaColonia(List<String> lista){
        Colonia colonia = new Colonia();
        AsentamientoTipo asentamientoTipo = new AsentamientoTipo();
        Ciudad ciudad = new Ciudad();
        Municipio municipio = new Municipio();
        Estado estado = new Estado();
        ZonaTipo zonaTipo = new ZonaTipo();

        colonia.setNombre(lista.get(ASENTAMIENTO_NOMBRE_POSICION));
        colonia.setCodigoPostal(lista.get(CODIGO_POSTAL_POSICION));
        colonia.setCodigoPostalAdministracionAsentamiento(lista.get(CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_POSICION));
        colonia.setCodigoPostalAdministracionAsentamientoOficina(lista.get(CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_OFICINA_POSICION));

        asentamientoTipo.setNombre( lista.get( ASENTAMIENTO_TIPO_POSICION ) );
        asentamientoTipo.setSepomexClave( lista.get( TIPO_ASENTAMIENTO_CLAVE_SEPOMEX_POSICION ) );
        colonia.setAsentamientoTipo(asentamientoTipo);

        ciudad.setNombre( lista.get( CIUDAD_NOMBRE_POSICION ) );
        ciudad.setClave( lista.get( CIUDAD_CLAVE_POSICION ) );
        colonia.setCiudad(ciudad);

        municipio.setNombre( lista.get( MUNICIPIO_NOMBRE_POSICION ) );
        municipio.setInegiClave( lista.get( MUNICIPIO_CLAVE_INEGI_POSICION ) );
        municipio.setInegiClave( lista.get( ASENTAMIENTO_IDENTIFICADOR_MUNICIPAL_POSICION ) );
        colonia.setMunicipio(municipio);

        estado.setNombre( lista.get( ESTADO_NOMBRE_POSICION ) );
        estado.setInegiClave( lista.get( ESTADO_INEGI_CLAVE_POSICION ) );
        colonia.setEstado(estado);

        zonaTipo.setNombre( lista.get( ZONA_UBICACION_ASENTAMIENTO_POSICION ) );
        colonia.setZonaTipo(zonaTipo);

        return colonia;
    }
}