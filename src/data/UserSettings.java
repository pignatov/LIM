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
/* Date: 2002-10-4
 * Time: 14:14:18 */

/**
 * Class: data.UserSettings
 * Purpose: Stores various settings about a user
 *          showtime    show time in receive message dialog
 *          me_in_list  show me in list of data.Users
 */

import java.awt.*;
import java.io.Serializable;

public final class UserSettings implements Serializable {
    /*showtime thrue= show time when receiving message*/
    private boolean showtime;
    /*inside settings dialog N/A*/
    private boolean inside;
    /*show me in user's list N/A*/
    private boolean me_in_list;
    /*beep upon receival*/
    private boolean beep;
    /*auto login*/
    private boolean autologin;
    /*default background Color*/
    private Color mBGColor;
    private boolean enableAutoAway;
    private int autoAwayTime;
    private int autoNATime;
    private boolean enableAutoLogin;
    private boolean autoSaveDimensions;
    private int mWidth;
    private int mHeigth;

    public boolean isAutoSaveDimensions() {
        return autoSaveDimensions;
    }

    public void setAutoSaveDimensions(final boolean aAutoSaveDimensions) {
        autoSaveDimensions = aAutoSaveDimensions;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(final int aWidth) {
        mWidth = aWidth;
    }

    public int getHeigth() {
        return mHeigth;
    }

    public void setHeigth(final int aHeigth) {
        mHeigth = aHeigth;
    }

    public boolean isEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(final boolean aEnableAutoLogin) {
        this.enableAutoLogin = aEnableAutoLogin;
    }

    public boolean isEnableAutoAway() {
        return enableAutoAway;
    }

    public void setEnableAutoAway(final boolean enableAutoAway) {
        this.enableAutoAway = enableAutoAway;
    }

    public int getAutoAwayTime() {
        return autoAwayTime;
    }

    public void setAutoAwayTime(final int autoAwayTime) {
        this.autoAwayTime = autoAwayTime;
    }

    public Color getmBGColor() {
        return mBGColor;
    }

    public void setmBGColor(final Color mBGColor) {
        this.mBGColor = mBGColor;
    }

    public boolean isAutologin() {
        return autologin;
    }

    public void setAutologin(final boolean autologin) {
        this.autologin = autologin;
    }

    public boolean isBeep() {
        return beep;
    }

    public void setBeep(final boolean beep) {
        this.beep = beep;
    }

    private String rfont = "Arial";
    private String sfont = "Arial";
    private int rsize = 12;
    private int ssize = 12;
    private String chatFont = "Arial";
    private int chatSize = 12;

    public String getChatFont() {
        return chatFont;
    }

    public void setChatFont(String chatFont) {
        this.chatFont = chatFont;
    }

    public int getChatSize() {
        return chatSize;
    }

    public void setChatSize(int chatSize) {
        this.chatSize = chatSize;
    }

    public String getRfont() {
        return rfont;
    }

    public void setRfont(final String rfont) {
        this.rfont = rfont;
    }

    public String getSfont() {
        return sfont;
    }

    public void setSfont(final String sfont) {
        this.sfont = sfont;
    }

    public int getRsize() {
        return rsize;
    }

    public void setRsize(final int rsize) {
        this.rsize = rsize;
    }

    public int getSsize() {
        return ssize;
    }

    public void setSsize(final int ssize) {
        this.ssize = ssize;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(final boolean aInside) {
        this.inside = aInside;
    }

    public int getAutoNATime() {
        return autoNATime;
    }

    public void setAutoNATime(final int autoNATime) {
        this.autoNATime = autoNATime;
    }

    /**
     * @return show me in list - true/false
     */
    public boolean isMe_in_list() {
        return me_in_list;
    }

    /**
     * @param me_in_list whether to show me or not in list
     */
    public void setMe_in_list(final boolean me_in_list) {
        this.me_in_list = me_in_list;
    }


    public UserSettings() {
        super();
        showtime = true;
        me_in_list = true;
        mBGColor = Color.GREEN;
        autoSaveDimensions = false;
    }

    public boolean isShowtime() {
        return showtime;
    }

    public void setShowtime(final boolean showtime) {
        this.showtime = showtime;
    }
}
