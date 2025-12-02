package br.classesimportantes.pessoas;

public class pessoa {
    
    String nome;
    String fone;
    String cpf;
    String id;
    String modo;
    String retmsg;
    String valor;

    // --- ADIÇÃO NECESSÁRIA 1: Construtor Vazio ---
    // Precisamos disso para o botão 'Limpar' e para o trataJSON criar o objeto
    public pessoa() {
    }

    // Seu construtor original
    public pessoa(String n, String f, String c, String i, String m, String r, String v) {
        this.nome = n;
        this.fone = f;
        this.cpf = c;
        this.id = i;
        this.modo = m;
        this.retmsg = r;
        this.valor = v;
    }

    // Seu método original
    public void setPessoa(String n, String f, String c, String i, String m, String r, String v) {
        this.nome = n;
        this.fone = f;
        this.cpf = c;
        this.id = i;
        this.modo = m;
        this.retmsg = r;
        this.valor = v;
    }

    // --- ADIÇÃO NECESSÁRIA 2: Setters Individuais ---
    // O sistema precisa preencher o ID que vem da API separado do Nome que vem da tela.
    public void setNome(String nome) { this.nome = nome; }
    public void setFone(String fone) { this.fone = fone; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setId(String id) { this.id = id; }
    public void setModo(String modo) { this.modo = modo; }
    public void setRetmsg(String retmsg) { this.retmsg = retmsg; }
    public void setValor(String valor) { this.valor = valor; }

    // Seus Getters originais
    public String getNome() {
        return this.nome;
    }

    public String getFone() {
        return this.fone;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getId() {
        return this.id;
    }

    public String getModo() {
        return this.modo;
    }

    public String getRetmsg() {
        return this.retmsg;
    }

    public String getValor() {
        return this.valor;
    }

}