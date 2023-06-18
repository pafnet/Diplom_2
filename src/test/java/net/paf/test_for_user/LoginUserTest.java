package net.paf.test_for_user;

import net.paf.user.UserGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import net.paf.user.User;
import net.paf.user.UserClient;
import net.paf.user.UserCredentials;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class LoginUserTest {
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

    private void assertLoginStatus(ValidatableResponse loginResponse, int expectedStatusCode) {
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(expectedStatusCode, loginStatusCode);
        accessToken = loginStatusCode == SC_OK ? loginResponse.extract().path("accessToken") : null;
        assertEquals(expectedStatusCode == SC_OK, accessToken != null);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userCanBeLoggedIn() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        assertLoginStatus(loginResponse, SC_OK);
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void userWithWrongEmailAddressNotLogin() {
        user.setEmail("lapshin@paf.net");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        assertLoginStatus(loginResponse, SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void userWithWrongPasswordNotLogin() {
        user.setPassword(null);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        assertLoginStatus(loginResponse, SC_UNAUTHORIZED);
    }
}
