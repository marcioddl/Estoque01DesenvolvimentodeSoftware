import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ClienteHTTP {

    private String usuario;
    private String senha;
    private String url;
    private String urlParameters;
    private int codRetorno;
    private String resposta;

    private final int TIMEOUT = 5000; // 5 segundos

    public ClienteHTTP(String usuario, String senha, String url) throws Exception {
        this.usuario = usuario;
        this.senha = senha;
        this.url = url;
        prepararParametros();
    }

    private void prepararParametros() throws Exception {
        this.urlParameters = "usuario=" + URLEncoder.encode(usuario, "UTF-8")
                           + "&senha=" + URLEncoder.encode(senha, "UTF-8");
    }

    public String conecta() throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Configurações básicas
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        con.setDoOutput(true);

        // Envia os dados POST
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        // Captura código de retorno HTTP
        codRetorno = con.getResponseCode();

        // Lê resposta (ou mensagem de erro)
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        codRetorno >= 400 ? con.getErrorStream() : con.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = in.readLine()) != null) {
                sb.append(linha).append("\n");
            }
            resposta = sb.toString().trim();
        }

        return resposta;
    }

    public int getCodRetorno() {
        return codRetorno;
    }

    public String getResposta() {
        return resposta;
    }
}
