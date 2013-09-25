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
/* Date: Sep 29, 2002 * Time: 5:38:39 PM */

import data.Data;
import utils.CommonValues;
import utils.PasswordList;
import utils.RandomString;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

final class MsgConnection extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private final Socket socket;
    private final int port;
    private final Data mData;

    public MsgConnection(final int aPort, final Socket aSocket, final Data aData) {
        this.socket = aSocket;
        this.port = aPort;
        this.mData = aData;
    }


    private int checkUIN(final String aInput, final String aHash, final String aChallenge) {
        if (PasswordList.checkUser(aInput, aHash, aChallenge) && (!mData.exist(aInput) || (mData.getStatus(aInput) == "Offline")))
            return 1; //TODO
        else
            return 0;
    }

    private String statusInfo(final String aInfo) {
        final Calendar now = Calendar.getInstance();
        final String hour = CommonValues.convert((new Long(now.get(Calendar.HOUR_OF_DAY))).toString(), 2) + ":" + CommonValues.convert((new Long(now.get(Calendar.MINUTE))).toString(), 2) + ":" + CommonValues.convert((new Long(now.get(Calendar.SECOND))).toString(), 2);
        return "[" + hour + "] " + aInfo;
    }

    public void run() {
        boolean logged = false;
        String bulk;
        String uin = new String("NOT");
        String destuin = new String("NOT");
        final String challenge;
        String hash;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("+OK Message server ready version [" + CommonValues.serverVersion + "|" + CommonValues.protocolVersion + " build on " + CommonValues.date + "]");
            challenge = RandomString.getRandomString();
            out.println("+CHA " + challenge);

            while (!logged) {
                final String input = in.readLine().toUpperCase();
                if (input.startsWith("UIN ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 3) {    //Something is wrong
                        out.println("-ERR UIN <UIN> <PASS>");
                    } else {
                        bulk = st.nextToken();
                        uin = st.nextToken();
                        hash = st.nextToken();
                        if (checkUIN(uin, hash, challenge) > 0)
                            logged = true;
                        else {
                            out.println("-ERR UIN not valid");
                            uin = "NOT";
                        }
                    }
                } else {
                    out.println("-ERR Authorization first");
                }
            }

            //We are logged now
            out.println("+OK UIN accepted");

            System.out.println(statusInfo(uin + " connected from " + socket.getInetAddress().getHostName()));

            while (true) {
                final String input = in.readLine().toUpperCase();
                if (input.startsWith("LIST")) {
                    if (!mData.exist(uin) || (mData.getStatus(uin) == "Offline")) mData.add(uin, in, out, socket.getInetAddress());
                    out.println("+OK");
                    for (int i = 0; i < mData.size(); i++) {
                        out.println(mData.getSUIN(i) + " " + mData.getStatus(mData.getSUIN(i)));
                    }
                    out.println(".");
                    mData.sysChat(uin, CommonValues.motd());
                    mData.sendAllBMsg(uin);
                }

                if (input.startsWith("SEND ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something is wrong
                        out.println("-ERR SEND <UIN>");
                    } else {
                        bulk = st.nextToken();
                        destuin = st.nextToken();
                        final Vector msg = new Vector();
                        if (mData.exist(destuin)) { //Yes, exists
                            String inp = new String();
                            while (!inp.startsWith(".")) {
                                inp = in.readLine();
                                System.out.println(inp);
                                msg.add(inp);
                            }
                            mData.send(uin, destuin, msg);
                        } else { //No, it doesn't
                            out.println("-ERR DEST UIN not available");
                        }
                    }
                }

                if (input.startsWith("VOICE ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something is wrong
                        out.println("-ERR VOICE <UIN>");
                    } else {
                        bulk = st.nextToken();
                        destuin = st.nextToken();
                        final Vector msg = new Vector();
                        if (mData.exist(destuin)) { //Yes, exists
                            mData.voice(uin, destuin);
                        } else { //No, it doesn't
                            out.println("-ERR DEST UIN not available");
                        }
                    }
                }

                if (input.startsWith("STAT ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 3) { //Something is wrong
                        out.println("-ERR STAT UIN newStatus");
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        final String status = st.nextToken();
                        mData.changeStatus(src, status);
                        mData.status(src, status);

                    }
                }
                /**
                 * Synopsis INFO UIN
                 * Returns +OK <code>IP</code>
                 */
                if (input.startsWith("INFO ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        out.println("-ERR INFO UIN");
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        out.println("+INFO " + src + " " + mData.getInetAddress(src));
                    }
                }

                /**
                 * Synopsis ALIVE UIN
                 * Returns +ALIVE <code>UIN</code>
                 */
                if (input.startsWith("ALIVE ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        out.println("-ERR ALIVE UIN");
                    } else {
                        String src = st.nextToken();
                        src = st.nextToken();
                        mData.refreshAlive(src);
                    }
                }

                /**
                 * Synopsis Chat UIN
                 * Returns +OK <code>IP</code>
                 */
                if (input.startsWith("CHAT ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something is wrong
                        out.println("-ERR CHAT <UIN>");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcuin = st.nextToken();
                        String message = new String();
                        if (mData.exist(srcuin)) { //Yes, exists
                            String inp = new String();
                            while (!inp.startsWith(".")) {
                                inp = in.readLine();
                                message += inp;
                            }
                            mData.chat(uin, message);
                            //sending
                        } else { //No, it doesn't
                            out.println("-ERR CHAT not available");
                        }
                    }
                }

                /**
                 * Synopsis Board  UIN
                 */
                if (input.startsWith("BOARD ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        out.println("-ERR BOARD <UIN>");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcuin = st.nextToken();
                        String message = new String();
                        if (mData.exist(srcuin)) { //Yes, exists
                            String inp = new String();
                            while (!inp.startsWith(".")) {
                                inp = in.readLine();
                                message += inp;
                            }
                            mData.boardMsg(uin, message);
                            //sending
                        } else { //No, it doesn't
                            out.println("-ERR BOARD not available");
                        }
                    }
                }

                /**
                 * Synopsis ERASEALL UIN
                 */
                if (input.startsWith("ERASEALL ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        out.println("-ERR ERASEALL <UIN>");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcuin = st.nextToken();
                        final String message = new String();
                        if (mData.exist(srcuin)) { //Yes, this user exists
                            //Erase all messages of srcuin
                            mData.eraseAllMsg(srcuin);
                            //Inform all other data.Users to reload message board contents
                            mData.sysMBUpdate(srcuin);
                        } else { //No, it doesn't
                            out.println("-ERR ERASEALL not available");
                        }
                    }
                }

                /**
                 * Synopsis Board  data.UIN
                 */
                if (input.startsWith("RELOAD ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 2) { //Something's wrong
                        out.println("-ERR RELOAD <UIN>");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcuin = st.nextToken();
                        final String message = new String();
                        if (mData.exist(srcuin)) { //Yes, exists
                            mData.sendAllBMsg(srcuin);
                            //sending
                        } else { //No, it doesn't
                            out.println("-ERR RELOAD not available");
                        }
                    }
                }

                /**
                 * Synopsis Board  FILESEND
                 */
                if (input.startsWith("FILESEND ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() < 5) { //Something's wrong
                        out.println("-ERR FILESEND <SRCUIN> <DESTUIN> <FILESIZE> <FILENAME>");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcUIN = st.nextToken();
                        final String destUIN = st.nextToken();
                        final String fileSize = st.nextToken();
                        int position = input.indexOf(fileSize);
                        final String fileName = input.substring(position + fileSize.length() + 1);

                        if (mData.exist(srcUIN)) { // Yes, exists
                            mData.sendFileRequest(srcUIN, destUIN, fileSize, fileName);
                            // sending file request
                        } else { // No, it doesn't
                            out.println("-ERR FILESEND not available");
                        }
                    }
                }

                if (input.startsWith("FILE ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() < 5) { //Something's wrong
                        out.println("-ERR FILE <SRCUIN> <DESTUIN> <PACKET> <SIZE> ENCODED");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcUIN = st.nextToken();
                        final String destUIN = st.nextToken();
                        final String sPacket = st.nextToken();
                        final String sSize = st.nextToken();
                        int size = Integer.parseInt(sSize);
                        int packet = Integer.parseInt(sPacket);
                        int position = input.indexOf(sSize);
                        String Data = input.substring(position + sSize.length() + 1, size);

/*                        if (mData.exist(srcUIN)) { // Yes, exists
                           mData.sendFileRequest(srcUIN, destUIN, fileSize, fileName);
                            // sending file request
                        } else { // No, it doesn't
                            out.println("-ERR FILESEND not available");
                        }*/
                    }
                }

                /**
                 * Synopsis Board  keyactive
                 */
                if (input.startsWith("KEYACTIVE ")) {
                    final StringTokenizer st = new StringTokenizer(input, " ");
                    if (st.countTokens() != 3) { //Something's wrong
                        out.println("-ERR KEYACTIVE <SRCUIN> YES|NO");
                        //..do some action
                    } else {
                        bulk = st.nextToken();
                        final String srcUIN = st.nextToken();
                        final String value = st.nextToken();

                        if (mData.exist(srcUIN)) { // Yes, exists
                            mData.userKeyActive(srcUIN, value);
                        } else { // No, it doesn't
                            out.println("-ERR KEYACTIVE not available");
                        }
                    }
                }

                if ("QUIT".equalsIgnoreCase(input)) {
                    out.println("+OK Bye.");
                    mData.remove(uin);
                    System.out.println(statusInfo(uin + " left"));
                    socket.close();
                    return;
                }
            }
        } catch (Exception ex) {
            if (!uin.equals("NOT")) mData.remove(uin);
            System.out.println(statusInfo(uin + " left"));
        }
    }
}

