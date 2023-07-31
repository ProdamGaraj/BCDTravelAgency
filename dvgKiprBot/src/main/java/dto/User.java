package dto;

import lombok.NonNull;
public class User {

    @NonNull
    String telegramId;
    @NonNull
    String first_name;
    @NonNull
    String username;
    String userGroup;


    public User(@NonNull String telegramId, @NonNull String first_name, @NonNull String username) {
        this.telegramId = telegramId;
        this.first_name = first_name;
        this.username = username;
        this.userGroup = null;
    }

   /* public static String calculateUserGroup(String telegramId){
        if()
    }*/
}
