package com.edu.edumediaplayer.playback;

public class SongFileInfo {

    int readCount;
    int format;
    int channels;
    int rate;
    int bits;
    int dataSize;
    int errorCode;

    public SongFileInfo(int readCount, int format, int channels, int rate, int bits, int dataSize, int errorCode) {
        this.readCount = readCount;
        this.format = format;
        this.channels = channels;
        this.rate = rate;
        this.bits = bits;
        this.dataSize = dataSize;
        this.errorCode = errorCode;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getErrorCode() {
        return errorCode;
    }
}