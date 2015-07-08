package com.cvte.ble.logic;

import android.content.Context;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jianhaohong on 6/17/15.
 */
public class BleListHolder {
    private static BleListHolder mBleListHolder;
    private HashMap<String, BleManager> mBleManagers = new HashMap<String, BleManager>();
    private Context mContext;

    private BleListHolder() {

    }

    public static BleListHolder getInstance() {
        if (mBleListHolder == null) {
            mBleListHolder = new BleListHolder();
        }
        return mBleListHolder;
    }

    public void init(Context context) {
        mContext = context;
    }

    public BleManager getBleManager(String tag) {
        BleManager bleManager = mBleManagers.get(tag);
        if (bleManager == null) {
            bleManager = new BleManager(mContext);
            mBleManagers.put(tag, bleManager);
        }

        return bleManager;
    }

    public void removeBleManager(String tag) {
        mBleManagers.remove(tag);
    }

    public void clearAll() {
        Set<String> keySet = mBleManagers.keySet();
        for (String tag : keySet) {
            mBleManagers.get(tag).dispose();
        }

        mBleManagers.clear();
    }
}
