package ru.orderbook.entity;

import io.restassured.response.Response;
import lombok.*;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

@Getter
@ToString
@EqualsAndHashCode
/**
 * Snapshot of marketdata(an order book contains the list of buy orders and the list of sell orders)
 */
public class MarketDataSnapshot {

    public MarketDataSnapshot(Response response ) {
        try {
            List asksList = response.jsonPath().getList("asks");
            List bidsList = response.jsonPath().getList("bids");

            for (int i = 0; i < asksList.size(); i++) {
                Map order = (Map) asksList.get(i);
                asks.add(new MarketDataOrder(order.get("price").toString(),  order.get("quantity").toString()));
            }

            for (int i = 0; i < bidsList.size(); i++) {
                Map order = (Map) bidsList.get(i);
                bids.add(new MarketDataOrder(order.get("price").toString(),  order.get("quantity").toString()));
            }
        } catch (JSONException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * An ask is the price sellers are asking for by selling  instrument
     */
    private List<MarketDataOrder> asks = new ArrayList<>();

    /**
     * A bid is the price buyers are bidding to buy
     */
    private List<MarketDataOrder> bids = new ArrayList<>();

}