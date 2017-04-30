package vwap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/*
  Assumed we only want FIRM prices
 */
public class CalculatorImpl implements Calculator {

    private static Logger LOGGER = LoggerFactory.getLogger(CalculatorImpl.class);

    private final Map<Instrument, Double[]> instrumentRunningTotal;

    public CalculatorImpl() {
        instrumentRunningTotal = new HashMap<>(20);
    }

    public TwoWayPrice applyMarketUpdate(MarketUpdate marketUpdate) {

        Instrument instrument = marketUpdate.getTwoWayPrice().getInstrument();

        instrumentRunningTotal.putIfAbsent(instrument, new Double[]{0d,0d,0d,0d});
        Double[] runningTotals = instrumentRunningTotal.get(instrument);
        if (marketUpdate.getTwoWayPrice().getState() == State.FIRM) {

            Double totalMarketBidValue = runningTotals[0];
            totalMarketBidValue = (marketUpdate.getTwoWayPrice().getBidPrice() * marketUpdate.getTwoWayPrice().getBidAmount()) + totalMarketBidValue;
            runningTotals[0] = totalMarketBidValue;

            Double totalMarketBidAmount = runningTotals[1];
            totalMarketBidAmount = marketUpdate.getTwoWayPrice().getBidAmount() + totalMarketBidAmount;
            runningTotals[1] = totalMarketBidAmount;

            Double totalMarketOfferValue = runningTotals[2];
            totalMarketOfferValue = (marketUpdate.getTwoWayPrice().getOfferPrice() * marketUpdate.getTwoWayPrice().getOfferAmount()) + totalMarketOfferValue;
            runningTotals[2] = totalMarketOfferValue;

            Double totalMarketOfferAmount = runningTotals[3];
            totalMarketOfferAmount = marketUpdate.getTwoWayPrice().getOfferAmount() + totalMarketOfferAmount;
            runningTotals[3] = totalMarketOfferAmount;

            Double vwapBid = totalMarketBidValue / totalMarketBidAmount;
            Double vwapOffer = totalMarketOfferValue / totalMarketOfferAmount;

            return new TwoWayPriceImpl(instrument, State.FIRM, vwapBid, totalMarketBidAmount, vwapOffer, totalMarketOfferAmount);

        }

        else {
            return new TwoWayPriceImpl(instrument, State.FIRM, runningTotals[0]/runningTotals[1], runningTotals[1], runningTotals[2]/runningTotals[3], runningTotals[3]);
        }


    }


}
