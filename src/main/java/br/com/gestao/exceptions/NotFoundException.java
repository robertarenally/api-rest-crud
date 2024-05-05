package br.com.gestao.exceptions;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8383626189456249258L;

	private String tipo;

	public NotFoundException() {
		super("Item não encontrado");
	}

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(String msg, String tipo) {
		super(msg);
		this.tipo = tipo;
	}

	public String getTipo() {
		return this.tipo;
	}

}