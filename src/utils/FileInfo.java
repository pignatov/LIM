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
package utils;

import java.util.Calendar;

final class FileInfo {
    private final String filename;
    //Source of file. This is actually user name of mSender
    private final String mSource;
    //Save to folder
    private final String mDest;
    //Size of file in KB
    private final long mSize;
    //Completed in KB
    private long mCompleted;

    public long getCompleted() {
        return mCompleted;
    }

    public long getSize() {
        return mSize;
    }

    public String getFilename() {
        return filename;
    }

    public String getDest() {
        return mDest;
    }

    public String getSource() {
        return mSource;
    }

    //Start Time
    private final Calendar startTime;

    public FileInfo(final String filename, final String mSource, final String mDest, final long mSize) {
        this.filename = filename;
        this.mSource = mSource;
        this.mDest = mDest;
        this.mSize = mSize;
        mCompleted = 0;
        startTime = Calendar.getInstance();
    }

    public int getPercent() {
        return (int) ((double) mCompleted / (double) mSize * 100);
    }

    private int getTimeElapsed() {
        final Calendar now = Calendar.getInstance();
        return (int) ((now.getTimeInMillis() - startTime.getTimeInMillis()) * 1000);
    }

    public int getTimeLeft() {
        return (int) ((double) 100 / (double) getPercent() * getTimeElapsed() - getTimeElapsed());
    }

    public double getAvgSpeed() {
        return (double) mCompleted / (double) getTimeElapsed();
    }

    public void setCompleted(final long aCompleted) {
        this.mCompleted = aCompleted;
    }

}
