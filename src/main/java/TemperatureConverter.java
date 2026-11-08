public class TemperatureConverter {
    public static double convert(TemperatureUnit from, TemperatureUnit to, double value) {
        if (from == null || to == null) {
            return 0.0;
        }

        switch (from) {
            case Fahrenheit:
                value = fahrenheitToCelsius(value);
                break;
            case Kelvin:
                value = kelvinToCelsius(value);
                break;
        }

        switch (to) {
            case Fahrenheit:
                return celsiusToFahrenheit(value);
            case Kelvin:
                return celsiusToKelvin(value);
            case Celsius:
                return value;
            default:
                return 0.0;
        }
    }

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    public static double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static boolean isExtremeTemperature(double celsius) {
        return celsius < -40.0 || celsius > 50.0;
    }
}