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
/* Date: Oct 30, 2002 * Time: 10:18:21 AM */

import data.Sender;
import utils.AttributedTextArea;
import utils.CommonValues;
import utils.EmoticonsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.Locale;

public final class MessageBoardPanel extends JPanel {
    private final String mUser;
    private final Sender mSender;
    public final AttributedTextArea jta = new AttributedTextArea();
    final Long[] mDays = {new Long(1), new Long(2), new Long(3), new Long(4), new Long(5), new Long(6), new Long(7), new Long(8),
                          new Long(9), new Long(10), new Long(11), new Long(12), new Long(13), new Long(14)};
    final Long[] mHours = {new Long(1), new Long(2), new Long(3), new Long(4), new Long(5), new Long(6), new Long(7), new Long(8),
                           new Long(9), new Long(10), new Long(11), new Long(12), new Long(13), new Long(14)};

    public MessageBoardPanel(final String aUser, final Sender aSender) {

        final JPanel pnlButtons = new JPanel();
        final JScrollPane jsp = new JScrollPane(jta);
        final JButton btnSaveToFile = new JButton(CommonValues.locale.Save);
        final JButton btnClear = new JButton(CommonValues.locale.Clear);
        final JButton btnHelp = new JButton(CommonValues.locale.Help);
        final JButton btnReload = new JButton(CommonValues.locale.Reload);
        final JButton btnEraseAll = new JButton(CommonValues.locale.EraseMyOwn);
        final JPanel mCommandLinePanel = new JPanel(new BorderLayout());
        final JButton mEmoticons = new JButton(new ImageIcon("img/01.gif"));
        final JTextField jtf = new JTextField();

        this.mSender = aSender;
        this.mUser = aUser;
        final Container content = this;
        content.setLayout(new BorderLayout());
        btnSaveToFile.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnClear.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnHelp.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnReload.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnEraseAll.setFont(new Font("Verdana", Font.PLAIN, 9));

        pnlButtons.add(btnSaveToFile);
        pnlButtons.add(btnClear);
        pnlButtons.add(btnHelp);
        pnlButtons.add(btnReload);
        pnlButtons.add(btnEraseAll);

        jta.setEditable(false);
        jtf.setLocale(new Locale("bg", "BG"));
        jtf.setEditable(true);
        jtf.setFont(new Font("Tahoma", Font.PLAIN, 14));

        final KeyListener chatKeyListener = new KeyListener() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    final Calendar now = Calendar.getInstance();
                    final String hour = MessageBoardPanel.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
                    final String date = MessageBoardPanel.convert((new Long(now.get(Calendar.DAY_OF_MONTH))).toString(), 2) + "/" + MessageBoardPanel.convert((new Long(now.get(Calendar.MONTH) + 1)).toString(), 2) + "/" + (new Long(now.get(Calendar.YEAR))).toString();
                    String mSendString = new String();
                    mSendString = "[" + date + "]" + "[" + hour + "] " + jtf.getText();

                    mSender.sendboard(mUser, mSendString);
                    BoardMsg(mUser, mSendString + "\n");
                    jtf.setText("");
                }
            }

            public void keyTyped(final KeyEvent e) {
                //...
            }

            public void keyReleased(final KeyEvent e) {
                //...
            }
        };
        jtf.addKeyListener(chatKeyListener);

        final ActionListener clearListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jta.setText("");
            }
        };
        btnClear.addActionListener(clearListener);

        final ActionListener eraseListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mSender.sendEraseAll(mUser);
                jta.setText("");
                mSender.sendReload(mUser);
            }
        };
        btnEraseAll.addActionListener(eraseListener);

        final ActionListener reloadListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                jta.setText("");
                mSender.sendReload(mUser);
            }
        };
        btnReload.addActionListener(reloadListener);

        final ActionListener helpListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final HelpContentsForm help_form = new HelpContentsForm();
                help_form.pack();
                help_form.setLocationRelativeTo(null);
                help_form.setVisible(true);
            }
        };
        btnHelp.addActionListener(helpListener);

        final ActionListener saveListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final InfoFrame noHelp = InfoFrame.instance(CommonValues.locale.Information, "No Save to File yet... Sorry :-)", 0);
                noHelp.pack();
                noHelp.setLocationRelativeTo(null);
                noHelp.setVisible(true);
            }
        };
        btnSaveToFile.addActionListener(saveListener);

        final ActionListener emoticonListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JWindow window = new JWindow();
                final EmoticonsPanel emoticons_panel = new EmoticonsPanel(jtf, window);
                window.getContentPane().add(emoticons_panel);
                window.setLocation(mEmoticons.getLocationOnScreen());
                window.pack();
                window.setVisible(true);
            }
        };
        mEmoticons.addActionListener(emoticonListener);
        mEmoticons.setSize(30, 30);

        content.add(pnlButtons, "North");
        mCommandLinePanel.add(jtf, "Center");
        mCommandLinePanel.add(mEmoticons, "East");
        content.add(mCommandLinePanel, "South");
        content.add(jsp, "Center");
    }

    public static void sysMsg(final String aMessage) {
        final Calendar now = Calendar.getInstance();
        final String hour = MessageBoardPanel.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
    }

    private static String convert(final String aInput, final int aSize) {
        String result = new String();
        if (aInput.length() >= aSize)
            result = aInput;
        else
            result += "0" + aInput;
        return result;
    }

    public void BoardMsg(final String aSendingUser, String aMessage) {
        final Calendar now = Calendar.getInstance();
        final String hour = MessageBoardPanel.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + MessageBoardPanel.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
        if (aMessage.equalsIgnoreCase("")) aMessage += "\n";
        if (aSendingUser.equalsIgnoreCase(mUser))
            jta.append("<" + aSendingUser + ">" + aMessage, Color.BLUE);
        else
            jta.append("<" + aSendingUser + ">" + aMessage, Color.BLACK);
        jta.setCaretPosition(jta.getDocument().getLength());
    }

}
