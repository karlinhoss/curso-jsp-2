package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp-2?autoReconnect=true";
	private static String user = "postgres";
	private static String senha = "admin";
	private static Connection connection;
	
	//M�todo obrigado para retornar a conex�o existente
	public static Connection getConnection() {
		return connection;
	}
	
	static {//M�todo statico criado, quando algu�m chamar a classe ele faz a conex�o
		conectar();
	}
	
	public SingleConnectionBanco() {//M�todo criado, para quando a classe for instanciada ele cria a conex�o.
		conectar();
	}
	
	private static void conectar() {
		try {
			
			if(connection == null) {
				Class.forName("org.postgresql.Driver");//sempre colocar, carrega o driver de conex�o do banco
				connection = DriverManager.getConnection(banco, user, senha);//conex�o atribui o driver com url, user e senha
				connection.setAutoCommit(false);//AutoCommit false, para n�o efetuar altera��es no banco sem nosso comando
			}
			
		} catch (Exception e) {
			e.printStackTrace();//Mostrar qual erro no momento de conectar
		}
	}
	
	

}
