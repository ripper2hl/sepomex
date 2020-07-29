package com.perales.sepomex.util;

import com.perales.sepomex.model.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by perales on 21/04/17.
 */
@Component
public class Parser {
    private final Logger logger = Logger.getGlobal();

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

    public static final int POSICIONES_MAXIMAS_SEPARADOR = 15;
    
    public static final String TEXT_FOR_DETECT_FIRST_LINE = "es elaborado por Correos de";
    public static final String TEXT_FOR_DETECT_FIELD_DESCRIPTION = "d_codigo";

    public Colonia convertirListaColonia(List<String> lista) {
        Colonia colonia = null;
        if(lista.size() > 13){
            CodigoPostal codigoPostal = new CodigoPostal();
            CodigoPostal codigoPostalAdministracionAsentamiento = new CodigoPostal();
            CodigoPostal codigoPostalAdministracionAsentamientoOficina = new CodigoPostal();
            InegiClaveCiudad inegiClaveCiudad = new InegiClaveCiudad();
            InegiClaveMunicipio inegiClaveMunicipio = new InegiClaveMunicipio();
            colonia = new Colonia();
            Municipio municipio = new Municipio();
            Estado estado = new Estado();
            ZonaTipo zonaTipo = new ZonaTipo();
    
            codigoPostal.setNombre(lista.get(CODIGO_POSTAL_POSICION));
            codigoPostalAdministracionAsentamiento.setNombre(lista.get(CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_POSICION));
            codigoPostalAdministracionAsentamientoOficina.setNombre(lista.get(CODIGO_POSTAL_ADMINISTRACION_ASENTAMIENTO_OFICINA_POSICION));
    
            colonia.setNombre(lista.get(ASENTAMIENTO_NOMBRE_POSICION));
            colonia.setCodigoPostal(codigoPostal);
            colonia.setCodigoPostalAdministracionAsentamiento(codigoPostalAdministracionAsentamiento);
            colonia.setCodigoPostalAdministracionAsentamientoOficina(codigoPostalAdministracionAsentamientoOficina);
    
            if( lista.size() > CIUDAD_CLAVE_POSICION ){
                inegiClaveCiudad.setNombre(lista.get(CIUDAD_CLAVE_POSICION));
                colonia.setInegiClaveCiudad(inegiClaveCiudad);
            }
    
            inegiClaveMunicipio.setNombre(lista.get(MUNICIPIO_CLAVE_INEGI_POSICION));
            colonia.setInegiClaveMunicipio(inegiClaveMunicipio);
    
            colonia.setAsentamientoTipo(obtenerAsentamientoTipo(lista));
            colonia.setCiudad(obtenerCiudad(lista));
            colonia.setMunicipio(obtenerMunicipio(lista));
            colonia.setEstado(obtenerEstado(lista));
            colonia.setZonaTipo(obtenerZonaTipo(lista));
    
            colonia.getCiudad().setEstado(colonia.getEstado());
            colonia.getMunicipio().setEstado(colonia.getEstado());
            colonia.setIdentificadorMunicipal(lista.get(ASENTAMIENTO_IDENTIFICADOR_MUNICIPAL_POSICION));
        }
        return colonia;
    }

    public AsentamientoTipo obtenerAsentamientoTipo(List<String> lista) {
        AsentamientoTipo asentamientoTipo = new AsentamientoTipo();
        asentamientoTipo.setNombre(lista.get(ASENTAMIENTO_TIPO_POSICION));
        asentamientoTipo.setSepomexClave(lista.get(TIPO_ASENTAMIENTO_CLAVE_SEPOMEX_POSICION));
        return asentamientoTipo;
    }

    public Ciudad obtenerCiudad(List<String> lista) {
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre(lista.get(CIUDAD_NOMBRE_POSICION));
        return ciudad;
    }

    public Municipio obtenerMunicipio(List<String> lista) {
        Municipio municipio = new Municipio();
        municipio.setNombre(lista.get(MUNICIPIO_NOMBRE_POSICION));
        return municipio;
    }

    public Estado obtenerEstado(List<String> lista) {
        Estado estado = new Estado();
        estado.setNombre(lista.get(ESTADO_NOMBRE_POSICION));
        estado.setInegiClave(lista.get(ESTADO_INEGI_CLAVE_POSICION));
        return estado;
    }

    public ZonaTipo obtenerZonaTipo(List<String> lista) {
        ZonaTipo zonaTipo = new ZonaTipo();
        zonaTipo.setNombre(lista.get(ZONA_UBICACION_ASENTAMIENTO_POSICION));
        return zonaTipo;
    }
    
    public void guardarArchivoEntidadesParseadas(String archivoSepomexNombre, String archivoParseadoNombre) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream( archivoSepomexNombre ), "UTF-8") ) ) {
            try(FileOutputStream fos = new FileOutputStream( archivoParseadoNombre, true ) ){
                try(ObjectOutputStream oos = new ObjectOutputStream( fos ) ){
                    List<Colonia> colonias = br.lines().parallel()
                            .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIRST_LINE) )
                            .filter( line -> !line.contains(Parser.TEXT_FOR_DETECT_FIELD_DESCRIPTION) )
                            .map( line -> Arrays.asList(line.split("\\|")) )
                            .map( list -> {
                                Colonia colonia = convertirListaColonia(list);
                                return colonia;
                            }).collect(Collectors.toList());
                    logger.info(colonias.toString());
                    oos.writeObject( colonias );
                }catch (IOException e){
                    throw e;
                }
            }catch (IOException e){
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}