package gui;

/*************************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *************************************************************************/
/* Date: Oct 9, 2002 * Time: 5:50:30 PM */

import data.Sender;
import data.entry;
import data.history;

import javax.swing.*;
import java.util.Calendar;

final class JModel extends AbstractListModel {
    history hist;
    private final Sender sender;
    private final String mSrc;
    private final String mDest;

    public JModel(final Sender _sender, final String aSrc, final String aDest) {
        sender = _sender;
        mSrc = aSrc;
        mDest = aDest;
    }

    public int getSize() {
        return sender.getUI().getHistorySize(mSrc);
    }

    public Object getElementAt(final int aIndex) {
        final entry mEntry = sender.getUI().getHistoryAt(mSrc, aIndex);
        final String value;
        value = mEntry.getDate().get(Calendar.HOUR_OF_DAY) + ":" + mEntry.getDate().get(Calendar.MINUTE) + " " + mEntry.getFrom() + " " + mEntry.getTo() + " " + mEntry.getMessage();
        return value;
    }

    public synchronized void add() {
        fireIntervalAdded(this, getSize(), getSize());
    }

    public synchronized void remove(final entry aEntry) {
        fireIntervalRemoved(this, getSize(), getSize());
    }
}

final class JHistory extends JPanel {
    private final Sender mSender;

    public JHistory(final Sender aSender, final String aSrc, final String aDest) {
        mSender = aSender;
        final JList data = new JList(new JModel(mSender, aSrc, aDest));
        add(data);
    }
}
