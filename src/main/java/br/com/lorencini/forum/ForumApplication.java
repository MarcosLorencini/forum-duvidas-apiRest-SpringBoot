package br.com.lorencini.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport//habilita para o spring pegar da requisição do parametros da url os campos de pagina e ordenacao e repassar par ao spring data
@EnableCaching //habilita o uso de caching na aplicacao
@EnableSwagger2 //habilita o swagger no projeto
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
