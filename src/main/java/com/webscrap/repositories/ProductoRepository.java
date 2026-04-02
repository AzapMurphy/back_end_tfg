package com.webscrap.repositories;

import com.webscrap.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByFirebaseUid(String firebaseUid);


    void deleteByFirebaseUid(String firebaseUid);
}
