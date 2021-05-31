package com.jb.ElvinaFinalSpringProject.Beans.Enums;

/***
 * Enum for different types of clients
 */
public enum ClientType {
    Administrator(1, "Administrator"),
    Customer(2, "Customer"),
    Company(3, "Company");

    private int id;
    private String name;

    ClientType(int id, String name) {
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
}
