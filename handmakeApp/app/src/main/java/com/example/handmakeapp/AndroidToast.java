package com.example.handmakeapp;

import android.content.Context;
import android.widget.Toast;

public class AndroidToast {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
