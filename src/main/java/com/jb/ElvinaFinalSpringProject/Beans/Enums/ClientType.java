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

    /**
     * Constructor of the Client type object
     * @param id id of the client type
     * @param name name of the client type
     */
    ClientType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for the id
     * @return the id of the client type
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
     * @return the name of the client type
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
}
