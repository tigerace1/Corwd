package com.example.chengen.crowdsafes;

import android.graphics.Bitmap;

public class NavItem {
    private String title;
    private Bitmap icon;
    public NavItem(String title,Bitmap icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
