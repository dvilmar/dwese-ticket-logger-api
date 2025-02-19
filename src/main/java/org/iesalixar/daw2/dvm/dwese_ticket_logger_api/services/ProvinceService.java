package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.ProvinceMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceService.class);

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Obtiene todas las provincias y las convierte en una lista de ProvinceDTO.
     *
     * @return Lista de ProvinceDTO.
     */
    public List<ProvinceDTO> getAllProvinces() {
        logger.info("Solicitando todas las provincias...");
        try {
            List<Province> provinces = provinceRepository.findAll();
            logger.info("Se han encontrado {} provincias.", provinces.size());
            return provinces.stream()
                    .map(provinceMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener la lista de provincias: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una provincia por su ID y la convierte en un ProvinceDTO.
     *
     * @param id Identificador único de la provincia.
     * @return ProvinceDTO de la provincia encontrada.
     * @throws IllegalArgumentException Si la provincia no existe.
     */
    public ProvinceDTO getProvinceById(Long id) {
        logger.info("Buscando provincia con ID {}", id);
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la provincia con ID {}", id);
                    return new IllegalArgumentException("La provincia no existe.");
                });
        logger.info("Provincia con ID {} encontrada.", id);
        return provinceMapper.toDTO(province);
    }

    /**
     * Crea una nueva provincia en la base de datos.
     *
     * @param provinceCreateDTO DTO que contiene los datos de la provincia a crear.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la provincia creada.
     * @throws IllegalArgumentException Si ya existe una provincia con el mismo código o si la región no existe.
     */
    public ProvinceDTO createProvince(ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        logger.info("Creando una nueva provincia con código {}", provinceCreateDTO.getCode());

        if (provinceRepository.existsByCode(provinceCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale);
            logger.warn("Error al crear provincia: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Region region = regionRepository.findById(provinceCreateDTO.getRegionId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.province-controller.insert.regionNotFound", null, locale);
                    logger.warn("Error al crear provincia: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        Province province = provinceMapper.toEntity(provinceCreateDTO, region);
        Province savedProvince = provinceRepository.save(province);
        logger.info("Provincia creada exitosamente con ID {}", savedProvince.getId());
        return provinceMapper.toDTO(savedProvince);
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param id ID de la provincia a actualizar.
     * @param provinceCreateDTO DTO que contiene los nuevos datos de la provincia.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la provincia actualizada.
     * @throws IllegalArgumentException Si la provincia no existe, el código ya está en uso o si la región no existe.
     */
    public ProvinceDTO updateProvince(Long id, ProvinceCreateDTO provinceCreateDTO, Locale locale) {
        logger.info("Actualizando provincia con ID {}", id);

        Province existingProvince = provinceRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la provincia con ID {}", id);
                    return new IllegalArgumentException("La provincia no existe.");
                });

        if (provinceRepository.existsProvinceByCodeAndNotId(provinceCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.province-controller.update.codeExist", null, locale);
            logger.warn("Error al actualizar provincia: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Region region = regionRepository.findById(provinceCreateDTO.getRegionId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.province-controller.update.regionNotFound", null, locale);
                    logger.warn("Error al actualizar provincia: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        existingProvince.setCode(provinceCreateDTO.getCode());
        existingProvince.setName(provinceCreateDTO.getName());
        existingProvince.setRegion(region);
        Province updatedProvince = provinceRepository.save(existingProvince);
        logger.info("Provincia con ID {} actualizada exitosamente.", id);
        return provinceMapper.toDTO(updatedProvince);
    }

    /**
     * Elimina una provincia por su ID.
     *
     * @param id Identificador único de la provincia.
     * @throws IllegalArgumentException Si la provincia no existe.
     */
    public void deleteProvince(Long id) {
        logger.info("Buscando provincia con ID {}", id);

        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la provincia con ID {}", id);
                    return new IllegalArgumentException("La provincia no existe.");
                });

        provinceRepository.deleteById(id);
        logger.info("Provincia con ID {} eliminada exitosamente.", id);
    }
}
