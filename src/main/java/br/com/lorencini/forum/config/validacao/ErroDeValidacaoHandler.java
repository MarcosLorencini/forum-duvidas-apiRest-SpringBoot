package br.com.lorencini.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroDeValidacaoHandler {//é um interceptador que quando ouver algum erro em qualquer controller o spring chama esta classe
	
	@Autowired
	private MessageSource messageSource;//classe do spring que ajuda a pegar a msg de erro no idioma correto
	
	//se acontecer qualquer esta exception MethodArgumentNotValidException(validacao de form) na controller o spring vai chamar
	//este método passando como pararametro a exception que aconteceu
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)//informa o status que o spring tem que devolver, pois 200 é padrão 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		//tratamento:
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		List<FieldError> fieldErrors =  exception.getBindingResult().getFieldErrors(); //pega todos os erros do form
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());//pega a msg + o locale atual do idioma
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
		
		
	}

}
