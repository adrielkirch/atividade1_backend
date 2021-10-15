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
import modelo.Cliente;
import modelo.Endereco;
import modelo.UnidadeFederativa;
import repositorio.ClienteRepositorio;
import repositorio.EnderecoRepositorio;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@WebServlet(value = "/endereco", asyncSupported = true)
public class EnderecoApi extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EnderecoRepositorio enderecoRepositorio = new EnderecoRepositorio();
	private Gson gson = new Gson();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String input = request.getParameter("action");

			switch (input) {
			case "adicionar":
				adicionarEndereco(request, response);
				break;
			case "atualizar":
				atualizarEndereco(request, response);
				break;
			case "remover":
				removerEndereco(request, response);
				break;
			default:
				response.getWriter().println("Valor do parametro invalido.");
			}

		} catch (NullPointerException e) {
			request.setAttribute("error", "O parametro 'action' deve ser informado na requisicao.");
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
			request.setAttribute("error", "O parametro 'action' deve ser informado na requisicao.");
			response.getWriter().println("O parametro 'action' deve ser informado na requisicao.");
		}

	}

	// Métodos de POST

	private void adicionarEndereco(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String logradouro = request.getParameter("logradouro");
		String cep = request.getParameter("cep");
		String bairro = request.getParameter("bairro");
		String cidade = request.getParameter("cidade");
		String uf = request.getParameter("uf");

		if (logradouro == null || cep == null || bairro == null || cidade == null || uf == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'logradouro','cep','bairro','cidade' e 'uf' sao obrigatorios.");
		} else {

			Endereco novoEndereco = this.enderecoRepositorio.adicionar(logradouro, Integer.parseInt(cep), bairro,
					cidade, UnidadeFederativa.valueOf(uf));
			// Gson
			String enderecoJsonString = this.gson.toJson(novoEndereco);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(enderecoJsonString);
			out.flush();
		}
	}

	private void atualizarEndereco(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String logradouro = request.getParameter("logradouro");
		String cep = request.getParameter("cep");
		String bairro = request.getParameter("bairro");
		String cidade = request.getParameter("cidade");
		String uf = request.getParameter("uf");

		if (id == null || logradouro == null || cep == null || bairro == null || cidade == null || uf == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametros 'id','logradouro','cep','bairro','cidade' e 'uf' sao obrigatorios.");
		} else {
			Endereco enderecoAtualizado = this.enderecoRepositorio.atualizar(Long.parseLong(id), logradouro,
					Integer.parseInt(cep), bairro, cidade, UnidadeFederativa.valueOf(uf));
			// Gson
			String enderecoJsonString = this.gson.toJson(enderecoAtualizado);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(enderecoJsonString);
			out.flush();
		}
	}

	private void removerEndereco(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		if (id == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("Parametro 'id' deve ser informado.");
		} else {
			this.enderecoRepositorio.remover(Long.parseLong(id));
			response.getWriter().println("{\"response\": \"Endereco removido com sucesso\"}");
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
			Endereco endereco = this.enderecoRepositorio.obterPorId(Long.parseLong(id));
			// Gson
			String enderecoJsonString = this.gson.toJson(endereco);
			PrintWriter out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			out.print(enderecoJsonString);
			out.flush();
		}
	}

	private void obterTodos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Endereco> enderecos = this.enderecoRepositorio.obterTodos();

		// Gson
		String enderecosJsonString = this.gson.toJson(enderecos);
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("UTF-8");
		out.print(enderecosJsonString);
		out.flush();
	}

}
