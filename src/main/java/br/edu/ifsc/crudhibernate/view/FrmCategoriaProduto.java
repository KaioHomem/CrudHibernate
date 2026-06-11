 package br.edu.ifsc.crudhibernate.view;

import br.edu.ifsc.crudhibernate.dao.CategoriaDAO;
import br.edu.ifsc.crudhibernate.dao.ProdutoDAO;
import br.edu.ifsc.crudhibernate.model.Categoria;
import br.edu.ifsc.crudhibernate.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de cadastro em duas tabelas relacionadas (Categoria x Produto).
 *
 * Ao selecionar uma categoria na tabela de cima, a tabela de baixo mostra
 * apenas os produtos vinculados a ela. Também há um JComboBox de categorias
 * usado na hora de salvar/alterar um produto.
 *
 * A interface foi construída em código puro (sem o editor visual do NetBeans),
 * então a classe compila em qualquer ambiente.
 *
 * Autores: Kaio Felipe Homem e Jefferson Machado
 */
public class FrmCategoriaProduto extends JFrame {

    // DAOs
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    // Modelos das duas tabelas
    private DefaultTableModel modeloCategorias;
    private DefaultTableModel modeloProdutos;

    // ---- Componentes: área de categorias ----
    private JTextField txtIdCategoria;
    private JTextField txtNomeCategoria;
    private JTextField txtDescricaoCategoria;
    private JTable tblCategorias;

    // ---- Componentes: área de produtos ----
    private JTextField txtIdProduto;
    private JTextField txtNomeProduto;
    private JTextField txtDescricaoProduto;
    private JTextField txtQuantidade;
    private JTextField txtPreco;
    private JComboBox<Categoria> cmbCategoria;
    private JTable tblProdutos;

    public FrmCategoriaProduto() {
        setTitle("Cadastro de Categorias e Produtos - Kaio Felipe Homem / Jefferson Machado");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 720);
        setLocationRelativeTo(null);

        initComponents();

