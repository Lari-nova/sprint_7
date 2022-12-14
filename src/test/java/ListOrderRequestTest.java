import API.OrderAPI;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import pojo.OrderRequest;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrderRequestTest {

    private static OrderAPI orderAPI;

    @Before
    public void setUp() {
        orderAPI = new OrderAPI();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Step("Проверяем, что в теле ответа возвращается список заказов и приходит код 200")
    public void getListOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderAPI.getOrderList(orderRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}