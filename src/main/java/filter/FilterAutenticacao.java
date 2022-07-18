package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/principal/*" }) // Intercepta todas as requisi�oes que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {

	private static Connection connection;

	public FilterAutenticacao() {

	}

	// Encerra os processos quando o servidor � parado, matar uma conex�o com o
	// banco
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Intercepta as requisi��es e as respostas no sistema
	// Tudo que a gente fizer no sistema vai passar por aki
	// Valida��o de autentica��o
	// Dar commit e rollback de transa��es no banco
	// Validar e fazer redirecionamento espec�ficos
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();

			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath();// Url que est� sendo acessada

			// Validar se est� logado, se n�o redireciona para tela de login.
			// Vai tentar acessar uma URL do site, mas n�o est� logado para isso
			if (usuarioLogado == null &&
					!urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {// Usu�rio n�o logado

				// Redireciona para o index.jsp para realizar login
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login");
				redireciona.forward(request, response);
				return;// Para a execu��o e redireciona para o login
			} else {
				chain.doFilter(request, response);
			}
			connection.commit();//Se tudo estiver certo ele salva no banco
			
		} catch (Exception e) {
			e.printStackTrace();
			
			//Mostra os erros da exce��o e direciona para a p�gina de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");// Objeto que redireciona
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	// Inicia os processos ou recurso quando o servidor sobe o projeto
	// Iniciar a conex�o com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}

}
