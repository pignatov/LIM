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
/* Date: Oct 27, 2002 * Time: 11:49:32 AM */

import data.Sender;
import utils.CommonValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class MessageHistoryFrame extends JFrame {
    public MessageHistoryFrame(final Sender sender, final String src, final String dest) {
        super(CommonValues.locale.History + " " + dest);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        final JHistory history = new JHistory(sender, src, dest);
        final JScrollPane scroll_history = new JScrollPane(history);
        content.add(scroll_history, "Center");
        final JPanel pnlButtons = new JPanel(new FlowLayout());

        final JButton btnOK = new JButton(CommonValues.locale.OK, new ImageIcon("img/ok.gif"));
        final ActionListener OKlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
        btnOK.addActionListener(OKlistener);
        pnlButtons.add(btnOK);
        content.add(pnlButtons, "South");
    }
}
