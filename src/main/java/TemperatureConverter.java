public class TemperatureConverter {
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }
    
    public static boolean isExtremeTemperature(double celsius) {
        return celsius < -40.0 || celsius > 50.0;
    }
}