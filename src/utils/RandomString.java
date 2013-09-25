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

import java.util.Random;

public final class RandomString {

    private static final Random rn = new Random();

    private RandomString() {
    }

    private static int rand(final int lo, final int hi) {
        final int n = hi - lo + 1;
        int i = RandomString.rn.nextInt() % n;
        if (i < 0)
            i = -i;
        return lo + i;
    }

    private static String getRandomString(final int lo, final int hi) {
        final int n = rand(lo, hi);
        final byte[] b = new byte[n];
        for (int i = 0; i < n; i++)
            b[i] = (byte) rand(33, 127);
        return new String(b);
    }

    public static String getRandomString() {
        return getRandomString(5, 25);
    }
}
