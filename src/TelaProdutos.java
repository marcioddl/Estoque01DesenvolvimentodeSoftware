import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// --- IMPORTS DOS SEUS PACOTES ---
import br.classesimportantes.pessoas.*;
import br.classesimportantes.acessoapi.*;
import br.classesimportantes.tratajson.*;

public class TelaProdutos extends JFrame {
    
    // Componentes Visuais
    private JTextField txtNome, txtCPF, txtTelefone, txtCartao, txtValor;
    private JLabel lblCartao;
    private JComboBox<String> comboPagamento;
    private JTextArea areaLog;
    private JButton btnEmitir, btnImprimir, btnLimpar;
    
    // --- CORREÇÃO: Variável global para ser acessada no botão ---
    private JPanel panelForm; 
    
    // Objetos de Negócio
    private pessoa transacaoAtual;
    private QRCodeGenerator QRcode;
    public listaDePessoas lista; 

    public TelaProdutos() {
        super("Sistema de Pagamento API");
        setSize(950, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); 
        getContentPane().setBackground(new Color(240, 242, 245)); 

        transacaoAtual = new pessoa(); 
        QRcode = new QRCodeGenerator();
        lista = new listaDePessoas();

        construirInterface();
    }

    private void construirInterface() {
        // --- 1. TOPO ---
        JPanel panelTopo = new JPanel();
        panelTopo.setBackground(new Color(0, 51, 102)); 
        panelTopo.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel lblTitulo = new JLabel("Pagamento Seguro");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("", Font.BOLD, 22));
        panelTopo.add(lblTitulo);
        add(panelTopo, BorderLayout.NORTH);

