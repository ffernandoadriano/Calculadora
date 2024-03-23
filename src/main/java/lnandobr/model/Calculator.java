package lnandobr.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lnandobr.enums.Operation;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Classe com as regras de negócio da aplicação
 */
public class Calculator {
    /**
     * Constante para o número máximo de dígitos suportados pela calculadora
     */
    private static final int MAX_LENGTH = 10; //12

    /**
     * Constante para o número máximo de casas decimais suportadas pela calculadora
     */
    private static final int MAX_FRACTION_DIGITS = 4;

    /**
     * Objeto para formatação do número
     */
    public static final NumberFormat NUMBER_FORMAT;

    static {
        // Usa o Locale BR para usar "," no lugar de "." na formatação
        NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.of("pt", "BR"));

        // O número mínimo de casas decimais é 0
        NUMBER_FORMAT.setMinimumFractionDigits(0);

        // significa que as casas decimais não serão arredondadas, mas sim truncadas para baixo.
        NUMBER_FORMAT.setRoundingMode(RoundingMode.DOWN);

        // Número máximo de casas decimais
        NUMBER_FORMAT.setMaximumFractionDigits(MAX_FRACTION_DIGITS);

        // (Desativa/Ativa) o separador de milhar
        NUMBER_FORMAT.setGroupingUsed(true);
    }

    /**
     * Valor atual mostrado no visor
     */
    private DoubleProperty currentValue = new SimpleDoubleProperty();

    /**
     * Indica se o valor do visor excedeu o máximo de dígitos que podem ser exibidos
     */
    private BooleanProperty error = new SimpleBooleanProperty();

    /**
     * Valor gravado para fazer parte de uma operação
     */
    private double savedValue;

    /**
     * Indica se a vírgula deve ser adicionada quando o próximo número for digitado
     */
    private boolean appendComma;

    /**
     * Armazena a operação corrente, que deve ser realizada quando o '=' for pressionado
     */
    private Operation currenteOperation;

    /**
     * Indica se quando o próximo número for digitado o visor deve ser limpado antes
     */
    private boolean clearOnNextNumber;

    /**
     * Indica se uma operação foi aplicada
     */
    private boolean operationApplied;

    public double getCurrentValue() {
        return currentValue.get();
    }

    public DoubleProperty currentValueProperty() {
        return currentValue;
    }

    public void setCurrentValue(double result) {
        this.currentValue.set(result);
    }

    /**
     * Adiciona um número no visor
     */
    public void appendNumber(int number) {
        // Converte o que está sendo exibido em String
        String resultStr = getResultAsString();

        if (clearOnNextNumber) {
            // Se precisa limpar o visor, zera o resultado
            resultStr = "";

            if (operationApplied) {
                // Se uma operação foi aplicada, reinicia a calculadora
                clear();
            } else {
                // Se a operação não foi aplicada, limpa a flag de clearOnNextNumber
                clearOnNextNumber = false;
            }

            // modify == to US
        } else if (resultStr.length() >= MAX_LENGTH) {
            // Se já chegou no limite de caracteres, não faz nada
            return;
        }

        if (appendComma) {
            // Se a vírgula precisa ser adicionada, coloca ela na frente do número
            resultStr += "," + number; // trocar pra . se mudar pra Inglês

            // A vírgula não é mais necessária
            appendComma = false;
        } else {
            // Se não precisa adicionar a vírgula, concatena o novo número no final dos números que estão visíveis
            resultStr += number;
        }

        // Soluação para Locale pt-BR
        // remover ou comentar esse codigo se for mudar de idioma
        resultStr = resultStr.replace(".", "").replace(",", ".");

        // Define o valor corrente convertendo a String em double
        setCurrentValue(Double.parseDouble(resultStr));
    }

    /**
     * Adiciona a vírgula ao visor
     */
    public void appendComma() {
        // Converte o que está sendo exibido em String
        String resultStr = getResultAsString();

        // Se ainda não contém a vírgula, habilita a vírgula no próximo número digitado
        if (!resultStr.contains(",")) {
            appendComma = true;
        }
    }

    /**
     * Limpa o objeto
     */
    public void clear() {
        setCurrentValue(0);
        savedValue = 0;
        currenteOperation = null;
        appendComma = false;
        clearOnNextNumber = false;
        operationApplied = false;
        setError(false);
    }

    public void doOperation(Operation operation) {
        if (operation != Operation.EQUALS) {
            // Se não for a operação de '=', armazena a operação e o valor corrente
            currenteOperation = operation;
            clearOnNextNumber = true;
            savedValue = getCurrentValue();
            operationApplied = false;

        } else if (!operationApplied && currenteOperation != null) {
            // Se for a operação de '=' e existe uma operação a ser processada, faz o cálculo
            double value = currenteOperation.apply(savedValue, getCurrentValue());
            operationApplied = true;
            setCurrentValue(value);
            clearOnNextNumber = true;

            // Converte o que está sendo exibido em String
            String resultStr = getResultAsString();
            if (resultStr.length() > MAX_LENGTH) {
                // Se o resultado tem tamanho maior do que o máximo permitido, trunca o valor e exibe o indicativo de erro
                resultStr = resultStr.substring(0, MAX_LENGTH);

                // Soluação para Locale pt-BR
                // remover ou comentar esse codigo se for mudar de idioma
                resultStr = resultStr.replace(".", "").replace(",", ".");

                setCurrentValue(Double.parseDouble(resultStr));
                setError(true);
            }
        }
        appendComma = false;
    }

    /**
     * Converte o que está sendo exibido em String
     */
    private String getResultAsString() {
        return NUMBER_FORMAT.format(getCurrentValue());
    }

    public boolean isError() {
        return error.get();
    }

    public BooleanProperty errorProperty() {
        return error;
    }

    public void setError(boolean error) {
        this.error.set(error);
    }
}
