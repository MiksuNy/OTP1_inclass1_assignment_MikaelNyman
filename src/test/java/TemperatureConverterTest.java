import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {
    @org.junit.jupiter.api.Test
    void convertTest() {
        assertEquals(32.0, TemperatureConverter.convert(TemperatureUnit.Celsius, TemperatureUnit.Fahrenheit, 0.0), 0.01);
        assertEquals(212.0, TemperatureConverter.convert(TemperatureUnit.Celsius, TemperatureUnit.Fahrenheit, 100.0), 0.01);
        assertEquals(273.15, TemperatureConverter.convert(TemperatureUnit.Celsius, TemperatureUnit.Kelvin, 0.0), 0.01);
        assertEquals(273.15, TemperatureConverter.convert(TemperatureUnit.Fahrenheit, TemperatureUnit.Kelvin, 32.0), 0.01);
        assertEquals(0.0, TemperatureConverter.convert(TemperatureUnit.Kelvin, TemperatureUnit.Celsius, 273.15), 0.01);
        assertEquals(32.0, TemperatureConverter.convert(TemperatureUnit.Kelvin, TemperatureUnit.Fahrenheit, 273.15), 0.01);
        assertEquals(25.0, TemperatureConverter.convert(TemperatureUnit.Celsius, TemperatureUnit.Celsius, 25.0), 0.01);
        assertEquals(75.0, TemperatureConverter.convert(TemperatureUnit.Fahrenheit, TemperatureUnit.Fahrenheit, 75.0), 0.01);
        assertEquals(300.0, TemperatureConverter.convert(TemperatureUnit.Kelvin, TemperatureUnit.Kelvin, 300.0), 0.01);
        assertEquals(0.0, TemperatureConverter.convert(null, TemperatureUnit.Celsius, 50.0), 0.01);
        assertEquals(0.0, TemperatureConverter.convert(TemperatureUnit.Celsius, null, 50.0), 0.01);
    }

    @org.junit.jupiter.api.Test
    void fahrenheitToCelsius() {
        double f1 = 123.0;
        double c1 = TemperatureConverter.fahrenheitToCelsius(f1);
        assertEquals(50.56, c1, 0.01);
        double f2 = 321.0;
        double c2 = TemperatureConverter.fahrenheitToCelsius(f2);
        assertEquals(160.56, c2, 0.01);
    }

    @org.junit.jupiter.api.Test
    void celsiusToFahrenheit() {
        double c1 = 123.0;
        double f1 = TemperatureConverter.celsiusToFahrenheit(c1);
        assertEquals(253.4, f1, 0.01);
        double c2 = 321.0;
        double f2 = TemperatureConverter.celsiusToFahrenheit(c2);
        assertEquals(609.8, f2, 0.01);
    }

    @org.junit.jupiter.api.Test
    void kelvinToCelsius() {
        double k = 300.0;
        double c = TemperatureConverter.kelvinToCelsius(k);
        assertEquals(26.85, c, 0.01);
    }

    @org.junit.jupiter.api.Test
    void isExtremeTemperature() {
        double c1 = 500.0;
        double c2 = 30.0;
        double c3 = -500.0;
        assertTrue(TemperatureConverter.isExtremeTemperature(c1));
        assertFalse(TemperatureConverter.isExtremeTemperature(c2));
        assertTrue(TemperatureConverter.isExtremeTemperature(c3));
    }
}