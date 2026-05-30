package br.edu.ifsc.crudhibernate;

import br.edu.ifsc.crudhibernate.view.FrmCategoriaProduto;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal - inicia a aplicação Swing.
 *
 * Atividade Prática - Cadastro em Duas Tabelas com Swing e Hibernate
 * Disciplina: Desenvolvimento de Sistemas Orientados a Objetos - CC3 - 2026/1
 *
 * Autores: Kaio Felipe Homem e Jefferson Machado
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new FrmCategoriaProduto().setVisible(true));
    }
}
