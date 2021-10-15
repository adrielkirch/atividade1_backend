package repositorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import modelo.Cliente;
import modelo.Endereco;

import java.util.ArrayList;


public class ClienteRepositorio {

	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-jpa");
	private EntityManager entityManager = entityManagerFactory.createEntityManager();

	public ClienteRepositorio() {

	}

	public Cliente obterPorId(Long id) {
		Cliente cliente = entityManager.find(Cliente.class, id);
		return cliente != null ? cliente : null;
	}

	public ArrayList<Cliente> obterTodos() {
	        return (ArrayList<Cliente>) entityManager.createQuery("from Cliente").getResultList();
	 }

	public Cliente adicionar (String nome, String complemento, Integer numero) {
		Cliente novoCliente = new Cliente(null,nome,complemento,numero,null);
		this.entityManager.getTransaction().begin();
		this.entityManager.persist(novoCliente);
		this.entityManager.getTransaction().commit();
		System.out.println("Cliente adicionado com sucesso");
		return novoCliente;
	}
	
	public Cliente atualizar (Long id,String nome, String complemento, Integer numero) {
		Cliente cliente = this.entityManager.find(Cliente.class, id);
		this.entityManager.getTransaction().begin();
		cliente.setNome(nome);
		cliente.setComplemento(complemento);
		cliente.setNumero(numero);
		this.entityManager.getTransaction().commit();
		System.out.println("Cliente atualizado com sucesso");
		return cliente;
	}
	
	public void remover (Long id) {
		Cliente cliente = this.entityManager.find(Cliente.class, id);
		this.entityManager.getTransaction().begin();
		this.entityManager.remove(cliente);
		this.entityManager.getTransaction().commit();
		System.out.println("Cliente removido com sucesso");
	}
	
	public void vincularEndereco (Long idCliente , Long idEndereco) {
		Cliente cliente = this.entityManager.find(Cliente.class, idCliente);
		Endereco endereco =   this.entityManager.find(Endereco.class, idEndereco);
		this.entityManager.getTransaction().begin();
		cliente.setEndereco(endereco);
		this.entityManager.getTransaction().commit();
		System.out.println("Endereco do cliente atualizado com sucesso");
	}
	
	public void desvincularEndereco (Long idCliente , Long idEndereco) {
		Cliente cliente = this.entityManager.find(Cliente.class, idCliente);
		Endereco endereco =   this.entityManager.find(Endereco.class, idEndereco);
		this.entityManager.getTransaction().begin();
		cliente.setEndereco(null);
		this.entityManager.getTransaction().commit();
		System.out.println("Endereco do cliente desvinculado com sucesso");
	}
	

	 
	
}
