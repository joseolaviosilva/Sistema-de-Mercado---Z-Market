package com.supermercado.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    // URL de conexão para MySQL. Inclui parâmetros para evitar avisos comuns.
    private static final String URL = "jdbc:mysql://localhost:3306/supermercado?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Usuário padrão do MySQL é 'root'
    private static final String PASSWORD = "admin"; // Altere para sua senha do MySQL

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Driver JDBC para MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}