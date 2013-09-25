package utils;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Manages user database
 */
public final class PasswordList {
    public PasswordList() {
    }

    /**
     * Authenticate user against hash database
     *
     * @param aUser      Username
     * @param aHash      Received hash from user after login
     * @param aChallenge Challenge send to client
     * @return True if authorization is right, false otherwise
     */
    public static boolean checkUser(final String aUser, final String aHash, final String aChallenge) {
        final String mServerHash = PasswordList.getHashForUser(aUser);
        if (mServerHash.equals("")) return false;
        final String mComparer = MD5sum.hash(mServerHash + aChallenge);
        return mComparer.equals(aHash);
    }

    public static Vector getRegisteredUsers(){
        Vector list = new Vector();
        String record;
        try {
            final BufferedReader in = PasswordList.openPasswd();
            while ((record = in.readLine()) != null){
                list.add(record.substring(0, record.indexOf(':')));
            }
            PasswordList.closePasswd(in);
        } catch (IOException ex) {
            System.out.println("EXCEPTION");
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Open 'passwd' file in current directory
     *
     * @return
     * @throws java.io.IOException
     */
    private static BufferedReader openPasswd() throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader("passwd"));
        return reader;
    }

    /**
     * Close open password file
     *
     * @param mReader
     * @throws java.io.IOException
     */
    private static void closePasswd(final BufferedReader mReader) throws IOException {
        mReader.close();
    }

    /**
     * Reads through password file and finds corresponding hash value for <i>user</i>
     *
     * @param aUser
     * @return
     */
    private static String getHashForUser(final String aUser) {
        String hash = new String("");
        String record;
        try {
            final BufferedReader in = PasswordList.openPasswd();
            while ((record = in.readLine()) != null)
                if (record.startsWith(aUser + ":")) {
                    hash = record.substring(aUser.length() + 1);
                    break;
                }
            PasswordList.closePasswd(in);
        } catch (IOException ex) {
            System.out.println("EXCEPTION");
            ex.printStackTrace();
        }
        return hash;
    }

    /**
     * Only for debug purposes
     *
     * @param args
     */
    public static void main(final String[] args) {
        final PasswordList pwl = new PasswordList();
        System.out.println(PasswordList.getHashForUser("plamen"));
    }
}
