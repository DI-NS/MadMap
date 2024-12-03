package MedMap;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

@SpringBootApplication
public class MedMapApplication {

	public static void main(String[] args) {
		generateJwtSecret();
		SpringApplication.run(MedMapApplication.class, args);
	}

	private static void generateJwtSecret() {
		// Caminho do arquivo application.yml
		File yamlFile = new File("src/main/resources/application.yml");

		// Gera uma chave secreta segura para HMAC-SHA-512
		var secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		byte[] secretKeyBytes = secretKey.getEncoded();

		// Converte para Base64
		String base64Secret = Base64.getEncoder().encodeToString(secretKeyBytes);

		// Valida o tamanho da chave antes de salvar
		if (secretKeyBytes.length < 32) {
			throw new RuntimeException("Generated JWT secret key is less than 256 bits. Regenerate the key.");
		}

		try {
			// Configura o loader para carregar o YAML
			LoaderOptions loaderOptions = new LoaderOptions();
			Yaml yaml = new Yaml(new Constructor(loaderOptions));

			Map<String, Object> yamlData;

			// Lê o conteúdo atual do application.yml
			if (yamlFile.exists()) {
				String content = Files.readString(yamlFile.toPath());
				yamlData = yaml.load(content);
			} else {
				yamlData = new java.util.HashMap<>();
			}

			// Atualiza o valor do jwt.secret
			Map<String, Object> jwtConfig = (Map<String, Object>) yamlData.get("jwt");
			if (jwtConfig == null) {
				jwtConfig = new java.util.HashMap<>();
				yamlData.put("jwt", jwtConfig);
			}
			jwtConfig.put("secret", base64Secret);

			// Configurações para preservar o formato ao salvar
			DumperOptions options = new DumperOptions();
			options.setPrettyFlow(true);
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			Yaml yamlWriter = new Yaml(options);

			// Escreve de volta no arquivo
			try (FileWriter writer = new FileWriter(yamlFile)) {
				yamlWriter.dump(yamlData, writer);
			}

			System.out.println("JWT secret key successfully written to application.yml");

		} catch (IOException e) {
			throw new RuntimeException("Failed to write to application.yml", e);
		}
	}

}
