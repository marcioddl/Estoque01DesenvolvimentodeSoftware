import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class TelaProdutos extends JFrame {
    private JTextField txtPesquisa;
    private JButton btnInserir, btnEditar, btnExcluir;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaProdutos() {
        super("Cadastro de Produtos");
        setSize(750, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //MENU 
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem sair = new JMenuItem("Sair");
        JMenuItem relatorio = new JMenuItem("Relatorio");
        sair.addActionListener(e -> System.exit(0));
        //relatorio.addActionListener(e -> relatorio());
        menuArquivo.add(relatorio);
        menuArquivo.add(sair);
        menuBar.add(menuArquivo);
        setJMenuBar(menuBar);

        //PARTE SUPERIOR
        JPanel painelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTop.add(new JLabel("Pesquisar por nome:"));
        txtPesquisa = new JTextField(15);
        painelTop.add(txtPesquisa);

        btnInserir = new JButton("Inserir");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        painelTop.add(btnInserir);
        painelTop.add(btnEditar);
        painelTop.add(btnExcluir);

        add(painelTop, BorderLayout.NORTH);

        //TABELA 
        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Quantidade");
        modelo.addColumn("Preço");

        tabela = new JTable(modelo);
        tabela.setRowHeight(25);
        tabela.setFillsViewportHeight(true);

        // Centralizar dados na tabela
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        
        carregarProdutos("");

        //EVENTOS 
        // Pesquisa automática enquanto digita
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { pesquisar(); }
            public void removeUpdate(DocumentEvent e) { pesquisar(); }
            public void changedUpdate(DocumentEvent e) { pesquisar(); }

            private void pesquisar() {
                carregarProdutos(txtPesquisa.getText().trim());
            }
        });

        btnInserir.addActionListener(e -> new DialogInserirProduto(this).setVisible(true));
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
    }

    public void carregarProdutos(String nomeFiltro) {
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,nomeFiltro + "%");
            ResultSet rs = stmt.executeQuery();

            modelo.setRowCount(0);
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());
        }
    }

    private void editarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar!");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String nome = JOptionPane.showInputDialog(this, "Nome:", modelo.getValueAt(linha, 1));
        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade:", modelo.getValueAt(linha, 2));
        String precoStr = JOptionPane.showInputDialog(this, "Preço:", modelo.getValueAt(linha, 3));

        try {
            int quantidade = Integer.parseInt(qtdStr);
            double preco = Double.parseDouble(precoStr);

            String sql = "UPDATE produtos SET nome=?, quantidade=?, preco=? WHERE id=?";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nome);
                stmt.setInt(2, quantidade);
                stmt.setDouble(3, preco);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }

            carregarProdutos(txtPesquisa.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage());
        }
    }

    private void excluirProduto() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir!");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        String sql = "DELETE FROM produtos WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            carregarProdutos(txtPesquisa.getText().trim());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage());
        }
    }
private void relatorio(){
    
}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaProdutos().setVisible(true));
    }
}
