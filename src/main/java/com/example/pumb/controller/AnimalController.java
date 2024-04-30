package com.example.pumb.controller;

import com.example.pumb.domain.Animal;
import com.example.pumb.service.AnimalService;
import com.example.pumb.service.FileReaderService;
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

@Controller
public class AnimalController {
    @Autowired
    AnimalService animalService;

    @Autowired
    FileReaderService  fileReaderService;

    @GetMapping(value={"/files/uploads"})
    public String uploadFile(HttpServletRequest request) {
        return "upload";
    }

    @PostMapping(value="/files/uploads")
    public String uploadFile(@RequestParam("file") MultipartFile dataFile, HttpServletRequest request) {
        if(dataFile.isEmpty()) {
            request.setAttribute("message", "File was empty");
        } else {
            String fileName = dataFile.getOriginalFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if(fileExtension.equals("csv")) {
                animalService.save(fileReaderService.getDataFromCSVFile(dataFile));
                request.setAttribute("message", "Data was successfully read from the file and saved to the database. Please make the GET request by the url /animals to see it.");
            } else if(fileExtension.equals("xml")) {
                animalService.save(fileReaderService.getDataFromXMLFile(dataFile));
                request.setAttribute("message", "Data was successfully read from the file and saved to the database. Please make the GET request by the url /animals to see it.");
            } else {
                request.setAttribute("message", "Unknown file extension!");
            }
        }
        return "message";
    }

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
