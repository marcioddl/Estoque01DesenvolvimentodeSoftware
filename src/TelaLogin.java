import javax.swing.*;
import java.awt.*;
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class TelaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JLabel lblMsg;
    private JButton btnLoginSimples;
    private JButton btnLoginSeguro;
    private JButton btnContinuar;
    private JButton btnSair; 
    
    private JPanel painelAcoes; 
    private JPanel painelLoginBotoes; 
    
    private final String AES_KEY = "1234567890123456"; 
    private final String ALGORITHM = "AES/ECB/PKCS5Padding"; 

    public TelaLogin() {
        super("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        painel.add(txtUsuario, gbc);

        // Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(20);
        painel.add(txtSenha, gbc);

        // Mensagem
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        lblMsg = new JLabel("", SwingConstants.CENTER);
        lblMsg.setForeground(new Color(0, 128, 0));
        painel.add(lblMsg, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2; 

        //Painel de botões de Login
        painelLoginBotoes = new JPanel(); 
        btnLoginSimples = new JButton("Login Simples");
        btnLoginSeguro = new JButton("Login Seguro AES");
        painelLoginBotoes.add(btnLoginSimples);
        painelLoginBotoes.add(btnLoginSeguro);
        painel.add(painelLoginBotoes, gbc);

        // 2. Painel de botões de Ação
        painelAcoes = new JPanel();
        btnContinuar = new JButton("Ir para Produtos >>");
        btnSair = new JButton("Sair/Logout"); 
        painelAcoes.add(btnContinuar);
        painelAcoes.add(btnSair);
        painelAcoes.setVisible(false); 
        painel.add(painelAcoes, gbc);

        add(painel);
        
        // Ações
        btnLoginSimples.addActionListener(e -> iniciarLoginSimples());
        btnLoginSeguro.addActionListener(e -> iniciarLoginSeguro());
        
        btnContinuar.addActionListener(e -> {
            TelaProdutos telaProdutos = new TelaProdutos();
            telaProdutos.setVisible(true);
            TelaLogin.this.dispose(); 
        });
        
        btnSair.addActionListener(e -> {
            txtUsuario.setText("");
            lblMsg.setText("Sessão encerrada. Efetue um novo Login.");
            lblMsg.setForeground(Color.BLACK); 
            
            painelAcoes.setVisible(false);
            painelLoginBotoes.setVisible(true);
            btnLoginSimples.setEnabled(true);
            btnLoginSeguro.setEnabled(true);
            
            TelaLogin.this.revalidate();
            TelaLogin.this.repaint();
        });
    }

    private void iniciarLoginSimples() {
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());
        String url = "https://datse.com.br/dev/syncjava.php";

        System.out.println("--- LOGIN SIMPLES ---");
        System.out.println("URL: " + url);
        System.out.println("Usuário: " + usuario);
        System.out.println("Senha Enviada: " + senha); 
        System.out.println("---------------------------");

        iniciarConexao(usuario, senha, url);
    }

    private void iniciarLoginSeguro() {
        String usuario = txtUsuario.getText();
        String senhaTextoPlano = new String(txtSenha.getPassword());
        String url = "https://datse.com.br/dev/syncjava2.php";
        
        String senhaCriptografada;
        try {
            senhaCriptografada = encrypt(senhaTextoPlano, AES_KEY);
            
            System.out.println("--- LOGIN CRIPTOGRAFIA ---");
            System.out.println("URL: " + url);
            System.out.println("Usuário: " + usuario);
            System.out.println("Senha Original: " + senhaTextoPlano);
            System.out.println("Senha Cifrada (Hash): " + senhaCriptografada);
            System.out.println("--------------------------");
            
            lblMsg.setText("Criptografia concluída. Conectando...");
        } catch (Exception e) {
            lblMsg.setText("Erro na criptografia: " + e.getMessage());
            lblMsg.setForeground(Color.RED);
            btnLoginSimples.setEnabled(true);
            btnLoginSeguro.setEnabled(true);
            return; 
        }

        iniciarConexao(usuario, senhaCriptografada, url);
    }
    
    private void iniciarConexao(String usuario, String senha, String url) {
        txtSenha.setText(""); 
        
        lblMsg.setText("Conectando à API...");
        lblMsg.setForeground(Color.BLACK); 

        painelAcoes.setVisible(false);
        painelLoginBotoes.setVisible(true); 
        btnLoginSimples.setEnabled(false);
        btnLoginSeguro.setEnabled(false);

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                try {
                    ClienteHTTP cliente = new ClienteHTTP(usuario, senha, url); 
                    String resposta = cliente.conecta();
                    int codigo = cliente.getCodRetorno();
                    
                    System.out.println("Código HTTP: " + codigo);
                    System.out.println(resposta);
                    
                    return "Código HTTP: " + codigo + "\n" + resposta;
                    
                } catch (Exception ex) {
                    return "Erro: " + ex.getMessage();
                }
            }

            @Override
            protected void done() {
                try {
                    String resultado = get(); 

                
                    if (resultado.toLowerCase().contains("login realizado com sucess") || 
                        resultado.toLowerCase().contains("login efetuad")) { 
                        
                        
                        lblMsg.setText("Login realizado com sucesso!");
                        lblMsg.setForeground(new Color(0, 128, 0)); 
                        painelLoginBotoes.setVisible(false);
                        painelAcoes.setVisible(true); 
                        
                    } else {
                        
                        lblMsg.setText(resultado); 
                        lblMsg.setForeground(Color.RED); 
                        btnLoginSimples.setEnabled(true);
                        btnLoginSeguro.setEnabled(true);
                        painelAcoes.setVisible(false);
                        painelLoginBotoes.setVisible(true);
                    }

                } catch (Exception ex) {
                    // ERRO DE THREAD/CONEXÃO
                    lblMsg.setText("Erro ao conectar: " + ex.getMessage());
                    lblMsg.setForeground(Color.RED);
                    btnLoginSimples.setEnabled(true);
                    btnLoginSeguro.setEnabled(true);
                    painelAcoes.setVisible(false);
                    painelLoginBotoes.setVisible(true);
                }
                TelaLogin.this.revalidate();
                TelaLogin.this.repaint();
            }
        };

        worker.execute();
    }
    
    public String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}