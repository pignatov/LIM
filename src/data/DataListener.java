package data;

import audio.PrepareAudio;
import gui.InfoFrame;
import gui.Model;
import gui.ReceivedMessageFrame;
import gui.YesNoFrame;
import utils.CommonValues;
import utils.IniReader;

import javax.swing.*;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Class <code>dataListener</code>.
 * <br>Main purpose is waiting for server messages and process them
 */
public final class DataListener extends Thread {
    private final String mUIN;
    private final Users mUsers;
    private final Sender mSender;
    private final Model mModel;

    /**
     * Constructor
     *
     * @param aSender data.Sender "mega" class. Various use: in, out streams, etc
     * @param aModel  JList AbstractListModel, used for manipulating user list visualisation
     */
    public DataListener(final Sender aSender, final Model aModel, final String aUIN) {
        super();
        mSender = aSender;
        mUsers = aSender.getUI();
        mModel = aModel;
        mUIN = aUIN;
    }

    /**
     * Main thread
     */
    public void run() {
        try {
            while (true) {
                final String str;

                str = mSender.mIn.readLine();

                if (str.startsWith("+ADD")) {       // +ADD data.UIN command received
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        System.err.println("Bad protocol request.(+ADD). See FAQ question #4 for more information");
                    } else {
                        final String bulk = st.nextToken();
                        final String user_to_add = st.nextToken();
                        System.out.println(user_to_add);
                        mUsers.add(user_to_add);
                        mModel.add();
                        mSender.getChat().sysMsg(user_to_add + " entered");
                        mSender.sendRequestInfo(user_to_add);
                        IniReader.fillData(mSender.getUI());
                    }
                }

                if (str.startsWith("+REM")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        System.err.println("Bad protocol request.(+REM). See FAQ question #5 for more information");
                    } else {
                        final String bulk = st.nextToken();
                        final String user_to_remove = st.nextToken();
                        System.out.println(user_to_remove);
                        mUsers.remove(user_to_remove);
                        mModel.remove();
                        mSender.getChat().sysMsg(user_to_remove + " exited");
                    }
                }

                if (str.startsWith("+FILESENT")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() < 5) { //Something's wrong
                        System.err.println("Bad protocol request.(+REM). See FAQ question #10 for more information");
                    } else {
                        final String bulk = st.nextToken();
                        final String sendingUser = st.nextToken();
                        final String receivingUser = st.nextToken();
                        final String sfileSize = st.nextToken();
                        int position = str.indexOf(sfileSize);
                        final String fileName = str.substring(position + sfileSize.length() + 1);
                        YesNoFrame yes_no_frame = new YesNoFrame("User " + sendingUser + " sent request for file " + fileName + " with size " + sfileSize + ". Do you want to accept? ");
                        yes_no_frame.pack();
                        yes_no_frame.setModal(true);
                        yes_no_frame.setLocationRelativeTo(null);
                        yes_no_frame.setVisible(true);

                        if (yes_no_frame.Result == 0) {
                            // "Yes" was pressed
                            System.out.println("User " + sendingUser + " sent request to " + receivingUser + " for file " + fileName);
                        } else {
                            // "No" was pressed
                            // nothing to do
                        }
//                        yes_no_frame.dispose();

                    }
                }

