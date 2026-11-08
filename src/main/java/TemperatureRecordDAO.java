import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TemperatureRecordDAO {
    public void save(TemperatureRecord temperatureRecord) throws SQLException {
        String sql = "INSERT INTO temperature_record (input, result, conversion_from_en_us, conversion_from_fi_fi, conversion_from_ja_jp, conversion_from_sv_se, conversion_from_ar_sa, conversion_to_en_us, conversion_to_fi_fi, conversion_to_ja_jp, conversion_to_sv_se, conversion_to_ar_sa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<String> unitsEnglish = List.of("Fahrenheit", "Kelvin", "Celsius");
        List<String> unitsArabic = List.of("فهرنهايت", "كلفن", "سيليزيوس");
        List<String> unitsJapanese = List.of("華氏", "ケルビン", "摂氏");

        int indexFrom = temperatureRecord.getConversionFrom().ordinal();
        int indexTo = temperatureRecord.getConversionTo().ordinal();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, temperatureRecord.getInput());
            preparedStatement.setDouble(2, temperatureRecord.getResult());
            preparedStatement.setString(3, unitsEnglish.get(indexFrom));
            preparedStatement.setString(4, unitsEnglish.get(indexFrom));
            preparedStatement.setString(5, unitsJapanese.get(indexFrom));
            preparedStatement.setString(6, unitsEnglish.get(indexFrom));
            preparedStatement.setString(7, unitsArabic.get(indexFrom));
            preparedStatement.setString(8, unitsEnglish.get(indexTo));
            preparedStatement.setString(9, unitsEnglish.get(indexTo));
            preparedStatement.setString(10, unitsJapanese.get(indexTo));
            preparedStatement.setString(11, unitsEnglish.get(indexTo));
            preparedStatement.setString(12, unitsArabic.get(indexTo));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private static TemperatureUnit getTemperatureUnitFromLocalized(String localized) {
        List<String> unitsEnglish = List.of("Fahrenheit", "Kelvin", "Celsius");
        List<String> unitsArabic = List.of("فهرنهايت", "كلفن", "سيليزيوس");
        List<String> unitsJapanese = List.of("華氏", "ケルビン", "摂氏");

        int englishIndex = unitsEnglish.indexOf(localized);
        int arabicIndex = unitsArabic.indexOf(localized);
        int japaneseIndex = unitsJapanese.indexOf(localized);

        if (englishIndex >= 0) {
            return TemperatureUnit.valueOf(unitsEnglish.get(englishIndex));
        } else if (arabicIndex >= 0) {
            return TemperatureUnit.valueOf(unitsEnglish.get(arabicIndex));
        } else if (japaneseIndex >= 0) {
            return TemperatureUnit.valueOf(unitsEnglish.get(japaneseIndex));
        }

        return null;
    }

    public List<TemperatureRecord> getAllRecords(String languageExtension) {
        List<TemperatureRecord> records = new ArrayList<>();
        String sql = "SELECT id, input, result, conversion_from_" + languageExtension + " as conversion_from, conversion_to_" + languageExtension + " as conversion_to FROM temperature_record";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                records.add(
                        new TemperatureRecord(
                                resultSet.getInt("id"),
                                resultSet.getFloat("input"),
                                resultSet.getFloat("result"),
                                getTemperatureUnitFromLocalized(resultSet.getString("conversion_from")),
                                getTemperatureUnitFromLocalized(resultSet.getString("conversion_to"))
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return records;
    }
}