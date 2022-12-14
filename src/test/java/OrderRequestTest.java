import API.OrderAPI;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.OrderRequest;

import static config.Config.getBaseUrl;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static test_data.OrderRequestTestData.getOrderRequestAllField;

@RunWith(Parameterized.class)
public class OrderRequestTest {

    public static String ORDERS_CANCEL_TRACK = "orders/cancel?track=";
    private String[] color;
    private static OrderAPI orderAPI;

    @Before
    public void setUp() {
        orderAPI = new OrderAPI();
    }

    public OrderRequestTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTextData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Step("Проверяем, создание заказа с корректными данными")
    public void createOrder() {
        OrderRequest orderRequest = getOrderRequestAllField();
        orderRequest.setColor(color);
        int track = orderAPI.createOrder(orderRequest)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                // проверяем, что в теле ответа track непустое
                .body("track", notNullValue())
                .extract()
                .path("track");

        String point = ORDERS_CANCEL_TRACK;
        ORDERS_CANCEL_TRACK = ORDERS_CANCEL_TRACK + track;
        given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .put(ORDERS_CANCEL_TRACK);
        ORDERS_CANCEL_TRACK = point;
    }
}