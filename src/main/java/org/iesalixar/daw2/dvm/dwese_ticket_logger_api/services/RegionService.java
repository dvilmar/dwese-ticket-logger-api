package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Obtiene todas las regiones y las convierte en una lista de RegionDTO.
     *
     * @return Lista de RegionDTO.
     */
    public Page<RegionDTO> getAllRegions(Pageable pageable) {
        logger.info("Solicitando todas las regiones...", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Region> regions = regionRepository.findAll(pageable);
            logger.info("Se han encontrado {} regiones.", regions.getNumberOfElements());
            return regions.map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de regiones: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una región por su ID y la convierte en un RegionDTO.
     *
     * @param id Identificador único de la región.
     * @return RegionDTO de la región encontrada.
     * @throws IllegalArgumentException Si la región no existe.
     */
    public RegionDTO getRegionById(Long id) {
        logger.info("Buscando región con ID {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la región con ID {}", id);
                    return new IllegalArgumentException("La región no existe.");
                });
        logger.info("Región con ID {} encontrada.", id);
        return regionMapper.toDTO(region);
    }

    /**
     * Crea una nueva región en la base de datos.
     *
     * @param regionCreateDTO DTO que contiene los datos de la región a crear.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la región creada.
     * @throws IllegalArgumentException Si ya existe una región con el mismo código.
     */
    public RegionDTO createRegion(RegionCreateDTO regionCreateDTO, Locale locale) {
        logger.info("Creando una nueva región con código {}", regionCreateDTO.getCode());
        if (regionRepository.existsByCode(regionCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            logger.warn("Error al crear región: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Region region = regionMapper.toEntity(regionCreateDTO);
        Region savedRegion = regionRepository.save(region);
        logger.info("Región creada exitosamente con ID {}", savedRegion.getId());
        return regionMapper.toDTO(savedRegion);
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param id Identificador de la región a actualizar.
     * @param regionCreateDTO DTO que contiene los nuevos datos de la región.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la región actualizada.
     * @throws IllegalArgumentException Si la región no existe o si el código ya está en uso.
     */
    public RegionDTO updateRegion(Long id, RegionCreateDTO regionCreateDTO, Locale locale) {
        logger.info("Actualizando región con ID {}", id);
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la región con ID {}", id);
                    return new IllegalArgumentException("La región no existe.");
                });

        if (regionRepository.existsRegionByCodeAndNotId(regionCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            logger.warn("Error al actualizar región: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        existingRegion.setCode(regionCreateDTO.getCode());
        existingRegion.setName(regionCreateDTO.getName());
        Region updatedRegion = regionRepository.save(existingRegion);
        logger.info("Región con ID {} actualizada exitosamente.", id);
        return regionMapper.toDTO(updatedRegion);
    }

    /**
     * Elimina una región específica por su ID.
     *
     * @param id Identificador único de la región.
     * @throws IllegalArgumentException Si la región no existe.
     */
    public void deleteRegion(Long id) {
        logger.info("Buscando región con ID {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la región con ID {}", id);
                    return new IllegalArgumentException("La región no existe.");
                });

        regionRepository.deleteById(id);
        logger.info("Región con ID {} eliminada exitosamente.", id);
    }
}
