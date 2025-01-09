package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.*;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Controller
@RequestMapping("/tickets")
public class TicketProductController {


    private static final Logger logger = LoggerFactory.getLogger(TicketProductController.class);


    @Autowired
    private TicketRepository ticketRepository;


    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private LocationRepository locationRepository;


    @Autowired
    private SupermarketRepository supermarketRepository;


    @Autowired
    private ProvinceRepository provinceRepository;


    @Autowired
    private MessageSource messageSource;


    /**
     * Lista todos los tickets disponibles y los muestra en la vista.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf que muestra los tickets.
     */
    @GetMapping
    public String listTickets(Model model) {
        logger.info("Solicitando la lista de todos los tickets...");
        List<Ticket> listTickets = null;
        try {
            listTickets = ticketRepository.findAll();
            logger.info("Se han cargado {} tickets.", listTickets.size());
        } catch (Exception e) {
            logger.error("Error al listar los tickets: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los tickets.");
        }
        model.addAttribute("listTickets", listTickets);
        return "ticket";
    }


    /**
     * Muestra el formulario para crear un nuevo ticket.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario de ticket.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo ticket.");
        List<Product> listProducts = productRepository.findAll();
        List<Location> listLocations = locationRepository.findAll();
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listLocations", listLocations);
        return "ticket-form.html";
    }


    /**
     * Inserta un nuevo ticket en la base de datos.
     *
     * @param ticket             Ticket a insertar.
     * @param result             Resultado de la validación del ticket.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param locale             Localización para mensajes de error.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de tickets si se inserta con éxito, o vuelve al formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insertTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Insertando nuevo ticket con fecha {}", ticket.getDate());
        try {
            if (result.hasErrors()) {
                List<Product> listProducts = productRepository.findAll();
                model.addAttribute("listProducts", listProducts);
                return "ticket-form.html";
            }
            ticketRepository.save(ticket);
            logger.info("Ticket insertado con éxito.");
        } catch (Exception e) {
            logger.error("Error al insertar el ticket: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.ticket-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/tickets";
    }


    /**
     * Muestra el formulario para editar un ticket existente.
     *
     * @param id    ID del ticket a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario de ticket.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el ticket con ID {}", id);
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()) {
            logger.warn("No se encontró el ticket con ID {}", id);
            return "redirect:/tickets";
        }
        List<Product> listProducts = productRepository.findAll();
        List<Location> listLocations = locationRepository.findAll();
        model.addAttribute("ticket", ticket.get());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listLocations", listLocations);
        return "ticket-form.html";
    }


    /**
     * Actualiza un ticket existente en la base de datos.
     *
     * @param ticket             Ticket a actualizar.
     * @param result             Resultado de la validación del ticket.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param locale             Localización para mensajes de error.
     * @param model              Modelo para pasar datos a la vista.
     * @return Redirección a la lista de tickets si se actualiza con éxito, o vuelve al formulario si hay errores.
     */
    @PostMapping("/update")
    public String updateTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale, Model model) {
        logger.info("Actualizando ticket con ID {}", ticket.getId());
        try {
            if (result.hasErrors()) {
                List<Product> listProducts = productRepository.findAll();
                model.addAttribute("listProducts", listProducts);
                return "ticket-form.html";
            }
            ticketRepository.save(ticket);
            logger.info("Ticket con ID {} actualizado con éxito.", ticket.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar el ticket con ID {}: {}", ticket.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.ticket-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/tickets";
    }


    /**
     * Elimina un ticket de la base de datos.
     *
     * @param id                 ID del ticket a eliminar.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la lista de tickets.
     */
    @PostMapping("/delete")
    public String deleteTicket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando ticket con ID {}", id);
        try {
            ticketRepository.deleteById(id);
            logger.info("Ticket con ID {} eliminado con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el ticket con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el ticket.");
        }
        return "redirect:/tickets";
    }


    /**
     * Muestra los detalles de un ticket.
     *
     * @param id    ID del ticket a mostrar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para los detalles del ticket.
     */
    @GetMapping("/detail")
    public String showTicketDetail(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando detalles para el ticket con ID {}", id);
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);


        if (ticketOptional.isEmpty()) {
            logger.warn("No se encontró el ticket con ID {}", id);
            return "redirect:/tickets";
        }


        Ticket ticket = ticketOptional.get();
        model.addAttribute("ticket", ticket);
        model.addAttribute("products", ticket.getProducts());


        return "ticket-detail.html";
    }




    /**
     * Busca productos que coincidan con el término ingresado y los muestra para añadir al ticket.
     *
     * @param productSearch Nombre o parte del nombre del producto.
     * @param ticketId      ID del ticket.
     * @param model         Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para mostrar los resultados de la búsqueda.
     */
    @PostMapping("/addExistingProduct")
    public String searchProduct(@RequestParam("productSearch") String productSearch, @RequestParam("ticketId") Long ticketId, Model model) {
        logger.info("Buscando productos que coincidan con '{}'", productSearch);
        List<Product> searchResults = productRepository.findByNameContainingIgnoreCase(productSearch);
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);


        if (ticketOpt.isPresent()) {
            model.addAttribute("ticket", ticketOpt.get());
            model.addAttribute("products", ticketOpt.get().getProducts());
        } else {
            model.addAttribute("errorMessage", "No se encontró el ticket.");
            return "redirect:/tickets";
        }


        model.addAttribute("searchResults", searchResults);
        return "ticket-detail";
    }


    /**
     * Añade un producto existente al ticket.
     *
     * @param ticketId         ID del ticket.
     * @param productId        ID del producto.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del ticket.
     */
    @PostMapping("/addProduct")
    public String addProductToTicket(@RequestParam("ticketId") Long ticketId, @RequestParam("productId") Long productId, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Añadiendo producto con ID {} al ticket con ID {}", productId, ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            Optional<Product> productOpt = productRepository.findById(productId);


            if (ticketOpt.isPresent() && productOpt.isPresent()) {
                Ticket ticket = ticketOpt.get();
                Product product = productOpt.get();
                ticket.getProducts().add(product);
                ticketRepository.save(ticket);
                logger.info("Producto añadido con éxito.");
            } else {
                logger.warn("No se encontró el ticket o el producto.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir el producto al ticket.");
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Violación de integridad de datos al insertar el ticket: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.ticket-controller.insert.integrity-violation", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/tickets/detail?id=" + ticketId;
        } catch (Exception e) {
            logger.error("Error al añadir el producto al ticket: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir el producto.");
        }
        return "redirect:/tickets/detail?id=" + ticketId;
    }




    /**
     * Añade un nuevo producto al ticket.
     *
     * @param ticketId          ID del ticket.
     * @param productName       Nombre del producto.
     * @param productPrice      Precio del producto.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del ticket.
     */
    @PostMapping("/addNewProduct")
    public String addNewProductToTicket(@RequestParam("ticketId") Long ticketId, @RequestParam("productName") String productName,
                                        @RequestParam("productPrice") BigDecimal productPrice, RedirectAttributes redirectAttributes) {
        logger.info("Añadiendo nuevo producto '{}' con precio {} al ticket con ID {}", productName, productPrice, ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);


            if (ticketOpt.isPresent()) {
                Ticket ticket = ticketOpt.get();


                // Verificar si ya existe un producto con el mismo nombre en el ticket
                boolean productExists = ticket.getProducts().stream()
                        .anyMatch(product -> product.getName().equalsIgnoreCase(productName));


                if (productExists) {
                    logger.warn("El producto con nombre '{}' ya existe en el ticket con ID {}", productName, ticketId);
                    redirectAttributes.addFlashAttribute("errorMessage", "El producto con el nombre especificado ya está asociado al ticket.");
                } else {
                    Product newProduct = new Product();
                    newProduct.setName(productName);
                    newProduct.setPrice(productPrice);
                    productRepository.save(newProduct);


                    ticket.getProducts().add(newProduct);
                    ticketRepository.save(ticket);


                    logger.info("Nuevo producto añadido con éxito.");
                }
            } else {
                logger.warn("No se encontró el ticket.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo añadir el producto al ticket.");
            }
        } catch (Exception e) {
            logger.error("Error al añadir el nuevo producto al ticket: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al añadir el nuevo producto.");
        }
        return "redirect:/tickets/detail?id=" + ticketId;
    }




    /**
     * Elimina un producto asociado al ticket.
     *
     * @param ticketId         ID del ticket.
     * @param productId        ID del producto.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la página de detalles del ticket.
     */
    @PostMapping("/removeProduct")
    public String removeProductFromTicket(@RequestParam("ticketId") Long ticketId, @RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando producto con ID {} del ticket con ID {}", productId, ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            Optional<Product> productOpt = productRepository.findById(productId);


            if (ticketOpt.isPresent() && productOpt.isPresent()) {
                Ticket ticket = ticketOpt.get();
                Product product = productOpt.get();
                ticket.getProducts().remove(product);
                ticketRepository.save(ticket);
                logger.info("Producto eliminado con éxito.");
            } else {
                logger.warn("No se encontró el ticket o el producto.");
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el producto del ticket.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el producto del ticket: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el producto.");
        }
        return "redirect:/tickets/detail?id=" + ticketId;
    }


}
