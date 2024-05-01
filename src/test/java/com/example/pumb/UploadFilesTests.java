package com.example.pumb;

import com.example.pumb.controller.AnimalController;
import com.example.pumb.domain.Animal;
import com.example.pumb.service.AnimalService;
import com.example.pumb.service.FileReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class UploadFilesTests {
    private MockMvc mockMvc;

    @Mock
    private AnimalService animalService;

    @Mock
    private FileReaderService fileReaderService;

    @InjectMocks
    private AnimalController animalController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(animalController).build();
    }

    @Test
    public void testUploadEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", "".getBytes());

        mockMvc.perform(multipart("/files/uploads").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testUploadCSVFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", getCSVContent().getBytes());

        doNothing().when(animalService).save(anyList());
        when(fileReaderService.getDataFromCSVFile(any())).thenReturn(getData());

        mockMvc.perform(multipart("/files/uploads").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].name").value("Milo"));
    }

    @Test
    public void testUploadXMLFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", getXMLContent().getBytes());

        doNothing().when(animalService).save(anyList());
        when(fileReaderService.getDataFromXMLFile(any())).thenReturn(getData());

        mockMvc.perform(multipart("/files/uploads").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].name").value("Milo"));
    }

    @Test
    public void testUploadWrongExtensionFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.txt", "text/txt", "".getBytes());

        mockMvc.perform(multipart("/files/uploads").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    private String getXMLContent() {
        return "<animals><animal><name>Milo</name><type>cat</type><sex>male</sex><weight>40</weight><cost>51</cost></animal></animals>";
    }

    private String getCSVContent() {
        return "name,type,sex,weight,cost\nBuddy,cat,female,41,78\nCooper,,female,46,23\nDuke,cat,male,33,108\nRocky,dog,,18,77\nSadie,cat,male,26,27";
    }

    private List<Animal> getData() {
        return Arrays.asList(
                new Animal(1, "Milo", "cat", "male", (short) 40, (short) 57, (byte) 3),
                new Animal(2, "Simon", "dog", "male", (short) 45, (short) 17, (byte) 1),
                new Animal(3, "Molly", "cat", "female", (short) 38, (short) 59, (byte) 3),
                new Animal(4, "Simba", "dog", "male", (short) 14, (short) 57, (byte) 3),
                new Animal(5, "Toby", "dog", "female", (short) 45, (short) 14, (byte) 1),
                new Animal(6, "Tucker", "cat", "female", (short) 10, (short) 44, (byte) 3),
                new Animal(7, "Zoe", "cat", "male", (short) 30, (short) 49, (byte) 3)
        );
    }
}
