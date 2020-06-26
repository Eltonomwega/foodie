package com.elton.foodie.Modals;

public class APIModalClass {
    String API_KEY;
    String SearchURL;
    String ImgURL;
    String size;
    String URL_Prefix;

    //&apiKey=7992174586184c75bc46188438e23e9b

    public APIModalClass() {
        this.API_KEY = "&apiKey=db9a577a695640528bb7a67487f8a907";
        this.SearchURL = "/information?includeNutrition=true";
        this.ImgURL = "https://spoonacular.com/";
        this.size= "-556x370.jpg";
        this.URL_Prefix = "https://api.spoonacular.com/recipes/";
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String getSearchURL() {
        return SearchURL;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public String getSize() {
        return size;
    }

    public String getURL_Prefix() {
        return URL_Prefix;
    }
}
