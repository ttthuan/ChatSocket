/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.io.File;
import java.util.List;

/**
 *
 * @author Totoro
 */
public class DataProvider {

    private String path = "../data/data.xml";

    public List<Account> getAllAccount() {
        List<Account> accounts = null;
        File file = new File(DataProvider.class.getResource(path).getPath());

        accounts = XMLReader.readXMLDocument(file);

        return accounts;
    }

    public Account login(String username, String password) {
        Account account = null;
        List<Account> accounts = getAllAccount();

        int i;
        for (i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUserName().equals(username) == true
                    && accounts.get(i).getPassword().equals(password) == true) {
                account = new Account(accounts.get(i).getUserName(), accounts.get(i).getPassword(), accounts.get(i).getFullName());
                break;
            }
        }
        return account;
    }
    
    public void addAccount(Account account){
        File file = new File(DataProvider.class.getResource(path).getPath());
        XMLReader.addAccount(account, file);
    }
    
    public void modiferAccount(Account account){
        File file = new File(DataProvider.class.getResource(path).getPath());
        XMLReader.modiferAccount(account, file);
    }
    
    public void deleteNode(String username){
        File file = new File(DataProvider.class.getResource(path).getPath());
        XMLReader.deleteNode(username, file);
    }

}
