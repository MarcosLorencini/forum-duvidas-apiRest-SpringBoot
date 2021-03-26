package br.com.lorencini.forum.config.validacao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.lorencini.forum.modelo.ProblemDetail;

@RestControllerAdvice
public class ProblemDetailControllerAdvice {
	
	@ExceptionHandler(Throwable.class) 
    public ResponseEntity<?> toProblemDetail(Throwable throwable) {
        ProblemDetail detail = new ProblemDetailBuilder(throwable).build();
 
       // log.debug(detail.toString(), throwable);
 
        return ResponseEntity.status(detail.getStatus())
            .contentType(ProblemDetail.JSON_MEDIA_TYPE)
            .body(detail);
    }

}
