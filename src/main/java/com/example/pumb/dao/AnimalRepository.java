package com.example.pumb.dao;

import com.example.pumb.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("animalRepository")
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
}
