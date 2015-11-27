package rv.achart.range.objects;

/**
 * Created by Rui Vieira on 25/11/2015.
 */
public class CurrencyPairRate {

    private String high;
    private String low;

    public CurrencyPairRate(String high, String low) {
        this.high = high;
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }
}
