package artchain.farmpro.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {

		Server localServer = new Server();
		localServer.setDescription("local server");
		localServer.setUrl("http://localhost:8080");

		Server productionServer = new Server();
		productionServer.setDescription("develop server");

		return new OpenAPI()
				.info(getSwaggerInfo())
				.servers(List.of(localServer, productionServer));
	}

	private Info getSwaggerInfo() {
		return new Info()
				.title("FarmPro API Docs")
				.description("프로덕션용 FarmPro API 명세서입니다.")
				.version("v1.0");
	}
}