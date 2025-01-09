package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;

    public List<RegionDTO> getAllRegions() {
        try {
            logger.info("Obteniendo todas las regiones...");
            List<Region> regions = regionRepository.findAll();
            logger.info("Se encontraron {} regiones.", regions.size());
            return regions.stream()
                    .map(regionMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener todas las regiones: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las regiones.", e);
        }
    }

    public Optional<RegionDTO> getRegionById(Long id) {
        try {
            logger.info("Buscando región con ID {}", id);
            return regionRepository.findById(id).map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al buscar región con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al buscar la región.", e);
        }
    }

    public ResponseEntity<?> createRegion(@Valid RegionCreateDTO regionCreateDTO, Locale locale) {
        try {
            logger.info("Creando una nueva región con código {}", regionCreateDTO.getCode());

            if (regionRepository.existsByCode(regionCreateDTO.getCode())) {
                String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
                logger.warn("Error al crear región: El código {} ya existe.", regionCreateDTO.getCode());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            Region region = regionMapper.toEntity(regionCreateDTO);
            Region savedRegion = regionRepository.save(region);

            logger.info("Región creada exitosamente con ID {}", savedRegion.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(regionMapper.toDTO(savedRegion));
        } catch (Exception e) {
            logger.error("Error al crear la región: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la región.");
        }
    }

    public ResponseEntity<?> updateRegion(Long id, @Valid RegionCreateDTO regionCreateDTO, Locale locale) {
        try {
            logger.info("Actualizando región con ID {}", id);

            Optional<Region> existingRegion = regionRepository.findById(id);
            if (!existingRegion.isPresent()) {
                logger.warn("No se encontró región con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La región no existe.");
            }

            if (regionRepository.existsRegionByCodeAndNotId(regionCreateDTO.getCode(), id)) {
                String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
                logger.warn("Error al actualizar región: El código {} ya está en uso.", regionCreateDTO.getCode());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            Region regionToUpdate = existingRegion.get();
            regionToUpdate.setCode(regionCreateDTO.getCode());
            regionToUpdate.setName(regionCreateDTO.getName());

            Region updatedRegion = regionRepository.save(regionToUpdate);

            logger.info("Región con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(regionMapper.toDTO(updatedRegion));
        } catch (Exception e) {
            logger.error("Error al actualizar región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la región");
        }
    }

    public ResponseEntity<?> deleteRegion(Long id) {
        try {
            logger.info("Eliminando región con ID {}", id);

            if (!regionRepository.existsById(id)) {
                logger.warn("No se encontró región con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La región no existe.");
            }

            regionRepository.deleteById(id);

            logger.info("Región con ID {} eliminada exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la región");
        }
    }
}
