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
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateUniqueUserTest {
    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        String accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        userClient.delete(UserCredentials.from(user), accessToken);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void userCreatedSuccessfully() {
        ValidatableResponse response = userClient.create(user);
        assertEquals(SC_OK, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }
}
