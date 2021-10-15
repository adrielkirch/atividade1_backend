package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "endereco")
public class Endereco {
	@Id                                                  
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Long id;
	private String logradouro;
	private Integer cep;
	private String bairro;
	private String cidade;
	private UnidadeFederativa uf;
	
	public Endereco() {
		
	}
	public Endereco(Long id, String logradouro, Integer cep, String bairro, String cidade, UnidadeFederativa uf) {
		super();
		this.id =id;
		this.logradouro = logradouro ;
		this.cep = cep;
		this.bairro = bairro;
		this.cidade = cidade;
		this.uf = uf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getCep() {
		return cep;
	}

	public void setCep(Integer cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public UnidadeFederativa getUf() {
		return uf;
	}

	public void setUf(UnidadeFederativa uf) {
		this.uf = uf;
	}

	@Override
	public String toString() {
		return "Endereco{" + "id=" + id + ", logradouro='" + logradouro + '\'' + ", cep=" + cep + ", bairro='" + bairro
				+ '\'' + ", cidade='" + cidade + '\'' + ", uf=" + uf.name() + '}';
	}
}