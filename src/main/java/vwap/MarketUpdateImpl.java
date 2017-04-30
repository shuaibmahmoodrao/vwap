package vwap;

public class MarketUpdateImpl implements MarketUpdate{

    private final Market market;
    private final TwoWayPrice twoWayPrice;

    public MarketUpdateImpl(Market market, TwoWayPrice twoWayPrice) {
        this.market = market;
        this.twoWayPrice = twoWayPrice;
    }

    public Market getMarket() {
        return market;
    }

    public TwoWayPrice getTwoWayPrice() {
        return twoWayPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketUpdateImpl that = (MarketUpdateImpl) o;

        return market == that.market;
    }

    @Override
    public int hashCode() {
        return market.hashCode();
    }
}
