package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.LocationDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.SupermarketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    // Convertir una entidad a LocationListDTO
    public LocationDTO toDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());

        // Asignar supermercado al DTO
        if (location.getSupermarket() != null) {
            dto.setSupermarket(new SupermarketDTO(
                    location.getSupermarket().getId(),
                    location.getSupermarket().getName()
            ));
        }

        // Asignar provincia al DTO, incluyendo la región si existe
        if (location.getProvince() != null) {
            Province province = location.getProvince();
            ProvinceDTO provinceDTO = new ProvinceDTO();
            provinceDTO.setId(province.getId());
            provinceDTO.setCode(province.getCode());
            provinceDTO.setName(province.getName());

            if (province.getRegion() != null) {
                Region region = province.getRegion();
                RegionDTO regionDTO = new RegionDTO();
                regionDTO.setId(region.getId());
                regionDTO.setCode(region.getCode());
                regionDTO.setName(region.getName());
                provinceDTO.setRegion(regionDTO);
            }

            dto.setProvince(provinceDTO);
        }

        return dto;
    }

    // Convertir LocationCreateDTO a una entidad
    public Location toEntity(LocationCreateDTO createDTO, Supermarket supermarket, Province province) {
        Location location = new Location();
        location.setAddress(createDTO.getAddress());
        location.setCity(createDTO.getCity());

        // Asignar supermercado y provincia
        location.setSupermarket(supermarket);
        location.setProvince(province);

        return location;
    }
}
