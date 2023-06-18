package net.paf.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(11) + "@paf.net", RandomStringUtils.randomAlphanumeric(11), RandomStringUtils.randomAlphabetic(11));
    }

}
