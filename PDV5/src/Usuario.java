public class Usuario {
    private final String nome;       // final para imutabilidade
    private final String senha;      // final para imutabilidade
    private final String tipo;       // final para imutabilidade

    public Usuario(String nome, String senha, String tipo) {
        // Validações básicas
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha inválida");
        }
        if (!tipo.equals("supervisor") && !tipo.equals("operador")) {
            throw new IllegalArgumentException("Tipo deve ser 'supervisor' ou 'operador'");
        }

        this.nome = nome;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters básicos GETTERS são métodos (funções) que retornam o valor de um atributo privado ou protegido de uma classe.
    public String getNome() { return nome; }
    public String getSenha() { return senha; }
    public String getTipo() { return tipo; }
}