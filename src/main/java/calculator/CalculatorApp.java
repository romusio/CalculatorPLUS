package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CalculatorApp extends Application {

    private TextField display;
    private double num1 = 0;
    private String operator = "";
    private boolean start = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Современный калькулятор");
        primaryStage.setResizable(false);

        VBox root = createInterface();
        Scene scene = new Scene(root, 300, 400);

        scene.getStylesheets().add(getClass().getResource("/calculator/calculator.css").toExternalForm());
        root.setStyle("-fx-background-color: #1C1C1E;");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createInterface() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        display = new TextField("0");
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPrefHeight(80);
        display.setStyle("-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #1C1C1E;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: transparent;" +
                "-fx-padding: 10"
        );

        GridPane buttonGrid = createButtonGrid();

        vbox.getChildren().addAll(display, buttonGrid);
        return vbox;
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        String[][] buttonLabels = {
                {"C", "±", "%", "÷"},
                {"7", "8", "9", "x"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"0", "", ".", "="}
        };

        for (int row = 0; row < buttonLabels.length; row++) {
            for (int col = 0; col < buttonLabels[row].length; col++) {
                String label = buttonLabels[row][col];

                if (label.isEmpty()) continue;
                Button button = createButton(label);
                if (label.equals("0")) {
                    GridPane.setColumnSpan(button, 2);
                    button.setPrefWidth(150);
                } else {
                    button.setPrefWidth(70);
                }

                button.setPrefHeight(70);
                grid.add(button, col, row);
            }
        }
        return grid;
    }


    private Button createButton(String text) {
        Button button = new Button(text);
        button.setOnAction(e -> processInput(text));

        String baseStyle = "-fx-font-size: 24px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 35; " +
                "-fx-border-radius: 35; " +
                "-fx-cursor: hand;";

        if (isNumber(text) || text.equals(".")) {
            button.setStyle(baseStyle + "-fx-background-color: #505050;" +
                    "-fx-text-fill: white;");
        } else if (isOperator(text) || text.equals("=")) {
            button.setStyle(baseStyle + "-fx-background-color: #FF9F0A;"
                    + "-fx-text-fill: white;");
        } else {
            button.setStyle(baseStyle + "-fx-background-color: #D4D4D2 ;" +
                    "-fx-text-fill: black;");
        }

        return button;
    }

    private void processInput(String input) {
        if (isNumber(input)) {
            if (start) {
                display.setText(input);
                start = false;
            } else {
                display.setText(display.getText() + input);
            }
        } else if (input.equals(".")) {
            if (start) {
                display.setText("0.");
                start = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else if (input.equals("C")) {
            display.setText("0");
            start = true;
            num1 = 0;
            operator = "";
        } else if (input.equals("±")) {
            double current = Double.parseDouble(display.getText());
            display.setText(formatResult(-current));
        } else if (input.equals("%")) {
            double current = Double.parseDouble(display.getText());
            display.setText(formatResult(current / 100));
        } else if (isOperator(input)) {
            if (!operator.isEmpty() && !start) {
                calculate();
            }
            num1 = Double.parseDouble(display.getText());
            operator = input;
            start = true;
        } else if (input.equals("=")) {
            if (!operator.isEmpty()) {
                calculate();
                operator = "";
                start = true;
            }
        }
    }

    private void calculate() {
        double num2 = Double.parseDouble(display.getText());
        double result;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "x":
                result = num1 * num2;
                break;
            case "÷":
                result = num1 / num2;
                break;
            default:
                result = 0;
        }
        display.setText(formatResult(result));
        num1 = result;
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            return String.format("%.8f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
        }
    }

    private boolean isNumber(String text) {
        return text.matches("\\d");
    }

    private boolean isOperator(String text) {
        return text.matches("[+\\-x÷]");
    }
}
