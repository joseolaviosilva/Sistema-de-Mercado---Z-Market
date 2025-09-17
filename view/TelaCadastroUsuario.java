package com.supermercado.view;

import com.supermercado.controller.UsuarioController;
import javax.swing.*;
import java.awt.*;

public class TelaCadastroUsuario extends JDialog {

    private final UsuarioController usuarioController;
    private JTextField nomeField, cpfField;
    private JCheckBox adminCheckBox;

    public TelaCadastroUsuario(Frame owner, UsuarioController usuarioController) {
        super(owner, "Zé Market - Cadastro de Usuário", true);
        this.usuarioController = usuarioController;

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Nome:"));
        nomeField = new JTextField();
        add(nomeField);

        add(new JLabel("CPF:"));
        cpfField = new JTextField();
        add(cpfField);
        
        add(new JLabel("É Administrador?"));
        adminCheckBox = new JCheckBox();
        add(adminCheckBox);

        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(e -> cadastrarUsuario());
        add(cadastrarButton);
        
        setVisible(true);
    }

    private void cadastrarUsuario() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        boolean sucesso = usuarioController.cadastrarUsuario(nome, cpf, isAdmin);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar. Verifique se o CPF já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}