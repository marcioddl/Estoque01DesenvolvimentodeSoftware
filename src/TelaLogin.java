import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public TelaLogin() {
        super("Login no Sistema");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 5, 5));
        painelCampos.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        painelCampos.add(txtUsuario);

        painelCampos.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painel.add(painelCampos);

        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnEntrar = new JButton("Entrar");
        JButton btnLimpar = new JButton("Limpar");
        botoes.add(btnEntrar);
        botoes.add(btnLimpar);

        painel.add(Box.createVerticalStrut(10));
        painel.add(botoes);

        add(painel);

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
