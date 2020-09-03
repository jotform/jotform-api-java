package com.jotform.api.samples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jotform.api.*;

public class PrintFormList {

	public static void main(String[] args) {
	
		JotForm client = new JotForm("YOUR API KEY");
		
		
		JSONObject formsResponse = client.getForms();
		
		try {
			JSONArray forms = formsResponse.getJSONArray("content");
			
			for (int i=0; i<forms.length(); i++){
				JSONObject form = forms.getJSONObject(i);
				
				System.out.println(form.get("title") + " (Total:" +form.get("count") + " New:" + form.get("new") + ")");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
