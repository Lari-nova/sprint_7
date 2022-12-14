import API.CourierAPI;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import pojo.CourierRequest;
import pojo.LoginRequest;
import test_data.LoginRequestTestData;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static test_data.CourierRequestTestData.getCourierRequestAllRequiredField;
import static test_data.LoginRequestTestData.invalidLoginPassword;
import static test_data.LoginRequestTestData.requestWithoutRequiredField;

public class LoginRequestTest {

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
    @DisplayName("Авторизация пользователя с валидным логином и паролем")
    @Step("Проверяем, авторизацию пользователя с корректным логином и паролем")
    public void courierAuthorizationWithCorrectlyPasswordLogin() {
        CourierRequest courierRequest = getCourierRequestAllRequiredField();
        courierAPI.createCourier(courierRequest);

        LoginRequest loginRequest = LoginRequestTestData.from(courierRequest);
        courierAPI.loginCourier(loginRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация пользователя с невалдиным логином и паролем")
    @Step("Проверяем, авторизацию пользователя с некорректным логином и паролем")
    public void courierAuthorizationWithIncorrectlyPasswordLogin() {
        LoginRequest loginRequest = invalidLoginPassword();
        courierAPI.loginCourier(loginRequest)
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация пользователя без одного обязательного поля в запросе")
    @Step("Проверяем, если какого-то поля нет, запрос возвращает ошибку")
    public void courierAuthorizationWithoutRequiredFieldInRequest() {
        LoginRequest loginRequest = requestWithoutRequiredField();
        courierAPI.loginCourier(loginRequest)
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}