package br.com.gestao.exceptions;

public class OtherErrorException extends RuntimeException {

	private static final long serialVersionUID = 1686639878664401349L;

	private String tipo;
	
	public OtherErrorException() {
		super("Ocorreu um tipo de erro inesperado.");
	}

	public OtherErrorException(String msg) {
		super(msg);
	}

	public OtherErrorException(String msg, String tipo) {
		super(msg);
		this.tipo = tipo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
