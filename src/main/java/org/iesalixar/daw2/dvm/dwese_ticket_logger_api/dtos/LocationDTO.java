package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;

/**
 * Clase DTO (Data Transfer Object) que representa una categoría.
 *
 * Esta clase se utiliza para transferir datos de una categoría
 * entre las capas de la aplicación, especialmente para exponerlos
 * a través de la API sin incluir información innecesaria o sensible.
 */
@Getter
@Setter
public class LocationDTO {
    private Long id;
    private String address;
    private String city;
    private Supermarket supermarket;
    private Province province;
}

