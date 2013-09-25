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
/* Date: 2002-10-4 * Time: 13:52:03*/

import data.LSSettings;
import data.Sender;
import data.UserSettings;
import utils.CommonValues;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Modify and saves various information about the program behaviour.
 * e.g. Font and size for sending/receiving messages
 */
final class SettingsDialog extends JDialog {
    private final UserSettings mUserSettings;

    /**
     * JFrame for adjusting and visualization of some settings
     *
     * @param aSender in this object most of the mUser definable information is stored
     */
    public SettingsDialog(final Sender aSender) {
        setTitle(CommonValues.locale.Settings);
        setResizable(false);
        final JTabbedPane jtpPane = new JTabbedPane();
        // Main Panel------------------------------------------------
        final JPanel pnlMain = new JPanel(new BorderLayout());
        final Sender mSender = aSender;
        final String[] sfont = {"Times", "Arial", "Courier", "Tahoma", "Verdana"};
        final Long[] ssize = {new Long(8), new Long(9), new Long(10), new Long(11), new Long(12), new Long(14), new Long(15), new Long(18)};
        final JLabel lblSend = new JLabel(CommonValues.locale.FontForSending);
        lblSend.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JLabel lblReceive = new JLabel(CommonValues.locale.FontForReceiving);
        lblReceive.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JButton save = new JButton(CommonValues.locale.Save, new ImageIcon("img/ok.gif"));
        final JButton dismiss = new JButton(CommonValues.locale.Dismiss, new ImageIcon("img/cancel.gif"));
        final JLayeredPane jlpOptions = new JLayeredPane();
        final JPanel pnlButtons = new JPanel(new FlowLayout());
        final JPanel sendFS = new JPanel(new FlowLayout());
        final JPanel receiveFS = new JPanel(new FlowLayout());
        final JPanel send = new JPanel(new GridLayout(1, 3));
        final JPanel receive = new JPanel(new GridLayout(1, 3));
        final JPanel mBGColor = new JPanel(new FlowLayout());
        final JLabel mColorLabel = new JLabel(CommonValues.locale.BackgroundColor);
        mColorLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JCheckBox cbTime = new JCheckBox(CommonValues.locale.showTime);
        cbTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JCheckBox me_in_list = new JCheckBox(CommonValues.locale.showMeInList);
        me_in_list.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JCheckBox sound_on_receive = new JCheckBox(CommonValues.locale.beepOnReceival);
        sound_on_receive.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JButton mColor = new JButton("");
        final JTextField txtSendSample = new JTextField(CommonValues.locale.SampleSend);
        final JTextField txtReceiveSample = new JTextField(CommonValues.locale.SampleReceive);
        final JComboBox cobSendFont = new JComboBox(sfont);
        cobSendFont.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobSendSize = new JComboBox(ssize);
        cobSendSize.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobReceiveFont = new JComboBox(sfont);
        cobReceiveFont.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobReceiveSize = new JComboBox(ssize);
        cobReceiveSize.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final LSSettings lssettings = new LSSettings();
        final String[] strAutoAway = {"1", "2", "3", "4", "5", "10", "15", "20"};
        final JPanel pnlAutoAway = new JPanel();
        final JLabel lblAutoAway1 = new JLabel(CommonValues.locale.AutoAwayAfter);
        lblAutoAway1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobAutoAway = new JComboBox(strAutoAway);
        final JLabel lblAutoAway2 = new JLabel(CommonValues.locale.Minutes);
        lblAutoAway2.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JLabel lblAutoAway3 = new JLabel(CommonValues.locale.NAAfter);
        lblAutoAway3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobNA = new JComboBox(strAutoAway);
        final JLabel lblAutoAway4 = new JLabel(CommonValues.locale.Minutes);
        lblAutoAway4.setFont(new Font("Tahoma", Font.PLAIN, 11));

        // Advanced Panel--------------------------------------------------
        final JPanel pnlAdvanced = new JPanel(new GridLayout(6, 1));
        final JCheckBox chkAutoAway = new JCheckBox(CommonValues.locale.EnableAutoAway);
        chkAutoAway.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JCheckBox chkAutoLogin = new JCheckBox(CommonValues.locale.EnableAutoLogin);
        chkAutoLogin.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JCheckBox chkAutoSaveDimensions = new JCheckBox(CommonValues.locale.AutoSaveDimensions);
        chkAutoSaveDimensions.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JPanel pnlChatFont = new JPanel(new GridLayout(1, 3));
        final JPanel pnlChatFS = new JPanel();
        final JLabel lblChatFont = new JLabel(CommonValues.locale.FontForChatMB);
        lblChatFont.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobChatFont = new JComboBox(sfont);
        cobChatFont.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JComboBox cobChatSize = new JComboBox(ssize);
        cobChatSize.setFont(new Font("Tahoma", Font.PLAIN, 11));
        final JTextField txtChatSendSample = new JTextField(CommonValues.locale.SampleSend);

        pnlChatFont.add(lblChatFont);
        pnlChatFS.add(cobChatFont);
        pnlChatFS.add(cobChatSize);
        pnlChatFont.add(pnlChatFS);
        pnlChatFont.add(txtChatSendSample);
        pnlChatFont.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        mUserSettings = mSender.getUser();
        mUserSettings.setInside(true);
        final Container content = getContentPane();
        content.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jlpOptions.setBorder(new TitledBorder(CommonValues.locale.Options));
        jlpOptions.setLayout(new GridLayout(6, 1));
        mColor.setBackground(mUserSettings.getmBGColor());
        mBGColor.add(mColorLabel);
        mBGColor.add(mColor);
        cbTime.setSelected(mUserSettings.isShowtime());
        me_in_list.setSelected(mUserSettings.isMe_in_list());
        sound_on_receive.setSelected(mUserSettings.isBeep());
        cobReceiveFont.setSelectedItem(mUserSettings.getRfont());
        cobSendFont.setSelectedItem(mUserSettings.getSfont());
        cobReceiveSize.setSelectedItem(new Long(mUserSettings.getRsize()));
        cobSendSize.setSelectedItem(new Long(mUserSettings.getSsize()));
        cobChatFont.setSelectedItem(mUserSettings.getChatFont());
        cobChatSize.setSelectedItem(new Long(mUserSettings.getChatSize()));
        txtSendSample.setFont(new Font(mUserSettings.getSfont(), Font.PLAIN, mUserSettings.getSsize()));
        txtReceiveSample.setFont(new Font(mUserSettings.getRfont(), Font.PLAIN, mUserSettings.getRsize()));
        chkAutoAway.setSelected(mUserSettings.isEnableAutoAway());
        chkAutoLogin.setSelected(mUserSettings.isEnableAutoLogin());
        cobAutoAway.setSelectedItem((new Long(mUserSettings.getAutoAwayTime())).toString());
        cobNA.setSelectedItem((new Long(mUserSettings.getAutoNATime())).toString());
        chkAutoSaveDimensions.setSelected(mUserSettings.isAutoSaveDimensions());
        txtChatSendSample.setFont(new Font(mUserSettings.getChatFont(), Font.PLAIN, mUserSettings.getChatSize()));

        jlpOptions.add(cbTime);
        jlpOptions.add(me_in_list);
        jlpOptions.add(sound_on_receive);
        jlpOptions.add(send);
        send.add(lblSend);
        sendFS.add(cobSendFont);
        sendFS.add(cobSendSize);
        send.add(sendFS);
        send.add(txtSendSample);
        jlpOptions.add(receive);
        receive.add(lblReceive);
        receiveFS.add(cobReceiveFont);
        receiveFS.add(cobReceiveSize);
        receive.add(receiveFS);
        receive.add(txtReceiveSample);
        pnlButtons.add(save);
        pnlButtons.add(dismiss);
        jlpOptions.add(mBGColor);
        pnlMain.add(jlpOptions, "West");
        content.add(pnlButtons, "South");

        pnlAutoAway.add(lblAutoAway1);
        pnlAutoAway.add(cobAutoAway);
        pnlAutoAway.add(lblAutoAway2);
        pnlAutoAway.add(lblAutoAway3);
        pnlAutoAway.add(cobNA);
        pnlAutoAway.add(lblAutoAway4);
        pnlAdvanced.add(chkAutoAway);
        pnlAdvanced.add(pnlAutoAway);
        pnlAdvanced.add(pnlChatFont);
        pnlAdvanced.add(chkAutoLogin);
        pnlAdvanced.add(chkAutoSaveDimensions);

        pnlAutoAway.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        if (mUserSettings.isEnableAutoAway()) {
            pnlAutoAway.setVisible(true);
        } else {
            pnlAutoAway.setVisible(false);
        }

        chkAutoAway.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (chkAutoAway.isSelected()) {
                    pnlAutoAway.setVisible(true);
                } else {
                    pnlAutoAway.setVisible(false);
                }
            }
        });

        jtpPane.add(CommonValues.locale.Main, pnlMain);
        jtpPane.add(CommonValues.locale.Advanced, pnlAdvanced);
        content.add("Center", jtpPane);

        final ActionListener savelistner = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                mUserSettings.setShowtime(cbTime.isSelected());
                mUserSettings.setMe_in_list(me_in_list.isSelected());
                mUserSettings.setBeep(sound_on_receive.isSelected());
                mUserSettings.setRfont((String) cobReceiveFont.getSelectedItem());
                mUserSettings.setSfont((String) cobSendFont.getSelectedItem());
                mUserSettings.setChatFont((String) cobChatFont.getSelectedItem());
                mUserSettings.setmBGColor(mColor.getBackground());
                Long temp = (Long) cobReceiveSize.getSelectedItem();
                mUserSettings.setRsize(temp.intValue());
                temp = (Long) cobSendSize.getSelectedItem();
                mUserSettings.setSsize(temp.intValue());
                temp = (Long) cobChatSize.getSelectedItem();
                mUserSettings.setChatSize(temp.intValue());
                mUserSettings.setEnableAutoAway(chkAutoAway.isSelected());
                mUserSettings.setEnableAutoLogin(chkAutoLogin.isSelected());
                mUserSettings.setAutoAwayTime(Integer.parseInt((String) cobAutoAway.getSelectedItem()));
                mUserSettings.setAutoSaveDimensions(chkAutoSaveDimensions.isSelected());
                mUserSettings.setAutoNATime(Integer.parseInt((String) cobNA.getSelectedItem()));
                try {
                    LSSettings.write(mUserSettings);
                } catch (IOException ioex) {
                    System.err.println("Cannot write Setting's file.");
                }
                mSender.getChat().setBGColor(mUserSettings.getmBGColor());
                dispose();
            }
        };
        save.addActionListener(savelistner);

        final KeyListener exitlistener = new KeyListener() {
            public void keyTyped(final KeyEvent e) {
            }

            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }

            public void keyReleased(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dispose();
            }
        };
        content.addKeyListener(exitlistener);

        final ActionListener dismisslistner = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
        dismiss.addActionListener(dismisslistner);


        final ActionListener sendFontSampleListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtSendSample.setFont(new Font((String) cobSendFont.getSelectedItem(), Font.PLAIN, ((Long) cobSendSize.getSelectedItem()).intValue()));
                txtSendSample.repaint();
            }
        };
        cobSendFont.addActionListener(sendFontSampleListener);

        final ActionListener sendSizeSampleListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtSendSample.setFont(new Font((String) cobSendFont.getSelectedItem(), Font.PLAIN, ((Long) cobSendSize.getSelectedItem()).intValue()));
                txtSendSample.repaint();
            }
        };
        cobSendSize.addActionListener(sendSizeSampleListener);

        final ActionListener receiveFontSampleListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtReceiveSample.setFont(new Font((String) cobReceiveFont.getSelectedItem(), Font.PLAIN, ((Long) cobReceiveSize.getSelectedItem()).intValue()));
                txtReceiveSample.repaint();
            }
        };
        cobReceiveFont.addActionListener(receiveFontSampleListener);

        final ActionListener receiveSizeSampleListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtReceiveSample.setFont(new Font((String) cobReceiveFont.getSelectedItem(), Font.PLAIN, ((Long) cobReceiveSize.getSelectedItem()).intValue()));
                txtReceiveSample.repaint();
            }
        };
        cobReceiveSize.addActionListener(receiveSizeSampleListener);

        final ActionListener ChatFontListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtChatSendSample.setFont(new Font((String) cobChatFont.getSelectedItem(), Font.PLAIN, ((Long) cobChatSize.getSelectedItem()).intValue()));
                txtChatSendSample.repaint();
            }
        };
        cobChatFont.addActionListener(ChatFontListener);

        final ActionListener ChatSizeListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                txtChatSendSample.setFont(new Font((String) cobChatFont.getSelectedItem(), Font.PLAIN, ((Long) cobChatSize.getSelectedItem()).intValue()));
                txtChatSendSample.repaint();
            }
        };
        cobChatSize.addActionListener(ChatSizeListener);

        final JDialog mThisFrame = this;
        final ActionListener mColorListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final Color mInitialColor = mUserSettings.getmBGColor();
                final Color newColor = JColorChooser.showDialog(mThisFrame, "Choose Color", mInitialColor);
                mColor.setBackground(newColor);
                mUserSettings.setmBGColor(newColor);
            }
        };
        mColor.addActionListener(mColorListener);
    }
}
