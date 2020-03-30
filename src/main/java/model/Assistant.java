package model;

import java.util.Objects;

public class Assistant {
    private String name;
    private String lastName;
    private int points;

    public Assistant(String name, String lastName, int points) {
        this.name = name;
        this.lastName = lastName;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Assistant{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return Objects.equals(name, assistant.name) &&
                Objects.equals(lastName, assistant.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName);
    }
}
