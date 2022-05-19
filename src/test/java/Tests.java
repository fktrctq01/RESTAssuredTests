import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.orderbook.entity.MarketDataSnapshot;
import ru.orderbook.entity.Order;
import ru.orderbook.service.RestAPI;

import static org.junit.Assert.assertEquals;

public class Tests {

    RestAPI restAPI = new RestAPI();

    @Before
    public void before(){
        System.out.println("Starting a test run");
        restAPI.doCleanRequest();
    }

    @After
    public void after(){
        restAPI.doCleanRequest();
        System.out.println("End of test execution");
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #1")
    @Severity(SeverityLevel.BLOCKER)
    @Test()
    public void test_createOrder_checkStatusCode() {
        Order order = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();

        Response response = restAPI.doCreateOrderRequest(order);
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #2")
    @Severity(SeverityLevel.BLOCKER)
    @Test()
    public void test_createOrder_checkBody() {
        Order order = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        Response response = restAPI.doCreateOrderRequest(order);

        Order createdOrder_2 = new Order(response);
        assertEquals("1", createdOrder_2.getId());
        assertEquals("100", createdOrder_2.getPrice());
        assertEquals("10", createdOrder_2.getQuantity());
        assertEquals("sell", createdOrder_2.getSide());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #3")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getEmptyMarketData_checkStatusCode() {
        Response response = restAPI.doGetMarkedDataRequest();
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #4")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getEmptyMarketData_checkBody() {
        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        assertEquals(0, marketData.getAsks().size());
        assertEquals(0, marketData.getBids().size());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #5")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getFillMarketData_checkStatusCode() {
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);
        Order order_2 = Order.builder()
                .id("2")
                .price("200")
                .quantity("20")
                .side("Buy")
                .build();
        restAPI.doCreateOrderRequest(order_2);

        Response response = restAPI.doGetMarkedDataRequest();
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #6")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getFillMarketData_checkBody() {
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);
        Order order_2 = Order.builder()
                .id("2")
                .price("200")
                .quantity("20")
                .side("Buy")
                .build();
        restAPI.doCreateOrderRequest(order_2);

        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        assertEquals(1, marketData.getAsks().size());
        assertEquals("200", marketData.getAsks().get(0).getPrice());
        assertEquals("20", marketData.getAsks().get(0).getQuantity());
        assertEquals(1, marketData.getBids().size());
        assertEquals("100", marketData.getBids().get(0).getPrice());
        assertEquals("10", marketData.getBids().get(0).getQuantity());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #7")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_cleanOrderBook_checkStatusCode() {
        Response response = restAPI.doCleanRequest();
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #8")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_cleanOrderBook() {
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);

        restAPI.doCleanRequest();

        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        assertEquals(0, marketData.getAsks().size());
        assertEquals(0, marketData.getBids().size());
    }

    @Epic("Testing of order books")
    @Feature(value = "CASE #9")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_deleteOrder_checkStatusCode() {
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);

        Response responseToDelOrder = restAPI.doDeleteRequest("1");
        assertEquals(200, responseToDelOrder.getStatusCode());
    }
}