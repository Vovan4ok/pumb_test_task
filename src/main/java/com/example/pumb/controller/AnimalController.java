package com.example.pumb.controller;

import com.example.pumb.domain.Animal;
import com.example.pumb.service.AnimalService;
import com.example.pumb.service.FileReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name="main_methods")
@Controller
public class AnimalController {
    @Autowired
    AnimalService animalService;

    @Autowired
    FileReaderService fileReaderService;

    @Operation(
            summary = "GET method of api /files/uploads",
            description = "Returns the jsp page with the small form where you can upload the csv or xml file"
    )
    @GetMapping(value={"/files/uploads"})
    public String uploadFile() {
        return "upload";
    }

    @Operation(
            summary = "POST method of api /files/uploads",
            description = "Gets the uploaded file, checks its extension for csv or xml and then saving the data from it to the database, returns the list of animals from the file"
    )
    @PostMapping(value="/files/uploads")
    @ResponseBody
    public List<Animal> uploadFile(@RequestParam("file") MultipartFile dataFile) {
        List<Animal> animals;
        if(dataFile.isEmpty()) {
            animals = new ArrayList<>();
        } else {
            String fileName = dataFile.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(fileExtension.equals("csv")) {
                animals = fileReaderService.getDataFromCSVFile(dataFile);
                animalService.save(animals);
            } else if(fileExtension.equals("xml")) {
                animals = fileReaderService.getDataFromXMLFile(dataFile);
                animalService.save(animals);
            } else {
                animals = new ArrayList<>();
            }
        }
        return animals;
    }

    @Operation(
            summary = "Main GET method of the api",
            description = "May get some unrequired params, such as type of the animal, sex or category and of course param order_by for ordering data in the right way, returns the List of animals"
    )
    @GetMapping(value="/animals", produces = {"application/json"})
    @ResponseBody
    public List<Animal> getAnimals(@RequestParam(required = false) String type, @RequestParam(required = false) String sex, @RequestParam(required = false) Byte category, @RequestParam(required = false) String order_by) {
        List<Animal> animals;

        if(order_by == null) {
            animals = animalService.findAll();
        } else {
            animals = animalService.findAllOrdered(order_by);
        }

        if(type != null) {
            animals = animals.stream().filter(a -> a.getType().equals(type)).collect(Collectors.toList());
        }
        if(sex != null) {
            animals = animals.stream().filter(a -> a.getSex().equals(sex)).collect(Collectors.toList());
        }
        if(category != null) {
            animals = animals.stream().filter(a -> a.getCategory().equals(category)).collect(Collectors.toList());
        }

        return animals;
    }
}