        // --- 2. CENTRO ---
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 20, 0)); 
        panelCentro.setBackground(new Color(240, 242, 245));
        panelCentro.setBorder(new EmptyBorder(10, 20, 10, 20));

        // >>> ESQUERDA: FORMULÁRIO <<<
        JPanel panelFormulario = new JPanel(new BorderLayout());
        panelFormulario.setOpaque(false);

        // --- CORREÇÃO: Instanciando a variável global panelForm ---
        panelForm = new JPanel(new GridLayout(0, 1, 0, 10)); 
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Adicionando Campos
        panelForm.add(criarLabel("Dados do Cliente"));
        panelForm.add(criarInput("Nome Completo:", txtNome = new JTextField()));
        panelForm.add(criarInput("CPF (11 dígitos):", txtCPF = new JTextField()));
        panelForm.add(criarInput("Telefone:", txtTelefone = new JTextField()));
        
        panelForm.add(new JSeparator());
        panelForm.add(criarLabel("Pagamento"));
        
        // Combo
        JPanel pPag = new JPanel(new GridLayout(2, 1));
        pPag.setBackground(Color.WHITE);
        pPag.add(new JLabel("Forma de Pagamento:"));
        String[] opcoes = {"PIX", "BOLETO", "CARTAO"};
        comboPagamento = new JComboBox<>(opcoes);
        comboPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pPag.add(comboPagamento);
        panelForm.add(pPag);

        // Cartão (Hack para layout)
        lblCartao = new JLabel("Número do Cartão:");
        txtCartao = new JTextField();
        JPanel pCartao = criarInput("", txtCartao); 
        pCartao.removeAll();
        pCartao.add(lblCartao);
        pCartao.add(txtCartao);
        panelForm.add(pCartao);
        
        // Estado inicial: Escondido
        lblCartao.setVisible(false); 
        txtCartao.setVisible(false);

        panelForm.add(criarInput("Valor da Compra (R$):", txtValor = new JTextField()));

        panelFormulario.add(panelForm, BorderLayout.CENTER);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panelBotoes.setOpaque(false);
        btnLimpar = new JButton("Limpar");
        btnEmitir = new JButton("PROCESSAR PAGAMENTO");
        btnImprimir = new JButton("Imprimir / Visualizar");

        
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnEmitir);
        panelBotoes.add(btnImprimir);
        panelFormulario.add(panelBotoes, BorderLayout.SOUTH);

        // >>> DIREITA: LOG <<<
        JPanel panelLog = new JPanel(new BorderLayout());
        panelLog.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(Color.GRAY), "Console de Debug (API Logs)"
        ));
        panelLog.setBackground(Color.WHITE);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(null);
        areaLog.setForeground(null);
        areaLog.setFont(null);
        areaLog.setMargin(null);
        
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(null);
        panelLog.add(scrollLog, BorderLayout.CENTER);

        panelCentro.add(panelFormulario);
        panelCentro.add(panelLog);
        add(panelCentro, BorderLayout.CENTER);

        configurarAcoes();
        logSistema("Sistema Iniciado. Aguardando dados...");
    }

    private void configurarAcoes() {
        // Dinâmica do Cartão
        comboPagamento.addActionListener(e -> {
            String sel = (String) comboPagamento.getSelectedItem();
            boolean isCartao = "CARTAO".equals(sel);
            lblCartao.setVisible(isCartao);
            txtCartao.setVisible(isCartao);
            if(!isCartao) txtCartao.setText("");
            
            // --- CORREÇÃO: Agora panelForm é global e visível aqui ---
            panelForm.revalidate(); 
            panelForm.repaint();
        });

        // Limpar
        btnLimpar.addActionListener(e -> {
            txtNome.setText(""); txtCPF.setText(""); txtTelefone.setText("");
            txtValor.setText(""); txtCartao.setText(""); 
            areaLog.setText(""); 
            logSistema("Campos limpos. Sistema pronto.");
            transacaoAtual = new pessoa();
            btnEmitir.setEnabled(true); btnEmitir.setText("PROCESSAR PAGAMENTO");
        });

        // EMITIR
        btnEmitir.addActionListener(e -> {
            if (txtNome.getText().trim().length() <= 3) { alerta("Nome muito curto."); return; }
            if (txtCPF.getText().trim().length() != 11) { alerta("CPF deve ter 11 dígitos."); return; }
            
            String tipoPag = (String) comboPagamento.getSelectedItem();
            if ("CARTAO".equals(tipoPag) && txtCartao.getText().trim().isEmpty()) { alerta("Cartão obrigatório."); return; }

            btnEmitir.setEnabled(false); btnEmitir.setText("Conectando...");
            areaLog.setText(""); 
            
            logSistema(">>> INICIANDO TRANSAÇÃO <<<");
            logSistema("Validando dados locais... OK");
            logSistema("Montando pacote HTTP (Simulando CURL):");
            logSistema("------------------------------------------------");
            logSistema("POST /dev/syncpix.php HTTP/1.1");
            logSistema("Host: www.datse.com.br");
            logSistema("Enviado:"+ txtNome.getText() + "&cpf=" + txtCPF.getText() + "&tipo=" + tipoPag + "&valor=" + txtValor.getText());
            logSistema("------------------------------------------------");
            logSistema("Enviando requisição... Aguarde...");

            new Thread(() -> {
                try {
                    pagamentoHTTP conexao = new pagamentoHTTP(
                        txtNome.getText(), txtCPF.getText(), txtCartao.getText(),
                        txtValor.getText(), tipoPag, "http://www.datse.com.br/dev/syncpix.php"
                    );

                    String jsonResposta = conexao.conecta();
                    int httpCode = conexao.codretorno; 

                    SwingUtilities.invokeLater(() -> {
                        logSistema("Resposta do Servidor recebida!");
                        logSistema("HTTP STATUS CODE: " + httpCode);
                        
                        // Se deu sucesso ou se a API retornou o JSON esperado mesmo com erro lógico
                        if (httpCode == 200 || jsonResposta.contains("cod_retorno")) {
                            logSistema("JSON RETORNADO:\n" + jsonResposta);
                            
                            trataJSON tratador = new trataJSON(jsonResposta);
                            transacaoAtual = tratador.tratarString();
                            
                            transacaoAtual.setNome(txtNome.getText());
                            transacaoAtual.setCpf(txtCPF.getText());
                            transacaoAtual.setFone(txtTelefone.getText());
                            lista.adicionarPessoa(transacaoAtual);

                            logSistema("------------------------------------------------");
                            logSistema("INTERPRETAÇÃO DO SISTEMA:");
                            logSistema("Mensagem API: " + transacaoAtual.getRetmsg());
                            logSistema("Modo Identificado: " + transacaoAtual.getModo());
                            logSistema("ID Transação: " + transacaoAtual.getId());
                            System.out.println("Total de Pessoas Salvas: " + lista.retornaLista().length);                                                        
                            logSistema("------------------------------------------------");
                            
                            String[] nomesSalvos = lista.retornaLista();
                                 for(String s : nomesSalvos) {
                                     if(s != null) System.out.println("Registro: " + s);
                                    }
                            
                            // Se o modo for ERRO, avisa diferente
                            if("ERRO".equals(transacaoAtual.getModo())) {
                                logSistema(">>> FALHA NA TRANSAÇÃO <<<");
                                alerta("Erro na API: " + transacaoAtual.getRetmsg());
                            } else {
                                logSistema(">>> PROCESSO FINALIZADO COM SUCESSO <<<");
                                logSistema("Clique em 'Imprimir / Visualizar' para ver o resultado.");
                            }

                        } else {
                            logSistema("ERRO DE SERVIDOR! Código: " + httpCode);
                            logSistema("Conteúdo: " + jsonResposta);
                        }
                        
                        btnEmitir.setEnabled(true); btnEmitir.setText("PROCESSAR PAGAMENTO");
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        logSistema("!!! ERRO FATAL DE CONEXÃO !!!");
                        logSistema("Detalhe: " + ex.getMessage());
                        btnEmitir.setEnabled(true); btnEmitir.setText("TENTAR NOVAMENTE");
                    });
                }
            }).start();
        });

        // IMPRIMIR
// --- BOTÃO IMPRIMIR (CORRIGIDO) ---
        btnImprimir.addActionListener(e -> {
            if(transacaoAtual == null || transacaoAtual.getModo() == null) {
                alerta("Realize o processamento primeiro.");
                return;
            }
            String modo = transacaoAtual.getModo();
            
            if("ERRO".equals(modo)) {
                alerta("Não é possível imprimir uma transação com erro.");
                return;
            }

            logSistema("Abrindo visualização para modo: " + modo);

            // 1. CASO PIX
            if("PIX".equals(modo) || "1".equals(modo)) {
                try { 
                    QRcode.gerarQRCode(transacaoAtual.getId()); 
                    // Passa os dados para a TelaPIX
                    new TelaPIX(txtNome.getText(), txtValor.getText(), transacaoAtual.getId()); 
                } 
                catch(Exception ex) { 
                    alerta("Erro QR: " + ex.getMessage()); 
                }
            } 
            // 2. CASO BOLETO (ESTAVA FALTANDO!)
            else if ("BOLETO".equals(modo) || "2".equals(modo)) {
                // Abre a TelaBoleto passando Valor e Nome
                new TelaBoleto(txtValor.getText(), txtNome.getText());
            } 
            // 3. CASO CARTÃO
            else if ("CARTAO".equals(modo) || "3".equals(modo)) {
                JOptionPane.showMessageDialog(this, 
                    "Transação Aprovada!\nID: " + transacaoAtual.getId(), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                alerta("Modo desconhecido: " + modo);
            }
        });
    }

    private void logSistema(String msg) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        areaLog.append("[" + time + "] " + msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength()); 
    }

    private JPanel criarInput(String titulo, JTextField campo) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(Color.WHITE);
        if(!titulo.isEmpty()) {
            JLabel l = new JLabel(titulo);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            p.add(l);
        }
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(150, 150, 150)), 
            new EmptyBorder(5, 5, 5, 5)
        ));
        p.add(campo);
        return p;
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(new Color(0, 51, 102));
        return l;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void alerta(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new TelaProdutos().setVisible(true));
    }
}