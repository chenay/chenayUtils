package com.chenay.platform.utils;

class BaseStationInfo {

    private int mcc;
    private int mnc;
    private int lac;
    private int cid;
    private int baseType;
    private int sid;
    private int nid;
    private int bid;

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getLac() {
        return lac;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public void setBaseType(int baseType) {
        this.baseType = baseType;
    }

    public int getBaseType() {
        return baseType;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getNid() {
        return nid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getBid() {
        return bid;
    }
}
