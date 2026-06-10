package Interface;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {

    public static void exibir(Stage stagePai, String mensagem) {
        Stage toastStage = new Stage();
        toastStage.initOwner(stagePai);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        // Criando o texto da mensagem
        Text text = new Text(mensagem);
        text.setFont(Font.font("System", 14));
        text.setFill(Color.WHITE);

        // Container do Toast (Fundo escuro semi-transparente)
        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 15; " +
                "-fx-background-color: rgba(30, 30, 40, 0.85); " +
                "-fx-padding: 10px 20px;");

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);

        // Centraliza o Toast na parte inferior da tela do app
        toastStage.setOnShown(e -> {
            toastStage.setX(stagePai.getX() + (stagePai.getWidth() / 2) - (toastStage.getWidth() / 2));
            toastStage.setY(stagePai.getY() + stagePai.getHeight() - 120);
        });

        toastStage.show();

        // MÁGICA DA TRANSPARÊNCIA (Fade-out)
        // Criamos uma transição de fade que vai durar 800 milissegundos aplicada no container 'root'
        FadeTransition fade = new FadeTransition(Duration.millis(800), root);
        fade.setFromValue(1.0); // Começa totalmente opaco (100%)
        fade.setToValue(0.0);   // Vai até ficar invisível (0%)

        // Quando a animação de desaparecer terminar, aí sim fechamos o Stage de verdade
        fade.setOnFinished(ae -> toastStage.close());

        // LINHA DO TEMPO: O Toast fica estático por 1.5 segundos e depois engata o fade-out
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ae -> fade.play()));
        timeline.play();
    }
}