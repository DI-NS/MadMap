package MedMap.exception;

import MedMap.service.AuthService;
import MedMap.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Test
    void shouldHandleUserAlreadyExistsException() throws Exception {
        // Simula que ao registrar uma UBS já existente, o service lança a exceção
        when(authService.register(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada."));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeUbs\":\"UBS Teste\", \"cnes\":\"123456\", \"address\":\"Rua ABC\", \"password\":\"senha\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Uma UBS com esse CNES já está registrada."));
    }

    @Test
    void shouldHandleInvalidCredentialsException() throws Exception {
        when(authService.login("naoencontrado", "qualquer"))
                .thenThrow(new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        mockMvc.perform(post("/auth/login")
                        .param("cnes","naoencontrado")
                        .param("password","qualquer"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("UBS não encontrada ou dados inválidos"));
    }
}
