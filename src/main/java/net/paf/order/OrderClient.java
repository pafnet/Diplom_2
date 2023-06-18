package net.paf.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import net.paf.Main;

public class OrderClient extends Main {

    @Step("Создать новый заказ {order}")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(getOrderPath())
                .then();
    }

    @Step("Получить все заказы {orders}")
    public ValidatableResponse getAllOrders() {
        return getBaseSpec()
                .get(getOrderPath() + "/all")
                .then();
    }

    @Step("Получить заказ пользователя {orders}")
    public ValidatableResponse getUserOrders(String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .get(getOrderPath())
                .then();
    }

    @Step("Получить все ингредиенты {ingredients}")
    public ValidatableResponse getIngredientsList() {
        return getBaseSpec()
                .get(getIngredients())
                .then();
    }
}
