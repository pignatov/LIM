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
/* Date: Oct 20, 2002 * Time: 11:14:06 PM */

import data.Sender;
import data.entry;
import utils.CommonValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Vector;

final class SendMessageFrame extends JFrame {
    private final Sender mSender;

    public SendMessageFrame(final Sender aSender, final String aSrc, final String aDest) {
        super("Message to " + aDest);
        mSender = aSender;
        final String src = aSrc;
        final String dest = aDest;
        final Container content = getContentPane();
        final JTextArea MessageBody = new JTextArea(5, 40);
        final JPanel pnlButtons = new JPanel(new FlowLayout());
        final JButton btnSend = new JButton(CommonValues.locale.Send, new ImageIcon("img/send.gif"));
        final JButton btnCancel = new JButton(CommonValues.locale.Cancel, new ImageIcon("img/cancel.gif"));
        final JScrollPane scroll_pane = new JScrollPane(MessageBody);

        content.setLayout(new BorderLayout());
        content.add(scroll_pane, "Center");
        content.add(pnlButtons, "South");
        pnlButtons.add(btnSend);
        pnlButtons.add(btnCancel);

        MessageBody.setFont(new Font(mSender.user.getSfont(), Font.PLAIN, mSender.user.getSsize()));

        final ActionListener sendlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Vector mesg = new Vector();
                final String str = MessageBody.getText();
                mesg.add(str);
                mSender.sendmsg(src, dest, mesg);
                final entry tentry = new entry(src, dest, mesg.toString(), Calendar.getInstance());
                mSender.getUI().addMsg(dest, tentry);
                /* We store all the infotmation twice
                 * This is about to change soon */
                mSender.hist.add(tentry);

                dispose();
            }
        };
        btnSend.addActionListener(sendlistener);

        final KeyListener sendKeyListener = new KeyListener() {
            public void keyPressed(final KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    final Vector mesg = new Vector();
                    final String str = MessageBody.getText();
                    mesg.add(str);
                    mSender.sendmsg(src, dest, mesg);
                    final entry tentry = new entry(src, dest, mesg.toString(), Calendar.getInstance());
                    mSender.getUI().addMsg(dest, tentry);
                    // The information is stored in both places currently
                    mSender.hist.add(tentry);
                    dispose();
                }
            }

            public void keyTyped(final KeyEvent e) {
                //...
            }

            public void keyReleased(final KeyEvent e) {
                //...
            }
        };
        MessageBody.addKeyListener(sendKeyListener);

        final ActionListener cancellistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
        btnCancel.addActionListener(cancellistener);
    }
}
