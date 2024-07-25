package artchain.farmpro.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Value("${ngrok.rest-docs.url}")
	private String restDocsUrl;

	@Value("${production.domain.url}")
	private String productionDomainUrl;

	@Bean
	public OpenAPI openAPI() {

		Server localServer = new Server();
		localServer.setDescription("local server");
		localServer.setUrl("http://localhost:8080");

		Server productionServer = new Server();
		productionServer.setDescription("develop server");
		productionServer.setUrl(productionDomainUrl);

		Server testServer = new Server();
		testServer.setDescription("develop server");
		testServer.setUrl(restDocsUrl);
		
		return new OpenAPI()
				.info(getSwaggerInfo())
				.servers(List.of(localServer, productionServer, testServer));
	}

	private Info getSwaggerInfo() {
		return new Info()
				.title("FarmPro API Docs")
				.description("프로덕션용 FarmPro API 명세서입니다.")
				.version("v1.0");
	}
}