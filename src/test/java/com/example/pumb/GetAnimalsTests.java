package com.example.pumb;

import com.example.pumb.controller.AnimalController;
import com.example.pumb.domain.Animal;
import com.example.pumb.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class GetAnimalsTests {

	private MockMvc mockMvc;

	@Mock
	private AnimalService animalService;

	@InjectMocks
	private AnimalController animalController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(animalController).build();
	}

	@Test
	public void testGetAnimalsWithNoParams() throws Exception {
		when(animalService.findAll()).thenReturn(getData());

		mockMvc.perform(get("/animals")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(7))
				.andExpect(jsonPath("$[2].name").value("Molly"));
	}

	@Test
	public void getAnimalsWithParamType() throws Exception {
		when(animalService.findAll()).thenReturn(getData());

		mockMvc.perform(get("/animals?type=cat")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(4))
				.andExpect(jsonPath("$[2].name").value("Tucker"));
	}

	@Test
	public void getAnimalsWithParamSex() throws Exception {
		when(animalService.findAll()).thenReturn(getData());

		mockMvc.perform(get("/animals?sex=female")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[2].name").value("Tucker"));
	}

	@Test
	public void getAnimalsWithParamCategory() throws Exception {
		when(animalService.findAll()).thenReturn(getData());

		mockMvc.perform(get("/animals?category=1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[1].name").value("Toby"));
	}

	@Test
	public void getAnimalsSortedByName() throws Exception {
		List<Animal> animals = getData();
		animals.sort(Comparator.comparing(Animal :: getName));

		when(animalService.findAllOrdered("name")).thenReturn(animals);

		mockMvc.perform(get("/animals?order_by=name")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(7))
				.andExpect(jsonPath("$[1].name").value("Molly"));
	}

	@Test
	public void getAnimalsSortedByWeight() throws Exception {
		List<Animal> animals = getData();
		animals.sort(Comparator.comparing(Animal :: getWeight));

		when(animalService.findAllOrdered("weight")).thenReturn(animals);

		mockMvc.perform(get("/animals?order_by=weight")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(7))
				.andExpect(jsonPath("$[0].name").value("Tucker"));
	}

	@Test
	public void getAnimalsSortedByCost() throws Exception {
		List<Animal> animals = getData();
		animals.sort(Comparator.comparing(Animal :: getCost));

		when(animalService.findAllOrdered("cost")).thenReturn(animals);

		mockMvc.perform(get("/animals?order_by=cost")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(7))
				.andExpect(jsonPath("$[0].name").value("Toby"));
	}

	@Test
	public void getAnimalsSortedByCategory() throws Exception {
		List<Animal> animals = getData();
		animals.sort(Comparator.comparing(Animal :: getCategory));

		when(animalService.findAllOrdered("category")).thenReturn(animals);

		mockMvc.perform(get("/animals?order_by=category")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(7))
				.andExpect(jsonPath("$[0].name").value("Simon"));
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
