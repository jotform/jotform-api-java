package com.jotform.api.samples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jotform.api.*;

public class Delete50Submissions {
		
	public static void main(String[] args) {
		
		JotForm client = new JotForm("YOUR API KEY");
		
		JSONObject submissionsResponse = client.getSubmissions("", "50", null, "");
		
		JSONArray submissions;
		try {
			submissions = submissionsResponse.getJSONArray("content");
		
			for (int i=0; i<submissions.length(); i++){
				JSONObject submission = submissions.getJSONObject(i);
				
				JSONObject result = client.deleteSubmission(Long.parseLong(submission.get("id").toString()));
				
				System.out.println(result);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}
}
