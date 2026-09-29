package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author cnvtte
 */
public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String tipoPerfil;

    public Usuario(int idUsuario, String nome, String email, String tipoPerfil) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.tipoPerfil = tipoPerfil;
    }

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTipoPerfil() { return tipoPerfil; }
    
}
