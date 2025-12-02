import javax.swing.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.BorderLayout;
import java.io.File;
import java.net.MalformedURLException;

public class TelaPIX {

    // Construtor agora recebe os dados da compra
    public TelaPIX(String nome, String valor, String idTransacao) {
        JFrame frame = new JFrame("Pagamento via PIX");
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false); 

        String imagePath = "QRCodePix.png"; 

        try {
            File imageFile = new File(imagePath);
            
            if (imageFile.exists()) {
                String imageUri = imageFile.toURI().toURL().toString();

                // HTML Melhorado com os dados
                String htmlContent = "<html><body style='font-family: sans-serif; padding: 10px;'>"
                    + "<div style='border: 2px solid #000000ff; padding: 15px; text-align: center; border-radius: 10px;'>"
                    + "  <h2 style='color: #060606ff;'>Pagamento via PIX</h2>"
                    + "  <hr>"
                    + "  <p align='left'><b>Pagador:</b> " + nome + "</p>"
                    + "  <p align='left'><b>Valor:</b> R$ " + valor + "</p>"
                    + "  <p align='left'><b>ID Transação:</b> <span style='font-size: 10px;'>" + idTransacao + "</span></p>"
                    + "  <hr>"
                    + "  <p>Abra o app do seu banco e escaneie:</p>"
                    + "  <img src=\"" + imageUri + "\" alt=\"QR Code PIX\" width=\"200\" height=\"200\">"
                    + "  <br><br>"
                    + "  <p style='font-size: 10px; color: gray;'>Este QR Code expira em 30 minutos.</p>"
                    + "</div>"
                    + "</body></html>";

                editorPane.setText(htmlContent);
            } else {
                 editorPane.setText("<html><body><center><h1>Erro</h1><p>O arquivo QRCodePix.png não foi encontrado.</p></center></body></html>");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            editorPane.setText("<html><body>Erro ao carregar a imagem.</body></html>");
        }

        JButton b = new JButton("Imprimir / Salvar PDF");
        frame.add(b, BorderLayout.SOUTH);
        
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    editorPane.print();
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na impressão: " + ex.getMessage());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(editorPane);
        frame.add(scrollPane, BorderLayout.CENTER); 
        
        frame.setSize(400, 600); // Aumentei um pouco a altura
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
    
    // Construtor vazio para manter compatibilidade (opcional)
    public TelaPIX() {
        this("Desconhecido", "0,00", "0000");
    }
}