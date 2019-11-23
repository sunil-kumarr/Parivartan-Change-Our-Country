package com.net.comy;

public class Category {
    private String title;
    private int image;
    public Category(String pTitle, int pImage) {
        title = pTitle;
        image = pImage;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
