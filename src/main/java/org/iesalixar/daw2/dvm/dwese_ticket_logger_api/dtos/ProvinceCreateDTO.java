package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) utilizado para crear una nueva provincia.
 *
 * Esta clase encapsula los datos necesarios para crear una entidad {@link Province},
 * asegurando que se cumplan las validaciones antes de procesar la solicitud.
 */
@Data
public class ProvinceCreateDTO {

    /**
     * Código de la provincia.
     * <p>
     * - No puede estar vacío. <br>
     * - Longitud máxima de 2 caracteres. <br>
     * - Ejemplo: "JA" para Jaén.
     * </p>
     */
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    private String code;

    /**
     * Nombre de la provincia.
     * <p>
     * - No puede estar vacío. <br>
     * - Longitud máxima de 100 caracteres. <br>
     * - Ejemplo: "Sevilla", "Jaén".
     * </p>
     */
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    private String name;

    /**
     * ID de la región asociada a la provincia.
     * <p>
     * - No puede ser nulo. <br>
     * - Debe corresponder a una región existente en la base de datos.
     * </p>
     */
    @NotNull(message = "{msg.province.region.notNull}")
    private Long regionId;
}
