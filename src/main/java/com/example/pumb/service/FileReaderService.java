package com.example.pumb.service;

import com.example.pumb.dao.CSVReader;
import com.example.pumb.dao.XMLReader;
import com.example.pumb.domain.Animal;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service("fileReaderService")
public class FileReaderService implements CSVReader, XMLReader {
    @Override
    public List<Animal> getDataFromCSVFile(MultipartFile file) {
        List<Animal> animals = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            for (CSVRecord record : csvParser) {
                String name = record.get("Name");
                String type = record.get("Type");
                String sex = record.get("Sex");
                Short weight = Short.parseShort(record.get("Weight"));
                Short cost = Short.parseShort(record.get("Cost"));

                Animal animal = new Animal(name, type, sex, weight, cost);
                animals.add(animal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return animals;
    }

    @Override
    public List<Animal> getDataFromXMLFile(MultipartFile file) {
        List<Animal> animals = new ArrayList<>();

        try {
            InputStream inputStream = file.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            NodeList animalNodes = doc.getElementsByTagName("animal");
            for (int i = 0; i < animalNodes.getLength(); i++) {
                Node animalNode = animalNodes.item(i);
                if (animalNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) animalNode;

                    String name = getTagValue("name", element);
                    String type = getTagValue("type", element);
                    String sex = getTagValue("sex", element);
                    Short weight = getTagValue("weight", element) == null ? null : Short.parseShort(getTagValue("weight", element));
                    Short cost = getTagValue("cost", element) == null ? null : Short.parseShort(getTagValue("cost", element));

                    Animal animal = new Animal(name, type, sex, weight, cost);
                    animals.add(animal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return animals;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            NodeList subList = nodeList.item(0).getChildNodes();
            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }
}
