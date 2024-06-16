package ru.almasgali.raif_test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

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

        Assertions.assertTrue(content.contains("\"currency\":\"RUB\""));
        Assertions.assertTrue(content.contains("\"balance\":0.0"));
    }

    private static String constructRequest(String currency) {
        return "{ \"currency\" : \"" + currency + "\" }";
    }
}
