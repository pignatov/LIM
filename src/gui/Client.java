package gui;

/*************************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
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
/* Date: Sep 30, 2002 * Time: 9:23:22 PM */

import audio.PrepareAudio;
import data.*;
import utils.CommonValues;
import utils.IniReader;
import utils.MD5sum;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Main Program Window
 */
final class MainWindowFrame extends JFrame {
    private JList mUsersList;
    private Sender mSender;
    final boolean inside_settings = false;
    public int last_index;
    private long inactivity;
    private boolean setToAwayInactivity = false;
    private JComboBox cmbStatus;
    private final String UIN;

    /**
     * Constructor
     *
     * @param aUIN      User, who has logged in
     * @param aPassword Password
     * @param aHost     list of hosts, where possible server might be started
     */
    public MainWindowFrame(final String aUIN, final String aPassword, final String aHost, final boolean isHash) throws Exception {
        super(aUIN);
        UIN = aUIN;
        String host = aHost;
        final String password = aPassword;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container content = getContentPane();
        setIconImage((new ImageIcon("img/mail.gif")).getImage());

        final JButton btnSettings = new JButton(CommonValues.locale.Settings, new ImageIcon("img/settings.gif"));
        btnSettings.setFont(new Font("Verdana", Font.PLAIN, 10));
        final JButton btnQuit = new JButton(CommonValues.locale.Quit, new ImageIcon("img/exit.gif"));
        btnQuit.setFont(new Font("Verdana", Font.PLAIN, 10));

        final BufferedReader in;
        final PrintWriter out;
/*        try {*/
        {
            final StringTokenizer tokenizer = new StringTokenizer(host, ":");
            String result = new String();
            Socket socket = new Socket();
            while (tokenizer.hasMoreTokens()) {
                result = tokenizer.nextToken();
                try {
                    socket = new Socket(result, 1001);
                    break;
                } catch (Exception ex) {
                    System.err.println(result + " is not a ChatServer host");
                }
            }
            if (!socket.isConnected()) throw new IOException();
            host = result;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
/*        } catch (IOException ioe) {
            System.err.println("Can not establish connection to ");
            JFrame frame = new InfoFrame("Error", "Can not establish connection to server", 1);
            frame.pack();
            frame.setVisible(true);

            ioe.printStackTrace();
            dispose();
        }*/
        }

        setIconImage((new ImageIcon("img/mail.gif")).getImage());

        mSender = new Sender(in, out);
        IniReader.fillData(mSender.getUI());
        mSender.setTimeOut(Calendar.getInstance().getTimeInMillis());

        final JChat chat = new JChat(UIN, mSender);
        final MessageBoardPanel msgboard = new MessageBoardPanel(UIN, mSender);
        final AboutPanel about = new AboutPanel();
        mSender.setChat(chat);
        mSender.setMsgboard(msgboard);
        mSender.setMainWindow(this);
        final JFrame mainWin = this;

        final JTabbedPane jtp = new JTabbedPane();
        jtp.addTab(CommonValues.locale.CommonChat, chat);
        jtp.addTab(CommonValues.locale.MessageBoard, msgboard);
        jtp.addTab(CommonValues.locale.About, about);

        try {
            String message;
            message = in.readLine();
            System.out.println(message);
            message = in.readLine();
            final String challenge = message.substring(5);
            System.out.println("Challenge: " + challenge);
            final String tempHash;
            if (isHash)
                tempHash = password;
            else
                tempHash = MD5sum.hash(password);
            final String mHashPassword = MD5sum.hash(tempHash + challenge);
            out.println("UIN " + UIN + " " + mHashPassword + "\r\n");
            out.flush();
            message = in.readLine().toUpperCase();
            if (message.startsWith("-ERR")) {
                throw new Exception("Cannot Login");
            }
            mSender.setPassphrase(mHashPassword);
            out.println("LIST \r\n");
            out.flush();
            message = in.readLine();
            System.out.println(message);
            while (!message.startsWith(".")) {
                final StringTokenizer st = new StringTokenizer(message, " ");
                if (st.countTokens() != 2) { //Something's wrong
                    //..do some action
                } else {
                    final String src = st.nextToken();
                    if (!src.startsWith(".")) mSender.getUI().add(src);
                    final String mnewStatus = st.nextToken();
                    mSender.getUI().changeStatus(src, mnewStatus);
                    mSender.sendRequestInfo(src);
                    IniReader.fillData(mSender.getUI());
                }
                message = in.readLine();
                System.out.println(message);
            }
        } catch (Exception ioe) {
            chat.chatMsg("SYSTEM", "Cannot Login");
/*            final InfoFrame msg = InfoFrame.instance(CommonValues.locale.Error, "Cannot Login", 1);
            msg.pack();
            msg.setVisible(true);
            this.dispose();*/
        }

        final Model list_model = new Model(mSender.getUI());
        final DataListener listener = new DataListener(mSender, list_model, UIN);
        listener.start();

        mUsersList = new JList(list_model);
        mUsersList.setCellRenderer(new MyCellRenderer(mSender.getUI()));
        mUsersList.setBackground(new Color(192, 192, 192));
        final JScrollPane scrollpane = new JScrollPane(mUsersList);
        final JPanel panel = new JPanel(new BorderLayout());
        final JPanel pnlButtons = new JPanel(new GridLayout(3, 1));

        final JCheckBox btnShowOffline = new JCheckBox( new ImageIcon("img/offline.gif"), false);

        btnShowOffline.setSelectedIcon(new ImageIcon("img/online.gif"));
        btnShowOffline.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a){
                mSender.getUI().setShowOfflineUsers(btnShowOffline.isSelected());
                list_model.update();
            }
        });

        final JPanel pnlAdditionalButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAdditionalButtons.add(btnShowOffline);
        final String[] sfont = {CommonValues.locale.Online, CommonValues.locale.Away, CommonValues.locale.DND, CommonValues.locale.NotAvailable, CommonValues.locale.Offline};

        cmbStatus = new JComboBox(sfont);
        cmbStatus.setFont(new Font("Verdana", Font.PLAIN, 11));

        cmbStatus.setSelectedIndex(0);
        final ComboCellRenderer mComboRenderer = new ComboCellRenderer();
        cmbStatus.setRenderer(mComboRenderer);

        try {
            mSender.user = LSSettings.read();
            mSender.getChat().setBGColor(mSender.user.getmBGColor());
        } catch (IOException ioex) {
            System.err.println("Cannot read Setting's file.");
            mSender.getChat().setBGColor(mSender.user.getmBGColor());
        }

        mSender.getChat().setChatFont(new Font(mSender.user.getChatFont(), Font.PLAIN, mSender.user.getChatSize()));

        final ActionListener statuslistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ComboCellRenderer.setLastindex(cmbStatus.getSelectedIndex());
                String mnewStatus = new String();
                switch (cmbStatus.getSelectedIndex()) {
                    case 0:
                        mnewStatus = "Online";
                        break;
                    case 1:
                        mnewStatus = "Away";
                        break;
                    case 2:
                        mnewStatus = "DND";
                        break;
                    case 3:
                        mnewStatus = "NA";
                        break;
                    case 4:
                        mnewStatus = "Offline";
                        mSender.getUI().changeStatusAll("Offline");
                        list_model.update();
                        break;
                }
                mSender.sendChangeStatus(UIN, mnewStatus);
            }
        };
        cmbStatus.addActionListener(statuslistener);

        final WindowFocusListener focus = new WindowFocusListener() {
            public void windowLostFocus(final WindowEvent e) {

            }

            public void windowGainedFocus(final WindowEvent e) {
                mainWin.setIconImage((new ImageIcon("img/mail.gif")).getImage());
            }
        };
        addWindowFocusListener(focus);


        pnlButtons.add(cmbStatus);
        pnlButtons.add(btnSettings);
        pnlButtons.add(btnQuit);

        panel.add(pnlAdditionalButtons, "North");
        panel.add(pnlButtons, "South");
        content.setLayout(new BorderLayout());
        content.add(panel, "West");
        content.add(jtp, "Center");
        panel.add(scrollpane, "Center");

        final MouseListener listclick = new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    final int index = mUsersList.locationToIndex(e.getPoint());
                    final JFrame msg = new SendMessageFrame(mSender, UIN, mSender.getUI().getAt(index));
                    msg.pack();
                    msg.setLocationRelativeTo(content);
                    msg.setVisible(true);
                }
            }
        };
        mUsersList.addMouseListener(listclick);

        final ActionListener exitlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(-1);
            }
        };
        btnQuit.addActionListener(exitlistener);

        final ActionListener settingsListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (true) {
                    final JDialog frame = new SettingsDialog(mSender);
                    frame.pack();
                    frame.setLocationRelativeTo(content);
                    frame.setModal(true);
                    frame.setVisible(true);
                }
                mSender.getChat().setBackground(mSender.user.getmBGColor());
                mSender.getChat().setChatFont(new Font(mSender.user.getChatFont(), Font.PLAIN, mSender.user.getChatSize()));
            }
        };
        btnSettings.addActionListener(settingsListener);

        final JPopupMenu popupMenu = new JPopupMenu();
        final ActionListener sendPopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int index = mUsersList.getSelectedIndex();
                final JFrame msg = new SendMessageFrame(mSender, UIN, mSender.getUI().getAt(index));
                msg.pack();
                msg.setLocationRelativeTo(content);
                msg.setVisible(true);
            }
        };

        final ActionListener infoPopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int index = mUsersList.getSelectedIndex();
                final UserInfo infoMsg = new UserInfo(mSender.getUI().getAt(index), mSender);
                infoMsg.setResizable(false);
                infoMsg.pack();
                infoMsg.setLocationRelativeTo(null);
                infoMsg.setVisible(true);
            }
        };

        final ActionListener historyPopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int index = mUsersList.getSelectedIndex();
                final MessageHistoryFrame msg_history_frame = new MessageHistoryFrame(mSender, mSender.getUI().getAt(index), UIN);
                msg_history_frame.pack();
                msg_history_frame.setLocationRelativeTo(content);
                msg_history_frame.setVisible(true);
            }
        };

        final ActionListener sendFilePopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int index = mUsersList.getSelectedIndex();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(content);
                File file = fileChooser.getSelectedFile();

                if (file != null) {
                    mSender.sendFileSendRequest(UIN, mSender.getUI().getAt(index), file.getName(), file.length());
                }
            }
        };

        final ActionListener voicePopupListener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int index = mUsersList.getSelectedIndex();
                final String src = mSender.getUI().getAt(index);
                final String ip = mSender.getUI().getIp(src);
                final StringTokenizer tk = new StringTokenizer(ip, "/");
                final String bulk = tk.nextToken();
                final String realip = tk.nextToken();
                mSender.sendvoice(UIN, src);
                final PrepareAudio prepare_audio = new PrepareAudio(mSender.getUI().getAt(index), realip);
                prepare_audio.pack();
                prepare_audio.setVisible(true);
            }
        };

        // Send Message menuItem
        final JMenuItem miSend = new JMenuItem(CommonValues.locale.SendMessage, new ImageIcon("img/send.gif"));
        miSend.addActionListener(sendPopupListener);
        popupMenu.add(miSend);
        final JMenuItem miHistory = new JMenuItem(CommonValues.locale.History, new ImageIcon("img/history.gif"));
        miHistory.addActionListener(historyPopupListener);
        popupMenu.add(miHistory);
        final JMenuItem miInfo = new JMenuItem(CommonValues.locale.ViewInfo, new ImageIcon("img/info.gif"));
        miInfo.addActionListener(infoPopupListener);
        popupMenu.add(miInfo);
        final JMenuItem miVoice = new JMenuItem(CommonValues.locale.VoiceRequest, new ImageIcon("img/voice.gif"));
        miVoice.addActionListener(voicePopupListener);
        popupMenu.add(miVoice);
        // Temporary : For the PC Magazine Edition 01/10/2004
