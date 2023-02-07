package com.example.sosapp.sms;

import android.telephony.SmsManager;

public class Send {
    public static void sendMessage(SMS sms) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.getCellNumber() , null , sms.getText() , null , null);
    }
}
