package com.CopySave.singleton;

public class Preferences {
    public static final Preferences instance = new Preferences();

    public static final int itemSpacing = 3;
    public static final int itemWidth = 200;
    public static final int itemMinimumHeight = 100;
    public static final int itemMaximumHeight = 200;
    public static final int selectedWidth = 20;
    public static final int shadowSpacing = 4;
    public static final int maximumItems = 100;
    public static final int waitForOpen = 1000;
    public static final int closeSpeed = 200;
    public static final int clipboardTimer = 1000;

    private java.util.prefs.Preferences prefs;

    public Preferences() {
        prefs = java.util.prefs.Preferences.userRoot().node(this.getClass().getName());
    }

    public int getItemSpacing() {
        return prefs.getInt("itemSpacing", itemSpacing);
    }

//    public void setItemSpacing(int value) {
//        prefs.putInt("itemSpacing", value);
//    }

    public int getItemWidth() {
        return prefs.getInt("itemWidth", itemWidth);
    }

//    public void setItemWidth(int value) {
//        prefs.putInt("itemWidth", value);
//    }

    public int getItemMinimumHeight() {
        return prefs.getInt("itemMinimumHeight", itemMinimumHeight);
    }

//    public void setItemMinimumHeigt(int value) {
//        prefs.putInt("itemMinimumHeight", value);
//    }

    public int getItemMaximumHeight() {
        return prefs.getInt("itemMaximumHeight", itemMaximumHeight);
    }

//    public void setItemMaximumHeight(int value) {
//        prefs.putInt("itemMaximumHeight", value);
//    }

    public int getSelectedWidth() {
        return prefs.getInt("selectedWidth", selectedWidth);
    }

//    public void setSelectedWidth(int value) {
//        prefs.putInt("selectedWidth", value);
//    }

    public int getShadowSpacing() {
        return prefs.getInt("shadowSpacing", shadowSpacing);
    }

//    public void setShadowSpacing(int value) {
//        prefs.putInt("shadowSpacing", value);
//    }

    public int getMaximumItems() {
        return prefs.getInt("maximumItems", maximumItems);
    }

//    public void setMaximumItems(int value) {
//        prefs.putInt("maximumItems", value);
//    }

    public int getWaitForOpen() {
        return prefs.getInt("waitForOpen", waitForOpen);
    }

//    public void setWaitForOpen(int value) {
//        prefs.putInt("waitForOpen", value);
//    }

    public int getCloseSpeed() {
        return prefs.getInt("closeSpeed", closeSpeed);
    }
//
//    public void setCloseSpeed(int value) {
//        prefs.putInt("closeSpeed", value);
//    }

    public int getClipboardTimer() {
        return prefs.getInt("clipboardTimer", clipboardTimer);
    }

//    public void setClipboardTimer(int value) {
//        prefs.putInt("clipboardTimer", value);
//    }

    public static Preferences getInstance() {
        return instance;
    }

}
