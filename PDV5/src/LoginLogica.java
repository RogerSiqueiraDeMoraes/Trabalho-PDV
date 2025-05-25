import java.util.ArrayList;
import java.util.List;

public class LoginLogica {
    private List<Usuario> usuarios;

    public LoginLogica() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("supervisor", "s2ddj8", "supervisor"));
        usuarios.add(new Usuario("operador", "k9j5g6", "operador"));
    }

    public Usuario autenticar(String nome, String senha) {
        // Validações básicas
        if (nome == null || senha == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getNome().equals(nome)) {
                if (usuario.getSenha().equals(senha)) {
                    return usuario;
                }
                return null; // Senha incorreta
            }
        }
        return null; // Usuário não encontrado
    }
}