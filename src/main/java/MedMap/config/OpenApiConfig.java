package MedMap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurações para a documentação da API usando OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura as informações básicas da API para a documentação.
     *
     * @return Objeto OpenAPI configurado.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MedMap API")
                        .version("1.0")
                        .description("Documentação da API MedMap"));
    }
}
