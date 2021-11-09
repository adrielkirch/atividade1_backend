package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import modelo.Cliente;
import modelo.UnidadeFederativa;

public class ClienteCidadeRepositorio {
	private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-jpa");
	private EntityManager entityManager = entityManagerFactory.createEntityManager();

	public ArrayList<Cliente> obterPorCidade (String cidade, String uf) {
		TypedQuery<Cliente> query =
                entityManager.createQuery("SELECT c FROM Cliente c where c.endereco.cidade = :cidade and c.endereco.uf = :uf",
                        Cliente.class);
        return (ArrayList<Cliente>) query.setParameter("cidade", cidade).setParameter("uf", UnidadeFederativa.valueOf(uf)).getResultList();
	}

}