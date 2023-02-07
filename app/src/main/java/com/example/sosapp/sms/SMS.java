package com.example.sosapp.sms;

import com.example.sosapp.locator.Locator;
import com.example.sosapp.message.Message;

public class SMS {
    private final Message message;
    private String text;
    private String cellNumber;

    public SMS(Message message, String cellNumber) {
        this.message = message;
        this.cellNumber = cellNumber;
    }

    public String getText(){ return text;}

    public void setMessage(Locator locator) {
        text = message.getMessage() + locator.toString();
    }

    public void setMessage(){
        text = message.getMessage();
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
}
