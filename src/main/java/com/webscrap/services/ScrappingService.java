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

        driver.get("https://www.compraonline.alcampo.es/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            WebElement cookiesBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))
            );
            cookiesBtn.click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("delivery-book-modal")));

            WebElement buscador = wait.until(ExpectedConditions.elementToBeClickable(By.id("search")));
            buscador.click();
            buscador.sendKeys(query);
            buscador.sendKeys(Keys.ENTER);

            /*JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 0; i < 3; i++) {
                js.executeScript("window.scrollBy(0, 1000);");
                Thread.sleep(1000);
            }*/

            wait.until(driverA -> {
                List<WebElement> elements = driverA.findElements(
                        By.cssSelector("div[class*='product-card']")
                );

                for (WebElement el : elements) {
                    if (!el.getText().isEmpty()) {
                        return true;
                    }
                }
                return false;
            });

            List<WebElement> productosWeb = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector("div[class*='product-card']")
                    )
            );

            for (int i = 0; i < Math.min(5, productosWeb.size()); i++) {
                try {
                    WebElement p = productosWeb.get(i);

                    String nombre = p.findElement(By.className("_text_cn5lb_1")).getText();
                    String precioRaw = p.findElement(By.className("_display_xy0eg_1")).getText();
                    double precioFinal = Double.parseDouble(
                            precioRaw.replaceAll("[^0-9,]", "").replace(",", ".")
                    );
                    String imagen = p.findElement(By.tagName("img")).getAttribute("src");

                    Supermercado supermercado = new Supermercado();
                    supermercado.setId(1L);

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
            System.out.println("Error Alcampo: " + e.getMessage());
        }

        return resultados;
    }

    public List<Producto> buscarDia(WebDriver driver, String query) {
        List<Producto> resultados = new ArrayList<>();

        driver.get("https://www.dia.es/");
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
                    supermercado.setId(3L);

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
}