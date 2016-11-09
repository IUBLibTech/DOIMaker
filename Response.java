package edu.indiana.doimaker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Response {

    public int responseCode;
    public String status;
    public String statusLineRemainder;
    public HashMap<String, String> metadata;

    public String toString () {
        StringBuffer b = new StringBuffer();
        b.append("responseCode=");
        b.append(responseCode);
        b.append("\nstatus=");
        b.append(status);
        b.append("\nstatusLineRemainder=");
        b.append(statusLineRemainder);
        b.append("\nmetadata");
        if (metadata != null) {
            b.append(" follows\n");
            Iterator<Map.Entry<String, String>> i =
                metadata.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, String> e = i.next();
                b.append(e.getKey() + ": " + e.getValue() + "\n");
            }
        } else {
            b.append("=null\n");
        }
        return b.toString();
    }

}
