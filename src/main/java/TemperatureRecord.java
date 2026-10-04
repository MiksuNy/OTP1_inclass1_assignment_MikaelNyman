public class TemperatureRecord {
    private int id;
    private double input;
    private double result;
    private String conversion;

    public TemperatureRecord(double input, double result, String conversion) {
        this.input = input;
        this.result = result;
        this.conversion = conversion;
    }

    public TemperatureRecord(int id, double input, double result, String conversion) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.conversion = conversion;
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

    public String getConversion() {
        return conversion;
    }
}