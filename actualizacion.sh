#!/bin/bash

# Descargar el archivo desde la URL
curl -O https://www.correosdemexico.gob.mx/datosabiertos/cp/cpdescarga.txt

# Cambiar el encoding del archivo a UTF-8
iconv -f ISO-8859-1 -t UTF-8 cpdescarga.txt -o cpdescarga_utf8.txt

# Realizar la solicitud PATCH con curl a la URL localhost:8080/v1/archivo/actualizacion
curl -X POST http://localhost:8080/v1/archivo/actualizacion -F file=@cpdescarga_utf8.txt

# Eliminar los archivos temporales
rm cpdescarga.txt cpdescarga_utf8.txt
