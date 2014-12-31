package info.rynkowski.hamsterclient.ui;

public class NavDrawerItem {

    private String mTitle;
    private int mIcon;
    private String mCount = "0";
    // boolean to set visiblity of the counter
    private boolean mIsCounterVisible = false;

    public NavDrawerItem(String title, int icon) {
        this.mTitle = title;
        this.mIcon = icon;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count) {
        this.mTitle = title;
        this.mIcon = icon;
        this.mIsCounterVisible = isCounterVisible;
        this.mCount = count;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getIcon() {
        return this.mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public String getCount() {
        return this.mCount;
    }

    public void setCount(String count) {
        this.mCount = count;
    }

    public boolean getCounterVisibility() {
        return this.mIsCounterVisible;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.mIsCounterVisible = isCounterVisible;
    }
}
