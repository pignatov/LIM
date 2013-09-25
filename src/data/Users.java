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

import gui.ReceivedMessageFrame;
import utils.IniReader;

import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

public final class Users {
    /*data contains ONLY list of data.Users*/
    private final Vector data = new Vector();
    private boolean mShowOfflineUsers;

    public boolean isShowOfflineUsers() {
        return mShowOfflineUsers;
    }

    public void setShowOfflineUsers(boolean aShowOfflineUsers) {
        this.mShowOfflineUsers = aShowOfflineUsers;
    }

    /*in this hashtable key is UIN and value is an extrafield object*/
    /**
     * <code>key</code> UIN
     * <code>value</code> object of type <code>extraflds</code>
     */
    private final Hashtable extra = new Hashtable();

    /**
     * All additional information about a given user is stored in this class
     */
    static final class extraFlds {
        String status;
        boolean poppedMsg;
        boolean Typing;

        public boolean isTyping() {
            return Typing;
        }

        public void setTyping(boolean aTyping) {
            Typing = aTyping;
        }

        public extraFlds() {
        }

        public Color getColor() {
            return color;
        }

        public void setColor(final Color color) {
            this.color = color;
        }

        history hist = new history();
        String host = "0.0.0.0";
        String ip = "0.0.0.0";
        Color color = new Color(0, 0, 0);

        public String getHost() {
            return host;
        }

        public void setHost(final String host) {
            this.host = host;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(final String ip) {
            this.ip = ip;
        }

        ReceivedMessageFrame rMsg;

        public ReceivedMessageFrame getFrame() {
            return rMsg;
        }

        public void setFrame(final ReceivedMessageFrame frame) {
            this.rMsg = frame;
        }

        public history getHist() {
            return hist;
        }

        public void setHist(final history hist) {
            this.hist = hist;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(final String status) {
            this.status = status;
        }

        public boolean isPoppedMsg() {
            return poppedMsg;
        }

        public void setPoppedMsg(final boolean poppedMsg) {
            this.poppedMsg = poppedMsg;
        }
    }

    /**
     * Default constructor. Still unsure what to put inside ;-)
     */
    public Users() {

        //... not sure yet
    }

    /**
     * Searchs whether <code>user</code> is real user or not
     *
     * @param user User's UIN
     * @return true if <code>user</code> exists, false otherwise
     */
    public boolean exist(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp != null;
    }

    public boolean changeStatus(final String user, final String newStatus) {
        final extraFlds temp = (extraFlds) extra.get(user);
        extra.remove(user);
        temp.setStatus(newStatus);
        extra.put(user, temp);
        return true;
    }
    
    public void changeStatusAll(final String newStatus){
        for (int i = 0; i < data.size(); i++){
            changeStatus((String)data.get(i), newStatus);
        }
    }

    public boolean changeTyping(final String aUser, final boolean aNewTyping) {
        final extraFlds temp = (extraFlds) extra.get(aUser);
        extra.remove(aUser);
        temp.setTyping(aNewTyping);
        extra.put(aUser, temp);
        return true;
    }

    public boolean changePopped(final String user, final boolean Popped) {
        final extraFlds temp = (extraFlds) extra.get(user);
        extra.remove(user);
        temp.setPoppedMsg(Popped);
        extra.put(user, temp);
        return true;
    }

    public String getStatus(final String user) {
        return ((extraFlds) extra.get(user)).getStatus();
    }

    public boolean getTyping(final String aUser) {
        return ((extraFlds) extra.get(aUser)).isTyping();
    }

    public boolean add(final String user) {
        data.add(user);
        final extraFlds temp = new extraFlds();
        temp.setStatus("Online");
        temp.setPoppedMsg(false);
        extra.put(user, temp);
        return true;
    }

    /**
     * @return number of Users
     */
    public int getsize() {
        int count = 0;
      if (mShowOfflineUsers){
        for (int i = 0; i < data.size(); i++){
            extraFlds temp = (extraFlds) extra.get((String)data.get(i));
            if (!temp.getStatus().equalsIgnoreCase("Offline"))
               count++;
        }
      }
       else{
          count = data.size();
      }
        return count;
    }

    public String getAt(final int i) {
      String retValue = "";
      int count = 0;
      if (mShowOfflineUsers){
        for (int j = 0; i < data.size(); j++){
            extraFlds temp = (extraFlds) extra.get((String)data.get(j));
            if (!temp.getStatus().equalsIgnoreCase("Offline")){
                if (count == i) return (String)data.get(j);
                count++;
            }
        }
      }
       else{
          retValue = (String) data.get(i);
      }
        return retValue;
    }

    /**
     * Removes user from current database
     *
     * @param aUser User's UIN
     * @return true if remove is successful, false otherewise
     */
    public boolean remove(final String aUser) {
        data.remove(aUser);
        extra.remove(aUser);
        // Even if aUser doesnot exist, it returns true
        return true;
    }

    /**
     * Adds message to messages' database
     *
     * @param user  User's UIN
     * @param Entry Message
     * @return
     */
    public int addMsg(final String user, final entry Entry) {
        final extraFlds temp = (extraFlds) extra.get(user);
        temp.getHist().add(Entry);
        return temp.getHist().getsize();
    }

    /**
     * Number of messages received or sent by <code>user</code>
     *
     * @param user User's UIN
     * @return number of message for user <code>user</code>
     */
    public int getHistorySize(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getHist().getsize();
    }

    public entry getHistoryAt(final String user, final int i) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getHist().getAt(i);
    }

    public boolean isPopped(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.isPoppedMsg();
    }

    public void setPopped(final String user, final boolean b) {
        final extraFlds temp = (extraFlds) extra.get(user);
        if (temp != null)    //This user exists
            temp.setPoppedMsg(b);
    }

    public ReceivedMessageFrame getReceivedMessage(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getFrame();
    }

    public void setReceivedMessage(final String user, final ReceivedMessageFrame rMsg) {
        final extraFlds temp = (extraFlds) extra.get(user);
        temp.setFrame(rMsg);
    }

    public String getHost(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getHost();
    }

    public void setHost(final String user, final String host) {
        final extraFlds temp = (extraFlds) extra.get(user);
        temp.setHost(host);
    }

    public String getIp(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getIp();
    }

    public void setIp(final String user, final String ip) {
        final extraFlds temp = (extraFlds) extra.get(user);
        temp.setIp(ip);
    }

    public Color getColor(final String user) {
        final extraFlds temp = (extraFlds) extra.get(user);
        return temp.getColor();
    }

    public void setColor(final String aUser, final Color aColor) {
        final extraFlds temp = (extraFlds) extra.get(aUser);
        temp.setColor(aColor);
        IniReader.changeData(aUser, aColor.getRGB());
    }

}
