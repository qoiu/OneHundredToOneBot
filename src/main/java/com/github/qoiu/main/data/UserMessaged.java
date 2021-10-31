package com.github.qoiu.main.data;

import java.util.Objects;

public class UserMessaged {
    private final String name;
    private final Long id;
    private final String message;

     public UserMessaged(String name, Long id, String message) {
        this.name = name;
        this.id = id;
        this.message = message;
    }

    public UserMessaged(Long id, String message) {
        this.name="";
        this.id = id;
        this.message = message;
    }

    public String getName() {
         return name;
     }

     public Long getId() {
         return id;
     }

     public String getMessage() {
         return message;
     }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessaged that = (UserMessaged) o;
        return Objects.equals(name, that.name) && Objects.equals(id, that.id) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, message);
    }
}


