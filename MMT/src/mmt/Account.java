/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.Serializable;

/**
 *
 * @author Totoro
 */
public class Account implements Serializable {

    private String userName;
    private String password;
    private String fullName;
    private String image;
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    
    public String getImage(){
        return image;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setImage(String image){
        this.image = image;
    }

    public Account(String userName, String password, String fullName) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
    }
    
    public Account(String userName, String password, String fullName, String image) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.image = image;
    }

    // Danh sách hàm hành sự của lớp "Tài Khoản"
    public void register(){
        
    }
    
    public void login(String userName, String password){
        
    }
    
    public void logOut(){
        
    }
}
