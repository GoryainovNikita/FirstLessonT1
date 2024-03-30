package com.example.supplier_service;


import com.example.supplier_service.product.model.Product;
import com.example.supplier_service.product.model.ProductDTO;
import com.example.supplier_service.product.repository.ProductRepository;
import com.example.supplier_service.product.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SupplierServiceApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

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
        productRepository.deleteAll();
    }


    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("CreateTestName");
        productDTO.setDescription("CreateTestDescription");
        productDTO.setCategoryName("CreateTestCategoryName");
        productDTO.setPrice(100);
        productDTO.setCategoryId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        String content = objectWriter.writeValueAsString(productDTO);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()));

        Product product = productService.findByName("CreateTestName");
        assertEquals(product.getName(), "CreateTestName");
        assertEquals(product.getDescription(), "CreateTestDescription");
        assertEquals(product.getPrice(), 100);
    }

    @Test
    public void testFindPaginated() throws Exception {
        Product product = new Product();
        product.setName("Test1");
        product.setDescription("FindPaginatedTestDescription1");
        product.setPrice(100);
        Product product1 = new Product();
        product1.setName("Test2");
        product1.setDescription("FindPaginatedTestDescription2");
        product1.setPrice(200);

        productService.save(product);
        productService.save(product1);

        mockMvc.perform(get("/products")
                        .param("numberPage", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

    }

    @Test
    public void testGetById() throws Exception {
        Product product = new Product();
        product.setName("TestById");
        product.setDescription("GetByIdTestDescription1");
        product.setPrice(100);
        productService.save(product);
        Product test = productService.findByName("TestById");

        mockMvc.perform(get("/products/"+ test.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestById"));

    }

    @Test
    public void testDeleteById() throws Exception {
        Product product = new Product();
        product.setName("TestDelete");
        product.setDescription("DeleteTestDescription1");
        product.setPrice(100);

        productService.save(product);

        Product test = productService.findByName("TestDelete");

        mockMvc.perform(delete("/products/"+ test.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(productService.findByName("TestDelete"), null);
    }

    @Test
    public void testFindByName() throws Exception {

        Product product = new Product();
        product.setName("TestFindByName");
        product.setDescription("FindByNameTestDescription");
        product.setPrice(100);

        productService.save(product);

        mockMvc.perform(get("/products").param("name", "TestFindByName"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestFindByName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("FindByNameTestDescription"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("100"));
    }

    @Test
    public void testFindByPrice() throws Exception {

        Product product = new Product();
        product.setName("TestFindByPrice");
        product.setDescription("FindByPriceTestDescription");
        product.setPrice(100);

        Product product1 = new Product();
        product1.setName("TestFindByPrice1");
        product1.setDescription("FindByPriceTestDescription1");
        product1.setPrice(100);

        productService.save(product);
        productService.save(product1);

        mockMvc.perform(get("/products").param("price", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindByPriceMoreThen() throws Exception {

        Product product = new Product();
        product.setName("TestFindByPrice");
        product.setDescription("FindByPriceTestDescription");
        product.setPrice(120);

        Product product1 = new Product();
        product1.setName("TestFindByPrice1");
        product1.setDescription("FindByPriceTestDescription1");
        product1.setPrice(80);

        Product product2 = new Product();
        product2.setName("TestFindByPrice2");
        product2.setDescription("FindByPriceTestDescription2");
        product2.setPrice(130);

        productService.save(product);
        productService.save(product1);
        productService.save(product2);

        mockMvc.perform(get("/products").param("price_more", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
    @Test
    public void testFindByPriceLessThen() throws Exception {

        Product product = new Product();
        product.setName("TestFindByPrice");
        product.setDescription("FindByPriceTestDescription");
        product.setPrice(70);

        Product product1 = new Product();
        product1.setName("TestFindByPrice1");
        product1.setDescription("FindByPriceTestDescription1");
        product1.setPrice(80);

        Product product2 = new Product();
        product2.setName("TestFindByPrice2");
        product2.setDescription("FindByPriceTestDescription2");
        product2.setPrice(130);

        productService.save(product);
        productService.save(product1);
        productService.save(product2);

        mockMvc.perform(get("/products").param("price_less", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    public void testFindByDesc() throws Exception {

        Product product = new Product();
        product.setName("TestFindByDesc");
        product.setDescription("FindByDescTestDescription");
        product.setPrice(100);

        productService.save(product);

        mockMvc.perform(get("/products").param("description", "FindByDescTestDescription"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestFindByDesc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("FindByDescTestDescription"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("100"));
    }
}
