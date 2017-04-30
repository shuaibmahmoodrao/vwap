package vwap;


import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class CalculatorTest {

    Calculator calculator;

    @Before
    public void setUp() {
        calculator = new CalculatorImpl();
    }

    @Test
    public void applyMarketUpdateCalculator() {
        applyMarketUpdateTest(calculator);
    }

    private void applyMarketUpdateTest(Calculator calculator) {

        MarketUpdate marketUpdate0 = new MarketUpdateImpl(Market.MARKET0, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, 1.0, 100, 2.0, 100));
        MarketUpdate marketUpdate00 = new MarketUpdateImpl(Market.MARKET0, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, 2.0, 200, 3.0, 200));
        MarketUpdate marketUpdate1 = new MarketUpdateImpl(Market.MARKET1, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, 2.0, 200, 3.0, 200));
        MarketUpdate marketUpdate2 = new MarketUpdateImpl(Market.MARKET2, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, 3.0, 300, 4.0, 300));
        MarketUpdate marketUpdate3 = new MarketUpdateImpl(Market.MARKET3, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.INDICATIVE, 3.0, 300, 4.0, 300));

        TwoWayPrice vwapTwoWayPrice = calculator.applyMarketUpdate(marketUpdate0);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(1.0));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(100.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(2.0));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(100.0));

        // apply same again
        vwapTwoWayPrice = this.calculator.applyMarketUpdate(marketUpdate0);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(1.0));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(200.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(2.0));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(200.0));

        // apply next
        vwapTwoWayPrice = calculator.applyMarketUpdate(marketUpdate00);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(1.5));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(400.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(2.5));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(400.0));


        // apply next
        vwapTwoWayPrice = calculator.applyMarketUpdate(marketUpdate1);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(1.6666666666666667));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(600.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(2.6666666666666665));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(600.0));

        // apply next
        vwapTwoWayPrice = calculator.applyMarketUpdate(marketUpdate2);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(2.111111111111111));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(900.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(3.111111111111111));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(900.0));

        // apply next Indicative
        vwapTwoWayPrice = calculator.applyMarketUpdate(marketUpdate3);
        assertNotNull(vwapTwoWayPrice);
        assertThat(vwapTwoWayPrice.getBidPrice(), is(2.111111111111111));
        assertThat(vwapTwoWayPrice.getBidAmount(), is(900.0));
        assertThat(vwapTwoWayPrice.getOfferPrice(), is(3.111111111111111));
        assertThat(vwapTwoWayPrice.getOfferAmount(), is(900.0));


    }

    @Test
    public void testExecTimesCalculator() {
        testExecTimes(calculator);
    }

    public void testExecTimes(Calculator calculator) {

        long start = System.nanoTime();
        Random random = new Random();
        int execs = 100000000;
        for (int i = 0; i < execs; i++) {
            calculator.applyMarketUpdate(new MarketUpdateImpl(
                    Market.values()[random.nextInt(Market.values().length - 1)],
                    new TwoWayPriceImpl(Instrument.values()[random.nextInt(Instrument.values().length - 1)], State.FIRM, 1.0 + i, 100 + i, 2.0 + i, 100 + i)));
        }

        long end = System.nanoTime();
        double avg = (end - start) / (double) execs;

        System.out.println("Average time taken to return vwap price: " + avg + "nanos");
    }


}