package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp-2?autoReconnect=true";
	private static String user = "postgres";
	private static String senha = "admin";
	private static Connection connection;
	
	//Método obrigado para retornar a conexão existente
	public static Connection getConnection() {
		return connection;
	}
	
	static {//Método statico criado, quando alguém chamar a classe ele faz a conexão
		conectar();
	}
	
	public SingleConnectionBanco() {//Método criado, para quando a classe for instanciada ele cria a conexão.
		conectar();
	}
	
	private static void conectar() {
		try {
			
			if(connection == null) {
				Class.forName("org.postgresql.Driver");//sempre colocar, carrega o driver de conexão do banco
				connection = DriverManager.getConnection(banco, user, senha);//conexão atribui o driver com url, user e senha
				connection.setAutoCommit(false);//AutoCommit false, para não efetuar alterações no banco sem nosso comando
			}
			
		} catch (Exception e) {
			e.printStackTrace();//Mostrar qual erro no momento de conectar
		}
	}
	
	

}
