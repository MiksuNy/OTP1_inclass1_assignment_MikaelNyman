import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {
    @org.junit.jupiter.api.Test
    void fahrenheitToCelsius() {
        var tc = new TemperatureConverter();
        double f1 = 123.0;
        double c1 = tc.fahrenheitToCelsius(f1);
        assertEquals(50.56, c1, 0.01);
        double f2 = 321.0;
        double c2 = tc.fahrenheitToCelsius(f2);
        assertEquals(160.56, c2, 0.01);
    }

    @org.junit.jupiter.api.Test
    void celsiusToFahrenheit() {
        var tc = new TemperatureConverter();
        double c1 = 123.0;
        double f1 = tc.celsiusToFahrenheit(c1);
        assertEquals(253.4, f1, 0.01);
        double c2 = 321.0;
        double f2 = tc.celsiusToFahrenheit(c2);
        assertEquals(609.8, f2, 0.01);
    }

    @org.junit.jupiter.api.Test
    void isExtremeTemperature() {
        var tc = new TemperatureConverter();
        double c1 = 500.0;
        double c2 = 30.0;
        double c3 = -50.0;
        assertTrue(tc.isExtremeTemperature(c1));
        assertFalse(tc.isExtremeTemperature(c2));
        assertTrue(tc.isExtremeTemperature(c3));
    }
}