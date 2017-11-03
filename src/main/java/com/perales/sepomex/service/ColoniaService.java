package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Colonia;
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

@Service
public class ColoniaService implements ServiceGeneric<Colonia, Integer> {

  @Autowired
  private ColoniaRepository coloniaRepository;

  private static final String FILE_NAME = "/tmp/test.txt";
  private static final int POSICIONES_MAXIMAS_SEPARADOR = 15;

  public Colonia buscarPorId(Integer id) {
    return null;
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

  @Transactional
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

  private void revisarColonia(Colonia colonia){
      guardar(colonia);
  }


}
