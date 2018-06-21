/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.Serializable;

/**
 *
 * @author Keven
 */
public class PackageChat implements Serializable {

    private Account account;
    private String content;
    private int h;
    private int m;
    private boolean isFile = false;

    public PackageChat(Account account, String content, int h, int m) {
        this.account = account;
        this.content = content;
        this.h = h;
        this.m = m;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public boolean isIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        PackageChat two = (PackageChat) obj;
        if (this.account.equals(two.getAccount())) {
            // kiểm tra giờ
            if (this.h == two.getH()) {
                if (this.m == two.getM()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return account.getUserName() + " " + content + " " + h + ":" + m;
    }

}
