package com.server.api.model;

public class NfcDeviceInfo {
    /**  */
    private String deviceid;

    /**  */
    private String nfcid;

    /**  */
    private String longitude;

    /**  */
    private String latitude;

    /**
     * 
     * @return deviceId 
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * 
     * @param deviceid 
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid == null ? null : deviceid.trim();
    }

    /**
     * 
     * @return nfcId 
     */
    public String getNfcid() {
        return nfcid;
    }

    /**
     * 
     * @param nfcid 
     */
    public void setNfcid(String nfcid) {
        this.nfcid = nfcid == null ? null : nfcid.trim();
    }

    /**
     * 
     * @return longitude 
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude 
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 
     * @return latitude 
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude 
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }
}