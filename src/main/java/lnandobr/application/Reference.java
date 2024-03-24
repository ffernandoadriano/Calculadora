package lnandobr.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * JavaFX Application
 */

public class Reference extends Application {

    /**
     * Método main()
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o layout
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Layout.fxml")));

        // adiciona um icone
        primaryStage.getIcons().add(new Image("/calculadora.png"));

        // Cria a cena
        Scene scene = new Scene(root, 330, 440);

        // Adiciona o arquivo CSS à cena
        scene.getStylesheets().add("/style.css");

        // Define parâmetros da janela e inicia
        primaryStage.setTitle("Calculadora");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}