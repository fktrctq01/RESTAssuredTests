package ru.orderbook.entity;

import io.restassured.response.Response;
import lombok.*;

import static org.junit.Assert.fail;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
/**
 * An order consists of instructions to buy or sell a security
 * The order contains the following parameters: Price, Quantity, Side
 */
public class Order {

    public Order(Response response ) {
        try {
            if (response.getStatusCode() != 200)
                throw new Exception("Статусный код ответа оличный от 200");
            id = response.jsonPath().getString("id");
            price = response.jsonPath().getString("price");
            quantity = response.jsonPath().getString("quantity");
            side = response.jsonPath().getString("side");
        } catch (Exception ex){
            fail(ex.getMessage());
        }
    }

    /**
     * optional param (integer/string, 10000 > id > 0)
     */
    private String id;

    /**
     * optional param (double/string, 10000 > price > 0, precision: two decimal places)
     */
    private String price;

    /**
     * required param (long/string, 10000 > quantity > 0)
     */
    private String quantity;

    /**
     * side - required param (string - Buy or Sell)
     */
    private String side;

    @Override
    public String toString() {
        return String.format("{\"id\":%s,\"price\":%s,\"quantity\":\"%s\",\"side\":\"%s\"}",
                id == null ? "null" : "\"" + id + "\"",
                price == null ? "null" : "\"" + price + "\"",
                quantity,
                side);
    }
}