package br.com.lorencini.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.lorencini.forum.modelo.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
	
	//seta as informaçãoes que o SpringFox Swagger precisa para configurar o projeto
	
	@Bean
	public Docket foruApi() {
		return new Docket(DocumentationType.SWAGGER_2)//indica o tipo de documentacao
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.lorencini.forum"))// a partir de qual pacote ele vai começar a ler as classes
                .paths(PathSelectors.ant("/**"))//quais endpoints o springfox vai ler para gerar o doc
                .build()
                .ignoredParameterTypes(Usuario.class)//ignora todas a urls que trabalham com a classe Usuario, caso contrario na tela do swagger vai aparece a senha do usuario
                .globalOperationParameters( Arrays.asList(//add paremetros globais no swagger 
                		new ParameterBuilder()
                		.name("Authorization") //nome do parametro
                		.description("Header para Token JWT") //descricao do param
                		.modelRef(new ModelRef("string")) //tipo do param
                		.parameterType("header") // tipo header 
                		.required(false) 
                		.build()));
		
	}

}
