package API;

import io.restassured.response.ValidatableResponse;
import pojo.CourierRequest;
import pojo.LoginRequest;

import static config.Config.getBaseUrl;
import static io.restassured.RestAssured.given;


public class CourierAPI {

    private static final String COURIER = "courier";
    private static final String COURIER_LOGIN = "courier/login";
    private static final String DELETE_COURIER = "courier/";

    public ValidatableResponse createCourier(CourierRequest courierRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .body(courierRequest)
                .post(COURIER)
                .then();

    }

    public ValidatableResponse loginCourier(LoginRequest loginRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .body(loginRequest)
                .post(COURIER_LOGIN)
                .then();
    }

    public ValidatableResponse deleteCourier(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .delete(DELETE_COURIER + id)
                .then();
    }

}