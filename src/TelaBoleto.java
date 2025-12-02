import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TelaBoleto extends JFrame {

    public TelaBoleto(String valor, String nome) {
        super("Visualização de Boleto");
        
        // Configurações da Janela
        setSize(450, 550);
        // DISPOSE fecha só essa janela, não o sistema todo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gera um código de barras fictício
        String codigoBarras = gerarCodigoBarras();

        // --- CONTEÚDO VISUAL (HTML para formatar bonito) ---
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        
        String html = "<html><body style='font-family: sans-serif; padding: 20px;'>"
                + "<div style='border: 1px solid #000; padding: 15px;'>"
                + "  <h2 style='text-align:center;'>BOLETO BANCÁRIO</h2>"
                + "  <hr>"
                + "  <p><b>Pagador:</b> " + nome + "</p>"
                + "  <p><b>Valor:</b> R$ " + valor + "</p>"
                + "  <p><b>Vencimento:</b> Daqui a 3 dias</p>"
                + "  <br><br>"
                + "  <p style='font-size: 10px;'>Recibo do Pagador</p>"
                + "  <div style='background-color: #eee; padding: 10px; border: 1px dashed #999; text-align: center;'>"
                + "    <b>LINHA DIGITÁVEL:</b><br>"
                +      codigoBarras
                + "  </div>"
                + "  <br>"
                + "  <center>|| |||| || ||||| || |||| |||| || |||</center>"
                + "</div>"
                + "</body></html>";

        editorPane.setText(html);
        add(new JScrollPane(editorPane), BorderLayout.CENTER);

        // --- BOTÃO DE AÇÃO ---
        JButton btnImprimir = new JButton("Imprimir / Salvar PDF");
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImprimir.setBackground(null);
        btnImprimir.setForeground(null);

        btnImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editorPane.print(); // Abre a janela de impressão do sistema
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao imprimir: " + ex.getMessage());
                }
            }
        });

        add(btnImprimir, BorderLayout.SOUTH);
        setVisible(true);
    }

    // Construtor vazio caso seja chamado sem parâmetros
    public TelaBoleto() {
        this("0,00", "Consumidor Final");
    }

    private String gerarCodigoBarras() {
        Random r = new Random();
        return "34191.79001 " + r.nextInt(99999) + "." + r.nextInt(99999) + " " + r.nextInt(99999) + "." + r.nextInt(99999) + " 8 " + r.nextInt(999999999);
    }
}