                if (str.startsWith("+KEYACTIVE")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 3) { //Something's wrong
                        System.err.println("Bad protocol request.(+REM). See FAQ question #10 for more information");
                    } else {
                        final String bulk = st.nextToken();
                        final String sendingUser = st.nextToken();
                        final String new_key_status = st.nextToken();
                        boolean data;
                        if (new_key_status.equalsIgnoreCase("YES"))
                            data = true;
                        else if (new_key_status.equalsIgnoreCase("NO"))
                            data = false;
                        else
                            break;
                        mSender.getUI().changeTyping(sendingUser, data);
                        mModel.update();
                    }
                }

                if (str.startsWith("+STATUS")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 3) { // Something's wrong
                        System.err.println("Bad protocol request.(+STATUS). See FAQ question #6 for more information");
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        final String mnewStatus = st.nextToken();
                        mUsers.changeStatus(src, mnewStatus);
                        mModel.update();

                        if (mnewStatus.equalsIgnoreCase("OFFLINE"))
                            mSender.getChat().sysMsg(src + " exited");
                    }
                }

                if (str.startsWith("+INFO")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 3) { // Something's wrong
                        System.err.println("Bad protocol request.(+INFO). See FAQ question #6 for more information");
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        final String info = st.nextToken();
                        mSender.getUI().setIp(src, info);
                    }
                }

                if (str.startsWith("+ALIVE")) {
                    mSender.send("ALIVE " + mUIN);
                    mSender.setTimeOut(Calendar.getInstance().getTimeInMillis());
                }

                if (str.startsWith("+CHAT")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        System.err.println("Bad protocol request.(+CHAT). See FAQ question #7 for more information");
                        //..do some action
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        String bulk;
                        String message = new String();
                        while (!(bulk = mSender.mIn.readLine()).startsWith(".")) {
                            System.out.println(bulk);
                            if (!bulk.startsWith(".")) message += bulk + "\n";
                        }
                        mSender.getChat().chatMsg(src, message);
                        if (!mSender.getMainWindow().isFocused())
                            mSender.getMainWindow().setIconImage((new ImageIcon("img/mail_open.gif")).getImage());
                        else
                            mSender.getMainWindow().setIconImage((new ImageIcon("img/mail.gif")).getImage());
                    }
                }

                if (str.startsWith("+MBOARD")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        System.err.println("Bad protocol request.(+CHAT). See FAQ question #7 for more information");
                        //..do some action
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        String bulk;
                        String message = new String();
                        while (!(bulk = mSender.mIn.readLine()).startsWith(".")) {
                            System.out.println(bulk);
                            if (!bulk.startsWith(".")) message += bulk + "\n";
                        }
                        mSender.getMsgboard().BoardMsg(src, message);
                    }
                }

                if (str.startsWith("+UPDATEBOARD")) {
                    mSender.getMsgboard().jta.setText("");
                    mSender.sendReload(mUIN);
                }

                if (str.startsWith("+VOICE ")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 3) { //Something's wrong
                        System.err.println("Bad protocol request.(+VOICE). See FAQ question #11 for more information");
                        //..do some action
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        final String dest = st.nextToken();
                        final String ip = mSender.getUI().getIp(src);
                        final StringTokenizer tk = new StringTokenizer(ip, "/");
                        final String bulk = tk.nextToken();
                        final String realip = tk.nextToken();
                        final PrepareAudio jf = new PrepareAudio(src, realip);
                        jf.pack();
                        jf.setVisible(true);
                    }
                }

                if (str.startsWith("+MSG")) {
                    final StringTokenizer st = new StringTokenizer(str, " ");
                    if (st.countTokens() != 3) { //Something's wrong
                        System.err.println("Bad protocol request.(+MSG). See FAQ question #8 for more information");
                        //..do some action
                    } else {
                        String bulk = st.nextToken();
                        final String src = st.nextToken();
                        final String dest = st.nextToken();
                        String message = new String();
                        while (!(bulk = mSender.mIn.readLine()).startsWith(".")) {
                            System.out.println(bulk);
                            if (!bulk.startsWith(".")) message += bulk + "\n";
                        }
                        final entry tentry = new entry(src, dest, message, Calendar.getInstance());
                        mSender.getUI().addMsg(src, tentry);
                        mSender.hist.add(tentry);
                        //TODO
                        if (mUsers.getStatus(dest).equalsIgnoreCase("DND"))
                            mSender.getChat().dndMsg("[" + src + "] " + message);
                        else {
                            if (mSender.getUI().isPopped(src)) {
                                (mSender.getUI().getReceivedMessage(src)).enableNext();
                            } else {
                                // A message has just arrived, so the last message is ours, but a fix is needed
                                final ReceivedMessageFrame frame = new ReceivedMessageFrame(mSender, src, dest, message, mSender.getUI().getHistorySize(src) - 1);
                                mSender.getUI().setReceivedMessage(src, frame);
                                frame.pack();
                                frame.setLocationRelativeTo(null);
                                frame.setVisible(true);
                            }
                        }
                    }
                } //if
            } //while
        } catch (Exception ioe) { // NEEDS A FIX!
            mSender.getChat().chatMsg("SYSTEM", "Cannot contact server");
/*            final InfoFrame errMsg = InfoFrame.instance(CommonValues.locale.Error, "Cannot contact server", 1);
            errMsg.pack();
            errMsg.setVisible(true);
            ioe.printStackTrace();*/
        }
    }
}