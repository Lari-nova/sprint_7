package API;

import io.restassured.response.ValidatableResponse;
import pojo.OrderRequest;

import static config.Config.getBaseUrl;
import static io.restassured.RestAssured.given;

public class OrderAPI {

    private static final String ORDER = "orders";


    public ValidatableResponse createOrder(OrderRequest orderRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .body(orderRequest)
                .post(ORDER)
                .then();
    }

    public ValidatableResponse getOrderList(OrderRequest orderRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUrl())
                .body(orderRequest)
                .get(ORDER)
                .then();
    }
}
