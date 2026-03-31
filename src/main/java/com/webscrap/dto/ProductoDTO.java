package com.webscrap.dto;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private String imagen;
    private String sessionId;
    private SupermercadoDTO supermercado;

    public ProductoDTO() {}

    public ProductoDTO(Long id, String nombre, Double precio, String imagen, String sessionId, SupermercadoDTO supermercado) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.sessionId = sessionId;
        this.supermercado = supermercado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public SupermercadoDTO getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(SupermercadoDTO supermercado) {
        this.supermercado = supermercado;
    }
}
