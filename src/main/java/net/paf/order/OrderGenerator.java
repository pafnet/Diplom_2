package net.paf.order;

import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.Map;

public class OrderGenerator {
    private static OrderClient orderClient = new OrderClient();

    public static Order getRandomOrder() {
        ValidatableResponse response = orderClient.getIngredientsList();

        List<Map<String, String>> ordersList = response.extract().path("data");
        int length = ordersList.size();
        int randomInt = (int) (Math.random() * length);
        String id = ordersList.get(randomInt).get("_id");

        return new Order(new String[]{id});
    }
}
