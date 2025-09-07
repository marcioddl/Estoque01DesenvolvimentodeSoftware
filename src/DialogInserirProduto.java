import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DialogInserirProduto extends JDialog {
    private JTextField tnome, tquantidade, tpreco;

    public DialogInserirProduto(TelaProdutos parent) {
        super(parent, "Inserir Produto", true);
        setSize(350, 200);
        setLocationRelativeTo(parent);

        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        painelCampos.add(new JLabel("Nome:"));
        tnome = new JTextField();
        painelCampos.add(tnome);

        painelCampos.add(new JLabel("Quantidade:"));
        tquantidade = new JTextField();
        painelCampos.add(tquantidade);

        painelCampos.add(new JLabel("Preço:"));
        tpreco = new JTextField();
        painelCampos.add(tpreco);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painel.add(painelCampos);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);

        painel.add(Box.createVerticalStrut(10));
        painel.add(botoes);

        add(painel);

        btnSalvar.addActionListener(e -> salvarProduto(parent));
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarProduto(TelaProdutos parent) {
        String nome = tnome.getText().trim();
        String qtdStr = tquantidade.getText().trim();
        String precoStr = tpreco.getText().trim();

        if (nome.isEmpty() || qtdStr.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!");
            return;
        }

        try {
            int quantidade = Integer.parseInt(qtdStr);
            double preco = Double.parseDouble(precoStr);

            String sql = "INSERT INTO produtos (nome, quantidade, preco) VALUES (?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nome);
                stmt.setInt(2, quantidade);
                stmt.setDouble(3, preco);
                stmt.executeUpdate();
            }

            parent.carregarProdutos("");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir produto: " + ex.getMessage());
        }
    }
}
