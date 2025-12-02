import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class QRCodeGenerator {

	public QRCodeGenerator() {

	}

	public static BufferedImage generateQRCodeImage(String payload, int width, int height) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // Nível de correção de erro

		BitMatrix bitMatrix = new MultiFormatWriter().encode(
			payload, BarcodeFormat.QR_CODE, width, height, hints
		);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

    	public void gerarQRCode(String id) {
		try {
            		// Exemplo de payload (apenas ilustrativo, a payload real é mais complexa)
            		// String pixPayload = "00020126580014br.gov.bcb.pix0136SEU_EMAIL_OU_CHAVE_AQUI5204000053039865802BR5913NOME_DO_DONO6008CIDADE_BR62070503***6304CRC5";
			String pixPayload = id;

            		BufferedImage qrCodeImage = generateQRCodeImage(pixPayload, 300, 300);

            		// Salva a imagem em um arquivo (opcional)
            		ImageIO.write(qrCodeImage, "PNG", new File("QRCodePix.png"));

		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
	}
}