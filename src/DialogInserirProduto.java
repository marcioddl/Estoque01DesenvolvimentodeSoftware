import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DialogInserirProduto extends JDialog {
    private JTextField tnome, tquantidade, tpreco;
    private JButton btnSalvar, btnCancelar;

    public DialogInserirProduto(TelaProdutos parent) {
        super(parent, "Inserir Produto", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Linha 0 - Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        tnome = new JTextField(15);
        painel.add(tnome, gbc);

        // Linha 1 - Quantidade
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        tquantidade = new JTextField(5);
        painel.add(tquantidade, gbc);

        // Linha 2 - Preço
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        tpreco = new JTextField(7);
        painel.add(tpreco, gbc);

        // Linha 3 - Botões
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        painel.add(painelBotoes, gbc);

        add(painel);

        // Eventos
        btnSalvar.addActionListener(e -> {
            try {
                String nome = tnome.getText();
                int quantidade = Integer.parseInt(tquantidade.getText());
                double preco = Double.parseDouble(tpreco.getText());

                String sql = "INSERT INTO produtos (nome, quantidade, preco) VALUES (?, ?, ?)";
                try (Connection conn = ConnectionFactory.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, nome);
                    stmt.setInt(2, quantidade);
                    stmt.setDouble(3, preco);
                    stmt.executeUpdate();
                }

                parent.carregarProdutos(""); // atualiza tabela na tela principal
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser números!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao inserir produto: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
