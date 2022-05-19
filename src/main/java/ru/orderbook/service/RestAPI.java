package ru.orderbook.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.json.JSONException;
import ru.orderbook.entity.Order;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * Сервис, позволяется отправлять запросы
 */
public class RestAPI {

    public static final class EndPoints {
        public static final String create = "/api/order/create";
        public static final String marketdata = "/api/marketdata";
        public static final String clean = "/api/order/clean";
        public static final String delete = "/api/order?id={id}";
        public static final String get = "/api/order?id={id}";
    }

    public RestAPI(){
        try {
            Properties property = new Properties();
            property.load(new FileInputStream("src/main/resources/config.properties"));

            RestAssured.baseURI = property.getProperty("url.host");
            RestAssured.defaultParser = Parser.JSON;

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    public Response doGetRequest(String id) throws JSONException {
        return given().pathParam("id", id).
                        when().get(EndPoints.get).
                        then().contentType(ContentType.JSON).extract().response();
    }

    public Response doDeleteRequest(String id) throws JSONException {
        return given().pathParam("id", id).
                when().delete(EndPoints.delete).
                then().contentType(ContentType.JSON).extract().response();
    }

    public Response doCleanRequest() throws JSONException {
        return given().
                when().get(EndPoints.clean).
                then().contentType(ContentType.JSON).extract().response();
    }

    public Response doGetMarkedDataRequest() throws JSONException {
        return given().
                when().get(EndPoints.marketdata).
                then().contentType(ContentType.JSON).extract().response();
    }

    public Response doCreateOrderRequest(Order order) throws JSONException {
        return given().body(order.toString()).
                accept(ContentType.JSON).
                contentType(ContentType.JSON).
                when().post(EndPoints.create).
                then().contentType(ContentType.JSON).extract().response();
    }
}