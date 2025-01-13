package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories.SupermarketRepository;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Locale;
import java.util.Optional;


/**
 * Controlador que maneja las operaciones CRUD para la entidad `Supermarket`.
 * Utiliza `SupermarketDAO` para interactuar con la base de datos.
 */
@Controller
@RequestMapping("/api/supermarkets")
public class SupermarketController {


    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);


    // DAO para gestionar las operaciones de las supermercados en la base de datos
    @Autowired
    private SupermarketRepository supermarketRepository;
    @Autowired
    private MessageSource messageSource;


    /**
     * Lista todas las supermercados y las pasa como atributo al modelo para que sean
     * accesibles en la vista `supermarket.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de supermercados.
     */
    @GetMapping
    public String listSupermarkets(Model model) {
        logger.info("Solicitando la lista de todas las supermercados...");
        List<Supermarket> listSupermarkets = null;
        listSupermarkets = supermarketRepository.findAll();
        logger.info("Se han cargado {} supermercados.", listSupermarkets.size());
        model.addAttribute("listSupermarkets", listSupermarkets); // Pasar la lista de supermercados al modelo
        return "supermarket"; // Nombre de la plantilla Thymeleaf a renderizar
    }


    /**
     * Muestra el formulario para crear una nueva supermercado.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva supermercado.");
        model.addAttribute("supermarket", new Supermarket()); // Crear un nuevo objeto Supermarket
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Muestra el formulario para editar una supermercado existente.
     *
     * @param id    ID de la supermercado a editar.
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la supermercado con ID {}", id);
        Optional<Supermarket> supermarket = supermarketRepository.findById(id);
        model.addAttribute("supermarket", supermarket.get());
        return "supermarket-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }


    /**
     * Inserta una nueva supermercado en la base de datos.
     *
     * @param supermarket              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/insert")
    public String insertSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva supermercado con nombre {}", supermarket.getName());
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketRepository.existsByName(supermarket.getName())) {
            logger.warn("El nombre de la supermercado {} ya existe.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.insert.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/new";
        }
        supermarketRepository.save(supermarket);
        logger.info("Supermercado {} insertada con éxito.", supermarket.getName());
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }


    /**
     * Actualiza una supermercado existente en la base de datos.
     *
     * @param supermarket              Objeto que contiene los datos del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/update")
    public String updateSupermarket(@Valid @ModelAttribute("supermarket") Supermarket supermarket, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando supermercado con ID {}", supermarket.getId());
        if (result.hasErrors()) {
            return "supermarket-form";  // Devuelve el formulario para mostrar los errores de validación
        }
        if (supermarketRepository.existsSupermarketByNameAndNotId(supermarket.getName(), supermarket.getId())) {
            logger.warn("El nombre de la supermercado {} ya existe para otra supermercado.", supermarket.getName());
            String errorMessage = messageSource.getMessage("msg.supermarket-controller.update.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/supermarkets/edit?id=" + supermarket.getId();
        }
        supermarketRepository.save(supermarket);
        logger.info("Supermercado con ID {} actualizada con éxito.", supermarket.getId());
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }


    /**
     * Elimina una supermercado de la base de datos.
     *
     * @param id                 ID de la supermercado a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de supermercados.
     */
    @PostMapping("/delete")
    public String deleteSupermarket(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando supermercado con ID {}", id);
        supermarketRepository.deleteById(id);
        logger.info("Supermercado con ID {} eliminada con éxito.", id);
        return "redirect:/supermarkets"; // Redirigir a la lista de supermercados
    }
}
