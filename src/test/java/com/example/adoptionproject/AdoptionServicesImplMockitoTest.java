package com.example.adoptionproject;

import com.example.adoptionproject.entities.*;
import com.example.adoptionproject.repositories.*;
import com.example.adoptionproject.services.AdoptionServicesImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdoptionServicesImplMockitoTest {

    private AdoptantRepository adoptantRepository;
    private AnimalRepository animalRepository;
    private AdoptionRepository adoptionRepository;

    private AdoptionServicesImpl adoptionServices;

    @BeforeEach
    public void setUp() {
        adoptantRepository = mock(AdoptantRepository.class);
        animalRepository = mock(AnimalRepository.class);
        adoptionRepository = mock(AdoptionRepository.class);
        adoptionServices = new AdoptionServicesImpl(adoptantRepository, animalRepository, adoptionRepository);
    }

    @Test
    public void testAddAdoptant() {
        Adoptant adoptant = new Adoptant();
        adoptant.setNom("Alice");

        when(adoptantRepository.save(adoptant)).thenReturn(adoptant);

        Adoptant result = adoptionServices.addAdoptant(adoptant);
        assertEquals(adoptant, result);
        verify(adoptantRepository).save(adoptant);
    }

    @Test
    public void testAddAnimal() {
        Animal animal = new Animal();
        animal.setNom("Bobby");

        when(animalRepository.save(animal)).thenReturn(animal);

        Animal result = adoptionServices.addAnimal(animal);
        assertEquals(animal, result);
        verify(animalRepository).save(animal);
    }

    @Test
    public void testAddAdoption_Success() {
        Adoptant adoptant = new Adoptant();
        adoptant.setIdAdoptant(1);
        Animal animal = new Animal();
        animal.setIdAnimal(2);
        Adoption adoption = new Adoption();

        when(adoptantRepository.findById(1)).thenReturn(Optional.of(adoptant));
        when(animalRepository.findById(2)).thenReturn(Optional.of(animal));
        when(adoptionRepository.save(adoption)).thenReturn(adoption);

        Adoption result = adoptionServices.addAdoption(adoption, 1, 2);

        assertNotNull(result);
        assertEquals(adoptant, result.getAdoptant());
        assertEquals(animal, result.getAnimal());
        verify(adoptionRepository).save(adoption);
    }

    @Test
    public void testAddAdoption_Failure() {
        when(adoptantRepository.findById(1)).thenReturn(Optional.empty());

        Adoption result = adoptionServices.addAdoption(new Adoption(), 1, 2);

        assertNull(result);
        verify(adoptionRepository, never()).save(any());
    }

    @Test
    public void testCalculFraisTotalAdoptions() {
        Adoption a1 = new Adoption();
        a1.setFrais(100f);
        Adoption a2 = new Adoption();
        a2.setFrais(200f);

        when(adoptionRepository.findByAdoptant_IdAdoptant(1)).thenReturn(Arrays.asList(a1, a2));

        float total = adoptionServices.calculFraisTotalAdoptions(1);
        assertEquals(300f, total, 0.01f);
    }
}
