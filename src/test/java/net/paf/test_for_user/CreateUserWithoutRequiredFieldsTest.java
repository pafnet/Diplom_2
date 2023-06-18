package net.paf.test_for_user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import net.paf.user.UserGenerator;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

public class CreateUserWithoutRequiredFieldsTest {
    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
        accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(UserCredentials.from(user), accessToken);
        }
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void userWithoutEmailNotCreated() {
        user.setEmail(null);
        ValidatableResponse response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("Email, password and name are required fields", response.extract().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void userWithoutPasswordNotCreated() {
        user.setPassword(null);
        ValidatableResponse response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("Email, password and name are required fields", response.extract().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void userWithoutNameNotCreated() {
        user.setName(null);
        ValidatableResponse response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("Email, password and name are required fields", response.extract().path("message"));
    }
}

