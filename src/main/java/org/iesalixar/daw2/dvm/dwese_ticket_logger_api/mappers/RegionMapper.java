package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

@Component
public class RegionMapper {

    /**
     * Convierte una entidad `Region` a un `RegionDTO` (datos básicos).
     *
     * @param region Entidad de región.
     * @return DTO correspondiente.
     */
    public RegionDTO toDTO(Region region) {
        RegionDTO dto = new RegionDTO(region.getId(), region.getCode(), region.getName());
        return dto;
    }

    /**
     * Convierte un `RegionDTO` a una entidad `Region`.
     *
     * @param dto DTO de región.
     * @return Entidad Region
     */
    public Region toEntity(RegionDTO dto) {
        Region region = new Region();
        region.setId(dto.getId());
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        return region;
    }

    /**
     * Convierte un `RegionCreateDTO` a una entidad `Region` (para creación).
     * @param createDTO DTO para crear regiones
     * @return Entidad Region
     */
    public Region toEntity(RegionCreateDTO createDTO) {
        Region region = new Region();
        region.setCode(createDTO.getCode());
        region.setName(createDTO.getName());
        return region;
    }
}
