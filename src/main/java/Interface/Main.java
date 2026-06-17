package Interface;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Aplica o tema global do AtlantaFX antes de carregar a cena
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Interface/TelaLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        if (getClass().getResource("/Interface/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());
        }

        stage.setTitle("Tela de Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}