package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionMapper regionMapper;

    /**
     * Lista todas las regiones almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<Page<RegionDTO>> getAllRegions(@PageableDefault(size = 10, sort = "name,asc") Pageable pageable) {
        logger.info("Solicitando la lista de todas las regiones... Página: {}, Tamaño: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<RegionDTO> regions = regionService.getAllRegions(pageable);
            logger.info("Se han encontrado {} regiones.", regions.getTotalElements());
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            logger.error("Error al listar las regiones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Obtiene una región específica por su ID.
     *
     * @param id ID de la región solicitada.
     * @return ResponseEntity con la región encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRegionById(@PathVariable Long id) {
        logger.info("Buscando región con ID {}", id);
        try {
            Optional<RegionDTO> regionDTO = Optional.ofNullable(regionService.getRegionById(id));

            if (regionDTO.isPresent()) {
                logger.info("Región con ID {} encontrada.", id);
                return ResponseEntity.ok(regionDTO.get());
            } else {
                logger.warn("No se encontró ninguna región con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La región no existe.");
            }
        } catch (Exception e) {
            logger.error("Error al buscar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la región.");
        }
    }

    /**
     * Crea una nueva región.
     *
     * @param regionCreateDTO DTO con los datos para crear la región.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la región creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createRegion(
            @Valid @RequestBody RegionCreateDTO regionCreateDTO,
            Locale locale) {
        logger.info("Creando una nueva región con código {}", regionCreateDTO.getCode());
        try {
            RegionDTO createdRegion = regionService.createRegion(regionCreateDTO, locale);
            logger.info("Región creada exitosamente con ID {}", createdRegion.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear la región: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear la región: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la región.");
        }
    }

    /**
     * Actualiza una región existente.
     *
     * @param id ID de la región a actualizar.
     * @param regionCreateDTO DTO con los datos para actualizar la región.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la región actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionCreateDTO regionCreateDTO,
            Locale locale) {
        logger.info("Actualizando región con ID {}", id);
        try {
            RegionDTO updatedRegion = regionService.updateRegion(id, regionCreateDTO, locale);
            logger.info("Región con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(updatedRegion);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la región.");
        }
    }

    /**
     * Elimina una región por su ID.
     *
     * @param id ID de la región a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        logger.info("Eliminando región con ID {}", id);
        try {
            regionService.deleteRegion(id);
            logger.info("Región con ID {} eliminada exitosamente.", id);
            return ResponseEntity.ok("Región eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la región.");
        }
    }
}
