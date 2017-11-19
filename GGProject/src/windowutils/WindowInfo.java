package windowutils;

import java.io.IOException;

public class WindowInfo {

    private final String id;
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    WindowInfo(String id, int x, int y, int width, int height){
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    WindowInfo(String id, Integer[] coords){
        this.id = id;
        this.x = coords[0];
        this.y = coords[1];
        this.width = coords[2];
        this.height = coords[3];
    }


    public void update() throws IOException {
        Integer[] coords = ActiveWindowInfo.getWindowCoords(id);
        this.x = coords[0];
        this.y = coords[1];
        this.width = coords[2];
        this.height = coords[3];
    }

    public String getId()       { return id; }
    public Integer getX()       { return x; }
    public Integer getY()       { return y; }
    public Integer getWidth()   { return width; }
    public Integer getHeight()  { return height; }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Window ID: ").append(id);
        sb.append("\nUpper Left X: ").append(x);
        sb.append("\nUpper Left Y: ").append(y);
        sb.append("\nWidht: ").append(width);
        sb.append("\nHeight: ").append(height);
        return sb.toString();
    }
}