/*      final JMenuItem miSendFile = new JMenuItem(CommonValues.locale.SendFile, new ImageIcon("img/send.gif"));
        miSendFile.addActionListener(sendFilePopupListener);
        popupMenu.add(miSendFile);*/

        final WindowListener window_listener = new WindowListener() {
            public void windowActivated(final WindowEvent e) {
            }

            public void windowOpened(final WindowEvent e) {
            }

            public void windowClosing(final WindowEvent e) {
                if (mSender.user.isAutoSaveDimensions()) {
                    mSender.user.setHeigth(e.getWindow().getHeight());
                    mSender.user.setWidth(e.getWindow().getWidth());
                    try {
                        LSSettings.write(mSender.user);
                    } catch (IOException ex) {
                        // Cannot save settings
                        System.err.println("Cannot save settings");
                    }
                }
            }

            public void windowClosed(final WindowEvent e) {
            }

            public void windowDeactivated(final WindowEvent e) {
            }

            public void windowIconified(final WindowEvent e) {
            }

            public void windowDeiconified(final WindowEvent e) {
            }
        };
        this.addWindowListener(window_listener);

        // Set the component to show the popup popupMenu
        mUsersList.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                    mUsersList.setSelectedIndex(mUsersList.locationToIndex(evt.getPoint()));
                }
            }

            public void mouseReleased(final MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                    mUsersList.setSelectedIndex(mUsersList.locationToIndex(evt.getPoint()));
                }
            }
        });
        mSender.getChat().sysMsg(CommonValues.version);

        final ActionListener stillOnline = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (Calendar.getInstance().getTimeInMillis() - mSender.getTimeOut() > CommonValues.noServer) {
                    chat.chatMsg("SYSTEM", CommonValues.locale.CannotAccess);
/*                    final InfoFrame errMsg = InfoFrame.instance(CommonValues.locale.Error, CommonValues.locale.CannotAccess, 1);
                    errMsg.pack();
                    errMsg.setVisible(true);*/
                }
            }
        };
        final Timer timerStillOnline = new Timer(CommonValues.refresh_interval, stillOnline);
        timerStillOnline.start();
        final InactivityListener inactivity_listener = new InactivityListener();
        Toolkit.getDefaultToolkit().addAWTEventListener(inactivity_listener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        final InactivityPoll inactivity_poll = new InactivityPoll(3);
        inactivity_poll.start();
    }

    private final class InactivityListener implements AWTEventListener {
        public void eventDispatched(final AWTEvent e) {
            inactivity = 0;
            if (setToAwayInactivity) {
                cmbStatus.setSelectedIndex(0);
                setToAwayInactivity = false;
            }
        }
    }

    private final class InactivityPoll extends Thread {
        final int mSeconds;
        final Timer timer;

        public InactivityPoll(final int aSeconds) {
            this.mSeconds = aSeconds;
            final TimerListener timer_listener = new TimerListener();
            timer = new Timer(mSeconds * 1000, timer_listener);
        }

        public void run() {
            timer.start();

        }

        private final class TimerListener implements ActionListener {
            public void actionPerformed(final ActionEvent e) {
                inactivity += mSeconds;
                if (mSender.getUser().isEnableAutoAway() && inactivity > mSender.getUser().getAutoAwayTime() * 60 && cmbStatus.getSelectedIndex() == 0) {
                    mSender.sendChangeStatus(UIN, "Away");
                    cmbStatus.setSelectedIndex(1);
                    setToAwayInactivity = true;
                }
                if (mSender.getUser().isEnableAutoAway() && inactivity > (mSender.getUser().getAutoAwayTime() + mSender.getUser().getAutoNATime()) * 60
                        && cmbStatus.getSelectedIndex() == 1 && setToAwayInactivity) {
                    mSender.sendChangeStatus(UIN, "Away");
                    cmbStatus.setSelectedIndex(3);
                    setToAwayInactivity = true;
                }
            }
        }
    }
}

