package data;

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

import gui.JChat;
import gui.MessageBoardPanel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * "Super" class data.Sender. Holds virtually all information about the user
 */
public final class Sender {
    public final BufferedReader mIn;
    private final PrintWriter mOut;
    public UserSettings user = new UserSettings();
    private Users mUsers = new Users();
    private JChat jchat;
    private JFrame MainWindow;
    private long TimeOut;

    public long getTimeOut() {
        return TimeOut;
    }

    public void setTimeOut(final long timeOut) {
        TimeOut = timeOut;
    }

    public JFrame getMainWindow() {
        return MainWindow;
    }

    public void setMainWindow(final JFrame mainWindow) {
        MainWindow = mainWindow;
    }

    private MessageBoardPanel msgboard;
    private String passphrase = new String();

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(final String passphrase) {
        this.passphrase = passphrase;
    }

    public MessageBoardPanel getMsgboard() {
        return msgboard;
    }

    public void setMsgboard(final MessageBoardPanel msgboard) {
        this.msgboard = msgboard;
    }

    public JChat getChat() {
        return jchat;
    }

    public void setChat(final JChat jchat) {
        this.jchat = jchat;
    }

    public Users getUI() {
        return mUsers;
    }

    public void setUsr(final Users aUsers) {
        this.mUsers = aUsers;
    }

    public final history hist = new history();

    public UserSettings getUser() {
        return user;
    }

    public void setUser(final UserSettings user) {
        this.user = user;
    }

    /**
     * Constructor
     *
     * @param aIn  input stream
     * @param aOut output stream
     */
    public Sender(final BufferedReader aIn, final PrintWriter aOut) {
        mIn = aIn;
        mOut = aOut;
    }

    public synchronized void send(final String str) {
        mOut.println(str);
        mOut.flush();
    }

    public synchronized String readLine() throws IOException {
        return mIn.readLine();
    }

    /**
     * Sends message message from user src to user dest
     *
     * @param aSrc  Source user (for future use)
     * @param aDest Destination user
     * @param aMsg  Message
     * @return true if sending is successful
     */
    public boolean sendmsg(final String aSrc, final String aDest, final Vector aMsg) {
        send("send " + aDest);
        for (int i = 0; i < aMsg.size(); i++) {
            send((String) aMsg.get(i));
        }
        send(".");
        return true;
        // TODO Looks whether message is send successfully or not
    }


    /**
     * Sends chat message message from src
     *
     * @param aSrc Source user
     * @param aMsg Message
     * @return true if successful, false otherwise
     */
    public boolean sendchat(final String aSrc, final String aMsg) {
        send("chat " + aSrc);
        send(aMsg);
        send(".");
        return true;
        // TODO Looks whether message is send successfully or not
    }

    /**
     * Sends message board update
     *
     * @param aSrc Source user
     * @param aMsg Message
     * @return true if successful, false otherwise
     */
    public boolean sendboard(final String aSrc, final String aMsg) {
        send("board " + aSrc);
        send(aMsg);
        send(".");
        return true;
        // TODO Looks whether message is send successfully or not
    }

    /**
     * @param aSrc  (For future use)
     * @param aDest
     * @return
     */
    public boolean sendvoice(final String aSrc, final String aDest) {
        send("VOICE " + aDest);
        return true;
        // TODO Looks whether message is send successfully or not
    }

    /**
     * Erase all message board messages
     *
     * @param aSrc Source user
     * @return true if successful, false otherwise
     */
    public boolean sendEraseAll(final String aSrc) {
        send("eraseall " + aSrc);
        return true;
    }

    /**
     * Force client to reload message board messages
     *
     * @param aSrc Source user
     * @return true if successful, false otherwise
     */
    public boolean sendReload(final String aSrc) {
        send("reload " + aSrc);
        return true;
    }

    /**
     * Request gui.info for user src
     *
     * @param aSrc Source user
     * @return true if successful, false otherwise
     */
    public boolean sendRequestInfo(final String aSrc) {
        send("info " + aSrc);
        return true;
        // TODO Looks whether message is send successfully or not
    }

    /**
     * Sends to server notification about changing status of user
     *
     * @param aSrc       User data.UIN
     * @param aNewStatus New Status (Online/Away/NA/DND
     * @return true on success
     */
    public boolean sendChangeStatus(final String aSrc, final String aNewStatus) {
        send("stat " + aSrc + " " + aNewStatus);
        return true;
    }

    public boolean sendFileSendRequest(final String aSrc, final String aDest, final String aFileName, final long aFileSize) {
        send("filesend " + aSrc + " " + aDest + " " + aFileSize + " " + aFileName);
        return true;
    }

    public boolean sendUserActivity(final String aSrc, final boolean aValue) {
        String data = aValue ? "YES" : "NO";
        send("keyactive " + aSrc + " " + data);
        return true;
    }
}
