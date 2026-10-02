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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Requisicao;


public class RequisicaoDAO {
    public List<Requisicao> listarRequisicao(){
        List<Requisicao> lista = new ArrayList<>();
        
        String sql = "SELECT r.id, r.utilizador_id, u.nome, r.data_requisicao, r.status " + "FROM requisicoes r " + "INNER JOIN utilizadores u ON r.utilizador_id = u.id";
        
        try (Connection conn = ConexaoBD.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Requisicao req = new Requisicao();
                req.setIdRequisicao(rs.getInt("id"));
                req.setIdUtilizador(rs.getInt("utilizador_id"));
                
               // Pega nome dos utilizadores
               req.setNomeUtilizador(rs.getString("nome"));
               req.setData(rs.getString("data_requisicao"));
               req.setStatus(rs.getString("status"));
               
               lista.add(req);
            }    
         } catch (SQLException e) {
             System.err.println("Erro: " + e.getMessage());
         }
        return lista;
    }
    
    public boolean atualizarStatusRequisicao(int idRequisicao, String statusSelecionado, String motivo) {
        String sql = "UPDATE requisicoes SET status = ?, motivo_reprovacao = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = ConexaoBD.conectar();
            conn.setAutoCommit(false); //inicia transacao
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, statusSelecionado);
            stmt.setString(2, motivo.isEmpty() ? null : motivo);
            stmt.setInt(3, idRequisicao);
            
            stmt.executeUpdate();
            conn.commit(); //confirma transação
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //Falha na transação
                } catch (SQLException ex) {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            //fecha conexao
        }
    }
}
