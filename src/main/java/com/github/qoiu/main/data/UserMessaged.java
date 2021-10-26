package com.github.qoiu.main.data;

 public class UserMessaged {
    private final String name;
    private final Long id;
    private final String message;

     public UserMessaged(String name, Long id, String message) {
        this.name = name;
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
 }


