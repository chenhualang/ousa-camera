package com.server.api.dao;

import com.server.api.model.NfcDeviceInfo;

public interface NfcDeviceDao {
    int deleteByPrimaryKey(String deviceid);

    int insert(NfcDeviceInfo record);

    int insertSelective(NfcDeviceInfo record);

    NfcDeviceInfo selectByPrimaryKey(String deviceid);

    int updateByPrimaryKeySelective(NfcDeviceInfo record);

    int updateByPrimaryKey(NfcDeviceInfo record);
}