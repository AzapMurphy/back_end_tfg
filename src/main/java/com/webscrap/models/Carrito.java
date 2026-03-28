package com.webscrap.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchos carritos → 1 usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Muchos carritos → 1 producto
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
}
