package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author cnvtte
 */



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/annas_archive";
    private static final String UTILIZADOR = "postgres";
    private static final String PALAVRA_PASSE = "";

    public static Connection conectar() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL, UTILIZADOR, PALAVRA_PASSE);
        conexao.setAutoCommit(false);
        return conexao;
    }
}
