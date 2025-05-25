import javafx.application.Application;
import javafx.application.Platform; // Importar Platform para fechar a aplicação FX
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import javax.swing.SwingUtilities; // Importar SwingUtilities

import application.PDVMercado; // Importar a classe do PDV

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ImageView IconeCarrinho = new ImageView(new Image("carrinho.png"));
        IconeCarrinho.setFitWidth(40);
        IconeCarrinho.setFitHeight(40);

        Label loginLabel = new Label("Login");
        loginLabel.setFont(Font.font("BoldItalic", 20));
        loginLabel.setStyle("-fx-font-weight: bold;");

        TextField textoUsuario = new TextField();
        textoUsuario.setPromptText("Usuario");
        textoUsuario.getStyleClass().add("texto-usuario");

        PasswordField senhaOculta = new PasswordField();
        senhaOculta.setPromptText("Senha");
        senhaOculta.getStyleClass().add("senha-oculta");

        CheckBox mostrarSenha = new CheckBox("Mostrar senha");
        TextField senhaVisivel = new TextField();
        senhaVisivel.setManaged(false);
        senhaVisivel.setVisible(false);

        senhaVisivel.managedProperty().bind(mostrarSenha.selectedProperty());
        senhaVisivel.visibleProperty().bind(mostrarSenha.selectedProperty());
        senhaOculta.managedProperty().bind(mostrarSenha.selectedProperty().not());
        senhaOculta.visibleProperty().bind(mostrarSenha.selectedProperty().not());
        senhaVisivel.textProperty().bindBidirectional(senhaOculta.textProperty());

        Button BotaoLogin = new Button("Entrar");
        BotaoLogin.setPrefWidth(200);
        BotaoLogin.getStyleClass().add("botao-login");


        BotaoLogin.setOnAction(e -> {
            String usuario = textoUsuario.getText();
            String senha = senhaOculta.getText();

            LoginLogica loginService = new LoginLogica();
            Usuario user = loginService.autenticar(usuario, senha);

            if (user != null) {
                System.out.println("Login como " + user.getTipo().toUpperCase() + " bem-sucedido!");

                // Fechar a janela de login
                stage.close();

                // Abrir a janela do PDV na thread correta
                SwingUtilities.invokeLater(() -> {
                    PDVMercado pdv = new PDVMercado();
                    pdv.setVisible(true);
                });

            } else {
                // Exibir uma mensagem de erro na interface 
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de Login");
                alert.setHeaderText(null);
                alert.setContentText("Usuário ou senha incorretos.");
                alert.showAndWait();
                System.out.println("Usuário ou senha incorretos.");
            }
        });
   

        Label link = new Label("Esqueceu sua senha? Clique aqui!");
        link.setStyle("-fx-text-fill: #2c7be5; -fx-underline: true;");

        VBox loginBox = new VBox(10, IconeCarrinho, loginLabel, textoUsuario, senhaOculta, senhaVisivel, mostrarSenha, BotaoLogin, link);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.getStyleClass().add("login-card");

        StackPane fundo = new StackPane();
        fundo.getChildren().add(loginBox);
        fundo.getStyleClass().add("background");

        Scene cena = new Scene(fundo, 800, 500);

        String cssPath = getClass().getResource("style.css").toExternalForm();
        if (cssPath != null) {
            cena.getStylesheets().add(cssPath);
        } else {
            System.err.println("Não foi possível encontrar o arquivo style.css!");
        }

        stage.setScene(cena);
        stage.setTitle("Tela de Login Mercado");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}