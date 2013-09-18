package com.jotform.api.samples;

import org.json.JSONObject;
import com.jotform.api.*;

public class Latest100Submissions {
		
	public static void main(String[] args) {
	
		JotForm client = new JotForm("YOUR API KEY");
		
		JSONObject submissions = client.getSubmissions("", "100", null, "created_at");
		
		System.out.println(submissions);
	}
}
