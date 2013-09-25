package gui;

/**
 * **********************************************************************
 * LAN Client/Server Instant Messaging
 * Copyright (C) 2002  Plamen Ignatov <plig@mail.bg>
 * <p/>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * ***********************************************************************
 */
/* Date: Oct 24, 2002 * Time: 3:29:32 PM */

public final class LoginData {
    private String UIN = new String();
    private String mPassword = new String();
    private String localhost = new String();

    public LoginData() {
        //Does nothing
    }

    public LoginData(final String UIN, final String password, final String localhost) {
        this.UIN = UIN;
        this.mPassword = password;
        this.localhost = localhost;
    }

    public String getUIN() {
        return UIN;
    }

    public void setUIN(final String UIN) {
        this.UIN = UIN;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(final String password) {
        this.mPassword = password;
    }

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(final String localhost) {
        this.localhost = localhost;
    }
}
