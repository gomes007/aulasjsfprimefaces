package br.com.project.util.all;

public enum StatusPersistencia {
	
	ERROR("Erro"), SUCESSO("Sucesso"),OBJETO_REFERENCIADO("Esse registro n�o pode ser apagado por possuir refer�ncias ao mesmo");

	private String name;

	private StatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
