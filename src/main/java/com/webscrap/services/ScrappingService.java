package com.webscrap.services;

import com.webscrap.models.Producto;
import com.webscrap.models.Supermercado;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



@Service
public class ScrappingService {

    public List<Producto> buscarAlcampo(WebDriver driver, String query) {

        List<Producto> resultados = new ArrayList<>();

        driver.manage().deleteAllCookies();
        driver.get("https://www.compraonline.alcampo.es/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            // Aceptar cookies
            WebElement cookiesBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            cookiesBtn.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.className("delivery-book-modal")
            ));

            // Buscar producto
            WebElement buscador = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("search"))
            );
            buscador.click();
            buscador.sendKeys(query);
            buscador.sendKeys(Keys.ENTER);

            // Esperar primer render
            wait.until(d -> d.findElements(By.cssSelector("div[class*='product-card']")).size() > 0);

            // SCROLL HASTA CONSEGUIR AL MENOS 5 PRODUCTOS
            List<WebElement> productosWeb = new ArrayList<>();
            int intentos = 0;

            while (productosWeb.size() < 5 && intentos < 10) {

                js.executeScript("window.scrollBy(0, 2500);");

                Thread.sleep(2000); // pequeño delay para lazy loading

                productosWeb = driver.findElements(By.cssSelector("div[class*='product-card']"));

                intentos++;
            }

            // procesar SOLO los válidos
            for (WebElement p : productosWeb) {

                try {
                    String nombre = "";
                    String precioRaw = "";
                    String imagen = "";

                    try {
                        nombre = p.findElement(By.cssSelector("[data-test='fop-title']")).getText();
                    } catch (Exception ignored) {}

                    try {
                        precioRaw = p.findElement(By.cssSelector("[data-test='fop-price']")).getText();
                    } catch (Exception ignored) {}

                    try {
                        imagen = p.findElement(By.tagName("img")).getAttribute("src");
                    } catch (Exception ignored) {}

                    // saltar productos incompletos
                    if (nombre.isBlank() || precioRaw.isBlank()) {
                        continue;
                    }

                    double precioFinal = Double.parseDouble(
                            precioRaw.replaceAll("[^0-9,]", "").replace(",", ".")
                    );

                    Supermercado supermercado = new Supermercado();
                    supermercado.setId(1L);

                    Producto producto = new Producto(
                            null, nombre, precioFinal, imagen, supermercado
                    );

                    resultados.add(producto);

                    if (resultados.size() == 5) break;

                } catch (Exception e) {
                    System.out.println("Error procesando producto: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error general Alcampo: " + e.getMessage());
        }

        return resultados;
    }
    public List<Producto> buscarDia(WebDriver driver, String query) {
        List<Producto> resultados = new ArrayList<>();
        // LIMPIAR estado del navegador
        driver.manage().deleteAllCookies();
        driver.get("https://www.dia.es/");
        // Pequeña pausa (anti-bot / carga inicial)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            WebElement cookiesBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            cookiesBtn.click();

            WebElement buscador = wait.until(
                    ExpectedConditions.elementToBeClickable(By.className("dia-search__bar"))
            );

            buscador.click();
            buscador.sendKeys(query);
            buscador.sendKeys(Keys.ENTER);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 0; i < 3; i++) {
                js.executeScript("window.scrollBy(0, 1000);");
                Thread.sleep(1000);
            }

            List<WebElement> productosWeb = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.className("search-product-card-list__item-container")
                    )
            );

            for (int i = 0; i < Math.min(5, productosWeb.size()); i++) {
                try {
                    WebElement p = productosWeb.get(i);

                    String nombre = p.findElement(
                            By.cssSelector(".search-product-card__product-name")
                    ).getText();

                    String precioRaw = p.findElement(
                            By.className("search-product-card__active-price")
                    ).getText();

                    double precioFinal = Double.parseDouble(
                            precioRaw.replaceAll("[^0-9,]", "").replace(",", ".")
                    );

                    String imagen = p.findElement(By.tagName("img")).getAttribute("src");

                    Supermercado supermercado = new Supermercado();
                    supermercado.setId(2L);

                    Producto producto = new Producto(
                            null,
                            nombre,
                            precioFinal,
                            imagen,
                            supermercado
                    );

                    resultados.add(producto);

                } catch (Exception ignored) {}
            }

        } catch (Exception e) {
            System.out.println("Error Día: " + e.getMessage());
        }

        return resultados;
    }

    public List<Producto> buscarAhorramas(WebDriver driver, String query) {
        List<Producto> resultados = new ArrayList<>();

        driver.manage().deleteAllCookies();
        driver.get("https://www.ahorramas.com/");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Cookies
            WebElement cookiesBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            cookiesBtn.click();

            // Buscador
            WebElement buscador = wait.until(
                    ExpectedConditions.elementToBeClickable(By.className("search-field"))
            );
            buscador.click();
            buscador.sendKeys(query);
            buscador.sendKeys(Keys.ENTER);

            // Esperar a productos
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".product-container")
            ));

            List<WebElement> productosWeb = driver.findElements(
                    By.cssSelector(".product-container")
            );

            for (int i = 0; i < Math.min(5, productosWeb.size()); i++) {
                try {
                    WebElement p = productosWeb.get(i);

                    // Nombre
                    String nombre = p.findElement(
                            By.cssSelector("h2.product-name-gtm")
                    ).getText();

                    // Precio
                    String precioRaw = p.findElement(
                            By.cssSelector(".sales .value")
                    ).getText();

                    double precioFinal = Double.parseDouble(
                            precioRaw.replaceAll("[^0-9,]", "").replace(",", ".")
                    );

                    String imagen = p.findElement(By.cssSelector("img.tile-image"))
                            .getAttribute("src");

                    // quitar parámetros
                    if (imagen.contains("?")) {
                        imagen = imagen.substring(0, imagen.indexOf("?"));
                    }

                    Supermercado supermercado = new Supermercado();
                    supermercado.setId(4L);

                    Producto producto = new Producto(
                            null, nombre, precioFinal, imagen, supermercado
                    );

                    resultados.add(producto);

                } catch (Exception e) {
                    System.out.println("Error producto: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Error Ahorramas: " + e.getMessage());
        }

        return resultados;
    }
}