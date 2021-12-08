package ptl.ajneb97.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeAccuracy {

    DAYS(1),
    HOURS(2),
    MINUTES(3),
    SECONDS(4);

    private final int weight;

    public static TimeAccuracy getByKey(String key) {
        if(key == null)
            return SECONDS;

        try {
            return TimeAccuracy.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return SECONDS;
        }
    }

    public boolean isMoreAccuratelyThan(TimeAccuracy other) {
        return this.weight > other.weight;
    }

    public long roundAmount(TimeAccuracy accuracy, long amount) {
        if(isMoreAccuratelyThan(accuracy))
            return amount;

        if(this.weight == accuracy.weight)
            return Math.max(amount, 1L);

        return 0L;
    }

}
