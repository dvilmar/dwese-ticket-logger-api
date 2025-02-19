package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.*;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Product;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Ticket;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.*;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente encargado de mapear entidades de tipo `Ticket` a DTOs
 * y viceversa, para facilitar la transferencia de datos entre las
 * capas de la aplicación.
 */
@Component
public class TicketMapper {

    /**
     * Convierte una entidad `Ticket` en un `TicketDTO`.
     *
     * @param ticket La entidad `Ticket` que se desea mapear.
     * @return Un objeto `TicketDTO` con los datos mapeados.
     */
    public TicketDTO toDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();

        // Asignar valores básicos del ticket
        dto.setId(ticket.getId());
        dto.setDate(ticket.getDate());
        dto.setDiscount(ticket.getDiscount());
        dto.setTotal(ticket.getTotal());

        // Mapear la ubicación asociada
        dto.setLocation(new LocationDTO(
                ticket.getLocation().getId(),
                ticket.getLocation().getAddress(),
                ticket.getLocation().getCity(),
                new SupermarketDTO(
                        ticket.getLocation().getSupermarket().getId(),
                        ticket.getLocation().getSupermarket().getName()
                ),
                new ProvinceDTO(
                        ticket.getLocation().getProvince().getId(),
                        ticket.getLocation().getProvince().getCode(),
                        ticket.getLocation().getProvince().getName(),
                        new RegionDTO(
                                ticket.getLocation().getProvince().getRegion().getId(),
                                ticket.getLocation().getProvince().getRegion().getCode(),
                                ticket.getLocation().getProvince().getRegion().getName()
                        )
                )
        ));

        // Mapear la lista de productos asociados
        dto.setProducts(ticket.getProducts().stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Convierte un objeto `TicketCreateDTO` en una entidad `Ticket`.
     *
     * @param createDTO El DTO con los datos necesarios para crear el ticket.
     * @param location La ubicación asociada al ticket.
     * @param products La lista de productos asociados al ticket.
     * @return Una entidad `Ticket` lista para ser persistida.
     */
    public Ticket toEntity(TicketCreateDTO createDTO, Location location, List<Product> products) {
        Ticket ticket = new Ticket();

        // Asignar valores básicos al ticket
        ticket.setDate(createDTO.getDate());
        ticket.setDiscount(createDTO.getDiscount());

        // Asignar la ubicación asociada
        ticket.setLocation(location);

        // Asignar la lista de productos asociados
        ticket.setProducts(products);

        return ticket;
    }
}
