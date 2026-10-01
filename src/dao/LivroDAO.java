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
import java.util.List;
import java.util.ArrayList;
import model.Livro;

public class LivroDAO {
    
    public boolean requisitarLivro(int idLivro, int idUsuario){
        String sqlSelectLock = "SELECT copias_disponiveis FROM livros WHERE id = ? FOR UPDATE";
        String sqlUpdateLivro = "UPDATE livros SET copias_disponiveis = copias_disponiveis - 1 WHERE id = ?";
        String sqlInsertRequisicao = "INSERT INTO requisicoes (utilizador_id, livro_id, status) VALUES (?, ?, 'PENDENTE')";
        
        Connection conn = null;
        PreparedStatement stmtLock = null;
        PreparedStatement stmtUpdate = null;
        PreparedStatement stmtInsert = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBD.conectar();
            
            // desativa autocommit, inicia transacao obrigatoria
            conn.setAutoCommit(false);
            
            // Cenario de concorrencia. Bloqueia o registro do livro
            stmtLock = conn.prepareStatement(sqlSelectLock);
            stmtLock.setInt(1, idLivro);
            rs = stmtLock.executeQuery();
            
            if (rs.next()){
                int copias = rs.getInt("copias_disponiveis");
                                      
                if (copias > 0) {
                    // Decrementa o estoque
                    stmtUpdate = conn.prepareStatement(sqlUpdateLivro);
                    stmtUpdate.setInt(1, idLivro);
                    stmtUpdate.executeUpdate();
                                                  
                    // Cria requisição
                    stmtInsert = conn.prepareStatement(sqlInsertRequisicao);
                    stmtInsert.setInt(1, idUsuario);
                    stmtInsert.setInt(2, idLivro);
                    stmtInsert.executeUpdate();
                                                  
                    // Sucesso executa a transação
                    conn.commit();
                    return true;
                } else {
                    // Falha (Sem estoque). Desfaz alterações
                    conn.rollback();
                    return false; 
                }
            } else {
                // Caso o livro não exista na base de dados
                conn.rollback();
                return false;
            }
            
        }catch (SQLException e) {
            // Falha critica. Garante ROLLBACK
            if (conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao executar o ROLLBACK: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao executar o ROLLBACK " + e.getMessage());
            return false;
        } finally {
            // Restaura o estado padrao e fecha as conexoes
            try {
                if (rs != null) rs.close();
                if (stmtLock != null) stmtLock.close();
                if (stmtUpdate != null) stmtUpdate.close();
                if (stmtInsert != null) stmtInsert.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexões: " + e.getMessage());
            }
        }
    }
    
    public List<Livro> consultarAcervo(String termoBusca){
        List<Livro> listaLivros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, ano, paginas, copias_disponiveis, copias_totais, isbn FROM livros WHERE titulo ILIKE ? OR autor ILIKE ?";
        
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termoBusca + "%");
            stmt.setString(2, "%" + termoBusca + "%");
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setAno(rs.getInt("ano"));
                livro.setPaginas(rs.getInt("paginas"));
                livro.setCopiasTotais(rs.getInt("copias_totais"));
                livro.setCopiasDisponiveis(rs.getInt("copias_disponiveis"));
                listaLivros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar o acervo: " + e.getMessage());
        }
        
        return listaLivros;
    }
}


