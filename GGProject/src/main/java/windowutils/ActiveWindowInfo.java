package windowutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveWindowInfo {

    private static String command = "xwininfo";
    private static String idParam = "-id";
    private static String[] activeWindowCommand = {"xprop", "-root"};


    public static WindowInfo getActiveWindowInfo() throws IOException {
       String id =  getActiveWindowId();
       Integer[] coords = getWindowCoords(id);
       return new WindowInfo(id, coords);
    }

    public static String getActiveWindowId() throws IOException {
        Process process = Runtime.getRuntime().exec(activeWindowCommand);

        InputStream inputStream = process.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        String p = "_NET_ACTIVE_WINDOW\\(WINDOW\\):\\s+window\\s+id\\s+#\\s+(0x[0-9a-f]+)\\s?+";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(result);
        if(!(m.find( ))) throw new IOException("Window id not found");
        String id = m.group(1);
        return id;
    }

    public static Integer[] getWindowCoords(String windowId) throws IOException {
        Integer[] coords = new Integer[4];
        String[] coordsCommand = {command, idParam, windowId};
        Process process = Runtime.getRuntime().exec(coordsCommand);
        InputStream inputStream = process.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        String p = "Absolute upper-left X:\\s+(\\d+)";
        Pattern r = Pattern.compile(p);
        Matcher m = r.matcher(result);
        if(!(m.find( ))) throw new IOException("Upper left X coordinate not found.");
        coords[0] = Integer.valueOf(m.group(1));

        p = "Absolute upper-left Y:\\s+(\\d+)";
        r = Pattern.compile(p);
        m = r.matcher(result);
        if(!(m.find( ))) throw new IOException("Upper left Y coordinate not found.");
        coords[1] = Integer.valueOf(m.group(1));

        p = "Width:\\s+(\\d+)";
        r = Pattern.compile(p);
        m = r.matcher(result);
        if(!(m.find( ))) throw new IOException("Width not found.");
        coords[2] = Integer.valueOf(m.group(1));

        p = "Height:\\s+(\\d+)";
        r = Pattern.compile(p);
        m = r.matcher(result);
        if(!(m.find( ))) throw new IOException("Height not found.");
        coords[3] = Integer.valueOf(m.group(1));

        return coords;
    }



    public static void updateCoords(WindowInfo windowInfo) throws IOException {
        getWindowCoords(windowInfo.getId());
    }


    public static void main(String[] args) throws Exception {
        System.out.println(ActiveWindowInfo.getActiveWindowInfo().toString());
    }
}
