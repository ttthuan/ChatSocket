/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Keven
 */
public class CustomizeAbstractListModel extends AbstractListModel<Account>{
    
    private List<Account> dsAccount = null;

    public List<Account> getDsAccount() {
        return dsAccount;
    }

    public void setDsAccount(List<Account> dsAccount) {
        this.dsAccount = dsAccount;
    }
    
    public CustomizeAbstractListModel(List<Account> dsAccount){
        this.dsAccount = dsAccount;
    }
    
    @Override
    public int getSize() {
        return dsAccount.size();
    }

    @Override
    public Account getElementAt(int index) {
        if(index >= dsAccount.size()){
            return null;
        }
        return dsAccount.get(index);
    }

    public void deleteAccount(Account account){
        int index = dsAccount.indexOf(account);
        dsAccount.remove(index);
        fireIntervalRemoved(this, index, index);
    }
    
    public void addAccount(Account account){
        dsAccount.add(account);
        int size = dsAccount.size();
        fireIntervalAdded(this, size-1, size-1);
    }
    
}
