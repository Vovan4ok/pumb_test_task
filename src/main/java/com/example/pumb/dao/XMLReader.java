package com.example.pumb.dao;

import com.example.pumb.domain.Animal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface XMLReader {
    List<Animal> getDataFromXMLFile(MultipartFile file);
}
