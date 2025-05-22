import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;        //Bibliotecas utilizadas//
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void  start(Stage stage) {
		ImageView IconeCarrinho = new ImageView(new Image("carrinho.png"));      //icone do carrinho
		IconeCarrinho.setFitWidth(40);
		IconeCarrinho.setFitHeight(40);
		
		Label loginLabel = new Label("Login");
		loginLabel.setFont(Font.font("BoldItalic",20));          //Cria o titulo de login com fonte maior
		loginLabel.setStyle("-fx-font-weight: bold;");
		
		TextField textoUsuario = new TextField();       //Campo de texto para o Usuario
		textoUsuario.setPromptText("Usuario");
		textoUsuario.getStyleClass().add("texto-usuario");
		
		PasswordField senhaOculta = new PasswordField();     //Campo de senha com os caracteres ocultados
		senhaOculta.setPromptText("Senha");
		senhaOculta.getStyleClass().add("senha-oculta");
		
		CheckBox mostrarSenha = new CheckBox("Mostrar senha");               //mostra os caracteres ocultados
		TextField senhaVisivel = new TextField();
		senhaVisivel.setManaged(false);
		senhaVisivel.setVisible(false);
		
		//Alterna entre o visivel e o oculto
		senhaVisivel.managedProperty().bind(mostrarSenha.selectedProperty());
		senhaVisivel.visibleProperty().bind(mostrarSenha.selectedProperty());
		
		senhaOculta.managedProperty().bind(mostrarSenha.selectedProperty().not());
		senhaOculta.visibleProperty().bind(mostrarSenha.selectedProperty().not());
		
		senhaVisivel.textProperty().bindBidirectional(senhaOculta.textProperty());
		
		Button BotaoLogin = new Button("Entrar");           //Um botao "Entrar"
		BotaoLogin.setPrefWidth(200);
		BotaoLogin.getStyleClass().add("botao-login");
		BotaoLogin.setOnAction(e -> {
		    String usuario = textoUsuario.getText();
		    String senha = senhaOculta.getText();

		    LoginLogica loginService = new LoginLogica();
		    Usuario user = loginService.autenticar(usuario, senha);

		    if (user != null) {
		        if (user.getTipo().equals("supervisor")) {
		            System.out.println("Login como SUPERVISOR bem-sucedido!");
		        } else if (user.getTipo().equals("operador")) {
		            System.out.println("Login como OPERADOR bem-sucedido!");
		        }
		    } else {
		        System.out.println("Usuário ou senha incorretos.");
		    }
		});

		
		Label link = new Label("Esqueceu sua senha? Clique aqui!");
		link.setStyle("-fx-text-fill: #2c7be5; -fx-underline: true;");    //texto azul simluando um link
		
		VBox loginBox = new VBox(10, IconeCarrinho, loginLabel, textoUsuario, senhaOculta, senhaVisivel, mostrarSenha, BotaoLogin, link);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.setPadding(new Insets(20));
		loginBox.getStyleClass().add("login-card");
		
		//Plano de fundo
		StackPane fundo = new StackPane();
		fundo.getChildren().add(loginBox);
		fundo.getStyleClass().add("background");
		
		Scene cena = new Scene(fundo, 800, 500);
		cena.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		stage.setScene(cena);
		stage.setTitle("Tela de Login Mercado");
		stage.show(); 
	}
		
		public static void main(String[] args) {
			launch(args);
		}
	
	
	}
