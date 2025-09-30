package com.supermercado.view;

import com.supermercado.controller.LoginController;
import com.supermercado.controller.ProdutoController;
import com.supermercado.dao.ProdutoDAO;
import com.supermercado.model.Produto;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaProdutosAdmin extends JFrame {

    private final ProdutoController produtoController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nomeField, precoField, estoqueField;
    private JButton addButton, editButton, deleteButton, logoutButton;

    public TelaProdutosAdmin() {
        this.produtoController = new ProdutoController(new ProdutoDAO());

        setTitle("Zé Market - Painel do Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        atualizarTabela();
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Zé Market", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        nomeField = new JTextField();
        precoField = new JTextField();
        estoqueField = new JTextField();
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Preço:"));
        formPanel.add(precoField);
        formPanel.add(new JLabel("Estoque:"));
        formPanel.add(estoqueField);
        
        addButton = new JButton("Adicionar");
        addButton.addActionListener(e -> adicionarProduto());
        formPanel.add(addButton);
        headerPanel.add(formPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque"}, 0);
        table = new JTable(tableModel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Editar Selecionado");
        editButton.addActionListener(e -> editarProduto());
        
        deleteButton = new JButton("Remover Selecionado");
        deleteButton.addActionListener(e -> removerProduto());
        
        logoutButton = new JButton("Deslogar");
        logoutButton.addActionListener(e -> deslogar());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        table.getSelectionModel().addListSelectionListener(event -> {
            if (table.getSelectedRow() != -1) {
                preencherFormulario();
            }
        });
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoController.listarProdutos();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidadeEstoque()});
        }
    }
    
    private void limparFormulario() {
        nomeField.setText("");
        precoField.setText("");
        estoqueField.setText("");
        table.clearSelection();
    }
    
    private void preencherFormulario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            nomeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            precoField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            estoqueField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void adicionarProduto() {
        try {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int estoque = Integer.parseInt(estoqueField.getText());
            if (produtoController.cadastrarProduto(nome, preco, estoque)) {
                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
                atualizarTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao adicionar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e estoque devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int estoque = Integer.parseInt(estoqueField.getText());
            if (produtoController.editarProduto(id, nome, preco, estoque)) {
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                atualizarTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e estoque devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (produtoController.removerProduto(id)) {
                JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
                atualizarTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao remover produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deslogar() {
        new LoginController(null).deslogar();
        dispose();
        new TelaLogin().setVisible(true);
    }
}