package com.perales.sepomex.controller;

import com.perales.sepomex.contract.ControllerGeneric;
import com.perales.sepomex.model.Colonia;
import com.perales.sepomex.service.ColoniaService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/colonia/")
@Tag(name = "Colonia API", description = "Endpoints para consultar información de colonias y códigos postales")
public class ColoniaController implements ControllerGeneric<Colonia, Integer> {

    private final ColoniaService coloniaService;

    @Autowired
    public ColoniaController(ColoniaService coloniaService) {
        this.coloniaService = coloniaService;
    }

    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Buscar colonia por ID",
        description = "Obtiene los detalles de una colonia específica utilizando su ID."
    )
    public Colonia buscarPorId(@PathVariable Integer id) {
        return coloniaService.buscarPorId(id.longValue());
    }

    @GetMapping(params = {"page", "size"})
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Listar todas las colonias",
        description = "Obtiene una lista paginada de todas las colonias disponibles."
    )
    public Page<Colonia> buscarTodos(
            @Parameter(description = "Número de página", example = "0", schema = @Schema(defaultValue = "0"))
            @RequestParam int page,
            @Parameter(description = "Tamaño de página", example = "33", schema = @Schema(defaultValue = "33"))
            @RequestParam int size) {
        return coloniaService.buscarTodos(page, size);
    }

    @Hidden
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colonia guardar(@Validated @RequestBody Colonia entity) {
        return coloniaService.guardar(entity);
    }

    @Hidden
    @PutMapping
    public Colonia actualizar(@Validated @RequestBody Colonia entity) {
        return coloniaService.actualizar(entity);
    }

    @Hidden
    @DeleteMapping("/{id}")
    public Colonia borrar(@PathVariable Integer id) {
        return coloniaService.borrar(id.longValue());
    }

    @GetMapping(value = "/municipio/{id}", params = {"page", "size"}, produces = "application/json")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Buscar colonias por municipio",
        description = "Obtiene una lista paginada de colonias asociadas a un municipio específico."
    )
    public Page<Colonia> findByEstadoId(
            @PathVariable Integer id,
            @Parameter(description = "Número de página", example = "0", schema = @Schema(defaultValue = "0"))
            @RequestParam int page,
            @Parameter(description = "Tamaño de página", example = "20", schema = @Schema(defaultValue = "20"))
            @RequestParam int size) {
        return coloniaService.findByMunicipioId(id, page, size);
    }

    @GetMapping(value = "/search")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Buscar colonias por criterios",
        description = "Permite buscar colonias utilizando el nombre de la colonia. Opcionalmente, se pueden filtrar por el ID del estado y/o municipio.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "nombre",
                description = "Nombre de la colonia a buscar",
                required = true,
                example = "cañada blanca"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "estado.id",
                description = "ID del estado (opcional)",
                required = false,
                example = "19"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "municipio.id",
                description = "ID del municipio (opcional)",
                required = false,
                example = "983"
            )
        }
    )
    public List<Colonia> search( @Parameter(hidden = true)  Colonia colonia) {
        return coloniaService.search(colonia);
    }

    @GetMapping("/codigopostal/{codigoPostal}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Buscar colonias por código postal",
        description = "Obtiene una lista de colonias asociadas a un código postal específico."
    )
    public ResponseEntity<List<Colonia>> buscarColoniasPorCodigoPostal(@PathVariable String codigoPostal) {
        List<Colonia> colonias = coloniaService.buscarColoniasPorCodigoPostal(codigoPostal);
        return ResponseEntity.ok(colonias);
    }
}