final class loginDialog extends JFrame {

    public loginDialog() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Container content = getContentPane();

        final JPanel fields = new JPanel(new GridLayout(3, 1));
        final JPanel passfield = new JPanel(new BorderLayout());
        final JPanel namefield = new JPanel(new BorderLayout());
        final JPanel hostfield = new JPanel(new BorderLayout());
        final JPanel buttons = new JPanel(new FlowLayout());
        final JTextField jtf = new JTextField(15);
        final JTextField hostf = new JTextField(15);
        final JPasswordField jpf = new JPasswordField(15);
        final JLabel jname = new JLabel("UIN ", Label.RIGHT);
        final JLabel jpass = new JLabel(CommonValues.locale.Password);
        final JLabel jhost = new JLabel(CommonValues.locale.Server);
        final JCheckBox rememberLogin = new JCheckBox(CommonValues.locale.RememberLogin);
        rememberLogin.setSelected(true);

        final JButton btnLogin = new JButton("Login", new ImageIcon("img/ok.gif"));
        final JButton btnExit = new JButton(CommonValues.locale.Exit);

        content.setLayout(new BorderLayout());
        setResizable(false);

        content.add(fields, "Center");

        fields.add(namefield, "Center");
        namefield.add(jname, "Center");
        namefield.add(jtf, "East");
        try {
            jtf.setText(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException uhe) {
            System.err.println("Cannot get localhost name. See FAQ:question #3 for explanation");
        }
        jtf.setToolTipText("Enter User Name");

        fields.add(passfield, "South");
        passfield.add(jpass, "Center");
        passfield.add(jpf, "East");
        jpf.setToolTipText("Enter Password");

        fields.add(hostfield);
        hostfield.add(jhost, "Center");
        hostfield.add(hostf, "East");
        hostf.setText("localhost");

        content.add(buttons, "South");
        buttons.add(btnLogin);
        buttons.add(btnExit);
        buttons.add(rememberLogin);

        LoginData ldata = new LoginData();
        try {
            ldata = new LoginData(InetAddress.getLocalHost().getHostName(), "", "localhost");
        } catch (UnknownHostException uhe) {
            System.err.println("Cannot get localhost name. See FAQ:question #3 for explanation");
        }

        try {
            ldata = LSLogin.read();

        } catch (FileNotFoundException fnfex) {
            System.err.println("Login data file not found. See FAQ:question #1 for explanation");
        }

        jtf.setText(ldata.getUIN());
        jpf.setText(ldata.getPassword());
        hostf.setText(ldata.getLocalhost());
        final String hash = ldata.getPassword();

        final ActionListener loginlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final LoginData loginInfo = new LoginData(jtf.getText(), new String(jpf.getPassword()), hostf.getText());
                if (rememberLogin.isSelected()) {
                    try {
                        if (hash.equalsIgnoreCase(loginInfo.getPassword())) // the text in the password box is still md5 hash
                            LSLogin.write(loginInfo, true);
                        else
                            LSLogin.write(loginInfo, false);
                        // Save the info
                    } catch (IOException ioex) {
                        System.err.println("Login data file I/O Error. See FAQ:question #2 for explanation");
                    }
                }
                boolean isHash = false;
                if (hash.equalsIgnoreCase(loginInfo.getPassword())) { // the text in the password box is still md5 hash
                    isHash = true;
                }
                try {
                    final JFrame frame = new MainWindowFrame(jtf.getText(), loginInfo.getPassword(), hostf.getText(), isHash);
                    frame.pack();
                    // if window size is too small do not apply window size from settings file
                    if (Client.user_settings.getHeigth() < 100 && Client.user_settings.getWidth() < 100)
                        frame.setSize(500, 300);
                    else
                        frame.setSize(Client.user_settings.getWidth(), Client.user_settings.getHeigth());
                    frame.setVisible(true);
                } catch (Exception ex) {

                }
                dispose();
            }
        };
        btnLogin.addActionListener(loginlistener);

        final ActionListener exitlistener = new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(-1);
            }
        };
        btnExit.addActionListener(exitlistener);
    }
}

final class Client extends JFrame {
    public static UserSettings user_settings;

    public Client() {
    }

    public static void main(final String[] args) {
        if (args.length > 0)
            if ("locale=BG".equalsIgnoreCase(args[0])) CommonValues.chooseLanguage("BG");

        final LoginData ldata;
        try {
            ldata = LSLogin.read();
            user_settings = LSSettings.read();

            if (user_settings.isEnableAutoLogin()) {
                final JFrame frame = new MainWindowFrame(ldata.getUIN(), ldata.getPassword(), ldata.getLocalhost(), true);
                frame.pack();
                frame.setSize(CommonValues.ClientWidth, CommonValues.ClientHeight);
                frame.setVisible(true);
            } else
                throw new Exception();
        } catch (Exception fnfex) {
            try{ user_settings = LSSettings.read();} catch(IOException fnf)
            {
                user_settings = new UserSettings();
            };
            final loginDialog login_frame = new loginDialog();
            login_frame.pack();
            login_frame.setLocationRelativeTo(null);
            login_frame.setVisible(true);
        }
      }
}