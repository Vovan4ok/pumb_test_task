package com.example.pumb.service;

import com.example.pumb.dao.AnimalRepository;
import com.example.pumb.domain.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Animal> findAllOrdered(String order_by) {
        return animalRepository.findAllOrdered(Sort.by(order_by));
    }

    public static boolean isValid(Animal animal) {
        return !(animal.getName() == null || animal.getName().isEmpty()) && !(animal.getType() == null || animal.getType().isEmpty()) && !(animal.getSex() == null || animal.getSex().isEmpty()) && animal.getWeight() != null && animal.getCost() != null;
    }

    public void save(List<Animal> animals) {
        for(Animal animal : animals) {
            if(isValid(animal)) {
                calculateCategory(animal);
                save(animal);
            }
        }
    }

    public void calculateCategory(Animal animal) {
        if(animal.getCost() <= 20) {
            animal.setCategory((byte) 1);
        } else if(animal.getCost() <= 40) {
            animal.setCategory((byte) 2);
        } else if(animal.getCost() <= 60) {
            animal.setCategory((byte) 3);
        } else {
            animal.setCategory((byte) 4);
        }
    }
}