package com.example.pumb.service;

import com.example.pumb.dao.AnimalRepository;
import com.example.pumb.domain.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("animalService")
public class AnimalService {
    @Autowired
    AnimalRepository animalRepository;

    public void save(Animal animal) {
        animalRepository.save(animal);
    }

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }
}