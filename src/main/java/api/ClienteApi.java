package api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Cliente;
import repositorio.ClienteRepositorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@WebServlet(value = "/cliente", asyncSupported = true)
public class ClienteApi extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String input = request.getParameter("action");

			switch (input) {
			case "adicionar":
				adicionarCliente(request, response);
				break;
			case "atualizar":
				atualizarCliente(request, response);
				break;
			case "remover":
				removerCliente(request, response);
				break;
			case "vincularEndereco":
				vincularEndereco(request, response);
				break;
			case "desvincularEndereco":
				desvincularEndereco(request, response);
				break;
			default:
				response.getWriter().println("Valor do parametro invalido.");
			}

		} catch (NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("O parametro 'action' deve ser informado na requisicao.");
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String input = request.getParameter("action");

			switch (input) {
			case "obterPorId":
				obterPorId(request, response);
				break;
			case "obterTodos":
				obterTodos(request, response);
				break;
			default:
				response.getWriter().println("Valor do parametro invalido.");
			}
		} catch (NullPointerException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("O parametro 'action' deve ser informado na requisicao.");
		}

	}

	// Métodos de POST

	private void adicionarCliente(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nome = request.getParameter("nome");
		String complemento = request.getParameter("complemento");
		String numero = request.getParameter("numero");

		if (nome == null || complemento == null || numero == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'nome','complemento' e 'numero' sao obrigatorios.");
		} else {

			Cliente novoCliente = this.clienteRepositorio.adicionar(nome, complemento, Integer.parseInt(numero));
			// Gson
			String clienteJsonString = this.gson.toJson(novoCliente);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(clienteJsonString);
			out.flush();
		}

	}

	private void atualizarCliente(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String complemento = request.getParameter("complemento");
		String numero = request.getParameter("numero");

		if (id == null || nome == null || complemento == null || numero == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'id',nome','complemento' e 'numero' sao obrigatorios.");
		} else {
			Cliente clienteAtualizado = this.clienteRepositorio.atualizar(Long.parseLong(id), nome, complemento,
					Integer.parseInt(numero));
			// Gson
			String clienteJsonString = this.gson.toJson(clienteAtualizado);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(clienteJsonString);
			out.flush();
		}
	}

	private void removerCliente(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		if (id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametro 'id' deve ser informado.");
		} else {
			this.clienteRepositorio.remover(Long.parseLong(id));
			response.getWriter().println("{\"response\": \"Cliente removido com sucesso\"}");
		}
	}

	private void vincularEndereco(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idCliente = request.getParameter("id");
		String idEndereco = request.getParameter("idEndereco");

		if (idCliente == null || idEndereco == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'id' e 'idEndereco' deve ser informado.");
		} else {
			this.clienteRepositorio.vincularEndereco(Long.parseLong(idCliente), Long.parseLong(idEndereco));
			response.getWriter().println("{\"response\": \"Endereco vinculado com sucesso\"}");
		}
	}

	private void desvincularEndereco(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idCliente = request.getParameter("id");
		String idEndereco = request.getParameter("idEndereco");

		if (idCliente == null || idEndereco == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'id' e 'idEndereco' deve ser informado.");
		} else {
			this.clienteRepositorio.desvincularEndereco(Long.parseLong(idCliente), Long.parseLong(idEndereco));
			response.getWriter().println("{\"response\": \"Endereco desvinculado com sucesso\"}");
		}
	}

	// Métodos de GET

	private void obterPorId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		if (id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametro 'id' deve ser informado.");
		} else {
			Cliente cliente = this.clienteRepositorio.obterPorId(Long.parseLong(id));

			// Gson
			String clienteJsonString = this.gson.toJson(cliente);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(clienteJsonString);
			out.flush();
		}
	}

	private void obterTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Cliente> clientes= this.clienteRepositorio.obterTodos();
		
		// Gson
		String clientesJsonString = this.gson.toJson(clientes);
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		out.print(clientesJsonString);
		out.flush();
	}

}
