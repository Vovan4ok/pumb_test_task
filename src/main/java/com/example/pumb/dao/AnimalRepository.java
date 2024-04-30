package com.example.pumb.dao;

import com.example.pumb.domain.Animal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.TreeSet;

@Repository("animalRepository")
public interface AnimalRepository extends JpaRepository<Animal, Integer> {
    @Query("select a from Animal a")
    List<Animal> findAllOrdered(Sort sort);
}
