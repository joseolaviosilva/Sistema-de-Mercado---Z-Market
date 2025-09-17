package com.supermercado.dao;

import com.supermercado.model.Usuario;
import com.supermercado.util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, cpf, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getCpf());
            ps.setBoolean(3, usuario.isAdmin());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";
        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                boolean isAdmin = rs.getBoolean("is_admin");
                return new Usuario(nome, cpf, isAdmin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}