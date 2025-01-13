package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;

import java.util.List;

public class ProvinceCreateDTO {

    @NotEmpty(message = "{msg.region.code.notEmpty")
    @Size(max = 2, message = "{msg.region.code.size}")
    private String code;

    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    private String name;

}
