import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
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

    private VBox root = new VBox();
    private TextField inputTextField;
    private ComboBox<String> conversionComboBox;
    private TableView<TemperatureRecord> tableView;
    private Label resultLabel = new Label();
    private Label inputLabel = new Label();
    private Label conversionLabel = new Label();
    private Label savedRecordsLabel = new Label();
    private Button calcButton = new Button();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Temperature Converter");

        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("English", "Finnish", "Japanese", "Swedish", "Arabic");
        langBox.setValue("English");
        langBox.setOnAction(e -> updateLanguage(langBox.getValue()));
        updateLanguage("English");

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

        calcButton = new Button(rb.getString("button_calculate_and_save"));
        resultLabel = new Label();

        inputLabel = new Label(rb.getString("label_input"));
        form.add(inputLabel, 0, 0);
        form.add(inputTextField, 1, 0);
        form.add(langBox, 2, 0);
        conversionLabel = new Label(rb.getString("label_conversion"));
        form.add(conversionLabel, 0, 1);
        form.add(conversionComboBox, 1, 1);
        form.add(calcButton, 1, 2);
        form.add(resultLabel, 1, 3);

        tableView = buildTableView();
        loadRecords();

        calcButton.setOnAction(e -> handleCalculateAndSave());

        savedRecordsLabel = new Label(rb.getString("label_saved_records"));
        root = new VBox(15, form, savedRecordsLabel, tableView);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);


        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

    private void updateLanguage(String choice) {
        Locale locale;
        switch (choice) {
            case "English":
                locale = new Locale("en", "us");
                break;
            case "Finnish":
                locale = new Locale("fi", "fi");
                break;
            case "Japanese":
                locale = new Locale("ja", "jp");
                break;
            case "Swedish":
                locale = new Locale("sv", "se");
                break;
            case "Arabic":
                locale = new Locale("ar", "sa");
                break;
            default:
                locale = Locale.getDefault();
                break;
        }
        rb = ResourceBundle.getBundle("MessageBundle", locale);
        resultLabel.setText("");
        inputLabel.setText(rb.getString("label_input"));
        conversionLabel.setText(rb.getString("label_conversion"));
        savedRecordsLabel.setText(rb.getString("label_saved_records"));
        calcButton.setText(rb.getString("button_calculate_and_save"));
        switch (rb.getLocale().toString()) {
            case "ar_SA":
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                break;
            default:
                root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
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
        launch(args);
    }
}