package MedMap.controller;

import MedMap.model.User;
import MedMap.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerShouldReturnSuccessMessage() throws Exception {
        User user = new User("UBS Teste", "654321", "Rua Z", "minhaSenha");
        when(authService.register(any(User.class))).thenReturn("UBS registrada com sucesso!");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("UBS registrada com sucesso!"));
    }

    @Test
    void loginShouldReturnJwtToken() throws Exception {
        when(authService.login("654321", "minhaSenha")).thenReturn("meu-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .param("cnes", "654321")
                        .param("password", "minhaSenha"))
                .andExpect(status().isOk())
                .andExpect(content().string("meu-jwt-token"));
    }
}
