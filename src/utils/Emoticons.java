package utils;

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


public final class Emoticons {
    private final Emotion[] emoticons = {new Emotion(":-)", "01"), new Emotion(":-(", "02"),
                                         new Emotion(":)", "01"), new Emotion(":(", "a"),
                                         new Emotion(";)", "03"), new Emotion(":|", "30"),
                                         new Emotion(":]", "18"), new Emotion(":')", "15"),
                                         new Emotion(":kiss", "10"), new Emotion(":lele", "11"),
                                         new Emotion(":nerven", "12"), new Emotion(":pich", "14"),
                                         new Emotion(":geek", "22"), new Emotion(":spi", "24"),
                                         new Emotion(":tiho", "27"), new Emotion(":ne", "28"),
                                         new Emotion(":hmm", "33"), new Emotion(":bravo", "35"),
                                         new Emotion(":otivam", "44"), new Emotion(":da", "45"),
                                         new Emotion(":joke", "55"), new Emotion(":rose", "u"),
                                         new Emotion(":ne2", "58"), new Emotion(":fun", "59"),
                                         new Emotion(":p", "32"),
                                         new Emotion(":B", "Bulgaria"), new Emotion(":gadove", "killthemall"),
                                         new Emotion(":beer", "beer"), new Emotion(":nenam", "06")};

    public retEmotion findNextEmoticon(final int aStart, final String aInput) {
        int min = aInput.length();
        retEmotion retValue = new retEmotion(-1, "", "");
        for (int i = 0; i < emoticons.length; i++) {
            final int result = aInput.indexOf(emoticons[i].getEmoticon(), aStart);
            if (result > 0 && result < min) {
                min = result;
                retValue = new retEmotion(min, emoticons[i].getEmoticon(), emoticons[i].getImageName());
            }
        }
        return retValue;
    }

}
