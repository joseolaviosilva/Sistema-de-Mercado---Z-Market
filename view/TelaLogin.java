package com.supermercado.view;

import com.supermercado.controller.LoginController;
import com.supermercado.controller.UsuarioController;
import com.supermercado.dao.UsuarioDAO;
import com.supermercado.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private final LoginController loginController;
    private JTextField cpfField, nomeField;

    public TelaLogin() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        this.loginController = new LoginController(usuarioDAO);

        setTitle("Zé Market - Login");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Zé Market", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> realizarLogin());
        formPanel.add(loginButton);

        JButton registerButton = new JButton("Cadastrar-se");
        registerButton.addActionListener(e -> abrirTelaCadastro());
        formPanel.add(registerButton);
        
        add(formPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private void realizarLogin() {
        String cpf = cpfField.getText();
        String nome = nomeField.getText();
        Usuario usuario = loginController.autenticar(cpf, nome);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            dispose();
            if (usuario.isAdmin()) {
                new TelaProdutosAdmin().setVisible(true);
            } else {
                new TelaCompraCliente().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "CPF ou Nome inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaCadastro() {
        new TelaCadastroUsuario(this, new UsuarioController(new UsuarioDAO()));
    }
}