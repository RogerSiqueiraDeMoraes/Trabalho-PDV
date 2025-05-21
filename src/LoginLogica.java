import java.util.ArrayList;
import java.util.List;

public class LoginLogica {
    private List<Usuario> usuarios;

    public LoginLogica() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("supervisor", "s2d4j8", "supervisor"));
        usuarios.add(new Usuario("operador", "k9j5g6", "operador"));
    }

    public Usuario autenticar(String nome, String senha) {
        for (Usuario u : usuarios) {
            if (u.getNome().equals(nome) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }
}
