jotform-api-java 
===============
JotForm API - Java Client


### Installation

Install via git clone:

        $ git clone git://github.com/jotform/jotform-api-java.git
        $ cd jotform-api-java
        

### Documentation

You can find the docs for the API of this client at [http://api.jotform.com/docs/](http://api.jotform.com/docs)

### Authentication

JotForm API requires API key for all user related calls. You can create your API Keys at  [API section](http://www.jotform.com/myaccount/api) of My Account page.

### Examples

Print all forms of the user

package com.jotform.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
   
Get submissions of the latest form

package com.jotform.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrintFormSubmissions {

	
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

Get latest 100 submissions ordered by creation date

package com.jotform.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrintLastSubmissions {

	
	public static void main(String[] args) {
	
		JotForm client = new JotForm("YOUR API KEY");
		
		JSONObject submissions = client.getSubmissions("", "100", null, "created_at");
		
		System.out.println(submissions);
		
	}

}

Submission and form filter examples

package com.jotform.api;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Filters {

	
	public static void main(String[] args) {
	
		JotForm client = new JotForm("8b36455f659087568bed58c1642082f4");
		
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

Delete last 50 submissions

package com.jotform.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeleteSubmissions {

	
	public static void main(String[] args) {
	
		JotForm client = new JotForm("YOUR API KEY");
		
		JSONObject submissionsResponse = client.getSubmissions("", "2", null, "");
		
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

First the JotForm class is included from the jotform-api-java/JotForm.java file. This class provides access to JotForm's API. You have to create an API client instance with your API key. 
In any case of exception (wrong authentication etc.), you can catch it or let it fail with fatal error.





    
