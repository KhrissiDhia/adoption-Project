package com.example.adoptionproject.services;

import com.example.adoptionproject.entities.Adoptant;
import com.example.adoptionproject.entities.Animal;
import com.example.adoptionproject.entities.Adoption;
import com.example.adoptionproject.repositories.AdoptantRepository;
import com.example.adoptionproject.repositories.AnimalRepository;
import com.example.adoptionproject.repositories.AdoptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdoptionServicesImpl implements IAdoptionServices {

    private final AdoptantRepository adoptantRepository;
    private final AnimalRepository animalRepository;
    private final AdoptionRepository adoptionRepository;

    @Override
    public Adoptant addAdoptant(Adoptant adoptant) {
        return adoptantRepository.save(adoptant);
    }

    @Override
    public Animal addAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    @Override
    public Adoption addAdoption(Adoption adoption, int idAdoptant, int idAnimal) {
        var adoptant = adoptantRepository.findById(idAdoptant).orElse(null);
        var animal = animalRepository.findById(idAnimal).orElse(null);
        if (adoptant != null && animal != null) {
            adoption.setAdoptant(adoptant);
            adoption.setAnimal(animal);
            return adoptionRepository.save(adoption);
        }
        return null;
    }

    @Override
    public List<Adoption> getAdoptionsByAdoptant(String nom) {
        return adoptionRepository.findByAdoptantNom(nom);
    }

    @Override
    public float calculFraisTotalAdoptions(int idAdoptant) {
        var adoptions = adoptionRepository.findByAdoptantIdAdoptant(idAdoptant);
        var total = 0f;
        for (var adoption : adoptions) {
            total += adoption.getFrais();
        }
        return total;
    }
}
