package net.paf.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import net.paf.Main;

public class UserClient extends Main {

    @Step("Create new user {user}")
    public ValidatableResponse create(User user) {
        return getBaseSpec()
                .body(user)
                .when()
                .post(getUserAuthPath() + "/register")
                .then();
    }

    @Step("Login with user {user}")
    public ValidatableResponse login(UserCredentials credentials) {
        return getBaseSpec()
                .body(credentials)
                .when()
                .post(getUserAuthPath() + "/login")
                .then();
    }

    @Step("Update user{user}")
    public ValidatableResponse update(User user, String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(getUserAuthPath() + "/user")
                .then();
    }

    @Step("Delete user {user}")
    public ValidatableResponse delete(UserCredentials credentials, String accessToken) {
        return getBaseSpec()
                .header("Authorization", accessToken)
                .body(credentials)
                .when()
                .delete(getUserAuthPath() + "/user")
                .then();
    }
}
