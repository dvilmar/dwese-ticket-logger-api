package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Region`.
 * Utiliza `RegionDAO` para interactuar con la base de datos.
 */
@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;

    // @Autowired
    // private MessageSource messageSource;

    /**
     * Lista todas las regiones almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de regiones o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        logger.info("Solicitando la lista de todas las regiones...");
        try {
            List<RegionDTO> regionDTOs = regionService.getAllRegions();
            logger.info("Se han encontrado {} regiones.", regionDTOs.size());
            return ResponseEntity.ok(regionDTOs);
        } catch (Exception e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
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
            Optional<RegionDTO> regionDTO = regionService.getRegionById(id);

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
     * Crea una nueva región en la base de datos.
     *
     * @param regionCreateDTO Objeto JSON que representa la nueva región.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con la región creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionCreateDTO regionCreateDTO, Locale locale) {
        logger.info("Insertando nueva región con código {}", regionCreateDTO.getCode());
       try {
           RegionDTO createdRegion = regionService.createRegion(regionCreateDTO, locale);
           return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
       } catch (IllegalArgumentException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       } catch (Exception e) {
           logger.error("Error al crear la región: {}", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la región.");
       }
    }

    /**
     * Actualiza una región existente por su ID.
     *
     * @param id Id de la región a actualizar.
     * @param regionCreateDTO Objeto JSON con los nuevos datos.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con la región actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable Long id, @Valid @RequestBody RegionCreateDTO regionCreateDTO, Locale locale) {
        logger.info("Actualizando región con ID {}", id);
        try {
            RegionDTO updatedRegion = regionService.updateRegion(id, regionCreateDTO, locale);
            return ResponseEntity.ok(updatedRegion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la región.");
        }
    }


    /**
     * Elimina una región específica por su ID.
     *
     * @param id ID de la región a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegion(@PathVariable Long id) {
        logger.info("Eliminando región con ID {}", id);
        try {
            regionService.deleteRegion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la región");
        }
    }
}