package com.webscrap.repositories;

import com.webscrap.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar productos por session_id
    List<Producto> findBySessionId(String sessionId);

    // Borrar productos por session_id
    void deleteBySessionId(String sessionId);
}
