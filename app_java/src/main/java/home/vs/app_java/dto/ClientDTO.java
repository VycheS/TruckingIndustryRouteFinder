package home.vs.app_java.dto;

import java.io.Serializable;

public class ClientDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String surname;
    private String name;
    private String patronymic;
    private String password;
    private String email;
    private String numberphone;
    private String role;
    private String strJson;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStrJson() {
        return strJson;
    }
    
     //TODO поправить данные при помощи JAKSON, чтобы на вход был объект а хранил string
    public void setStrJson(String json) {
        this.strJson = json;
    }
}
