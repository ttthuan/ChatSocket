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
public class CustomizeAbstractListChat extends AbstractListModel<PackageChat> {

    private List<PackageChat> dsChat = null;

    public List<PackageChat> getDsChat() {
        return dsChat;
    }

    public void setDsChat(List<PackageChat> dsChat) {
        this.dsChat = dsChat;
        fireContentsChanged(this, 0, dsChat.size()-1);
    }

    public CustomizeAbstractListChat(List<PackageChat> dsChat) {
        this.dsChat = dsChat;
    }

    @Override
    public int getSize() {
        return dsChat.size();
    }

    @Override
    public PackageChat getElementAt(int index) {
        if (index >= dsChat.size()) {
            return null;
        }
        return dsChat.get(index);
    }

    public synchronized void addToList(PackageChat pckChat) {
        dsChat.add(pckChat);
        int size = dsChat.size();
        fireIntervalAdded(this, size - 1, size - 1);
    }
}
