package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.ProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    /**
     * Lista todas las provincias almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de provincias o un mensaje de error.
     */
    @GetMapping
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces() {
        logger.info("Solicitando la lista de todas las provincias...");
        try {
            List<ProvinceDTO> provinces = provinceService.getAllProvinces();
            logger.info("Se han encontrado {} provincias.", provinces.size());
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene una provincia específica por su ID.
     *
     * @param id ID de la provincia solicitada.
     * @return ResponseEntity con la provincia encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProvinceById(@PathVariable Long id) {
        logger.info("Buscando provincia con ID {}", id);
        try {
            ProvinceDTO provinceDTO = provinceService.getProvinceById(id);
            logger.info("Provincia con ID {} encontrada.", id);
            return ResponseEntity.ok(provinceDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("No se encontró ninguna provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al buscar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la provincia.");
        }
    }

    /**
     * Crea una nueva provincia.
     *
     * @param provinceCreateDTO DTO con los datos para crear la provincia.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la provincia creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createProvince(
            @Valid @RequestBody ProvinceCreateDTO provinceCreateDTO,
            Locale locale) {
        logger.info("Creando una nueva provincia con código {}", provinceCreateDTO.getCode());
        try {
            ProvinceDTO createdProvince = provinceService.createProvince(provinceCreateDTO, locale);
            logger.info("Provincia creada exitosamente con ID {}", createdProvince.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProvince);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la provincia.");
        }
    }

    /**
     * Actualiza una provincia existente.
     *
     * @param id ID de la provincia a actualizar.
     * @param provinceCreateDTO DTO con los datos para actualizar la provincia.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la provincia actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(
            @PathVariable Long id,
            @Valid @RequestBody ProvinceCreateDTO provinceCreateDTO,
            Locale locale) {
        logger.info("Actualizando provincia con ID {}", id);
        try {
            ProvinceDTO updatedProvince = provinceService.updateProvince(id, provinceCreateDTO, locale);
            logger.info("Provincia con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(updatedProvince);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la provincia.");
        }
    }

    /**
     * Elimina una provincia por su ID.
     *
     * @param id ID de la provincia a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            provinceService.deleteProvince(id);
            logger.info("Provincia con ID {} eliminada exitosamente.", id);
            return ResponseEntity.ok("Provincia eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la provincia.");
        }
    }
}
