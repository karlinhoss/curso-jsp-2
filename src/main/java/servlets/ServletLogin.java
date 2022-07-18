package servlets;

import java.io.IOException;

import dao.DAOLoginRespository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;

//O Chamado Controller são as servlets ou ServletLoginController
@WebServlet(urlPatterns = { "/principal/ServletLogin", "/ServletLogin" }) // Mapeamento de URL que vem da tela
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DAOLoginRespository daoLoginRepository = new DAOLoginRespository();

	public ServletLogin() {

	}

	// Recebe os dados pela URL em parâmetros
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//método Post do formulário ao entrar no ServletLogin manda para o doGet que chama o doPost para resolver
		String acao = request.getParameter("acao");
		
		if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			request.getSession().invalidate(); //Invalida a sessão e apaga os atributos que valida a sessão
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp"); //Redireciona para o index.jsp
			redirecionar.forward(request, response);
		}else {
			doPost(request, response);	
		}	
	}

	// Recebe os dados enviados por um formulário
	// Request é o que receber os dados da tela do usuário
	// Response é o que você vai mandar os dados para a tela do usuário
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Criar as variáveis para recerber os parâmetros
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");
		
		

		try {
			// Verificação se os campos estão vazios
			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {

				// Criar o objeto que vai receber os parametros
				ModelLogin modelLogin = new ModelLogin();
				modelLogin.setLogin(login);
				modelLogin.setSenha(senha);

				if (daoLoginRepository.validarAutenticacao(modelLogin)) {// Valida o login e senha

					// Coloca o usuário na sessão. Mantem o usuário logado no site
					request.getSession().setAttribute("usuario", modelLogin.getLogin());

					if (url == null || url.equals("null")) {// Verifica se a url é nula ou se a url está atribuída nula
						url = "principal/principal.jsp"; // Então volta para tela inicial
					}

					RequestDispatcher redirecionar = request.getRequestDispatcher(url);// Redirecionamento para o site
					redirecionar.forward(request, response);

				} else {// Caso os campos estejam vazios ele vai redirecionar para a página do index.jsp com informandao a mensagem.
					RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");// Objeto que redireciona
					request.setAttribute("msg", "Informe o login e senha corretamente");
					redirecionar.forward(request, response);
				}

			} else {// Caso os campos estejam vazios ele vai redirecionar para a página do index.jsp com informandao a mensagem.
				RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");// Objeto que redireciona
				request.setAttribute("msg", "Informe o login e senha corretamente");
				redirecionar.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Mostra os erros da exceção e direciona para a página de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");// Objeto que redireciona
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
