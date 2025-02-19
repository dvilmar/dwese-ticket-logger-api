package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.ProvinceDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.springframework.stereotype.Component;

/**
 * Componente responsable de mapear entre entidades de tipo {@link Province}
 * y sus correspondientes objetos DTO ({@link ProvinceDTO}, {@link ProvinceCreateDTO}).
 *
 * Este mapper facilita la conversión entre los objetos utilizados en la base de datos
 * y aquellos que se transfieren a través de la API.
 */
@Component
public class ProvinceMapper {

    /**
     * Convierte una entidad {@link Province} en un objeto DTO {@link ProvinceDTO}.
     *
     * @param province La entidad {@link Province} a convertir.
     * @return Un objeto {@link ProvinceDTO} con los datos de la entidad,
     *         o {@code null} si la entidad es {@code null}.
     */
    public ProvinceDTO toDTO(Province province) {
        if (province == null) {
            return null;
        }

        ProvinceDTO provinceDTO = new ProvinceDTO();
        provinceDTO.setId(province.getId());
        provinceDTO.setCode(province.getCode());
        provinceDTO.setName(province.getName());

        // Mapear la región asociada si está presente
        if (province.getRegion() != null) {
            Region region = province.getRegion();
            RegionDTO regionDTO = new RegionDTO(region.getId(), region.getCode(), region.getName());
            provinceDTO.setRegion(regionDTO);
        }

        return provinceDTO;
    }

    /**
     * Convierte un objeto DTO {@link ProvinceCreateDTO} en una entidad {@link Province}.
     *
     * @param createDTO El DTO que contiene los datos de la provincia a crear.
     * @param region La entidad {@link Region} asociada a la provincia.
     * @return Una entidad {@link Province} con los datos del DTO y la región asociada,
     *         o {@code null} si el DTO es {@code null}.
     */
    public Province toEntity(ProvinceCreateDTO createDTO, Region region) {
        if (createDTO == null) {
            return null;
        }

        Province province = new Province();
        province.setCode(createDTO.getCode());
        province.setName(createDTO.getName());
        province.setRegion(region);

        return province;
    }
}
