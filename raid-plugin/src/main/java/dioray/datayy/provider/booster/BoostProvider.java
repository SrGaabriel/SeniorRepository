package dioray.datayy.provider.booster;

public class BoostProvider {

    private static BoostProvider boostProvider;

    public static BoostProvider getInstance() {
        return boostProvider == null ? (boostProvider = new BoostProvider()) : boostProvider;
    }



}
