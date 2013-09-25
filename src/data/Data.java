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
/* Date: Sep 29, 2002 * Time: 6:16:59 PM */

import utils.CommonValues;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;


final class MBItem implements Serializable{
    private String mUser;
    private String mMessage;

    public MBItem(final String aUser, final String aMessage) {
        this.mUser = aUser;
        this.mMessage = aMessage;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(final String aUser) {
        this.mUser = aUser;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(final String aMessage) {
        this.mMessage = aMessage;
    }
}

public final class Data {
    private final Vector users = new Vector();
    private final Hashtable extra = new Hashtable();
    private Vector messages = new Vector();


    public Vector getMBItems(){
        return messages;
    }

    public void setMBItems(Vector aMBItems){
        messages = (Vector)aMBItems.clone();
    }

    static final class extraFlds {
        String status;
        InetAddress iAddress;
        // passphrase is used when encrypting/decrypting strings
        String passphrase;
        long lastAlive = (Calendar.getInstance()).getTimeInMillis();

        public String getPassphrase() {
            return passphrase;
        }

        public void setPassphrase(final String passphrase) {
            this.passphrase = passphrase;
        }

        public long getLastAlive() {
            return lastAlive;
        }

        public void setLastAlive(final long lastAlive) {
            this.lastAlive = lastAlive;
        }

        public InetAddress getiAddress() {
            return iAddress;
        }

        public void setiAddress(final InetAddress iAddress) {
            this.iAddress = iAddress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(final String status) {
            this.status = status;
        }
    }

    public synchronized boolean add(final String aUIN, final BufferedReader in, final PrintWriter out, final InetAddress inetAddress) {
        UIN _uin;
        if (exist(aUIN) && (getStatus(aUIN) == "Offline")){
            UIN temp_uin = retUIN(aUIN);
            temp_uin.setIn(in);
            temp_uin.setOut(out);
            setInetAddress(aUIN, inetAddress);
            changeStatus(aUIN, "Online");
            for (int i = 0; i < size(); i++) {
                _uin = get(i);
                if (_uin.getOut() != null)
                    _uin.getOut().println("+STATUS " + aUIN + " " + "Online");
            }
            extraFlds temp = (extraFlds) extra.get(aUIN);
            temp.setLastAlive((Calendar.getInstance()).getTimeInMillis());
            return true;
        }
        if (exist(aUIN)) {
            return false;
        }
        {
            final UIN uin = new UIN(aUIN, in, out);
            users.add(uin);
            extraFlds temp = new extraFlds();
            temp.setStatus("Online");
            extra.put(aUIN.toUpperCase(), temp);
            setInetAddress(aUIN, inetAddress);
            for (int i = 0; i < size(); i++) {
                _uin = get(i);
                changeStatus(aUIN, "Online");
            temp = (extraFlds) extra.get(aUIN);
            temp.setLastAlive((Calendar.getInstance()).getTimeInMillis());
                if (_uin.getOut() != null)
                    _uin.getOut().println("+ADD " + aUIN);
                if (_uin.getOut() != null)
                    _uin.getOut().println("+STATUS " + aUIN + " " + "Online");
            }
            return true;
        }
    }

    public synchronized boolean changeStatus(final String user, final String newStatus) {
        final extraFlds temp = (extraFlds) extra.get(user);
        extra.remove(user);
        temp.setStatus(newStatus);
        extra.put(user.toUpperCase(), temp);
        if (newStatus.equalsIgnoreCase("Offline")){
            UIN uin = retUIN(user);
            try{
            uin.getIn().close();
            uin.getOut().close();
            }catch (Exception ex){

            }finally{
            uin.setIn(null);
            uin.setOut(null);
            }
        }
        return true;
    }

    public synchronized boolean setPassphrase(final String aUser, final String aPassphrase) {
        final extraFlds temp = (extraFlds) extra.get(aUser);
        temp.setPassphrase(aPassphrase);
        extra.put(aUser.toUpperCase(), temp);
        return true;
    }

    public synchronized String getPassphrase(final String user) {
        return ((extraFlds) extra.get(user)).getPassphrase();
    }

    private synchronized boolean setInetAddress(final String user, final InetAddress ia) {
        final extraFlds temp = (extraFlds) extra.get(user);
        extra.remove(user);
        temp.setiAddress(ia);
        extra.put(user.toUpperCase(), temp);
        return true;
    }

    public synchronized String getStatus(final String user) {
        return ((extraFlds) extra.get(user)).getStatus();
    }

    public InetAddress getInetAddress(final String user) {
        return ((extraFlds) extra.get(user)).getiAddress();
    }

    public synchronized void status(final String src, final String status) {
        UIN muin;
        for (int i = 0; i < size(); i++) {
            muin = get(i);
            if (muin.getOut() != null)
            muin.getOut().println("+STATUS " + src + " " + status);
        }
    }

    public synchronized void keepAlive() {
        UIN muin;
        for (int i = 0; i < size(); i++) {
            muin = get(i);
            if (muin.getOut() != null)
            muin.getOut().println("+ALIVE ");
        }
    }

    public synchronized void refreshAlive(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        final long now = (Calendar.getInstance()).getTimeInMillis();
        if (Math.abs(now - temp.getLastAlive()) > CommonValues.alive_user_slow_response) { //user has almost died: slow response
//            if (getStatus(user) != "Offline")
//                remove(user);
        } else
            temp.setLastAlive(now);
    }

    public synchronized void diedUsers() {
        UIN muin;
        for (int i = 0; i < size(); i++) {
            muin = get(i);
            final extraFlds temp = (extraFlds) extra.get(muin.getUIN());
            final long now = (Calendar.getInstance()).getTimeInMillis();
            if (Math.abs(now - temp.getLastAlive()) > CommonValues.alive_user_limit) { //Nothing heard from user in last 20 sec.
//                if (getStatus(muin.getUIN()) != "Offline")
//                    remove(muin.getUIN());
            }
        }
    }

    public synchronized boolean remove(final String _UIN) {
        int user_to_remove = -1;
        if (!exist(_UIN)) return true; //user doesn't exist in reality ;-) It doesn't matter whether we'll return false or true
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (uin.getUIN().equalsIgnoreCase(_UIN))
                user_to_remove = i;
            else
            {
                changeStatus(_UIN, "Offline");
                UIN _uin = get(i);
                if (_uin.getOut() != null)
                _uin.getOut().println("+STATUS " + _UIN + " " + "Offline");
            }
        }
        if (user_to_remove > -1) {
            return true;
        } else
            return false;
    }

    public synchronized boolean exist(final String aUIN) {
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (aUIN.equalsIgnoreCase(uin.getUIN())) return true;
        }
        return false;
    }

    private synchronized UIN get(final int aIndex) {
        return (UIN) users.get(aIndex);
    }

    private synchronized UIN retUIN(final String aUIN) {
        UIN uin;
        for (int i = 0; i < size(); i++) {
            uin = get(i);
            if (uin.getUIN().equalsIgnoreCase(aUIN)) return uin;
        }
        return null;
    }

    public String getSUIN(final int i) {
        return ((UIN) users.get(i)).getUIN();
    }

    public synchronized boolean send(final String aSrcUIN, final String aDestUIN, final Vector aMessage) {
        final PrintWriter out = retUIN(aDestUIN).getOut();
        if (out != null){
            out.println("+MSG " + aSrcUIN + " " + aDestUIN + "\n");
            for (int i = 0; i < aMessage.size(); i++) {
                out.println((String) aMessage.get(i));
            }
        }
        return true;
    }

    public synchronized boolean sendFileRequest(final String aSrcUIN, final String aDestUIN, final String aFileSize, final String aFileName) {
        final PrintWriter out = retUIN(aDestUIN).getOut();
        out.println("+FILESENT " + aSrcUIN + " " + aDestUIN + " " + aFileSize + " " + aFileName + "\n");
        return true;
    }

    public synchronized boolean voice(final String srcUIN, final String destUIN) {
        final PrintWriter out = retUIN(destUIN).getOut();
        out.println("+VOICE " + srcUIN + " " + destUIN + "\n");
        return true;
    }


    public synchronized boolean chat(final String srcUIN, final String message) {
        UIN muin;
        for (int i = 0; i < size(); i++) {
            muin = get(i);
            if (muin.getOut() != null){
                muin.getOut().println("+CHAT " + srcUIN);
                muin.getOut().println(message);
                muin.getOut().println(".");
            }
        }
        return true;
    }

    public synchronized boolean userKeyActive(final String srcUIN, final String value) {
        UIN muin;
        if (!(value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("NO")))
            return false;
        for (int i = 0; i < size(); i++) {
            muin = get(i);
            if (muin.getOut() != null)
                muin.getOut().println("+KEYACTIVE " + srcUIN + " " + value);
        }
        return true;
    }

    public synchronized boolean sendAllBMsg(final String aUser) {
        for (int i = 0; i < MBsize(); i++) {
            final MBItem mbitem = getMBmsg(i);
            board1Msg(aUser, mbitem.getUser(), mbitem.getMessage());
        }
        return true;
    }

    public synchronized boolean eraseAllMsg(final String aUser) {
        for (int i = 0; i < MBsize(); i++) {
            final MBItem mbitem = getMBmsg(i);
            if (aUser.equalsIgnoreCase(mbitem.getUser())) {
                messages.remove(i);
                --i;
            }
        }
        try{
           data.LSMessageBoard.write(messages);
        }catch (Exception ex){
            // We cannot save messageboard items
        }
        return true;
    }

    public synchronized boolean boardMsg(final String destUIN, final String message) {
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (!uin.getUIN().equalsIgnoreCase(destUIN)) {
                if (uin.getOut() != null){
                    uin.getOut().println("+CHAT " + "System");
                    uin.getOut().println("Message Board was updated by " + destUIN);
                    uin.getOut().println(".");
                }
            }
        }
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (!uin.getUIN().equalsIgnoreCase(destUIN)) {
                if (uin.getOut() != null){
                    uin.getOut().println("+MBOARD " + destUIN);
                    uin.getOut().println(message);
                    uin.getOut().println(".");
                }
            }
        }
        addMBmsg(destUIN, message);
        return true;
    }

    private synchronized boolean board1Msg(final String oneuser, final String destUIN, final String message) {
        UIN uin;
        for (int i = 0; i < size(); i++) {
            uin = get(i);
            if (uin.getUIN().equalsIgnoreCase(oneuser)) {
                if (uin.getOut() != null){
                    uin.getOut().println("+MBOARD " + destUIN);
                    uin.getOut().println(message);
                    uin.getOut().println(".");
                }
            }
        }
        return true;
    }

    public synchronized boolean sysChat(final String destUIN, final String message) {
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (uin.getUIN().equalsIgnoreCase(destUIN)) {
                if (uin.getOut() != null){
                    uin.getOut().println("+CHAT " + "System");
                    uin.getOut().println(message);
                    uin.getOut().println(".");
                }
            }
        }
        return true;
    }

    public synchronized boolean sysMBUpdate(final String destUIN) {
        for (int i = 0; i < size(); i++) {
            final UIN uin = get(i);
            if (!uin.getUIN().equalsIgnoreCase(destUIN)) {
                if (uin.getOut() != null){
                    uin.getOut().println("+UPDATEBOARD");
                }
            }
        }
        return true;
    }

    private synchronized void addMBmsg(final String user, final String message) {
        final MBItem mb = new MBItem(user, message);
        messages.add(mb);
        try{
           data.LSMessageBoard.write(messages);
        }catch (Exception ex){
            // We cannot save messageboard items
        }
    }

    private synchronized int MBsize() {
        return messages.size();
    }

    private synchronized MBItem getMBmsg(final int i) {
        return (MBItem) messages.get(i);
    }

    public synchronized int size() {
        return users.size();
    }
}
