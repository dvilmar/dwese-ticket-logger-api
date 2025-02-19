package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) que representa una provincia.
 *
 * Esta clase se utiliza para transferir datos de una provincia
 * entre las capas de la aplicación, especialmente para exponerlos
 * a través de la API sin incluir información innecesaria o sensible.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceDTO {

    /**
     * Identificador único de la provincia.
     * <p>
     * Es el mismo ID que se encuentra en la entidad {@link Province}
     * de la base de datos.
     * </p>
     */
    private Long id;

    /**
     * Código de la provincia.
     * <p>
     * - Máximo de 2 caracteres. <br>
     * - Ejemplo: "JA" para Jaén.
     * </p>
     */
    private String code;

    /**
     * Nombre de la provincia.
     * <p>
     * - Ejemplo: "Sevilla", "Jaén".
     * </p>
     */
    private String name;

    /**
     * Información básica de la región asociada.
     * <p>
     * Representa la región a la que pertenece la provincia.
     * Se utiliza un objeto simplificado de tipo {@link RegionDTO}
     * para evitar incluir toda la información de la entidad región.
     * </p>
     */
    private RegionDTO region;
}
