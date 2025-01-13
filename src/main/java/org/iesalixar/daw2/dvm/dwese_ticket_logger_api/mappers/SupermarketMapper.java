package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

@Component
public class SupermarketMapper {

    public RegionDTO toDTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setId(region.getId());
        dto.setCode(region.getCode());
        dto.setName(region.getName());
        return dto;
    }

    public Region toEntity(RegionDTO dto) {
        Region region = new Region();
        region.setCode(dto.getCode());
        region.setName(dto.getName());
        return region;
    }

    public Region toEntity(RegionCreateDTO createDTO) {
        Region region = new Region();
        region.setCode(createDTO.getCode());
        region.setName(createDTO.getName());
        return region;
    }
}
