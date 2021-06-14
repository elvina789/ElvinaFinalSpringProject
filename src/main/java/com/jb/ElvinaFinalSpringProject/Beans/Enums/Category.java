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

    /**
     * Constructor of Category object
     * @param id id of the category
     * @param name name of the category
     */
    Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for the id
     * @return the id of the category
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the id
     * @param id id to set
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     *  Getter for the name
     * @return the name of the category
     */

    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     * @param name name to set
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * function for creating String representation of the current category class
     * @return the string representation of the category
     */
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Method that retuns the category by its id
     * @param id the id of the category
     * @return category by id
     */

    public static Category idToCategory(int id) {
        return Arrays.stream(Category.values())
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }
}