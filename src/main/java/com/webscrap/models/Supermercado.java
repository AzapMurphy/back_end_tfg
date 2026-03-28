package com.webscrap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "supermercados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String url;

    // 1 supermercado -> muchos productos
    @OneToMany(mappedBy = "supermercado")
    @JsonIgnore
    private List<Producto> productos;
}