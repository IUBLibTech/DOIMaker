package edu.indiana.doimaker;

import java.io.*;
import java.net.*;
import java.util.*;

public class DOIMaker {
	
	//based on code from http://ezid.cdlib.org/doc/apidoc.html
	
	static String SERVER = "";
    static String USERNAME = "";
    static String PASSWORD = "";
    
    public DOIMaker(String server, String username, String password) {
    	SERVER = server;
    	USERNAME = username;
    	PASSWORD = password;
    	Authenticator.setDefault(new DOIMakerAuthenticator());
    }
    
    static private class DOIMakerAuthenticator extends Authenticator {
        protected PasswordAuthentication getPasswordAuthentication () {
            return new PasswordAuthentication(USERNAME, PASSWORD.toCharArray());
        }
    }
    
    public Response issueRequest (String method, String path, HashMap<String, String> metadata) throws Exception {
        HttpURLConnection c = (HttpURLConnection) (new URL(SERVER + "/" + path)).openConnection();
        c.setRequestMethod(method);
        c.setRequestProperty("Accept", "text/plain");
        if (metadata != null) {
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            OutputStreamWriter w = new OutputStreamWriter(c.getOutputStream(), "UTF-8");
            w.write(toAnvl(metadata));
            w.flush();
        }
        Response r = new Response();
        r.responseCode = c.getResponseCode();
        InputStream is = r.responseCode < 400? c.getInputStream() : c.getErrorStream();
        if (is != null) {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(is, "UTF-8"));
            String[] kv = parseAnvlLine(br.readLine());
            r.status = kv[0];
            r.statusLineRemainder = kv[1];
            HashMap<String, String> d = new HashMap<String, String>();
            String l;
            while ((l = br.readLine()) != null) {
                kv = parseAnvlLine(l);
                d.put(kv[0], kv[1]);
            }
            if (d.size() > 0) r.metadata = d;
            br.close();
        }
        return r;
    }

    private static String encode (String s) {
        return s.replace("%", "%25").replace("\n", "%0A").
            replace("\r", "%0D").replace(":", "%3A");
    }

    private static String toAnvl (HashMap<String, String> metadata) {
        Iterator<Map.Entry<String, String>> i =
            metadata.entrySet().iterator();
        StringBuffer b = new StringBuffer();
        while (i.hasNext()) {
            Map.Entry<String, String> e = i.next();
            b.append(encode(e.getKey()) + ": " +
                     encode(e.getValue()) + "\n");
        }
        return b.toString();
    }

    private static String decode (String s) {
        StringBuffer b = new StringBuffer();
        int i;
        while ((i = s.indexOf("%")) >= 0) {
            b.append(s.substring(0, i));
            b.append((char)
                     Integer.parseInt(s.substring(i+1, i+3), 16));
            s = s.substring(i+3);
        }
        b.append(s);
        return b.toString();
    }

    private static String[] parseAnvlLine (String line) {
        String[] kv = line.split(":", 2);
        kv[0] = decode(kv[0]).trim();
        kv[1] = decode(kv[1]).trim();
        return kv;
    }    

}
