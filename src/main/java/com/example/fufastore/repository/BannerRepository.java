package com.example.fufastore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.fufastore.model.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {

        @Query(value = """
                        SELECT * From banner
                        ORDER BY created_at DESC
                        LIMIT 1
                        """, nativeQuery = true)
        Banner findBanner();

        @Query(value = """
                        SELECT * From banner
                        WHERE status = TRUE
                        ORDER BY id
                        """, nativeQuery = true)
        List<Banner> findIsActive();

}
