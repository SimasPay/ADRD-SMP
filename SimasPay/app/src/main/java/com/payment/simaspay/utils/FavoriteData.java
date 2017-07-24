package com.payment.simaspay.utils;

/**
 * Created by widy on 4/12/17.
 * 12
 */

public class FavoriteData {

    String categoryName, categoryID, categoryCode, favoriteLabel;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getFavoriteLabel() {
        return favoriteLabel;
    }

    public void setFavoriteLabel(String favoriteLabel) {
        this.favoriteLabel = favoriteLabel;
    }
}
