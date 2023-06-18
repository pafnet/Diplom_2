package net.paf.order;

import java.util.Arrays;

public class Order {

    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = Arrays.copyOf(ingredients, ingredients.length);
    }

    public String[] getIngredients() {
        return Arrays.copyOf(ingredients, ingredients.length);
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = Arrays.copyOf(ingredients, ingredients.length);
    }
}
