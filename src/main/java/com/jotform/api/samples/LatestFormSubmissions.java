package com.jotform.api.samples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jotform.api.*;

public class LatestFormSubmissions {

	public static void main(String[] args) {
		
		JotForm client = new JotForm("YOUR API KEY");
		
		JSONObject formsResponse = client.getForms("", "1", null, "");
		
		try {
			JSONArray forms = formsResponse.getJSONArray("content");
			
			JSONObject latestForm = forms.getJSONObject(0);
			
			long latestFormID = Long.parseLong(latestForm.get("id").toString());
			
			JSONObject submissions = client.getFormSubmissions(latestFormID);
			
			System.out.println(submissions);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
