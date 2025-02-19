package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.SupermarketMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona las operaciones CRUD para los supermercados.
 */
@Service
public class SupermarketService {

    private static final Logger logger = LoggerFactory.getLogger(SupermarketService.class);

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private SupermarketMapper supermarketMapper;

    /**
     * Obtiene todos los supermercados.
     *
     * @return Lista de DTOs de supermercados.
     */
    public List<SupermarketDTO> getAllSupermarkets() {
        logger.info("Obteniendo todos los supermercados...");
        List<Supermarket> supermarkets = supermarketRepository.findAll();
        return supermarkets.stream()
                .map(supermarketMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un supermercado por su ID.
     *
     * @param id Identificador del supermercado.
     * @return DTO del supermercado encontrado.
     */
    public SupermarketDTO getSupermarketById(Long id) {
        logger.info("Buscando supermercado con ID {}", id);
        Supermarket supermarket = supermarketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El supermercado no existe."));
        return supermarketMapper.toDTO(supermarket);
    }

    /**
     * Crea un nuevo supermercado.
     *
     * @param createDTO DTO con los datos del supermercado a crear.
     * @return DTO del supermercado creado.
     */
    public SupermarketDTO createSupermarket(SupermarketCreateDTO createDTO) {
        logger.info("Creando un nuevo supermercado con nombre {}", createDTO.getName());
        if (supermarketRepository.existsByName(createDTO.getName())) {
            throw new IllegalArgumentException("El nombre del supermercado ya existe.");
        }
        Supermarket supermarket = supermarketMapper.toEntity(createDTO);
        Supermarket savedSupermarket = supermarketRepository.save(supermarket);
        return supermarketMapper.toDTO(savedSupermarket);
    }

    /**
     * Actualiza un supermercado existente.
     *
     * @param id        ID del supermercado a actualizar.
     * @param createDTO DTO con los nuevos datos del supermercado.
     * @return DTO del supermercado actualizado.
     */
    public SupermarketDTO updateSupermarket(Long id, SupermarketCreateDTO createDTO) {
        logger.info("Actualizando supermercado con ID {}", id);
        Supermarket existingSupermarket = supermarketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El supermercado no existe."));
        if (supermarketRepository.existsSupermarketByNameAndNotId(createDTO.getName(), id)) {
            throw new IllegalArgumentException("El nombre del supermercado ya existe.");
        }
        existingSupermarket.setName(createDTO.getName());
        Supermarket updatedSupermarket = supermarketRepository.save(existingSupermarket);
        return supermarketMapper.toDTO(updatedSupermarket);
    }

    /**
     * Elimina un supermercado por su ID.
     *
     * @param id ID del supermercado a eliminar.
     */
    public void deleteSupermarket(Long id) {
        logger.info("Eliminando supermercado con ID {}", id);
        if (!supermarketRepository.existsById(id)) {
            throw new IllegalArgumentException("El supermercado no existe.");
        }
        supermarketRepository.deleteById(id);
    }
}
