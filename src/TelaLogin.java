import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar, btnLimpar;

    public TelaLogin() {
        super("Login no Sistema");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel central com GridBagLayout
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Linha 0 - Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        painel.add(txtUsuario, gbc);

        // Linha 1 - Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        painel.add(txtSenha, gbc);

        // Linha 2 - Botões
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnEntrar = new JButton("Entrar");
        btnLimpar = new JButton("Limpar");
        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnLimpar);

        painel.add(painelBotoes, gbc);

        add(painel);

        // Eventos
        btnEntrar.addActionListener(e -> autenticar());
        btnLimpar.addActionListener(e -> {
            txtUsuario.setText("");
            txtSenha.setText("");
        });
    }

    private void autenticar() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Login realizado com sucesso!");
                dispose();
                new TelaProdutos().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Usuário ou senha incorretos!");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
