package com.supermercado.main;

import com.supermercado.view.TelaLogin;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}