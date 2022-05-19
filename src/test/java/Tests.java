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

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #1")
    @Description("Проверка статусного кода ответа при создании ордера")
    @Severity(SeverityLevel.BLOCKER)
    @Test()
    public void test_createOrder_checkStatusCode() {
        Order order = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();

        // Отправляем запрос на создание ордера и получаем  ответ
        Response response = restAPI.doCreateOrderRequest(order);

        // Проверяем, что ордер создан успешно
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #2")
    @Description("Проверка тела ответа при создании оредера")
    @Severity(SeverityLevel.BLOCKER)
    @Test()
    public void test_createOrder_checkBody() {
        Order order = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();

        // Отправляем запрос на создание ордера и получаем  ответ
        Response response = restAPI.doCreateOrderRequest(order);

        // Проверяем, что ордер создан успешно
        Order createdOrder_2 = new Order(response);
        assertEquals("1", createdOrder_2.getId());
        assertEquals("100", createdOrder_2.getPrice());
        assertEquals("10", createdOrder_2.getQuantity());
        assertEquals("sell", createdOrder_2.getSide());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #3")
    @Description("Проверка получения статусного кода при запросе пустой маркетдаты")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getEmptyMarketData_checkStatusCode() {
        // Отправляем запрос на получение маркетдаты
        Response response = restAPI.doGetMarkedDataRequest();

        // Проверяем код ответа
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #4")
    @Description("Проверка тела ответа при запросе пустой маркетдаты")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getEmptyMarketData_checkBody() {
        // Отправляем запрос на получение маркетдаты
        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        // Проверяем, что в теле ответа приходят пустые списка на продажу и покупку
        assertEquals(0, marketData.getAsks().size());
        assertEquals(0, marketData.getBids().size());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #5")
    @Description("Проверка статусного кода ответа при запросе НЕ пустой маркетдаты")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getFillMarketData_checkStatusCode() {
        // Создаем два отрера на покупку и продажу
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

        // Отправляем запрос на получение маркетдаты
        Response response = restAPI.doGetMarkedDataRequest();

        // Проверяем код ответа
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #6")
    @Description("Проверка тела ответа при запросе НЕ пустой маркетдаты")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_getFillMarketData_checkBody() {
        // Создаем два отрера на покупку и продажу
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

        // Отправляем запрос на получение маркетдаты
        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        //Проверяем, что все 2 заяки есть в снепшоте
        assertEquals(1, marketData.getAsks().size());
        assertEquals("200", marketData.getAsks().get(0).getPrice());
        assertEquals("20", marketData.getAsks().get(0).getQuantity());
        assertEquals(1, marketData.getBids().size());
        assertEquals("100", marketData.getBids().get(0).getPrice());
        assertEquals("10", marketData.getBids().get(0).getQuantity());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #7")
    @Description("Проверка статусного кода при запросе очистки стакана")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_cleanOrderBook_checkStatusCode() {
        // Отправляем запрос для очистки стакана
        Response response = restAPI.doCleanRequest();

        // Проверяем код ответа
        assertEquals(200, response.getStatusCode());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #8")
    @Description("Проверка удаления заявок при очистке стакана")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_cleanOrderBook() {
        // Создаем два ордера на покупку и продажу
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);

        // Отправляем запрос для очистки стакана
        restAPI.doCleanRequest();

        // Отправляем запрос на получение маркетдаты
        Response response = restAPI.doGetMarkedDataRequest();
        MarketDataSnapshot marketData = new MarketDataSnapshot(response);

        // Проверяем, что в теле ответа приходят пустые списка на продажу и покупку
        assertEquals(0, marketData.getAsks().size());
        assertEquals(0, marketData.getBids().size());
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #9")
    @Description("Проверка удаления заявки")
    @Severity(SeverityLevel.CRITICAL)
    @Test()
    public void test_deleteOrder_checkStatusCode() {
        // Создаем два ордера на покупку и продажу
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        restAPI.doCreateOrderRequest(order_1);

        //Удаляем заявку и проверяем ответ
        Response responseToDelOrder = restAPI.doDeleteRequest("1");
        assertEquals(200, responseToDelOrder.getStatusCode());
    }
}