package ru.almasgali.raif_test;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.almasgali.raif_test.repository.AccountRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void clearDB() {
        accountRepository.deleteAll();
    }

    @Test
    public void registerNoRequestBody() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void registerBadRequestParam() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("bad request")).andExpect(status().isBadRequest());
    }

    @Test
    public void registerWrongCurrency() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content("bad request")).andExpect(status().isBadRequest());
    }

    @Test
    public void registerBadContentType() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_XML)
                .content(constructRequest("RUB"))).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void registerGoodRequest() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("RUB", JsonPath.read(content, "$.currency"));
        Assertions.assertEquals(0.0, JsonPath.read(content, "$.balance"));
    }

    @Test
    public void depositNoParam() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        mockMvc.perform(put("/account/" + id + "/deposit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void depositBadParam() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        mockMvc.perform(put("/account/" + id + "/deposit?lol=1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void depositBadValue() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        mockMvc.perform(put("/account/" + id + "/deposit?amount=lol"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void depositNegativeValue() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        mockMvc.perform(put("/account/" + id + "/deposit?amount=-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void depositBadId() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        mockMvc.perform(put("/account/" + (id + 1) + "/deposit?amount=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void depositGoodRequest() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        MvcResult accountResult = mockMvc.perform(put("/account/" + id + "/deposit?amount=1"))
                .andExpect(status().isOk()).andReturn();

        content = accountResult.getResponse().getContentAsString();
        Assertions.assertEquals(1.0, JsonPath.read(content, "$.balance"));
    }

    @Test
    public void depositThenWithdraw() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        MvcResult accountResult = mockMvc.perform(put("/account/" + id + "/deposit?amount=1"))
                .andExpect(status().isOk()).andReturn();

        content = accountResult.getResponse().getContentAsString();
        Assertions.assertEquals(1.0, JsonPath.read(content, "$.balance"));

        accountResult = mockMvc.perform(put("/account/" + id + "/withdraw?amount=1"))
                .andExpect(status().isOk()).andReturn();

        content = accountResult.getResponse().getContentAsString();
        Assertions.assertEquals(0.0, JsonPath.read(content, "$.balance"));
    }

    @Test
    public void withdrawTooMuch() throws Exception {
        MvcResult result = mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated()).andReturn();

        String content = result.getResponse().getContentAsString();
        long id = JsonPath.parse(content).read("$.id", Long.class);

        MvcResult accountResult = mockMvc.perform(put("/account/" + id + "/deposit?amount=1"))
                .andExpect(status().isOk()).andReturn();

        content = accountResult.getResponse().getContentAsString();
        Assertions.assertEquals(1.0, JsonPath.read(content, "$.balance"));

        mockMvc.perform(put("/account/" + id + "/withdraw?amount=2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetByCurrency() throws Exception {
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("EUR")))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(constructRequest("RUB")))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/account?currency=RUB")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<String> currencies = JsonPath.parse(content).read("$..currency");
        Assertions.assertEquals(2, currencies.size());
        Assertions.assertEquals("RUB", currencies.get(0));
        Assertions.assertEquals("RUB", currencies.get(1));

        result = mockMvc.perform(get("/account?currency=EUR")).andReturn();
        content = result.getResponse().getContentAsString();
        currencies = JsonPath.parse(content).read("$..currency");
        Assertions.assertEquals(1, currencies.size());
        Assertions.assertEquals("EUR", currencies.get(0));

        result = mockMvc.perform(get("/account?currency=USD")).andReturn();
        content = result.getResponse().getContentAsString();
        currencies = JsonPath.parse(content).read("$..currency");
        Assertions.assertEquals(0, currencies.size());
    }

    private static String constructRequest(String currency) {
        return "{ \"currency\" : \"" + currency + "\" }";
    }
}
