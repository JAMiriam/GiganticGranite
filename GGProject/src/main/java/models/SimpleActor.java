package models;

public class SimpleActor {
    private String name;
    private String imdb_id;
    private int left;
    private int top;
    private int right;
    private int bottom;

    public SimpleActor(){};

    public SimpleActor(int a, int b, int c, int d) {
        setPos(a, b, c, d);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
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
        return imdb_id + "; " + name + "; " + "[" + left + ", " + top + ", " + right + ", " + bottom + "]";
    }
}
