package repositorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import modelo.Cliente;
import modelo.Endereco;
import modelo.UnidadeFederativa;

import java.math.BigInteger;
import java.util.ArrayList;

public class EnderecoRepositorio {

	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-jpa");
	private EntityManager entityManager = entityManagerFactory.createEntityManager();

	public EnderecoRepositorio() {

	}

	public Endereco obterPorId(Long id) {
		Endereco endereco = entityManager.find(Endereco.class, id);
		return endereco != null ? endereco : null;
	}

	public ArrayList<Endereco> obterTodos() {
		return (ArrayList<Endereco>) entityManager.createQuery("from Endereco").getResultList();
	}

	

	public Endereco adicionar(String logradouro, Integer cep, String bairro, String cidade, UnidadeFederativa uf) {
		Endereco novoEndereco = new Endereco(null, logradouro, cep, bairro, cidade, uf);
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(novoEndereco);
		this.entityManager.getTransaction().commit();
		System.out.println("Endereco adicionado com sucesso");
		return novoEndereco;
	}

	public Endereco atualizar(Long id, String logradouro, Integer cep, String bairro, String cidade,
			UnidadeFederativa uf) {
		Endereco endereco = this.entityManager.find(Endereco.class, id);
		this.entityManager.getTransaction().begin();
		endereco.setLogradouro(logradouro);
		endereco.setCep(cep);
		endereco.setBairro(bairro);
		endereco.setCidade(cidade);
		endereco.setUf(uf);
		this.entityManager.getTransaction().commit();
		System.out.println("Endereco atualizado com sucesso");
		return endereco;
	}

	public void remover(Long id) {
		Endereco endereco = this.entityManager.find(Endereco.class, id);

		this.entityManager.getTransaction().begin();
		this.entityManager.remove(endereco);
		this.entityManager.getTransaction().commit();
		System.out.println("Endereco removido com sucesso");
	}

}
