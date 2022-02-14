jotform-api-java 
===============
[JotForm API](https://api.jotform.com/docs/) - Java Client


### Installation

Install via git clone:

        $ git clone git://github.com/jotform/jotform-api-java.git
        $ cd jotform-api-java
        

### Documentation

You can find the docs for the API of this client at [https://api.jotform.com/docs/](https://api.jotform.com/docs)

### Authentication

JotForm API requires API key for all user related calls. You can create your API Keys at  [API section](https://www.jotform.com/myaccount/api) of My Account page.

### Examples

Print all forms of the user

```java
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
``` 
   
Get submissions of the latest form

```java
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
``` 

Get latest 100 submissions ordered by creation date

```java
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
``` 

Submission and form filter examples

```java
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
``` 

Delete last 50 submissions

```java
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
``` 

First the _JotForm_ class is included from the _jotform-api-java/JotForm.java_ file. This class provides access to JotForm's API. You have to create an API client instance with your API key. 
In case of an exception (wrong authentication etc.), you can catch it or let it fail with a fatal error.

