package cl.duoc.bff_mesatech.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PruebaController {

    @GetMapping("/hola")
    public String holaBff() {
        return "¡Token validado con éxito! Tienes acceso al BFF.";
    }
}