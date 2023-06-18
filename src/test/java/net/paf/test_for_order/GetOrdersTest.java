package net.paf.test_for_order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.paf.order.OrderClient;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import net.paf.user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    private OrderClient orderClient;
    private User user;
    private UserClient userClient;
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
    @DisplayName("Получение всех заказов")
    public void getAllOrders() {
        ValidatableResponse response = orderClient.getAllOrders();
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAuthorizedUserOrders() {
        ValidatableResponse response = orderClient.getUserOrders(accessToken);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getUnauthorizedUserOrders() {
        String invalidAccessToken = "invalid-access-token";
        ValidatableResponse response = orderClient.getUserOrders(invalidAccessToken);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
}
