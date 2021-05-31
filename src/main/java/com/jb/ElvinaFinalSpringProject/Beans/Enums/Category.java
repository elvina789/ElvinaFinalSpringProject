package com.jb.ElvinaFinalSpringProject.Beans.Enums;


import java.util.Arrays;

/***
 * Enum for different categories of coupons
 */
public enum Category {
    Food(1, "Food"),
    Electricity(2, "Electricity"),
    Restaurant(3, "Restaurant"),
    Vacation(4, "Vacation");

    private int id;
    private String name;

    Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static Category idToCategory(int id) {
        return Arrays.stream(Category.values())
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }
}