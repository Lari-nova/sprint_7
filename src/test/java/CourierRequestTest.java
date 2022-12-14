import API.CourierAPI;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import pojo.CourierRequest;
import pojo.LoginRequest;
import test_data.LoginRequestTestData;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static test_data.CourierRequestTestData.getCourierRequestAllRequiredField;
import static test_data.CourierRequestTestData.getCourierRequestWithoutRequiredField;

public class CourierRequestTest {

    private static CourierAPI courierAPI;

    @Before
    public void setUp() {
        courierAPI = new CourierAPI();
    }

    @AfterClass
    public static void setId() {
        LoginRequest loginRequest = LoginRequestTestData.from(getCourierRequestAllRequiredField());
        int id = courierAPI.loginCourier(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        courierAPI.deleteCourier(id);
    }

    @Test
    @DisplayName("Создание курьера")
    @Step("Проверяем создание курьера: запрос возвращает код 201 и ok: true")
    public void createCourier() {
        CourierRequest courierRequest = getCourierRequestAllRequiredField();
        courierAPI.createCourier(courierRequest)
                .statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание существующего клиента")
    @Step("Проверяем, если курьер уже создан: запрос возвращает код 409 и в теле ответа приходит" +
            " message: \"Этот логин уже используется. Попробуйте другой.\"")
    public void createCourierCopy() {
        CourierRequest courierRequest = getCourierRequestAllRequiredField();
        courierAPI.createCourier(courierRequest)
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Test
    @DisplayName("Создание клиента при отсутствии в запросе одного из полей")
    @Step("Проверяем если одного из полей нет, запрос возвращает ошибку: код ответа 400" +
            " и в теле message соответствует \"Недостаточно данных для создания учетной записи\"")
    public void createCourierCopyWithoutRequiredField() {
        CourierRequest courierRequest = getCourierRequestWithoutRequiredField();
                courierAPI.createCourier(courierRequest)
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}