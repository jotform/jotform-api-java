package com.jotform.api.samples;

import java.util.HashMap;

import org.json.JSONObject;
import com.jotform.api.*;

public class SubmissionFormFilters {
		
	public static void main(String[] args) {
		
		JotForm client = new JotForm("YOUR API KEY");
		
		HashMap<String, String> submissionFilter = new HashMap<String, String>();
		submissionFilter.put("id:gt", "FORM ID");
		submissionFilter.put("created_at:gt", "DATE");

		JSONObject submissions = client.getSubmissions("", "", submissionFilter, "");
		
		System.out.println(submissions);
		
		HashMap<String, String> formFilter = new HashMap<String, String>();
		formFilter.put("id:gt", "FORM ID");
		
		JSONObject forms = client.getForms("", "", formFilter, "");
		
		System.out.println(forms);
	}
}
