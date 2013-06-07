package com.jotform.api.samples;

import java.util.Iterator;
import org.json.*;
import com.jotform.api.*;

public class GetTodaysSubmissions {

	public static void main(String[] args) {
		
		JotForm client = new JotForm("REPLACE_WITH_YOUR_APIKEY");
		
		// TODO: update with submission query to only fetch submissions received today
		JSONObject submissionsResponse = client.getSubmissions();
		//JSONObject formsResponse = client.getForms();
		
		try {
			
			JSONArray submissions = submissionsResponse.getJSONArray("content");
			//JSONArray forms = formsResponse.getJSONArray("content");
			
			for (int i=0; i< submissions.length() ; i++){
				JSONObject submission = submissions.getJSONObject(i);
				
				System.out.println("ip :" + submission.get("ip") + " date:" + submission.get("created_at"));
				System.out.println("submission content :\n");
				
				JSONObject fields = submission.getJSONObject("fields");
				Iterator<String> fieldsIterator = submission.getJSONObject("fields").keys();
				
				while(fieldsIterator.hasNext()){

					String key = fieldsIterator.next();
					String answer = fields.getString(key);
					System.out.println(
						String.format("%20s %s", key, answer)
					);
				}
				
				System.out.println("\n");
				
			}
		
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		
	}

}
