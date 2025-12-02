package br.classesimportantes.tratajson;

import br.classesimportantes.pessoas.pessoa;

public class trataJSON {
    private String json;

    public trataJSON(String json) {
        this.json = json;
    }

    public pessoa tratarString() {
        pessoa p = new pessoa(); 
        
        // 1. Tenta extrair a mensagem (msg)
        String msgReal = "Processado";
        if(json.contains("\"msg\":")) {
            try {
                // Lógica simples para pegar o texto entre aspas depois de "msg":
                String[] partes = json.split("\"msg\":");
                if (partes.length > 1) {
                    String resto = partes[1].trim();
                    // Remove aspas iniciais e finais do valor
                    if(resto.startsWith("\"")) resto = resto.substring(1);
                    int fim = resto.indexOf("\"");
                    if(fim > 0) msgReal = resto.substring(0, fim);
                }
            } catch (Exception e) { msgReal = "Erro ao ler msg"; }
        }
        p.setRetmsg(msgReal);

        // 2. Identifica o Modo (PIX, BOLETO, CARTAO ou ERRO)
        // Ignora maiúsculas/minúsculas para ser robusto
        String jsonUpper = json.toUpperCase();
        
        if (jsonUpper.contains("PIX")) {
            p.setModo("PIX"); 
        } else if (jsonUpper.contains("CARTAO")) {
            p.setModo("CARTAO"); 
        } else if (jsonUpper.contains("BOLETO")) {
            p.setModo("BOLETO"); 
        } else {
            // Se não achou nenhuma palavra chave, é erro
            p.setModo("ERRO");
        }
        
        // 3. ID
        p.setId("REQ-" + System.currentTimeMillis());
        
        return p;
    }
}