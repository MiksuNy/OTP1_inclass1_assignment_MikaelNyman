import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private final TemperatureRecordDAO temperatureRecordDAO = new TemperatureRecordDAO();

    private TextField inputTextField;
    private ComboBox<String> conversionComboBox;
    private Label resultLabel;
    private TableView<TemperatureRecord> tableView;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Temperature Converter");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        inputTextField = new TextField();
        conversionComboBox = new ComboBox<>();
        List<String> conversions = new ArrayList<>();
        conversions.add("Fahrenheit to Celsius");
        conversions.add("Celsius to Fahrenheit");
        conversions.add("Kelvin to Celsius");
        conversionComboBox.getItems().addAll(conversions);

        Button calcButton = new Button("Calculate & Save");
        resultLabel = new Label();

        form.add(new Label("Input:"), 0, 0);
        form.add(inputTextField, 1, 0);
        form.add(new Label("Conversion:"), 0, 1);
        form.add(conversionComboBox, 1, 1);
        form.add(calcButton, 1, 2);
        form.add(resultLabel, 1, 3);

        tableView = buildTableView();
        loadRecords();

        calcButton.setOnAction(e -> handleCalculateAndSave());

        VBox root = new VBox(15, form, new Label("Saved Records:"), tableView);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);

        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

    private void handleCalculateAndSave() {
        try {
            double input = Float.parseFloat(inputTextField.getText());
            String conversion = conversionComboBox.getValue();

            if (conversion == null) {
                showError("Please select a conversion");
                return;
            }

            double result = 0.0;
            switch (conversion) {
                case "Fahrenheit to Celsius":
                    result = TemperatureConverter.fahrenheitToCelsius(input);
                    break;
                case "Celsius to Fahrenheit":
                    result = TemperatureConverter.celsiusToFahrenheit(input);
                    break;
                case "Kelvin to Celsius":
                    result = TemperatureConverter.kelvinToCelsius(input);
                    break;
            }

            resultLabel.setText(String.format("Result: %.2f", result));
            temperatureRecordDAO.save(new TemperatureRecord(input, result, conversion));
        } catch (Exception e) {
            showError("Error calculating, " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    private TableView<TemperatureRecord> buildTableView() {
        TableView<TemperatureRecord> table = new TableView<>();

        TableColumn<TemperatureRecord, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));

        TableColumn<TemperatureRecord, Number> inputCol = new TableColumn<>("Input");
        inputCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getInput()));

        TableColumn<TemperatureRecord, Number> resultCol = new TableColumn<>("Result");
        resultCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getResult()));

        TableColumn<TemperatureRecord, String> conversionCol = new TableColumn<>("Conversion");
        conversionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getConversion()));

        table.getColumns().addAll(idCol, inputCol, resultCol, conversionCol);
        return table;
    }

    private void loadRecords() {
        try {
            List<TemperatureRecord> records = temperatureRecordDAO.getAllRecords();
            tableView.getItems().setAll(records);
        } catch (SQLException e) {
            showError("Failed to load records: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}