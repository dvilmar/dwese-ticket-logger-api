package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.LocationMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Obtiene todas las ubicaciones y las convierte en una lista de LocationDTO.
     *
     * @return Lista de LocationDTO.
     */
    public List<LocationDTO> getAllLocations() {
        logger.info("Solicitando todas las ubicaciones...");
        try {
            List<Location> locations = locationRepository.findAll();
            logger.info("Se han encontrado {} ubicaciones.", locations.size());
            return locations.stream().map(locationMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener la lista de ubicaciones: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una ubicación por su ID y la convierte en un LocationDTO.
     *
     * @param id Identificador único de la ubicación.
     * @return LocationDTO de la ubicación encontrada.
     * @throws IllegalArgumentException Si la ubicación no existe.
     */
    public LocationDTO getLocationById(Long id) {
        logger.info("Buscando ubicación con ID {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la ubicación con ID {}", id);
                    return new IllegalArgumentException("La ubicación no existe.");
                });
        logger.info("Ubicación con ID {} encontrada.", id);
        return locationMapper.toDTO(location);
    }

    /**
     * Crea una nueva ubicación en la base de datos.
     *
     * @param locationCreateDTO DTO que contiene los datos de la ubicación a crear.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la ubicación creada.
     * @throws IllegalArgumentException Si el supermercado o la provincia no existen.
     */
    public LocationDTO createLocation(LocationCreateDTO locationCreateDTO, Locale locale) {
        logger.info("Creando una nueva ubicación con dirección {}", locationCreateDTO.getAddress());

        Supermarket supermarket = supermarketRepository.findById(locationCreateDTO.getSupermarketId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.location-controller.insert.supermarketNotFound", null, locale);
                    logger.warn("Error al crear ubicación: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        Province province = provinceRepository.findById(locationCreateDTO.getProvinceId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.location-controller.insert.provinceNotFound", null, locale);
                    logger.warn("Error al crear ubicación: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        Location location = locationMapper.toEntity(locationCreateDTO, supermarket, province);
        Location savedLocation = locationRepository.save(location);
        logger.info("Ubicación creada exitosamente con ID {}", savedLocation.getId());
        return locationMapper.toDTO(savedLocation);
    }

    /**
     * Actualiza una ubicación existente en la base de datos.
     *
     * @param id ID de la ubicación a actualizar.
     * @param locationCreateDTO DTO que contiene los nuevos datos de la ubicación.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la ubicación actualizada.
     * @throws IllegalArgumentException Si la ubicación no existe, el supermercado no existe o la provincia no existe.
     */
    public LocationDTO updateLocation(Long id, LocationCreateDTO locationCreateDTO, Locale locale) {
        logger.info("Actualizando ubicación con ID {}", id);

        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la ubicación con ID {}", id);
                    return new IllegalArgumentException("La ubicación no existe.");
                });

        Supermarket supermarket = supermarketRepository.findById(locationCreateDTO.getSupermarketId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.location-controller.update.supermarketNotFound", null, locale);
                    logger.warn("Error al actualizar ubicación: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        Province province = provinceRepository.findById(locationCreateDTO.getProvinceId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.location-controller.update.provinceNotFound", null, locale);
                    logger.warn("Error al actualizar ubicación: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        existingLocation.setAddress(locationCreateDTO.getAddress());
        existingLocation.setCity(locationCreateDTO.getCity());
        existingLocation.setSupermarket(supermarket);
        existingLocation.setProvince(province);

        Location updatedLocation = locationRepository.save(existingLocation);
        logger.info("Ubicación con ID {} actualizada exitosamente.", id);
        return locationMapper.toDTO(updatedLocation);
    }

    /**
     * Elimina una ubicación por su ID.
     *
     * @param id Identificador único de la ubicación.
     * @throws IllegalArgumentException Si la ubicación no existe.
     */
    public void deleteLocation(Long id) {
        logger.info("Buscando ubicación con ID {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró la ubicación con ID {}", id);
                    return new IllegalArgumentException("La ubicación no existe.");
                });

        locationRepository.deleteById(id);
        logger.info("Ubicación con ID {} eliminada exitosamente.", id);
    }
}