        // Carregamentos iniciais
        carregarCategorias();
        carregarComboCategorias();
    }

    // ====================================================================
    // Montagem da interface
    // ====================================================================
    private void initComponents() {
        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        principal.add(criarAreaCategorias());
        principal.add(Box.createVerticalStrut(15));
        principal.add(criarAreaProdutos());

        setContentPane(new JScrollPane(principal));
    }

    private JPanel criarAreaCategorias() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Área 1 - Categorias"));

        // Formulário
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        txtIdCategoria = new JTextField(6);
        txtIdCategoria.setEditable(false);
        txtNomeCategoria = new JTextField(20);
        txtDescricaoCategoria = new JTextField(20);

        addLinha(form, gbc, 0, "ID Categoria:", txtIdCategoria);
        addLinha(form, gbc, 1, "Nome da Categoria:", txtNomeCategoria);
        addLinha(form, gbc, 2, "Descrição da Categoria:", txtDescricaoCategoria);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btNova = new JButton("Nova Categoria");
        JButton btSalvar = new JButton("Salvar Categoria");
        JButton btAlterar = new JButton("Alterar Categoria");
        JButton btExcluir = new JButton("Excluir Categoria");
        botoes.add(btNova);
        botoes.add(btSalvar);
        botoes.add(btAlterar);
        botoes.add(btExcluir);

        btNova.addActionListener(e -> limparCamposCategoria());
        btSalvar.addActionListener(e -> salvarCategoria());
        btAlterar.addActionListener(e -> alterarCategoria());
        btExcluir.addActionListener(e -> excluirCategoria());

        // Tabela
        modeloCategorias = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Descrição"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCategorias = new JTable(modeloCategorias);
        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aoSelecionarCategoria();
            }
        });

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tblCategorias), BorderLayout.CENTER);
        painel.setPreferredSize(new Dimension(860, 300));
        return painel;
    }

    private JPanel criarAreaProdutos() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBorder(BorderFactory.createTitledBorder(
                "Área 2 - Produtos da categoria selecionada"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;

        txtIdProduto = new JTextField(6);
        txtIdProduto.setEditable(false);
        txtNomeProduto = new JTextField(20);
        txtDescricaoProduto = new JTextField(20);
        txtQuantidade = new JTextField(8);
        txtPreco = new JTextField(8);
        cmbCategoria = new JComboBox<>();

        addLinha(form, gbc, 0, "ID Produto:", txtIdProduto);
        addLinha(form, gbc, 1, "Nome do Produto:", txtNomeProduto);
        addLinha(form, gbc, 2, "Descrição do Produto:", txtDescricaoProduto);
        addLinha(form, gbc, 3, "Quantidade:", txtQuantidade);
        addLinha(form, gbc, 4, "Preço:", txtPreco);
        addLinha(form, gbc, 5, "Categoria:", cmbCategoria);

        // Quando muda a categoria no combo, recarrega os produtos dela
        cmbCategoria.addActionListener(e -> carregarProdutosDaCategoria());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btNovo = new JButton("Novo Produto");
        JButton btSalvar = new JButton("Salvar Produto");
        JButton btAlterar = new JButton("Alterar Produto");
        JButton btExcluir = new JButton("Excluir Produto");
        botoes.add(btNovo);
        botoes.add(btSalvar);
        botoes.add(btAlterar);
        botoes.add(btExcluir);

        btNovo.addActionListener(e -> limparCamposProduto());
        btSalvar.addActionListener(e -> salvarProduto());
        btAlterar.addActionListener(e -> alterarProduto());
        btExcluir.addActionListener(e -> excluirProduto());

        modeloProdutos = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Descrição", "Quantidade", "Preço", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProdutos = new JTable(modeloProdutos);
        tblProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aoSelecionarProduto();
            }
        });

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(form, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tblProdutos), BorderLayout.CENTER);
        painel.setPreferredSize(new Dimension(860, 320));
        return painel;
    }

    private void addLinha(JPanel form, GridBagConstraints gbc, int linha,
                          String rotulo, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        form.add(new JLabel(rotulo), gbc);
        gbc.gridx = 1;
        form.add(campo, gbc);
    }

    // ====================================================================
    // CRUD de Categoria
    // ====================================================================
    private void carregarCategorias() {
        modeloCategorias.setRowCount(0);
        List<Categoria> lista = categoriaDAO.listarTodos();
        for (Categoria c : lista) {
            modeloCategorias.addRow(new Object[]{c.getId(), c.getNome(), c.getDescricao()});
        }
    }

    private void salvarCategoria() {
        if (txtNomeCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome da categoria.");
            return;
        }
        Categoria c = new Categoria();
        c.setNome(txtNomeCategoria.getText().trim());
        c.setDescricao(txtDescricaoCategoria.getText().trim());
        categoriaDAO.salvar(c);

        JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!");
        limparCamposCategoria();
        carregarCategorias();
        carregarComboCategorias();
    }

    private void alterarCategoria() {
        if (txtIdCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela.");
            return;
        }
        Categoria c = new Categoria();
        c.setId(Integer.parseInt(txtIdCategoria.getText().trim()));
        c.setNome(txtNomeCategoria.getText().trim());
        c.setDescricao(txtDescricaoCategoria.getText().trim());
        categoriaDAO.atualizar(c);

        JOptionPane.showMessageDialog(this, "Categoria alterada com sucesso!");
        limparCamposCategoria();
        carregarCategorias();
        carregarComboCategorias();
    }

    private void excluirCategoria() {
        if (txtIdCategoria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela.");
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "Excluir a categoria selecionada? (os produtos vinculados também podem ser afetados)",
                "Confirmação", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            categoriaDAO.excluir(Integer.parseInt(txtIdCategoria.getText().trim()));
            JOptionPane.showMessageDialog(this, "Categoria excluída!");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível excluir. Verifique se há produtos vinculados.\n" + ex.getMessage());
        }
        limparCamposCategoria();
        carregarCategorias();
        carregarComboCategorias();
    }

    /**
     * Dica 7: ao clicar numa categoria, preenche o formulário e carrega
     * na tabela de produtos apenas os produtos daquela categoria.
     */
    private void aoSelecionarCategoria() {
        int linha = tblCategorias.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Integer idCategoria = (Integer) modeloCategorias.getValueAt(linha, 0);
        txtIdCategoria.setText(String.valueOf(idCategoria));
        txtNomeCategoria.setText(String.valueOf(modeloCategorias.getValueAt(linha, 1)));
        Object desc = modeloCategorias.getValueAt(linha, 2);
        txtDescricaoCategoria.setText(desc == null ? "" : desc.toString());

        // Sincroniza o combo com a categoria selecionada
        selecionarCategoriaNoCombo(idCategoria);
        // Carrega os produtos dessa categoria
        carregarProdutosPorCategoria(idCategoria);
    }

    private void limparCamposCategoria() {
        txtIdCategoria.setText("");
        txtNomeCategoria.setText("");
        txtDescricaoCategoria.setText("");
        tblCategorias.clearSelection();
    }

    // ====================================================================
    // ComboBox de categorias
    // ====================================================================
    private void carregarComboCategorias() {
        Categoria atual = (Categoria) cmbCategoria.getSelectedItem();
        cmbCategoria.removeAllItems();
        for (Categoria c : categoriaDAO.listarTodos()) {
            cmbCategoria.addItem(c);
        }
        if (atual != null) {
            selecionarCategoriaNoCombo(atual.getId());
        }
    }

    private void selecionarCategoriaNoCombo(Integer idCategoria) {
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).getId().equals(idCategoria)) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private Categoria getCategoriaSelecionadaCombo() {
        return (Categoria) cmbCategoria.getSelectedItem();
    }

    // ====================================================================
    // CRUD de Produto
    // ====================================================================
    private void carregarProdutosPorCategoria(Integer idCategoria) {
        modeloProdutos.setRowCount(0);
        List<Produto> lista = produtoDAO.listarPorCategoria(idCategoria);
        for (Produto p : lista) {
            modeloProdutos.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getDescricao(),
                    p.getQuantidade(), p.getPreco(),
                    p.getCategoria() != null ? p.getCategoria().getNome() : ""});
        }
    }

    /** Recarrega a tabela com base na categoria escolhida no combo. */
    private void carregarProdutosDaCategoria() {
        Categoria c = getCategoriaSelecionadaCombo();
        if (c != null) {
            carregarProdutosPorCategoria(c.getId());
        }
    }

    /** Lê os dados do formulário e devolve um Produto pronto. */
    private Produto lerProdutoDoFormulario() {
        Produto p = new Produto();
        if (!txtIdProduto.getText().trim().isEmpty()) {
            p.setId(Integer.parseInt(txtIdProduto.getText().trim()));
        }
        p.setNome(txtNomeProduto.getText().trim());
        p.setDescricao(txtDescricaoProduto.getText().trim());
        p.setQuantidade(txtQuantidade.getText().trim().isEmpty()
                ? 0 : Integer.parseInt(txtQuantidade.getText().trim()));
        p.setPreco(txtPreco.getText().trim().isEmpty()
                ? 0.0 : Double.parseDouble(txtPreco.getText().trim().replace(",", ".")));
        // Associa a categoria selecionada no combo
        p.setCategoria(getCategoriaSelecionadaCombo());
        return p;
    }

    private boolean validarProduto() {
        if (getCategoriaSelecionadaCombo() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma categoria antes de cadastrar o produto.");
            return false;
        }
        if (txtNomeProduto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
            return false;
        }
        return true;
    }

    private void salvarProduto() {
        if (!validarProduto()) {
            return;
        }
        try {
            Produto p = lerProdutoDoFormulario();
            p.setId(null); // garante inserção
            produtoDAO.salvar(p);
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");
            limparCamposProduto();
            carregarProdutosDaCategoria();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade/Preço inválidos.");
        }
    }

    private void alterarProduto() {
        if (txtIdProduto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        if (!validarProduto()) {
            return;
        }
        try {
            Produto p = lerProdutoDoFormulario();
            produtoDAO.atualizar(p);
            JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!");
            limparCamposProduto();
            carregarProdutosDaCategoria();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade/Preço inválidos.");
        }
    }

    private void excluirProduto() {
        if (txtIdProduto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        int op = JOptionPane.showConfirmDialog(this,
                "Excluir o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) {
            return;
        }
        produtoDAO.excluir(Integer.parseInt(txtIdProduto.getText().trim()));
        JOptionPane.showMessageDialog(this, "Produto excluído!");
        limparCamposProduto();
        carregarProdutosDaCategoria();
    }

    /**
     * Dica 11: ao clicar num produto, preenche o formulário e seleciona
     * automaticamente a categoria dele no combo.
     */
    private void aoSelecionarProduto() {
        int linha = tblProdutos.getSelectedRow();
        if (linha < 0) {
            return;
        }
        txtIdProduto.setText(String.valueOf(modeloProdutos.getValueAt(linha, 0)));
        txtNomeProduto.setText(String.valueOf(modeloProdutos.getValueAt(linha, 1)));
        Object desc = modeloProdutos.getValueAt(linha, 2);
        txtDescricaoProduto.setText(desc == null ? "" : desc.toString());
        Object qtd = modeloProdutos.getValueAt(linha, 3);
        txtQuantidade.setText(qtd == null ? "" : qtd.toString());
        Object preco = modeloProdutos.getValueAt(linha, 4);
        txtPreco.setText(preco == null ? "" : preco.toString());

        // Seleciona a categoria do produto no combo
        Integer idProduto = (Integer) modeloProdutos.getValueAt(linha, 0);
        Produto p = produtoDAO.buscarPorId(idProduto);
        if (p != null && p.getCategoria() != null) {
            selecionarCategoriaNoCombo(p.getCategoria().getId());
        }
    }

    /** Método de limpeza separado (Dica 11): não apaga a categoria selecionada. */
    private void limparCamposProduto() {
        txtIdProduto.setText("");
        txtNomeProduto.setText("");
        txtDescricaoProduto.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
        tblProdutos.clearSelection();
    }
}
