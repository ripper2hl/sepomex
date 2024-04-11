package com.perales.sepomex.service;

import com.perales.sepomex.contract.ServiceGeneric;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.model.Archivo;
import com.perales.sepomex.repository.ArchivoRepository;
import io.undertow.server.handlers.form.FormData;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.fileupload.FileItem;
@Service
public class ArchivoService implements ServiceGeneric<Archivo, Integer> {

    @Autowired
    private ArchivoRepository ArchivoRepository;

    @Autowired
    private ColoniaService coloniaService;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public Archivo buscarPorId(Integer id) {
        return ArchivoRepository.findById(id).get();
    }
    
    @Transactional(readOnly = true)
    public Page<Archivo> buscarTodos(int page, int size) {
        int firstResult = page * size;
        return ArchivoRepository.findAll( PageRequest.of( firstResult, size) );
    }
    
    @Transactional
    public Archivo guardar(Archivo entity) {
        return ArchivoRepository.save(entity);
    }
    
    @Transactional
    public Archivo actualizar(Archivo entity) {
        return ArchivoRepository.saveAndFlush(entity);
    }
    
    @Transactional
    public Archivo borrar(Integer id) {
        Archivo Archivo = ArchivoRepository.findById(id).get();
        ArchivoRepository.deleteById(id);
        return Archivo;
    }

    private void descargarArchivo(String fileUrl, String destinationPath) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    public Boolean procesarArchivo() {
        try {
            String fileUrl = "https://www.correosdemexico.gob.mx/datosabiertos/cp/cpdescarga.txt";
            String tempFilePath = "/tmp/tempFile" + System.currentTimeMillis() + ".txt";
            descargarArchivo(fileUrl, tempFilePath);
            File file = new File(tempFilePath);
            FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()),
                    false, file.getName(), (int) file.length(), file.getParentFile());

            // Leer el contenido del archivo y copiarlo al FileItem
            try (InputStream input = new FileInputStream(file);
                 OutputStream outputStream = fileItem.getOutputStream()) {
                IOUtils.copy(input, outputStream);
            }

            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            return coloniaService.actualizacionMasiva(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
