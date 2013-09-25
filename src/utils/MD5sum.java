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
/* Date: Jan 21, 2003 * Time: 5:30:09 PM */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5sum {

    private MD5sum() {
    }

    public static String hash(final String string) {
        final MessageDigest md5;
        String output = new String();

        try {
            md5 = MessageDigest.getInstance("MD5");
            final byte[] hash1 = md5.digest(string.getBytes());
            for (int i = 0; i < hash1.length; i++) {
                output += Integer.toHexString(hash1[i] & 0xFF).toUpperCase();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("This algorithm is not supported");
        }

        return output;
    }

    public static void main(final String[] args) {
        final String name;
        final String pass;
        final String encodedpass;
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Username: ");
            name = input.readLine();
            System.out.print("Enter password: ");
            pass = input.readLine();
            encodedpass = hash(pass);
            System.out.println("\nGenerated hash pair\n[" + name.toUpperCase() + ":" + encodedpass + "]");

        } catch (IOException ex) {
            System.out.println("IO Error occurred");
        }
    }

}
