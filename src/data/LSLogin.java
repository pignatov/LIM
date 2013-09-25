package data;

/*************************************************************************
 * LAN gui.Client/Server Instant Messaging
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
/* Date: Oct 24, 2002 * Time: 3:45:09 PM */

import gui.LoginData;
import utils.MD5sum;

import java.io.*;

public final class LSLogin {

    public static LoginData read() throws FileNotFoundException {
        final BufferedReader LoginFile = new BufferedReader(new FileReader("login.txt"));
        String UIN = new String();
        String passwordHash = new String();
        String host = new String();

        try {
            UIN = LoginFile.readLine();
            passwordHash = LoginFile.readLine();
            host = LoginFile.readLine();
            if (UIN == null) UIN = "";
            if (passwordHash == null) passwordHash = "";
            if (host == null) host = "";
            LoginFile.close();
        } catch (IOException ioex) {
            System.err.println("Error in File");
        }
        return new LoginData(UIN, passwordHash, host);
    }

    public static void write(final LoginData aData, final boolean isHash) throws IOException {
        final BufferedWriter LoginFile = new BufferedWriter(new FileWriter("login.txt"));

        try {
            LoginFile.write(aData.getUIN() + "\n");
            final String mPassword = aData.getPassword();
            final String hash;
            if (isHash)
                hash = mPassword;
            else
                hash = MD5sum.hash(mPassword);
            LoginFile.write(hash + "\n");
            LoginFile.write(aData.getLocalhost() + "\n");
            LoginFile.close();
        } catch (IOException ioex) {
            System.err.println("Error in File");
        }
    }

}
