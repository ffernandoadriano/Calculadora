package lnandobr.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.NumberStringConverter;
import lnandobr.enums.Operation;
import lnandobr.model.Calculator;

/**
 * Controller do JavaFX
 */
public class LayoutController {
    @FXML
    private Label lblError;
    @FXML
    private Label lblResult;

    /**
     * Objeto que possui as regras de negócio
     */
    private final Calculator calculator = new Calculator();


    @FXML
    private void initialize() {
        lblResult.textProperty().bindBidirectional(calculator.currentValueProperty(), new NumberStringConverter(Calculator.NUMBER_FORMAT));
        lblError.visibleProperty().bind(calculator.errorProperty());
    }

    /**
     * O usuário digitou um número de 0 a 9 via botão
     */
    @FXML
    public void onNumber(ActionEvent event) {
        // Obtém o texto do botão pressionado para saber que número foi digitado
        Button button = (Button) event.getSource();
        //Quando você pressiona Enter após clicar em um botão, o evento de clique do botão pode continuar sendo disparado repetidamente enquanto o botão permanecer com foco.
        //Para resolver esse problema, você pode desfocar o botão após o clique para evitar que ele continue recebendo eventos de teclado.
        // Desfocar o botão após o clique
        button.getParent().requestFocus();
        int number = Integer.parseInt(button.getText());
        calculator.appendNumber(number);
    }

    /**
     * O usuário digitou via teclado
     */
    @FXML
    public void onKey(KeyEvent event) {
        // Obtém o que foi pressionado do teclado
        String input = event.getCharacter();
        handleKey(event, input);
    }

    /**
     * aplica a regra de acordo com o evento do teclado
     */

    private void handleKey(KeyEvent event, String input) {
        // Captura os números digitados no teclado [0-9]
        if (input.matches("\\d")) {
            int number = Integer.parseInt(input);
            calculator.appendNumber(number);

            // Captura as operações digitas no teclado [* / - +]
        } else if (input.matches("[/*\\-+]")) {
            input = input.replace("*", "X").replace("-", "—").replace("+", "➕").replace("/", "➗");
            Operation operation = Operation.fromSymbol(input.charAt(0));
            calculator.doOperation(operation);

            // Verifica se a tecla pressionada é Backspace
        } else if ("\b".equals(event.getCharacter())) {
            calculator.clear();

            // verifica se a tecla pressionada é [.]
        } else if (input.matches(".")) {
            // chama o método para indicar que tem uma vírgula
            onComma();

        } else {
            event.consume(); // Ignorar o evento
        }

        // retorna o foco para label resultado
        lblResult.requestFocus();
    }

    /**
     * O usuário solicitou a limpeza dos cálculos
     */
    @FXML
    public void onClear() {
        calculator.clear();
    }

    /**
     * O usuário digitou a vírgula
     */
    @FXML
    public void onComma() {
        calculator.appendComma();
    }

    /**
     * O usuário digitou uma operação
     */
    @FXML
    public void onOperation(ActionEvent event) {
        // Obtém o texto do botão pressionado para saber que operação foi escolhida
        Button button = (Button) event.getSource();
        Operation operation = Operation.fromSymbol(button.getText().charAt(0));
        calculator.doOperation(operation);
    }
}
