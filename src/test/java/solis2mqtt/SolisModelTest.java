package solis2mqtt;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SolisModelTest {

    @Test
    public void json() {
        SolisModel model = new SolisModel();
        model.setPower(Optional.of(123));
        model.setYieldToday(Optional.of(1.3));
        model.setYieldTotal(Optional.of(33.4));
        String expected = "{\"power\":123,\"today\":1.3,\"total\":33.4}";

        assertEquals(expected, model.json());
    }

    @Test
    public void jsonWithOnlyPower(){
        SolisModel model = new SolisModel();
        model.setPower(Optional.of(123));
        String expected = "{\"power\":123}";

        assertEquals(expected, model.json());
    }

    @Test
    public void jsonWithOnlyToday() {
        SolisModel model = new SolisModel();
        model.setYieldToday(Optional.of(1.3));
        String expected = "{\"today\":1.3}";

        assertEquals(expected, model.json());
    }

    @Test
    public void jsonWithOnlyTotal() {
        SolisModel model = new SolisModel();
        model.setYieldTotal(Optional.of(2.3));
        String expected = "{\"total\":2.3}";

        assertEquals(expected, model.json());
    }

    @Test
    public void jsonWithNothing() {
        SolisModel model = new SolisModel();
        String expected = "{}";

        assertEquals(expected, model.json());
    }
}