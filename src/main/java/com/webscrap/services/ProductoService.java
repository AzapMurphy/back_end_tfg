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


    public List<Producto> ejecutarScrapingYGuardar(String texto) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        List<Producto> listaFinal = new ArrayList<>();

        try {
            String query = texto;

            listaFinal.addAll(scrappingService.buscarAlcampo(driver, query));
            System.out.println("1  => " + listaFinal);

            listaFinal.addAll(scrappingService.buscarDia(driver, query));
            System.out.println("2  => " +listaFinal);

        } finally {
            driver.quit();
        }

        return productoRepository.saveAll(listaFinal);
    }
    // ✅ ESTE ES EL QUE TE FALTA
    public List<ProductoDTO> getAll() {
        return productoRepository.findAll().stream().map(p -> {

            SupermercadoDTO supermercadoDTO = new SupermercadoDTO(
                    p.getSupermercado().getId(),
                    p.getSupermercado().getNombre()
            );

            return new ProductoDTO(
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getImagen(),
                    supermercadoDTO
            );

        }).toList();
    }

}
