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
import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Locale;

public final class JChat extends JPanel {
    private final AttributedTextArea ataChat = new AttributedTextArea();
    private final String mUser;
    private final Sender mSender;
    private Calendar LastStroke;
    private boolean keyFlag;
    private javax.swing.Timer keyTimer;
    private boolean fixForEnterKey;

    public void setChatFont(Font aFont){
        ataChat.setFont(aFont);
    }

    public JChat(final String aUser, final Sender aSender) {
        keyFlag = false;
        mSender = aSender;
        LastStroke = Calendar.getInstance();
        // Set LastStroke 60 secs before startup
        LastStroke.setTimeInMillis(LastStroke.getTimeInMillis() - 60 * 1000);
        this.mUser = aUser;
        final Container content = this;
        final JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton btnSaveToFile = new JButton(CommonValues.locale.Save);
        final JButton btnClear = new JButton(CommonValues.locale.Clear);
        final JButton btnHelp = new JButton(CommonValues.locale.Help);
        final JPanel mCommandLinePanel = new JPanel(new BorderLayout());
        final JButton mEmoticons = new JButton(new ImageIcon("img/01.gif"));
        final JTextField chat_field = new JTextField();
        final JScrollPane scroll_pane = new JScrollPane(ataChat);

//        ataChat.addHyperlinkListener();


        content.setLayout(new BorderLayout());
        btnSaveToFile.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnSaveToFile.setSize(25, 10);
        btnSaveToFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnClear.setSize(25, 10);
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHelp.setFont(new Font("Verdana", Font.PLAIN, 9));
        btnHelp.setSize(25, 10);
        btnHelp.setAlignmentX(Component.LEFT_ALIGNMENT);
        ataChat.setFont(new Font(aSender.user.getChatFont(), Font.PLAIN, aSender.user.getChatSize()));
        ataChat.setEditable(true);
        ataChat.setLocale(new Locale("bg", "BG"));
        ataChat.setForeground(new Color(0, 0, 255));
        ataChat.setAutoscrolls(true);
        mEmoticons.setSize(30, 30);
        chat_field.setLocale(new Locale("bg", "BG"));
        chat_field.setFont(new Font("Tahoma", Font.PLAIN, 14));

        int delay = 1000; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                long difference = Calendar.getInstance().getTimeInMillis() - LastStroke.getTimeInMillis();
                if ((difference > (CommonValues.KeyTypeTolerance * 1000)) && keyFlag) {
                    if (keyFlag) {
                        keyFlag = false;
                        mSender.sendUserActivity(mUser, keyFlag);
                    }
                }
            }
        };
        keyTimer = new Timer(delay, taskPerformer);
        keyTimer.start();

        final KeyListener chatKeyListener = new KeyListener() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mSender.sendchat(mUser, chat_field.getText());
                    chat_field.setText("");
                    if (keyFlag) {
                        keyFlag = false;
                        mSender.sendUserActivity(mUser, keyFlag);
                        fixForEnterKey = true;
                    }
                }
            }

            public void keyTyped(final KeyEvent e) {
                if (fixForEnterKey) {
                    fixForEnterKey = false;
                    return;
                }
                if ((!fixForEnterKey) && (!keyFlag)) {
                    keyFlag = true;
                    mSender.sendUserActivity(mUser, keyFlag);
                    fixForEnterKey = false;
                }
                LastStroke = Calendar.getInstance();
            }

            public void keyReleased(final KeyEvent e) {
            }
        };
        chat_field.addKeyListener(chatKeyListener);

        final JPopupMenu menu = new JPopupMenu();
        final ActionListener copyPopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ataChat.copy();
            }
        };

        final JMenuItem miCopy = new JMenuItem("Copy", new ImageIcon("img/copy.gif"));
        miCopy.addActionListener(copyPopupListener);
        menu.add(miCopy);

        ataChat.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });

        final ActionListener saveListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    final JFileChooser jfc = new JFileChooser();
                    final File savefile = jfc.getSelectedFile();
                    jfc.showSaveDialog(content);

                    if (savefile != null) {
                        final PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(savefile)));
                        final Calendar now = Calendar.getInstance();
                        final String hour = JChat.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
                        final String date = JChat.convert((new Long(now.get(Calendar.DAY_OF_MONTH))).toString(), 2) + "/" + JChat.convert((new Long(now.get(Calendar.MONTH))).toString(), 2) + "/" + (new Long(now.get(Calendar.YEAR))).toString();
                        log.println("[LIM LOG File]");
                        log.println("[Date:" + date + "]");
                        log.println("[Time: " + hour + " ]");
                        log.println("[User: " + mUser + " ]");
                        log.println(ataChat.getText());
                        log.println("[END OF LOG FILE]");
                        log.close();
                    }
                } catch (IOException fnf) {
                    fnf.printStackTrace();
                }
            }
        };
        btnSaveToFile.addActionListener(saveListener);

        final ActionListener clearListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ataChat.setText("");
            }
        };
        btnClear.addActionListener(clearListener);

        final ActionListener helpListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final HelpContentsForm help_form = new HelpContentsForm();
                help_form.pack();
                help_form.setLocationRelativeTo(null);
                help_form.setVisible(true);
            }
        };
        btnHelp.addActionListener(helpListener);

        pnlButtons.add(btnSaveToFile);
        pnlButtons.add(btnClear);
        pnlButtons.add(btnHelp);

        final ActionListener emoticonListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JWindow window = new JWindow();
                final EmoticonsPanel emoticons_panel = new EmoticonsPanel(chat_field, window);
                window.getContentPane().add(emoticons_panel);
                window.setLocation(mEmoticons.getLocationOnScreen());
                window.pack();
                window.setVisible(true);
            }
        };
        mEmoticons.addActionListener(emoticonListener);
        mEmoticons.setSize(30, 30);

        content.add(pnlButtons, "North");
        content.add(scroll_pane, "Center");
        mCommandLinePanel.add(chat_field, "Center");
        mCommandLinePanel.add(mEmoticons, "East");
        content.add(mCommandLinePanel, "South");
    }

    public void sysMsg(final String message) {
        final Calendar now = Calendar.getInstance();
        final String hour = JChat.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
        ataChat.setForeground(new Color(255, 0, 0));
        ataChat.append("[" + hour + "]<System>" + message + "\n", Color.RED);
        ataChat.setForeground(new Color(0, 0, 255));
        ataChat.validate();
    }

    public void dndMsg(final String message) {
        final Calendar now = Calendar.getInstance();
        final String hour = JChat.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
        ataChat.setForeground(new Color(255, 0, 0));
        ataChat.append("[" + hour + "]<silent>" + message + "\n", Color.DARK_GRAY);
        ataChat.setForeground(new Color(0, 0, 255));
        ataChat.validate();
    }

    /**
     * Sets background color for the chat pane
     *
     * @param aColor
     */
    public void setBGColor(final Color aColor) {
        ataChat.setBackground(aColor);
    }

    /**
     * Adds leading zeros if necessary
     *
     * @param str  String to add leading zeroes to
     * @param size Size of desired string length
     * @return Modified string
     */
    private static String convert(final String str, final int size) {
        String result = new String();
        if (str.length() >= size)
            result = str;
        else
            result += "0" + str;
        return result;
    }

    public void chatMsg(final String senduser, String message) {
        final Calendar now = Calendar.getInstance();
        final String hour = JChat.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + JChat.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
        if ("".equalsIgnoreCase(message)) message += "\n";
        Color mDisplayColor = Color.RED;
        if (!"SYSTEM".equalsIgnoreCase(senduser)) mDisplayColor = mSender.getUI().getColor(senduser);
        if (senduser.equalsIgnoreCase(mUser))
            ataChat.append("[" + hour + "]<" + senduser + ">" + message, mDisplayColor);
        else
            ataChat.append("[" + hour + "]<" + senduser + ">" + message, mDisplayColor);
        ataChat.setCaretPosition(ataChat.getDocument().getLength());
        if (mSender.user.isBeep()) ataChat.getToolkit().beep();
        if (message.toUpperCase().indexOf(mUser.toUpperCase()) >= 0) {
            ataChat.getToolkit().beep();
            ataChat.getToolkit().beep();
        }
    }
}
