package com.webscrap.controllers;


import com.webscrap.dto.ProductoDTO;
import com.webscrap.dto.ScrapeRequest;
import com.webscrap.models.Producto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.webscrap.services.ProductoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// localhost:8080/api/usuarios/endpoinr
@RestController
@RequestMapping("api/productos")
// TODO configurar las CORS
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos, Obtener productos SOLO de esta token
    @GetMapping("/getAllProductos")
    public ResponseEntity<?> getAllProductos(HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        try {
            String firebaseUid = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()
                    .toString();
            List<ProductoDTO> lista = productoService.getByFirebaseUid(firebaseUid);
            response.put("code", 1);
            response.put("message", "obtenida la lista de productos");
            response.put("total", lista.size());
            response.put("data", lista);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 2);
            response.put("message", "error en el proceso");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 🔥 NUEVO ENDPOINT: hace TODO (scraping + guardado)
    @PostMapping("/scrape")
    public ResponseEntity<?> scrapeYGuardar(@RequestBody ScrapeRequest request, HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            String firebaseUid = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()
                    .toString();

            List<Producto> productos = productoService.ejecutarScrapingYGuardar(
                    request.getTexto(),
                    firebaseUid  // CAMBIO
            );


            response.put("code", 1);
            response.put("message", "scraping realizado y guardado correctamente");
            response.put("total", productos.size());
            response.put("data", productos);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("code", 2);
            response.put("message", "error en scraping: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Logout - borra SOLO los datos de esta sesion
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            String firebaseUid = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal()
                    .toString();
            productoService.deleteByFirebaseUid(firebaseUid);;
            session.invalidate();

            response.put("code", 1);
            response.put("message", "sesion cerrada y datos eliminados");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 2);
            response.put("message", "error al cerrar sesion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
