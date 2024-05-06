package br.com.gestao.commons;

public class ResponseWrapper<T> {

	private T data;
	private String message;

	// Construtores
	public ResponseWrapper() {}

	public ResponseWrapper(T data, String message) {
		this.data = data;
		this.message = message;
	}

	// Getters e Setters
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
