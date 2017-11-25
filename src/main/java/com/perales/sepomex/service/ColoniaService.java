package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.*;
import com.perales.sepomex.repository.ColoniaRepository;
import com.perales.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class ColoniaService implements ServiceGeneric<Colonia, Integer> {

  @Autowired
  private ColoniaRepository coloniaRepository;

  @Autowired
  private MunicipioService municipioService;

  @Autowired
  private CiudadService ciudadService;

  @Autowired
  private EstadoService estadoService;

  @Autowired
  private AsentamientoTipoService asentamientoTipoService;

  @Autowired
  private ZonaTipoService zonaTipoService;

  private static final String FILE_NAME = "/tmp/sepomex.txt";
  private static final int POSICIONES_MAXIMAS_SEPARADOR = 15;

  public Colonia buscarPorId(Integer id) {
    return coloniaRepository.getOne(id);
  }

  public List<Colonia> buscarTodos(int page, int size) {
    return null;
  }

  public Colonia guardar(Colonia entity) {
    return coloniaRepository.save(entity);
  }

  public Colonia actualizar(Colonia entity) {
    return null;
  }

  public Colonia borrar(Integer id) {
    return null;
  }

  public Boolean cargaMasiva() throws IOException {
    Parser parser = new Parser();
    List<String> strings = Files.readAllLines(Paths.get(FILE_NAME), Charset.forName("UTF-8"));
    Integer contador = 0;
    for (String s : strings) {
      contador++;
      List<String> list = Arrays.asList(s.split("\\|"));
      if (list.size() == POSICIONES_MAXIMAS_SEPARADOR) {
        Colonia colonia = parser.convertirListaColonia(list);
        revisarColonia(colonia);
        System.out.println(colonia);
      }
    }
    System.out.println(contador);
    return true;
  }

  private void revisarColonia(Colonia colonia) {
    AsentamientoTipo asentamientoTipo = asentamientoTipoService
        .findBySepomexClave(colonia.getAsentamientoTipo().getSepomexClave());
    if (asentamientoTipo == null) {
      asentamientoTipo = asentamientoTipoService.guardar(colonia.getAsentamientoTipo());
      colonia.setAsentamientoTipo(asentamientoTipo);
    } else {
      colonia.setAsentamientoTipo(asentamientoTipo);
    }

    Estado estado = estadoService.findByInegiClave(colonia.getEstado().getInegiClave());
    if (estado == null) {
      estado = estadoService.guardar(colonia.getEstado());
      colonia.setEstado(estado);
      System.out.println(estado);
    } else {
      colonia.setEstado(estado);
    }
    System.out.println("Clave de ciudad" +  colonia.getClaveCiudad());
    System.out.println("Clave de municipio" +  colonia.getClaveMunicipio());

    Ciudad ciudad = ciudadService.findFirstByNombreAndEstadoId(colonia.getCiudad().getNombre(), estado.getId());
    if (ciudad == null) {
      colonia.getCiudad().setEstado(estado);
      ciudad = ciudadService.guardar(colonia.getCiudad());
      colonia.setCiudad(ciudad);
    } else {
      colonia.setCiudad(ciudad);
    }

    Municipio municipio = municipioService.findFirstByNombreAndEstadoId(colonia.getMunicipio().getNombre(), estado.getId());
    if (municipio == null) {
      colonia.getMunicipio().setEstado(estado);
      municipio = municipioService.guardar( colonia.getMunicipio() );
      colonia.setMunicipio(municipio);
    } else {
      colonia.setMunicipio(municipio);
    }

    ZonaTipo zonaTipo = zonaTipoService.findByNombre(colonia.getZonaTipo().getNombre());
    if (zonaTipo == null) {
      zonaTipo = zonaTipoService.guardar(colonia.getZonaTipo());
      colonia.setZonaTipo(zonaTipo);
    } else {
      colonia.setZonaTipo(zonaTipo);
    }
    guardar(colonia);

  }

}
