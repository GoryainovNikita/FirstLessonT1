package com.example.supplier_service;

import com.example.supplier_service.category.model.Category;
import com.example.supplier_service.category.model.CategoryDTO;
import com.example.supplier_service.category.repository.CategoryRepository;
import com.example.supplier_service.category.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SupplierServiceApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerTest{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    public static void beforeAll(){
        postgres.start();
    }

    @AfterAll
    public static void afterAll(){
        postgres.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    public void beforeEach(){
        categoryRepository.deleteAll();
    }


    @Test
    public void testCreateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("TestName");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        String content = objectWriter.writeValueAsString(categoryDTO);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));

        Category categoryServiceById = categoryService.findByName("TestName");
        Category category = new Category();
        category.setName("TestName");

        assertEquals(categoryServiceById, category);
    }

    @Test
    public void testFindPaginated() throws Exception {
        Category category = new Category();
        category.setName("Test1");
        Category category1 = new Category();
        category1.setName("Test2");

        categoryService.save(category);
        categoryService.save(category1);

        mockMvc.perform(get("/categories")
                .param("numberPage", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetById() throws Exception {
        Category category = new Category();
        category.setName("TestById");
        categoryService.save(category);
        Category test = categoryService.findByName("TestById");

        mockMvc.perform(get("/categories/"+ test.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestById"));

    }

    @Test
    public void testDeleteById() throws Exception {
        Category category = new Category();
        category.setName("TestDelete");

        categoryService.save(category);

        Category test = categoryService.findByName("TestDelete");

        mockMvc.perform(delete("/categories/"+ test.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(categoryService.findByName("TestDelete"), null);
    }

    @Test
    public void testFindByName() throws Exception {

        Category category = new Category();
        category.setName("TestFindByName");

        categoryService.save(category);

        mockMvc.perform(get("/categories").param("name", "TestFindByName"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestFindByName"));
    }

}
