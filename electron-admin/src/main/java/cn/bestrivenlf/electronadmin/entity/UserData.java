package cn.bestrivenlf.electronadmin.entity;

import lombok.Data;

@Data
public class UserData {
    private int id;
    private String userName;
    private String password;
    private String role;
}
