package br.com.lorencini.forum.config.validacao;

import java.net.URI;
import java.util.UUID;

import br.com.lorencini.forum.modelo.ProblemDetail;

public class ProblemDetailBuilder {

	private final Throwable throwable;

	public ProblemDetailBuilder(Throwable throwable) {
		this.throwable = throwable;
	}

	ProblemDetail build() {
		ProblemDetail detail = new ProblemDetail();
		detail.setType(buildType());
		detail.setTitle(buildTitle());
		detail.setDetail(buildDetailMessage());
		detail.setStatus(buildStatus());
		detail.setInstance(buildInstance());
		return detail;

	}

	private URI buildType() {
		return URI.create("https://api.myshop.example/apidocs/" + javadocName(throwable.getClass()) + ".html");
	}

	private static String javadocName(Class<?> type) {
		return type.getName().replace('.', '/') // the package names are delimited like a path
				.replace('$', '.'); // nested classes are delimited with a period
	}

	private String buildTitle() {
		return camelToWords(throwable.getClass().getSimpleName());
	}

	private static String camelToWords(String input) {
		return String.join(" ", input.split("(?=\\p{javaUpperCase})"));
	}

	private String buildDetailMessage() {
		return throwable.getMessage();
	}

	private int buildStatus() {
		Status status = throwable.getClass().getAnnotation(Status.class);
		if (status != null) {
			return status.value();
		} else {
			
			return  500;
			//return  HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode();
		}
	}

	private URI buildInstance() {
		return URI.create("urn:uuid:" + UUID.randomUUID());
	}

}