final class MsgServer extends Thread {
    private final int Port;
    private static final Data mData = new Data();

    public MsgServer() {
        this.Port = 1001;
    }

    public MsgServer(final int Port) {
        this.Port = Port;
    }

    public void run() {
        try {
            Vector users = PasswordList.getRegisteredUsers();
            for (int i = 0; i < users.size(); i++){
                mData.add((String)users.get(i), null, null, null);
                mData.changeStatus((String)users.get(i), "Offline");
            }

            try{
               Vector mbMessages = new Vector();
               mbMessages = data.LSMessageBoard.read();
               mData.setMBItems(mbMessages);
            }catch (Exception ex){
                // We cannot read saved messageboard items
            }

            final ServerSocket s = new ServerSocket(Port);
            System.out.println("<MsgServer> Server started on port: " + Port);

            class keepAliveTask extends TimerTask {
                public void run() {
                    MsgServer.mData.keepAlive();
                }
            }
            final Timer timerKeepAlive = new Timer();
            timerKeepAlive.scheduleAtFixedRate(new keepAliveTask(), CommonValues.refresh_interval, CommonValues.refresh_interval);

            class diedUsersTask extends TimerTask {
                public void run() {
                    MsgServer.mData.diedUsers();
                }
            }
            final Timer timerDiedUsers = new Timer();
            timerDiedUsers.scheduleAtFixedRate(new diedUsersTask(), CommonValues.check_for_dead_users, CommonValues.check_for_dead_users);

            while (true) {
                final Socket socket = s.accept();
                final MsgConnection msg = new MsgConnection(Port, socket, MsgServer.mData);
                msg.start();
            }
        } catch (Exception ex) {
            System.out.println("Unable to open server socket. Possible reasons: ");
            System.out.println("\t1)Insufficient rights? (only root can start server)");
            System.out.println("\t2)Another server running on " + Port);
            System.out.println("\t1)Wrong JDK. Server needs at least JDK1.1+, client - JDK1.3+");
        }
    }
}