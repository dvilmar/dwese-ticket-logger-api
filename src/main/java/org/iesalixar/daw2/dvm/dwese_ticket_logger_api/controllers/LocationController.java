package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    /**
     * Lista todas las ubicaciones almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de ubicaciones o un mensaje de error.
     */
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        logger.info("Solicitando la lista de todas las ubicaciones...");
        try {
            List<LocationDTO> locations = locationService.getAllLocations();
            logger.info("Se han encontrado {} ubicaciones.", locations.size());
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            logger.error("Error al listar las ubicaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene una ubicación específica por su ID.
     *
     * @param id ID de la ubicación solicitada.
     * @return ResponseEntity con la ubicación encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Long id) {
        logger.info("Buscando ubicación con ID {}", id);
        try {
            LocationDTO locationDTO = locationService.getLocationById(id);
            logger.info("Ubicación con ID {} encontrada.", id);
            return ResponseEntity.ok(locationDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("No se encontró ninguna ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al buscar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la ubicación.");
        }
    }

    /**
     * Crea una nueva ubicación.
     *
     * @param locationCreateDTO DTO con los datos para crear la ubicación.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la ubicación creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createLocation(
            @Valid @RequestBody LocationCreateDTO locationCreateDTO,
            Locale locale) {
        logger.info("Creando una nueva ubicación con dirección {}", locationCreateDTO.getAddress());
        try {
            LocationDTO createdLocation = locationService.createLocation(locationCreateDTO, locale);
            logger.info("Ubicación creada exitosamente con ID {}", createdLocation.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear la ubicación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear la ubicación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la ubicación.");
        }
    }

    /**
     * Actualiza una ubicación existente.
     *
     * @param id ID de la ubicación a actualizar.
     * @param locationCreateDTO DTO con los datos para actualizar la ubicación.
     * @param locale Idioma para los mensajes de error.
     * @return ResponseEntity con la ubicación actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationCreateDTO locationCreateDTO,
            Locale locale) {
        logger.info("Actualizando ubicación con ID {}", id);
        try {
            LocationDTO updatedLocation = locationService.updateLocation(id, locationCreateDTO, locale);
            logger.info("Ubicación con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(updatedLocation);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la ubicación.");
        }
    }

    /**
     * Elimina una ubicación por su ID.
     *
     * @param id ID de la ubicación a eliminar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        logger.info("Eliminando ubicación con ID {}", id);
        try {
            locationService.deleteLocation(id);
            logger.info("Ubicación con ID {} eliminada exitosamente.", id);
            return ResponseEntity.ok("Ubicación eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar la ubicación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la ubicación.");
        }
    }
}
