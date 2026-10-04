import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemperatureRecordDAO {
    public void save(TemperatureRecord temperatureRecord) throws SQLException {
        String sql = "INSERT INTO temperature_record (input, result, conversion) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, temperatureRecord.getInput());
            preparedStatement.setDouble(2, temperatureRecord.getResult());
            preparedStatement.setString(3, temperatureRecord.getConversion());
        }
    }

    public List<TemperatureRecord> getAllRecords() throws SQLException {
        List<TemperatureRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM temperature_record";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                records.add(
                        new TemperatureRecord(
                                resultSet.getInt("id"),
                                resultSet.getFloat("input"),
                                resultSet.getFloat("result"),
                                resultSet.getString("conversion")
                        )
                );
            }
        }

        return records;
    }
}