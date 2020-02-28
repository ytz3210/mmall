package cn.yang.tmall.pojo;

import lombok.Data;
@Data
public class User extends BaseEntity{

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;
}