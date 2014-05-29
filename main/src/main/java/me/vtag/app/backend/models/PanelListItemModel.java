package me.vtag.app.backend.models;

/**
 * Created by nageswara on 5/1/14.
 */
public class PanelListItemModel {
    private String type;
    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public PanelListItemModel(){}

    public PanelListItemModel(String type, String title, int icon){
        this.type = type;
        this.title = title;
        this.icon = icon;
    }

    public PanelListItemModel(String type, String title, int icon, boolean isCounterVisible, String count){
        this.type = type;
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getType(){
        return this.type;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
