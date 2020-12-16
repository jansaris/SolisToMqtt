package solis2mqtt;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
public class SolisModel {
    private Optional<Integer> power = Optional.empty();
    private Optional<Double> yieldToday = Optional.empty();
    private Optional<Double> yieldTotal = Optional.empty();

    @Override
    public String toString() {
        return "SolisModel{"
                + "power=" + power
                + ",yieldToday=" + yieldToday
                + ",yieldTotal=" + yieldTotal
                + '}';
    }

    public String json() {
        ArrayList<String> values = new ArrayList<>();
        power.ifPresent(integer -> values.add("\"power\":" + integer));
        yieldToday.ifPresent(aDouble -> values.add("\"today\":" + aDouble));
        yieldTotal.ifPresent(aDouble -> values.add("\"total\":" + aDouble));
        return "{" + String.join(",", values) + "}";
    }
}
