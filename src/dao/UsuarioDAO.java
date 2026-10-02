/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author cnvtte
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Usuario;


public class UsuarioDAO {
    public Usuario autenticarUsuario(String email, String senha) {
        String sql = "SELECT id, nome, email, tipo_utilizador FROM utilizadores WHERE email = ? AND senha = ?";
        Usuario usuario = null;
        
        try (Connection conn = ConexaoBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Retorna Usuario
                usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("tipo_utilizador")
                );
            }
        }catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
        }
            
        return usuario; 
    }



















    
}
