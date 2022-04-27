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
        restAPI.doCleanRequest();
    }

    @Epic("Тестирование торгового стакана заявок")
    @Feature(value = "CASE #1")
    @Description("Смоук тест для проверки работоспособности всех сервисов")
    @Severity(SeverityLevel.BLOCKER)
    @Test()
    public void SmokeTest() {
        System.out.println("Starting a test run");

        // Создаем 2 ордера
        Order order_1 = Order.builder()
                .id("1")
                .price("100")
                .quantity("10")
                .side("Sell")
                .build();
        Order order_2 = Order.builder()
                .id("2")
                .price("200")
                .quantity("20")
                .side("Buy")
                .build();

        // Проверяем, что ордер создан успешно
        Response responseOnCreateOrder_1 = restAPI.doCreateOrderRequest(order_1);
        assertEquals(200, responseOnCreateOrder_1.getStatusCode());

        // Проверяем, что ордер создан успешно
        Response responseOnCreateOrder_2 = restAPI.doCreateOrderRequest(order_2);
        assertEquals(200, responseOnCreateOrder_2.getStatusCode());

        // А также проверяем, что в теле ответа данные приходят корректные
        Order createdOrder_2 = new Order(responseOnCreateOrder_2);
        assertEquals("2", createdOrder_2.getId());
        assertEquals("200", createdOrder_2.getPrice());
        assertEquals("20", createdOrder_2.getQuantity());
        assertEquals("buy", createdOrder_2.getSide());


        // Получаем маркетдату
        MarketDataSnapshot marketData = restAPI.doGetMarkedDataRequest();

        //Проверяем, что все 2 заяки есть в снепшоте
        assertEquals(3, marketData.getAsks().size());
        assertEquals("200", marketData.getAsks().get(0).getPrice());
        assertEquals("20", marketData.getAsks().get(0).getQuantity());
        assertEquals(1, marketData.getBids().size());
        assertEquals("100", marketData.getBids().get(0).getPrice());
        assertEquals("10", marketData.getBids().get(0).getQuantity());



        //Удаляем заявку и проверяем ответ
        Response responseToDelOrder = restAPI.doDeleteRequest("1");
        assertEquals(200, responseToDelOrder.getStatusCode());
        Order deletedOrder_1 = new Order(responseToDelOrder);
        assertEquals("1", deletedOrder_1.getId());
        assertEquals("100", deletedOrder_1.getPrice());
        assertEquals("10", deletedOrder_1.getQuantity());
        assertEquals("sell", deletedOrder_1.getSide());

        //Проверяем, что одна заявка действительно удалена, а вторая на месте
        assertEquals(404, restAPI.doGetRequest("1").getStatusCode());
        assertEquals(200, restAPI.doGetRequest("2").getStatusCode());



        //Чистим стакан
        restAPI.doCleanRequest();

        //Проверяем, что вторая заявка тоже удалена
        assertEquals(404, restAPI.doGetRequest("2").getStatusCode());

        System.out.println("End of test execution");
    }

    @After
    public void after(){
        restAPI.doCleanRequest();
    }

}