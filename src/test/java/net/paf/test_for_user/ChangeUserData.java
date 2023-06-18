package net.paf.test_for_user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import net.paf.user.UserGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeUserData {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(UserCredentials.from(user), accessToken);
        }
    }

    private String loginAndGetAccessToken(User user) {
        return userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void authorizedUserChangesSuccessful() {
        accessToken = loginAndGetAccessToken(user);
        user.setPassword("practicum.yandex.ru");
        user.setEmail(RandomStringUtils.randomAlphanumeric(11) + "@paf.net");
        user.setName("Pavel");
        ValidatableResponse response = userClient.update(user, accessToken);
        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void unauthorizedUserChangesNotSuccessful() {
        user.setPassword("practicum.yandex.ru");
        user.setEmail(RandomStringUtils.randomAlphanumeric(11) + "@paf.net");
        user.setName("Pavel");
        ValidatableResponse response = userClient.update(user, "");
        assertEquals(SC_UNAUTHORIZED, response.extract().statusCode());
        assertEquals("You should be authorised", response.extract().path("message"));
    }
}
