package edu.indiana.doimaker;

import java.net.URLEncoder;
import java.util.HashMap;

public class DOIMakerDemo {
	
	public static void main (String[] args) throws Exception {

		DOIMaker doimaker = new DOIMaker("https://ezid.cdlib.org", "apitest", "apitest");
        
        // Sample POST request.
        System.out.println("Issuing POST request...");
        HashMap<String, String> metadata = new HashMap<String, String>();
        metadata.put("datacite.creator", "Joe Sample");
        metadata.put("datacite.title", "A Sample Item");
        metadata.put("datacite.publisher", "A Sample Publisher");
        metadata.put("datacite.publicationyear", "2016");
        metadata.put("datacite.resourcetype", "Software");
        metadata.put("_target", "http://www.google.com");
        Response r = doimaker.issueRequest("POST", "shoulder/doi:10.5072/FK2", metadata);
        if (r.responseCode == 400) {
        	System.out.println("ERROR - Invalid request");
        	System.out.println(r);
        	System.exit(1);
        }
        if (r.responseCode == 401) {
        	System.out.println("ERROR - Invalid credentials supplied");
        	System.out.println(r);
        	System.exit(1);
        }
        if (r.responseCode != 201) {
        	System.out.println("ERROR");
        	System.out.println(r);
        	System.exit(1);
        }
        System.out.print(r);

        // Sample GET request.
        System.out.println("\nIssuing GET request...");
        String id = r.statusLineRemainder.substring(0, r.statusLineRemainder.indexOf("|") - 1);
        System.out.println(id);
        r = doimaker.issueRequest("GET", "id/" + URLEncoder.encode(id, "UTF-8"), null);
        if (r.responseCode != 200) {
        	System.out.println("ERROR");
        	System.out.println(r);
        	System.exit(1);
        }
        System.out.print(r);
    }

}
