package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.LocationRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Location`.
 * Utiliza `LocationDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/locations")
public class LocationController {


    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);


    // DAO para gestionar las operaciones de las localizaciones en la base de datos
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SupermarketRepository supermarketRepository;
    @Autowired
    private ProvinceRepository provinceRepository;


    /**
     * Lista todas las localizaciones y las pasa como atributo al modelo para que sean
     * accesibles en la vista `location.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de localizaciones.
     */
    @GetMapping
    public String listLocations(Model model) {
        logger.info("Solicitando la lista de todas las localizaciones...");
        List<Location> listLocations = null;
        listLocations = locationRepository.findAll();
        logger.info("Se han cargado {} localizaciones.", listLocations.size());
        model.addAttribute("listLocations", listLocations); // Pasar la lista de localizaciones al modelo
        return "location"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva localización.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) throws SQLException {
        logger.info("Mostrando formulario para nueva localización.");
        model.addAttribute("location", new Location()); // Crear un nuevo objeto Location
        model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
        model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar una localización existente.
     *
     * @param id    ID de la localización a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la localización con ID {}", id);
        Optional<Location> location = locationRepository.findById(id);
        List<Province> provinces = provinceRepository.findAll();
        List<Supermarket> supermarkets = supermarketRepository.findAll();
        model.addAttribute("location", location.get());
        model.addAttribute("provinces", provinces); // Lista de provincias
        model.addAttribute("supermarkets", supermarkets); // Lista de supermercados
        return "location-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva localización en la base de datos.
     *
     * @param location              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de localizaciones.
     */
    @PostMapping("/insert")
    public String insertLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {
        logger.info("Insertando nueva localización con dirección {}", location.getAddress());
        if (result.hasErrors()) {
            model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
            model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsByAddress(location.getAddress())) {
            logger.warn("El dirección de la localización {} ya existe.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.insert.addressExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/new";
        }
        locationRepository.save(location);
        logger.info("Localización {} insertada con éxito.", location.getAddress());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }


    /**
     * Actualiza una localización existente en la base de datos.
     *
     * @param location              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de localizaciones.
     */
    @PostMapping("/update")
    public String updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {
        logger.info("Actualizando localización con ID {}", location.getId());
        if (result.hasErrors()) {
            model.addAttribute("provinces", provinceRepository.findAll()); // Lista de provincias
            model.addAttribute("supermarkets", supermarketRepository.findAll()); // Lista de supermercados
            return "location-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (locationRepository.existsLocationByAddressAndNotId(location.getAddress(), location.getId())) {
            logger.warn("El dirección de la localización {} ya existe para otra localización.", location.getAddress());
            String errorMessage = messageSource.getMessage("msg.location-controller.update.addressExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/locations/edit?id=" + location.getId();
        }
        locationRepository.save(location);
        logger.info("Localización con ID {} actualizada con éxito.", location.getId());
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }


    /**
     * Elimina una localización de la base de datos.
     *
     * @param id                 ID de la localización a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de localizaciones.
     */
    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando localización con ID {}", id);
        locationRepository.deleteById(id);
        logger.info("Localización con ID {} eliminada con éxito.", id);
        return "redirect:/locations"; // Redirigir a la lista de localizaciones
    }
}
