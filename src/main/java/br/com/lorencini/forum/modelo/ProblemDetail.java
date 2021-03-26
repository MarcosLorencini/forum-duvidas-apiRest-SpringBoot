package br.com.lorencini.forum.modelo;

import java.net.URI;

import org.springframework.http.MediaType;

public class ProblemDetail {

	public static final MediaType JSON_MEDIA_TYPE = MediaType.valueOf("application/problem+json");
	 	
	    private URI type;
	    private String title;
	    private String detail;
	    private Integer status;
	    private URI instance;
	    
		public URI getType() {
			return type;
		}
		public void setType(URI type) {
			this.type = type;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public URI getInstance() {
			return instance;
		}
		public void setInstance(URI instance) {
			this.instance = instance;
		}
	    
	    
}
