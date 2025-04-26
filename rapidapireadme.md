# API de Códigos Postales de México 🌐

Bienvenido a la **API de Códigos Postales de México **, una herramienta poderosa para consultar información sobre colonias, códigos postales, municipios, estados y más. Esta API está diseñada para ser fácil de usar y altamente funcional, proporcionando datos precisos y actualizados.

---

## **Características Principales**
- 📍 **Buscar colonias por ID**: Obtén detalles específicos de una colonia.
- 🗂️ **Listar todas las colonias**: Consulta una lista paginada de todas las colonias disponibles.
- 🏙️ **Buscar colonias por municipio**: Encuentra colonias asociadas a un municipio específico.
- 🔍 **Buscar colonias por criterios**: Filtra colonias por nombre, estado o municipio.
- ✉️ **Buscar colonias por código postal**: Obtén colonias asociadas a un código postal.
- 🗺️ **Consultar municipios, estados y zonas**: Accede a información detallada sobre municipios, estados y tipos de zonas.

---

## **Base URL**
La URL base de la API es proporcionada por RapidAPI y se configura automáticamente al suscribirse al servicio.

---

## **Endpoints**
### **1. Buscar colonia por ID**
- **Descripción**: Obtiene los detalles de una colonia específica utilizando su ID.
- **Método**: `GET`
- **URL**: `/v1/colonia/{id}`
- **Parámetros**:
  - `id` (path): ID único de la colonia (requerido).
- **Ejemplo de Respuesta**:
  ```json
  {
    "id": 88724,
    "nombre": "Cañada Blanca",
    "estado": {
      "id": 19,
      "nombre": "Nuevo León"
    },
    "municipio": {
      "id": 983,
      "nombre": "Guadalupe"
    },
    "codigoPostal": {
      "id": 1,
      "nombre": "67100"
    }
  }
  ```

---

### **2. Listar todas las colonias**
- **Descripción**: Obtiene una lista paginada de todas las colonias disponibles.
- **Método**: `GET`
- **URL**: `/v1/colonia`
- **Parámetros**:
  - `page` (query): Número de página (requerido, por defecto `0`).
  - `size` (query): Tamaño de página (requerido, por defecto `33`).
- **Ejemplo de Respuesta**:
  ```json
  {
    "content": [
      {
        "id": 88724,
        "nombre": "Cañada Blanca"
      },
      {
        "id": 88725,
        "nombre": "San Ángel"
      }
    ],
    "totalElements": 100,
    "totalPages": 3,
    "size": 33,
    "number": 0
  }
  ```

---

### **3. Buscar colonias por municipio**
- **Descripción**: Obtiene una lista paginada de colonias asociadas a un municipio específico.
- **Método**: `GET`
- **URL**: `/v1/colonia/municipio/{id}`
- **Parámetros**:
  - `id` (path): ID del municipio (requerido).
  - `page` (query): Número de página (requerido, por defecto `0`).
  - `size` (query): Tamaño de página (requerido, por defecto `20`).
- **Ejemplo de Respuesta**:
  ```json
  {
    "content": [
      {
        "id": 88724,
        "nombre": "Cañada Blanca"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
  ```

---

### **4. Buscar colonias por criterios**
- **Descripción**: Permite buscar colonias utilizando el nombre de la colonia. Opcionalmente, se pueden filtrar por el ID del estado y/o municipio.
- **Método**: `GET`
- **URL**: `/v1/colonia/search`
- **Parámetros**:
  - `nombre` (query): Nombre de la colonia (requerido).
  - `estado.id` (query): ID del estado (opcional).
  - `municipio.id` (query): ID del municipio (opcional).
- **Ejemplo de Respuesta**:
  ```json
  [
    {
      "id": 88724,
      "nombre": "Cañada Blanca"
    }
  ]
  ```

---

### **5. Buscar colonias por código postal**
- **Descripción**: Obtiene una lista de colonias asociadas a un código postal específico.
- **Método**: `GET`
- **URL**: `/v1/colonia/codigopostal/{codigoPostal}`
- **Parámetros**:
  - `codigoPostal` (path): Código postal (requerido).
- **Ejemplo de Respuesta**:
  ```json
  [
    {
      "id": 88724,
      "nombre": "Cañada Blanca"
    }
  ]
  ```

---

### **6. Consultar municipios por estado**
- **Descripción**: Obtiene una lista paginada de municipios asociados a un estado específico.
- **Método**: `GET`
- **URL**: `/v1/municipio/estado/{id}`
- **Parámetros**:
  - `id` (path): ID del estado (requerido).
  - `page` (query): Número de página (requerido, por defecto `0`).
  - `size` (query): Tamaño de página (requerido, por defecto `20`).
- **Ejemplo de Respuesta**:
  ```json
  {
    "content": [
      {
        "id": 983,
        "nombre": "Guadalupe"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "size": 20,
    "number": 0
  }
  ```

---

## **Códigos de Respuesta**
- `200 OK`: Solicitud exitosa.
- `400 Bad Request`: Parámetros inválidos.
- `404 Not Found`: Recurso no encontrado.
- `500 Internal Server Error`: Error interno del servidor.

---

## **Autenticación**
La autenticación y la URL base son gestionadas automáticamente por RapidAPI al suscribirse al servicio.

---

¡Gracias por usar la API de Códigos Postales de México! 🚀