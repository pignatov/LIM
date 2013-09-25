package gui;

import data.Users;

import javax.swing.*;

public final class Model extends AbstractListModel {
    private final Users mUsr;

    public Model(final Users aUsr) {
        mUsr = aUsr;
    }

    public int getSize() {
        return mUsr.getsize();
    }

    public Object getElementAt(final int i) {
        return mUsr.getAt(i);
    }

    public synchronized void add() {
        fireIntervalAdded(this, mUsr.getsize(), mUsr.getsize());
    }

    public synchronized void remove() {
        fireIntervalRemoved(this, mUsr.getsize(), mUsr.getsize());
    }

    /**
     * Forces all contents of JList to update
     */
    public synchronized void update() {
        fireIntervalRemoved(this, mUsr.getsize(), mUsr.getsize());
    }

}