package br.com.lorencini.forum.config.validacao;


//representa o erro de validação representa o json de erro devolvido pelo spring
public class ErroDeFormularioDto {
	
	private String campo;
	private String erro;
	
	public ErroDeFormularioDto(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
	
	
	
	

}
