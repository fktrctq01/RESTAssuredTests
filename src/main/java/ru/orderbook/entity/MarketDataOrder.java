package ru.orderbook.entity;

import lombok.Data;
import lombok.NonNull;

@Data
/**
 * An order consists of instructions to buy or sell a security
 */
public class MarketDataOrder {
        /**
         * required param (double/string, 10000 > price > 0, precision: two decimal places)
         */
        @NonNull
        private String price;

        /**
         * required param (long/string, 10000 > quantity > 0)
         */
        @NonNull
        private String quantity;
}
