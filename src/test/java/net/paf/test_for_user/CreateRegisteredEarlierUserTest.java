package net.paf.test_for_user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import net.paf.user.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

public class CreateRegisteredEarlierUserTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(UserCredentials.from(user), accessToken);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void userNotCreated() {
        ValidatableResponse response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("User already exists", response.extract().path("message"));
    }
}
