import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public class Main extends Application {
    private static ResourceBundle rb;

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
        conversions.add("Fahrenheit -> Celsius");
        conversions.add("Celsius -> Fahrenheit");
        conversions.add("Kelvin -> Celsius");
        conversionComboBox.getItems().addAll(conversions);

        Button calcButton = new Button(rb.getString("button_calculate_and_save"));
        resultLabel = new Label();

        form.add(new Label(rb.getString("label_input")), 0, 0);
        form.add(inputTextField, 1, 0);
        form.add(new Label(rb.getString("label_conversion")), 0, 1);
        form.add(conversionComboBox, 1, 1);
        form.add(calcButton, 1, 2);
        form.add(resultLabel, 1, 3);

        tableView = buildTableView();
        loadRecords();

        calcButton.setOnAction(e -> handleCalculateAndSave());

        VBox root = new VBox(15, form, new Label(rb.getString("label_saved_records")), tableView);
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
                showError(rb.getString("error_please_select_a_conversion"));
                return;
            }

            double result = 0.0;
            switch (conversion) {
                case "Fahrenheit -> Celsius":
                    result = TemperatureConverter.fahrenheitToCelsius(input);
                    break;
                case "Celsius -> Fahrenheit":
                    result = TemperatureConverter.celsiusToFahrenheit(input);
                    break;
                case "Kelvin -> Celsius":
                    result = TemperatureConverter.kelvinToCelsius(input);
                    break;
            }

            resultLabel.setText(String.format(rb.getString("label_result"), result));
            temperatureRecordDAO.save(new TemperatureRecord(input, result, conversion));
            loadRecords();
        } catch (Exception e) {
            showError(rb.getString("error_calculating") + e.getMessage());
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
            for (var item : records) {
                System.out.println(item.toString());
            }

            tableView.getItems().setAll(records);
            for (var item : tableView.getItems()) {
                System.out.println(item.toString());
            }
        } catch (SQLException e) {
            showError(rb.getString("error_failed_to_load_records") + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a language");
        System.out.println("1: English");
        System.out.println("2: Finnish");
        System.out.println("3: Japanese");
        System.out.println("4: Swedish");

        Locale locale;
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                locale = new Locale("en", "us");
                break;
            case 2:
                locale = new Locale("fi", "fi");
                break;
            case 3:
                locale = new Locale("ja", "jp");
                break;
            case 4:
                locale = new Locale("sv", "se");
                break;
            default:
                System.out.println("Invalid choice, using default language");
                locale = new Locale("en", "us");
        }

        rb = ResourceBundle.getBundle("MessageBundle", locale);
        launch(args);
    }
}