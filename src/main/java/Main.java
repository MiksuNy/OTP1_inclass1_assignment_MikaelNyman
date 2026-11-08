import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.*;

public class Main extends Application {
    private static ResourceBundle rb;

    private final TemperatureRecordDAO temperatureRecordDAO = new TemperatureRecordDAO();

    private VBox root = new VBox();
    private TextField inputTextField = new TextField();
    private ComboBox<TemperatureUnit> conversionFromComboBox = new ComboBox<>();
    private ComboBox<TemperatureUnit> conversionToComboBox = new ComboBox<>();
    private TableView<TemperatureRecord> tableView = new TableView<>();
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

        conversionFromComboBox = new ComboBox<>();
        conversionToComboBox = new ComboBox<>();
        conversionFromComboBox.setItems(FXCollections.observableArrayList(
               TemperatureUnit.Fahrenheit,
               TemperatureUnit.Kelvin,
               TemperatureUnit.Celsius
        ));
        conversionFromComboBox.setConverter(new StringConverter<TemperatureUnit>() {
            @Override
            public String toString(TemperatureUnit object) {
                return object != null ? rb.getString(object.toString().toLowerCase()) : "";
            }

            @Override
            public TemperatureUnit fromString(String string) {
                return null;
            }
        });
        conversionToComboBox.setItems(FXCollections.observableArrayList(
                TemperatureUnit.Fahrenheit,
                TemperatureUnit.Kelvin,
                TemperatureUnit.Celsius
        ));
        conversionToComboBox.setConverter(new StringConverter<TemperatureUnit>() {
            @Override
            public String toString(TemperatureUnit object) {
                return object != null ? rb.getString(object.toString().toLowerCase()) : "";
            }

            @Override
            public TemperatureUnit fromString(String string) {
                return null;
            }
        });

        calcButton = new Button(rb.getString("button_calculate_and_save"));

        resultLabel = new Label();
        inputLabel = new Label(rb.getString("label_input"));
        form.add(inputLabel, 0, 0);
        form.add(inputTextField, 1, 0);
        form.add(langBox, 2, 0);
        conversionLabel = new Label(rb.getString("label_conversion"));
        form.add(conversionLabel, 0, 1);
        form.add(conversionFromComboBox, 1, 1);
        form.add(conversionToComboBox, 1, 2);
        form.add(calcButton, 1, 3);
        form.add(resultLabel, 1, 4);

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

        TemperatureUnit currentFrom = conversionFromComboBox.getValue();
        TemperatureUnit currentTo = conversionToComboBox.getValue();

        conversionFromComboBox.setItems(FXCollections.observableArrayList(
                TemperatureUnit.Fahrenheit,
                TemperatureUnit.Kelvin,
                TemperatureUnit.Celsius
        ));
        conversionFromComboBox.setValue(currentFrom);

        conversionToComboBox.setItems(FXCollections.observableArrayList(
                TemperatureUnit.Fahrenheit,
                TemperatureUnit.Kelvin,
                TemperatureUnit.Celsius
        ));
        conversionToComboBox.setValue(currentTo);

        switch (rb.getLocale().toString()) {
            case "ar_SA":
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                break;
            default:
                root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        loadRecords();
    }

    private void handleCalculateAndSave() {
        try {
            double input = Float.parseFloat(inputTextField.getText());
            TemperatureUnit conversionFrom = conversionFromComboBox.getValue();
            TemperatureUnit conversionTo = conversionToComboBox.getValue();

            if (conversionFrom == null || conversionTo == null) {
                showError(rb.getString("error_please_select_a_conversion"));
                return;
            }

            double result = TemperatureConverter.convert(conversionFrom, conversionTo, input);

            resultLabel.setText(String.format(rb.getString("label_result"), result));

            temperatureRecordDAO.save(new TemperatureRecord(input, result, conversionFrom, conversionTo));
            loadRecords();
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

        TableColumn<TemperatureRecord, String> conversionFromCol = new TableColumn<>("Conversion From");
        conversionFromCol.setCellValueFactory(data -> {
            TemperatureUnit temperatureUnit = data.getValue().getConversionFrom();
            String unitString = temperatureUnit != null ? rb.getString(temperatureUnit.toString().toLowerCase()) : "";
            return new javafx.beans.property.SimpleStringProperty(unitString);
        });

        TableColumn<TemperatureRecord, String> conversionToCol = new TableColumn<>("Conversion To");
        conversionToCol.setCellValueFactory(data -> {
            TemperatureUnit temperatureUnit = data.getValue().getConversionTo();
            String unitString = temperatureUnit != null ? rb.getString(temperatureUnit.toString().toLowerCase()) : "";
            return new javafx.beans.property.SimpleStringProperty(unitString);
        });

        table.getColumns().addAll(idCol, inputCol, resultCol, conversionFromCol, conversionToCol);
        return table;
    }

    private void loadRecords() {
        try {
            List<TemperatureRecord> records = temperatureRecordDAO.getAllRecords(rb.getLocale().toString().toLowerCase());
            for (var item : records) {
                System.out.println(item.toString());
            }

            tableView.getItems().setAll(records);
            for (var item : tableView.getItems()) {
                System.out.println(item.toString());
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            showError(rb.getString("error_failed_to_load_records") + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}