package com.flightbook.activity;

public class MyDialogInterface {

	DialogReturn dialogReturn;

    public interface DialogReturn {
        void onDialogCompleted(boolean answer, String type);
    }

    public void setListener(DialogReturn dialogReturn) {
        this.dialogReturn = dialogReturn;
    }

    public DialogReturn getListener() {
        return dialogReturn;
    }
	
}
