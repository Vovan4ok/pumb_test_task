package com.example.pumb.controller;

import com.example.pumb.service.AnimalService;
import com.example.pumb.service.FileReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
}
