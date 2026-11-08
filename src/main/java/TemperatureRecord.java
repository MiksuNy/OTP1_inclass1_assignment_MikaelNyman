public class TemperatureRecord {
    private int id;
    private double input;
    private double result;
    private TemperatureUnit conversionFrom;
    private TemperatureUnit conversionTo;

    public TemperatureRecord(double input, double result, TemperatureUnit conversionFrom, TemperatureUnit conversionTo) {
        this.input = input;
        this.result = result;
        this.conversionFrom = conversionFrom;
        this.conversionTo = conversionTo;
    }

    public TemperatureRecord(int id, double input, double result, TemperatureUnit conversionFrom, TemperatureUnit conversionTo) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.conversionFrom = conversionFrom;
        this.conversionTo = conversionTo;
    }

    public int getId() {
        return id;
    }

    public double getInput() {
        return input;
    }

    public double getResult() {
        return result;
    }

    public TemperatureUnit getConversionFrom() {
        return conversionFrom;
    }

    public TemperatureUnit getConversionTo() {
        return conversionTo;
    }
}