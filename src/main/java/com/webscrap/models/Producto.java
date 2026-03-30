package com.webscrap.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Double precio;

    @Column(columnDefinition = "TEXT")
    private String imagen;

    // Relación con supermercado (muchos productos → 1 supermercado)
    @ManyToOne
    @JoinColumn(name = "id_supermercado")
    private Supermercado supermercado;



}
