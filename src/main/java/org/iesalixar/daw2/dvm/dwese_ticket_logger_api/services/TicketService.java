package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.TicketCreateDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos.TicketDTO;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Ticket;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Product;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.mappers.TicketMapper;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.TicketRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Obtiene todos los tickets y los convierte en una lista de TicketDTO.
     *
     * @return Lista de TicketDTO.
     */
    public List<TicketDTO> getAllTickets() {
        logger.info("Solicitando todos los tickets...");
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            logger.info("Se han encontrado {} tickets.", tickets.size());
            return tickets.stream().map(ticketMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener la lista de tickets: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un ticket por su ID y lo convierte en un TicketDTO.
     *
     * @param id Identificador único del ticket.
     * @return TicketDTO del ticket encontrado.
     * @throws IllegalArgumentException Si el ticket no existe.
     */
    public TicketDTO getTicketById(Long id) {
        logger.info("Buscando ticket con ID {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró el ticket con ID {}", id);
                    return new IllegalArgumentException("El ticket no existe.");
                });
        logger.info("Ticket con ID {} encontrado.", id);
        return ticketMapper.toDTO(ticket);
    }

    /**
     * Crea un nuevo ticket en la base de datos.
     *
     * @param ticketCreateDTO DTO que contiene los datos del ticket a crear.
     * @param locale Idioma para los mensajes de error.
     * @return DTO del ticket creado.
     * @throws IllegalArgumentException Si la ubicación o los productos no existen.
     */
    public TicketDTO createTicket(TicketCreateDTO ticketCreateDTO, Locale locale) {
        logger.info("Creando un nuevo ticket...");

        Location location = locationRepository.findById(ticketCreateDTO.getLocationId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.ticket-service.create.locationNotFound", null, locale);
                    logger.warn("Error al crear ticket: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        List<Product> products = productRepository.findAllById(ticketCreateDTO.getProductIds());
        if (products.isEmpty()) {
            String errorMessage = messageSource.getMessage("msg.ticket-service.create.productsNotFound", null, locale);
            logger.warn("Error al crear ticket: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Ticket ticket = ticketMapper.toEntity(ticketCreateDTO, location, products);
        Ticket savedTicket = ticketRepository.save(ticket);
        logger.info("Ticket creado exitosamente con ID {}", savedTicket.getId());
        return ticketMapper.toDTO(savedTicket);
    }

    /**
     * Actualiza un ticket existente en la base de datos.
     *
     * @param id ID del ticket a actualizar.
     * @param ticketCreateDTO DTO que contiene los nuevos datos del ticket.
     * @param locale Idioma para los mensajes de error.
     * @return DTO del ticket actualizado.
     * @throws IllegalArgumentException Si el ticket, la ubicación o los productos no existen.
     */
    public TicketDTO updateTicket(Long id, TicketCreateDTO ticketCreateDTO, Locale locale) {
        logger.info("Actualizando ticket con ID {}", id);

        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró el ticket con ID {}", id);
                    return new IllegalArgumentException("El ticket no existe.");
                });

        Location location = locationRepository.findById(ticketCreateDTO.getLocationId())
                .orElseThrow(() -> {
                    String errorMessage = messageSource.getMessage("msg.ticket-service.update.locationNotFound", null, locale);
                    logger.warn("Error al actualizar ticket: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        List<Product> products = productRepository.findAllById(ticketCreateDTO.getProductIds());
        if (products.isEmpty()) {
            String errorMessage = messageSource.getMessage("msg.ticket-service.update.productsNotFound", null, locale);
            logger.warn("Error al actualizar ticket: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        existingTicket.setDate(ticketCreateDTO.getDate());
        existingTicket.setDiscount(ticketCreateDTO.getDiscount());
        existingTicket.setLocation(location);
        existingTicket.setProducts(products);

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        logger.info("Ticket con ID {} actualizado exitosamente.", id);
        return ticketMapper.toDTO(updatedTicket);
    }

    /**
     * Elimina un ticket por su ID.
     *
     * @param id Identificador único del ticket.
     * @throws IllegalArgumentException Si el ticket no existe.
     */
    public void deleteTicket(Long id) {
        logger.info("Buscando ticket con ID {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontró el ticket con ID {}", id);
                    return new IllegalArgumentException("El ticket no existe.");
                });

        ticketRepository.deleteById(id);
        logger.info("Ticket con ID {} eliminado exitosamente.", id);
    }

    /**
     * Añade un producto a un ticket.
     *
     * @param ticketId ID del ticket al que se añadirá el producto.
     * @param productId ID del producto a añadir.
     * @return TicketDTO actualizado con el nuevo producto añadido.
     * @throws IllegalArgumentException Si el ticket o el producto no existen.
     */
    public TicketDTO addProductToTicket(Long ticketId, Long productId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("El ticket no existe."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));

        // Añadir el producto al ticket si no está ya presente
        if (!ticket.getProducts().contains(product)) {
            ticket.getProducts().add(product);
        } else {
            throw new IllegalArgumentException("El producto ya está asociado al ticket.");
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(updatedTicket);
    }

    /**
     * Elimina un producto de un ticket.
     *
     * @param ticketId ID del ticket del que se eliminará el producto.
     * @param productId ID del producto a eliminar.
     * @return TicketDTO actualizado sin el producto eliminado.
     * @throws IllegalArgumentException Si el ticket o el producto no existen, o si el producto no está asociado al ticket.
     */
    public TicketDTO removeProductFromTicket(Long ticketId, Long productId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("El ticket no existe."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));

        // Eliminar el producto del ticket si está presente
        if (ticket.getProducts().contains(product)) {
            ticket.getProducts().remove(product);
        } else {
            throw new IllegalArgumentException("El producto no está asociado al ticket.");
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDTO(updatedTicket);
    }
}
