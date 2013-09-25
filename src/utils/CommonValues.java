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
/* Date: Nov 9, 2002 * Time: 7:34:52 PM */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Contains some important constant utils.values
 */
public final class CommonValues {
    public static final String version = "Java Client v 0.3.5  [30/12/04]";
    public static final String bVersion = "03501";
    public static final String serverVersion = "0.3.5";
    public static final String protocolVersion = "0.3.5";
    public static final String date = "20/09/2004";
    public static final String inifile = "lim.ini";
    public static final int refresh_interval = 10000;
    public static final int check_for_dead_users = 75000;
    public static final int alive_user_limit = 45000;
    public static final int alive_user_slow_response = 35000;
    public static final int noServer = 45000;
    public static final int ClientWidth = 500;
    public static final int ClientHeight = 300;
    // After what amount of seconds we may conclude typing is over
    public static final int KeyTypeTolerance = 20;
    public static final Emotion[] emoticons = {new Emotion(":-)", "01"), new Emotion(":-(", "02"),
                                               new Emotion(":)", "01"), new Emotion(":(", "a"),
                                               new Emotion(";)", "03"), new Emotion(":|", "30"),
                                               new Emotion(":]", "18"), new Emotion(":')", "15"),
                                               new Emotion(":kiss", "10"), new Emotion(":lele", "11"),
                                               new Emotion(":pich", "14"),
                                               new Emotion(":geek", "22"), new Emotion(":spi", "24"),
                                               new Emotion(":tiho", "27"), new Emotion(":ne", "28"),
                                               new Emotion(":hmm", "33"), new Emotion(":bravo", "35"),
                                               new Emotion(":otivam", "44"), new Emotion(":da", "45"),
                                               new Emotion(":joke", "55"), new Emotion(":rose", "u"),
                                               new Emotion(":fun", "59"),
                                               new Emotion(":p", "32"),
                                               new Emotion(":B", "Bulgaria"), new Emotion(":gadove", "killthemall"),
                                               new Emotion(":beer", "beer")};

    public static Locale locale = new Locale();

    public CommonValues() {
    }

    public static void chooseLanguage(final String lang) {
        if (lang.equalsIgnoreCase("BG")) locale = new LocaleBG();
    }

    /**
     * Reads <para>motd</para> file, which resides in the ChatServer directory
     *
     * @return Message of the day String
     */
    public static String motd() {
        String value = "Welcome to LIM!";
        final BufferedReader in;
        try {
            final File file = new File("motd");
            in = new BufferedReader(new FileReader(file));
            value = in.readLine();
            in.close();
        } catch (IOException fnf) {
            fnf.printStackTrace();
        }
        return value;
    }

    public static String convert(final String str, final int size) {
        String result = new String();
        if (str.length() >= size)
            result = str;
        else
            result += "0" + str;
        return result;
    }

    public static String translateVersion(final String version) {
        final int major = Integer.parseInt(version.substring(0, 1));
        final int minor = Integer.parseInt(version.substring(1, 2));
        final int subminor = Integer.parseInt(version.substring(2, 3));
        final int type = Integer.parseInt(version.substring(3, 4));
        final int subtype = Integer.parseInt(version.substring(4, 5));
        String sType = new String();
        switch (type) {
            case 0:
                sType = "beta";
                break;
            case 1:
                sType = "pre";
                break;
            case 2:
                sType = "rc";
                break;
            case 3:
                sType = "final";
                break;
        }
        final String retType = major + "." + minor + "." + subminor + " " + sType + " " + subtype;
        return retType;
    }
}
