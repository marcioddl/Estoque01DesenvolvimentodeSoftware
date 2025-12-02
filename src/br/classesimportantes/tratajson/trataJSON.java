package br.classesimportantes.tratajson;

import br.classesimportantes.pessoas.pessoa;

public class trataJSON {
    private String json;

    public trataJSON(String json) {
        this.json = json;
    }

    public pessoa tratarString() {
        pessoa p = new pessoa(); 
        
        // extrair a mensagem
        String msgReal = "Processado";
        if(json.contains("\"msg\":")) {
            try {
                // remover aspas da mensagem
                String[] partes = json.split("\"msg\":");
                if (partes.length > 1) {
                    String resto = partes[1].trim();                   
                    if(resto.startsWith("\"")) resto = resto.substring(1);
                    int fim = resto.indexOf("\"");
                    if(fim > 0) msgReal = resto.substring(0, fim);
                }
            } catch (Exception e) { msgReal = "Erro ao ler msg"; }
        }
        p.setRetmsg(msgReal);

        // Identifica o Modo
        String jsonUpper = json.toUpperCase();
        
        if (jsonUpper.contains("PIX")) {
            p.setModo("PIX"); 
        } else if (jsonUpper.contains("CARTAO")) {
            p.setModo("CARTAO"); 
        } else if (jsonUpper.contains("BOLETO")) {
            p.setModo("BOLETO"); 
        } else {
            p.setModo("ERRO");
        }
        
        // ID
        p.setId("REQ-" + System.currentTimeMillis());
        
        return p;
    }
}