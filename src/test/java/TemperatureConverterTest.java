import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

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
    void isExtremeTemperature() {
        double c1 = 500.0;
        double c2 = 30.0;
        double c3 = -50.0;
        assertTrue(TemperatureConverter.isExtremeTemperature(c1));
        assertFalse(TemperatureConverter.isExtremeTemperature(c2));
        assertTrue(TemperatureConverter.isExtremeTemperature(c3));
    }
}