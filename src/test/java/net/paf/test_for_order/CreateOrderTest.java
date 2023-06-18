package net.paf.test_for_order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.paf.order.Order;
import net.paf.order.OrderClient;
import net.paf.order.OrderGenerator;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import net.paf.user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
        orderClient = new OrderClient();
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user), accessToken);
    }

    @Test
    @DisplayName("Создание заказа пользователем с авторизацией")
    public void userAuthorizedOrderSuccessfullyCreated() {
        Order order = OrderGenerator.getRandomOrder();
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.statusCode(SC_OK);
        assertTrue(response.extract().path("success"));
    }

    @Test
    @DisplayName("Создание заказа пользователем без авторизации")
    public void userNotAuthorizedOrderNotCreated() {
        Order order = OrderGenerator.getRandomOrder();
        ValidatableResponse response = orderClient.createOrder(order, "");
        response.statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void orderWithoutIngredientsNotCreated() {
        Order orderWithoutIngredients = OrderGenerator.getRandomOrder();
        orderWithoutIngredients.setIngredients(new String[0]);
        ValidatableResponse response = orderClient.createOrder(orderWithoutIngredients, accessToken);
        response.statusCode(SC_BAD_REQUEST);
        assertEquals("Ingredient ids must be provided", response.extract().path("message"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void orderWithWrongIngredientHashNotCreated() {
        Order orderWithBrokenHash = OrderGenerator.getRandomOrder();
        orderWithBrokenHash.setIngredients(new String[]{"practicum.yandex.ru"});
        ValidatableResponse response = orderClient.createOrder(orderWithBrokenHash, accessToken);
        response.statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
