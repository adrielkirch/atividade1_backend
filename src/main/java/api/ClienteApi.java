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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import modelo.Cliente;
import repositorio.ClienteCidadeRepositorio;
import repositorio.ClienteRepositorio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@WebServlet(value = "/cliente", asyncSupported = true)
public class ClienteApi extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
	private ClienteCidadeRepositorio clienteCidadeRepositorio = new ClienteCidadeRepositorio();
	private Gson gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

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
			case "obterPorCidade":
				obterPorCidade(request, response);
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
			String clienteJsonString = this.gsonBuilder.toJson(novoCliente);
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
			String clienteJsonString = this.gsonBuilder.toJson(clienteAtualizado);
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
			String clienteJsonString = this.gsonBuilder.toJson(cliente);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(clienteJsonString);
			out.flush();
		}
	}

	private void obterTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Cliente> clientes = this.clienteRepositorio.obterTodos();

		// Gson
		String clientesJsonString = this.gsonBuilder.toJson(clientes);
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		out.print(clientesJsonString);
		out.flush();
	}

	private void obterPorCidade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cidade = request.getParameter("cidade");
		String uf = request.getParameter("uf");

		if (uf == null || cidade == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametro 'cidade' e 'uf' devem ser informados.");
		} else {

			JsonObject mainJson = new JsonObject();
			mainJson.addProperty("cidade", cidade);
			mainJson.addProperty("uf", uf);

			ArrayList<Cliente> listaClientes = this.clienteCidadeRepositorio.obterPorCidade(cidade, uf);
			String strJson = this.gsonBuilder.toJson(listaClientes);

			JsonElement jsonElement = new JsonElement() {
				@Override
				public JsonElement deepCopy() {
					return null;
				}
			};

			jsonElement = JsonParser.parseString(strJson);
			mainJson.add("clientes", jsonElement);
			strJson = gsonBuilder.toJson(mainJson);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(strJson);
			out.flush();
		}
	}

}
