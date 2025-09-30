package com.supermercado.main;

import com.supermercado.view.TelaLogin;
import javax.swing.SwingUtilities;

public class MainApp {
    
    public static void main(String[] args) {
        // Inicia a interface gráfica na Event Dispatch Thread (EDT) para garantir a segurança das threads.
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}