package com.example.adoptionproject;
import com.example.adoptionproject.entities.*;
import com.example.adoptionproject.repositories.*;
import com.example.adoptionproject.services.AdoptionServicesImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionServicesImplTest {

    @Mock
    private AdoptantRepository adoptantRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AdoptionRepository adoptionRepository;

    @InjectMocks
    private AdoptionServicesImpl adoptionServices;

    @Test
    void testAddAdoptant() {
        Adoptant adoptant = new Adoptant();
        adoptant.setNom("John");

        when(adoptantRepository.save(adoptant)).thenReturn(adoptant);

        Adoptant result = adoptionServices.addAdoptant(adoptant);

        assertEquals(adoptant, result);
        verify(adoptantRepository, times(1)).save(adoptant);
    }

    @Test
    void testAddAnimal() {
        Animal animal = new Animal();
        animal.setNom("Rex");

        when(animalRepository.save(animal)).thenReturn(animal);

        Animal result = adoptionServices.addAnimal(animal);

        assertEquals(animal, result);
        verify(animalRepository, times(1)).save(animal);
    }

    @Test
    void testAddAdoption_success() {
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
        verify(adoptionRepository, times(1)).save(adoption);
    }

    @Test
    void testAddAdoption_notFound() {
        when(adoptantRepository.findById(1)).thenReturn(Optional.empty());

        Adoption result = adoptionServices.addAdoption(new Adoption(), 1, 2);
        assertNull(result);
    }

    @Test
    void testGetAdoptionsByAdoptant() {
        List<Adoption> adoptions = Collections.singletonList(new Adoption());
        when(adoptionRepository.findByAdoptantNom("John")).thenReturn(adoptions);

        List<Adoption> result = adoptionServices.getAdoptionsByAdoptant("John");
        assertEquals(1, result.size());
        verify(adoptionRepository, times(1)).findByAdoptantNom("John");
    }

    @Test
    void testCalculFraisTotalAdoptions() {
        Adoption a1 = new Adoption();
        a1.setFrais(100.0f);
        Adoption a2 = new Adoption();
        a2.setFrais(150.0f);

        when(adoptionRepository.findByAdoptantIdAdoptant(1)).thenReturn(Arrays.asList(a1, a2));

        float total = adoptionServices.calculFraisTotalAdoptions(1);
        assertEquals(250.0f, total);
    }
}
