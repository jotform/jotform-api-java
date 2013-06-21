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
	
		JotForm client = new JotForm("8b36455f659087568bed58c1642082f4");
		
		
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
   
Get latest submissions of the user
    
