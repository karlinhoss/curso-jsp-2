package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRespository {
	
	private Connection connection = null;//Objeto de conexão
	
	//Construtor com a conexão pronta para o banco
	public DAOLoginRespository() {
		connection = SingleConnectionBanco.getConnection();	
	}
	
	//Métoco para validar login
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {
		
		//Select para verificar usuário e senha
		//String sql = "select * from model_login where upper(login) = uper(?) and upper(senha) = (?) ";//upper, função sql do postgres
		String sql = "select * from model_login where login = ? and senha = ? ";						//valida o login e senha tudo de uma mesma
		PreparedStatement statement = connection.prepareStatement(sql);								//admin, admin														//forma, mesmo digitando Admin Admin, senha é 
																									
		statement.setString(1, modelLogin.getLogin());
		statement.setString(2, modelLogin.getSenha());
		
		ResultSet resultSet = statement.executeQuery();//Resultado do select
		
		if(resultSet.next()) {//If para validar o retorno do banco se existe
			return true;//autenticado
		}
		return false;//não autenticado
	}
	
}
