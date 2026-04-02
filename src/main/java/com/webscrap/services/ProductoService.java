package com.webscrap.services;


import com.webscrap.dto.ProductoDTO;
import com.webscrap.dto.SupermercadoDTO;
import com.webscrap.models.Producto;
import com.webscrap.models.Supermercado;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.webscrap.repositories.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional


public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ScrappingService scrappingService;

    private String textoGuardado;

    public void guardarTexto(String texto) {
        this.textoGuardado = texto;
    }

    public String obtenerTexto() {
        return textoGuardado;
    }


    public List<Producto> ejecutarScrapingYGuardar(String texto, String firebaseUid) {
        List<Producto> listaFinal = new ArrayList<>();
        // ALCAMPO
        WebDriver driverAlcampo = new ChromeDriver(getOptions());
        try {
            listaFinal.addAll(scrappingService.buscarAlcampo(driverAlcampo, texto));
        } finally {
            driverAlcampo.quit();
        }

        // DIA
        WebDriver driverDia = new ChromeDriver(getOptions());
        try {
            listaFinal.addAll(scrappingService.buscarDia(driverDia, texto));
        } finally {
            driverDia.quit();
        }

        // AHORRAMAS
        WebDriver driverAhorramas = new ChromeDriver(getOptions());
        try {
            listaFinal.addAll(scrappingService.buscarAhorramas(driverAhorramas, texto));
        } finally {
            driverAhorramas.quit();
        }

        // Asignar token a cada producto antes de guardar
        for (Producto p : listaFinal) {
            p.setFirebaseUid(firebaseUid);
        }

        return productoRepository.saveAll(listaFinal);
    }
    // Obtener solo productos de este token
    public List<ProductoDTO> getByFirebaseUid(String firebaseUid) {
        return productoRepository.findByFirebaseUid(firebaseUid).stream()
                .map(p -> {
                    SupermercadoDTO supermercadoDTO = new SupermercadoDTO(
                            p.getSupermercado().getId(),
                            p.getSupermercado().getNombre()
                    );
                    return new ProductoDTO(
                            p.getId(),
                            p.getNombre(),
                            p.getPrecio(),
                            p.getImagen(),
                            p.getFirebaseUid(),
                            supermercadoDTO
                    );
                }).toList();
    }


    //Configuración Selenium
    private ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--start-maximized");
        return options;
    }


    public void deleteByFirebaseUid(String firebaseUid) {
        productoRepository.deleteByFirebaseUid(firebaseUid);
    }

}
