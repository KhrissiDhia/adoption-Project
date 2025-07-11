package com.example.adoptionproject.repositories;

import com.example.adoptionproject.entities.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {
    List<Adoption> findByAdoptantNom(String nom);
    List<Adoption> findByAdoptantIdAdoptant(int idAdoptant);
}
