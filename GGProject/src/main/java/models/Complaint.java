package models;

import org.json.JSONObject;

public class Complaint {
    private String name;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private String localPath;

    public Complaint(String name, int[] pos, String localPath){
        this.name = name;
        this.left = pos[0];
        this.top = pos[1];
        this.right = pos[2];
        this.bottom = pos[3];
        this.localPath = localPath;
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setImdb_id(String localPath) {
        this.localPath = localPath;
    }

    public void setPos(int l, int t, int r, int b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }

    public Integer[] getPos() {
        return new Integer[]{left, top, right, bottom};
    }

    public String toString() {
        return localPath + "; " + name + "; " + "[" + left + ", " + top + ", " + right + ", " + bottom + "]";
    }

    public String toJSON() {
        return "{ \"name\": \"" + name + "\", \"left\": " + left + ", \"top\": "+ top + ", \"right\": "+
                right + ", \"bottom\": "+ bottom +" }";
    }
}
