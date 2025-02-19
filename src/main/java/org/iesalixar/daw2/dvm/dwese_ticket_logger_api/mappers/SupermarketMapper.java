package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre entidades de tipo {@link Supermarket} y sus DTOs correspondientes.
 */
@Component
public class SupermarketMapper {

    /**
     * Convierte una entidad {@link Supermarket} en un DTO {@link SupermarketDTO}.
     *
     * @param supermarket La entidad a convertir.
     * @return El DTO resultante.
     */
    public SupermarketDTO toDTO(Supermarket supermarket) {
        if (supermarket == null) {
            return null;
        }
        SupermarketDTO dto = new SupermarketDTO();
        dto.setId(supermarket.getId());
        dto.setName(supermarket.getName());
        return dto;
    }

    /**
     * Convierte un DTO {@link SupermarketCreateDTO} en una entidad {@link Supermarket}.
     *
     * @param createDTO El DTO a convertir.
     * @return La entidad resultante.
     */
    public Supermarket toEntity(SupermarketCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }
        return new Supermarket(createDTO.getName());
    }
}
