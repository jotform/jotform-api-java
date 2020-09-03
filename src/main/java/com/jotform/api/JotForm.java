/**
 * JotForm API - Java Client
 *
 * @copyright   2013 Interlogy, LLC.
 * @link        http://www.jotform.com
 * @version     1.0
 * @package     JotFormAPI
 */

package com.jotform.api;

import org.json.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.entity.StringEntity;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JotForm {

    private static String baseUrl = "https://api.jotform.com/";
    private static String version = "v1";
    
    private String apiKey;
    private boolean debugMode;
    
    public JotForm() {
    	this.apiKey = null;
    	this.debugMode = false;
    }
    
    public JotForm(String apiKey){
    	this(apiKey, false);
    }
    
    public JotForm(String apiKey, boolean debugMode){
        this.apiKey = apiKey;
        this.debugMode = debugMode;
    }
   
    /**
     * Get JotForm client debug mode
     * @return JotForm client debug mode
     */
    public boolean getDebugMode() {return this.debugMode;}
    /**
     * Set JotForm client debug mode
     * @param value Debug mode new value
     */
    public void setDebugMode(boolean value) {this.debugMode = value;}
    
    private void Log(String message){
        if (this.debugMode){
            System.out.println(message);
        }
    }

    private JSONObject executeHttpRequest(String path, HashMap<String,String> params, String method) throws UnsupportedEncodingException {
        DefaultHttpClient client = new DefaultHttpClient();
        
        HttpUriRequest req;
        HttpResponse resp;

        if (method.equals("GET")){
        	req = new HttpGet(JotForm.baseUrl + JotForm.version + path);
            req.addHeader("apiKey", this.apiKey);
            
            if(params != null) {
                URI uri = null;
                URIBuilder ub = new URIBuilder(req.getURI());
                
            	Set<String> keys = params.keySet();
            	for(String key: keys) {
            		try {
						uri = ub.addParameter(key,params.get(key)).build();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
            	}
            	((HttpRequestBase) req).setURI(uri);
            }
        } else if (method.equals("POST")) {
            req = new HttpPost(JotForm.baseUrl + JotForm.version + path);
            req.addHeader("apiKey", this.apiKey);

            if (params != null) {
	            Set<String> keys = params.keySet();
	            
	            List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
	            
	            for(String key : keys) {
	            	parameters.add(new BasicNameValuePair(key, params.get(key)));
	            }
	            
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
	            ((HttpPost) req).setEntity(entity);
            }
        } else if (method.equals("DELETE")) {
            req = new HttpDelete(JotForm.baseUrl + JotForm.version + path);
            req.addHeader("apiKey", this.apiKey);
        } else {
        	req = null;
        }
        
        try {
            resp = client.execute(req);
            
            int statusCode = resp.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                this.Log(resp.getStatusLine().getReasonPhrase());
            }
            
            return new JSONObject(readInput(resp.getEntity().getContent()));

        } catch (IOException e) {
        	
        } catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private JSONObject executeHttpRequest(String path, JSONObject params) throws UnsupportedEncodingException {
    	DefaultHttpClient client = new DefaultHttpClient();
        
        HttpUriRequest req;
        HttpResponse resp;
        
    	req = new HttpPut(JotForm.baseUrl + JotForm.version + path);
        req.addHeader("apiKey", this.apiKey);
        
        if (params != null) {
			try {
				StringEntity s = new StringEntity(params.toString());
	    	    s.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    	    HttpEntity entity = s;
	    	    ((HttpPut) req).setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
        
        try {
            resp = client.execute(req);
            
            int statusCode = resp.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                this.Log(resp.getStatusLine().getReasonPhrase());
            }
            return new JSONObject(readInput(resp.getEntity().getContent()));

        } catch (IOException e) {
        	
        } catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private static String readInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte bytes[] = new byte[1024];

        int n = in.read(bytes);

        while (n != -1) {
            out.write(bytes, 0, n);
            n = in.read(bytes);
        }
        return new String(out.toString());
    }

    private JSONObject executeGetRequest(String path, HashMap<String,String> params) {
        try {
			return executeHttpRequest(path, params, "GET");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    private JSONObject executePostRequest(String path, HashMap<String,String> params) {
        try {
			return executeHttpRequest(path, params, "POST");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private JSONObject executePutRequest(String path, JSONObject params) {
        try {
			return executeHttpRequest(path, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private JSONObject executeDeleteRequest(String path, HashMap<String,String> params) {
        try {
			return executeHttpRequest(path, params, "DELETE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    private HashMap<String, String> createConditions(String offset, String limit, HashMap<String, String> filter, String orderBy) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("offset", offset);
    	args.put("limit", limit);
    	args.put("orderby", orderBy);
    	
    	Set<String> keys = args.keySet();
    	for(String key: keys) {
    		if (args.get(key) != "") {
    			params.put(key, args.get(key));
    		}
    	}
    	
    	if(filter != null) {
    		JSONObject filterObject = new JSONObject((Map)filter);
    		params.put("filter", filterObject.toString());
    	}
    	
    	return params;
    }
    
    private HashMap<String, String> createHistoryQuery(String action, String date, String sortBy, String startDate, String endDate) {
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("action", action);
    	args.put("date", date);
    	args.put("sortBy", sortBy);
    	args.put("startDate", startDate);
    	args.put("endDate", endDate);
    	
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	Set<String> keys = args.keySet();
    	for(String key: keys) {
    		params.put(key, args.get(key));
    	}
    	return params;
    }

    /**
     * Get user account details for a JotForm user.
     * @return Returns user account type, avatar URL, name, email, website URL and account limits.
     */
    public JSONObject getUser() {
        return executeGetRequest("/user", null);
    }

    /**
     * Get number of form submissions received this month.
     * @return Returns number of submissions, number of SSL form submissions, payment form submissions and upload space used by user.
     */
    public JSONObject getUsage() {
        return executeGetRequest("/user/usage", null);
    }
    
    /**
     * Get a list of forms for this account
     * @return Returns basic details such as title of the form, when it was created, number of new and total submissions.
     */
    public JSONObject getForms() {
    	return executeGetRequest("/user/forms", null);
    }
    
    /**
     * Get a list of forms for this account
     * @param offset Start of each result set for form list.
     * @param limit Number of results in each result set for form list.
     * @param filter Filters the query results to fetch a specific form range.
     * @param orderBy Order results by a form field name.
     * @return Returns basic details such as title of the form, when it was created, number of new and total submissions.
     */
    public JSONObject getForms(String offset, String limit, HashMap<String, String> filter, String orderBy) {
    	HashMap<String, String> params = createConditions(offset, limit, filter, orderBy);
    	
    	return executeGetRequest("/user/forms", params);
    }
    
    /**
     * Get a list of submissions for this account.
     * @return Returns basic details such as title of the form, when it was created, number of new and total submissions.
     */
    public JSONObject getSubmissions() {
        return executeGetRequest("/user/submissions", null);
    }

    /**
     * Get a list of submissions for this account.
     * @param offset Start of each result set for form list.
     * @param limit Number of results in each result set for form list.
     * @param filter Filters the query results to fetch a specific form range.
     * @param orderBy Order results by a form field name.
     * @return Returns basic details such as title of the form, when it was created, number of new and total submissions.
     */
    public JSONObject getSubmissions(String offset, String limit, HashMap<String, String> filter, String orderBy) {
    	HashMap<String, String> params = createConditions(offset, limit, filter, orderBy);
    	
        return executeGetRequest("/user/submissions", params);
    }

    /**
     * Get a list of sub users for this account.
     * @return Returns list of forms and form folders with access privileges.
     */
    public JSONObject getSubUsers() {
        return executeGetRequest("/user/subusers", null);
    }

    /**
     * Get a list of form folders for this account.
     * @return Returns name of the folder and owner of the folder for shared folders.
     */
    public JSONObject getFolders() {
        return executeGetRequest("/user/folders", null);
    }

    /**
     * List of URLS for reports in this account.
     * @return Returns reports for all of the forms. ie. Excel, CSV, printable charts, embeddable HTML tables.
     */
    public JSONObject getReports() {
        return executeGetRequest("/user/reports", null);
    }
    
    /**
     * Get user's settings for this account.
     * @return Returns user's time zone and language.
     */
    public JSONObject getSettings() {
        return executeGetRequest("/user/settings", null);
    }
    
    /**
     * Update user's settings
     * @param settings New user setting values with setting keys
     * @return Returns changes on user settings.
     */
    public JSONObject updateSettings(HashMap<String, String> settings) {
    	return executePostRequest("/user/settings", settings);
    }

    /**
     * Get user activity log.
     * @return Returns activity log about things like forms created/modified/deleted, account logins and other operations.
     */
    public JSONObject getHistory() {
        return executeGetRequest("/user/history", null);
    }
  
    /**
     * Get user activity log.
     * @param action Filter results by activity performed. Default is 'all'.
     * @param date Limit results by a date range. If you'd like to limit results by specific dates you can use startDate and endDate fields instead.
     * @param sortBy Lists results by ascending and descending order.
     * @param startDate Limit results to only after a specific date. Format: MM/DD/YYYY.
     * @param endDate Limit results to only before a specific date. Format: MM/DD/YYYY.
     * @return Returns activity log about things like forms created/modified/deleted, account logins and other operations.
     */
    public JSONObject getHistory(String action, String date, String sortBy, String startDate, String endDate) {
    	HashMap<String, String> params = createHistoryQuery(action, date, sortBy, startDate, endDate);
    	
        return executeGetRequest("/user/history", params);
    }

    /**
     * Get basic information about a form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns form ID, status, update and creation dates, submission count etc.
     */
    public JSONObject getForm(long formID) {
        return executeGetRequest("/form/" + formID, null);
    }

    /**
     * Get a list of all questions on a form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns question properties of a form.
     */
    public JSONObject getFormQuestions(long formID) {
        return executeGetRequest("/form/" + formID + "/questions", null);
    }

    /**
     * Get details about a question
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param qid Identifier for each question on a form. You can get a list of question IDs from /form/{id}/questions.
     * @return Returns question properties like required and validation.
     */
    public JSONObject getFormQuestion(long formID, long qid) {
        return executeGetRequest("/form/" + formID + "/question/" + qid, null);
    }

    /**
     * List of a form submissions.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns submissions of a specific form.
     */
    public JSONObject getFormSubmissions(long formID) {
        return executeGetRequest("/form/" + formID + "/submissions", null);
    }
    
    /**
     * List of a form submissions.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param offset Start of each result set for form list.
     * @param limit Number of results in each result set for form list.
     * @param filter Filters the query results to fetch a specific form range.
     * @param orderBy Order results by a form field name.
     * @return Returns submissions of a specific form.
     */
    public JSONObject getFormSubmissions(long formID, String offset, String limit, HashMap<String, String> filter, String orderBy) {
    	HashMap<String, String> params = createConditions(offset, limit, filter, orderBy);
    	
        return executeGetRequest("/form/" + formID + "/submissions", params);
    }

    /**
     * Submit data to this form using the API.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param submission Submission data with question IDs.
     * @return Returns posted submission ID and URL.
     */
    public JSONObject createFormSubmission(long formID, HashMap<String, String> submission) {
    	HashMap<String, String> parameters = new HashMap<String, String>();
    	
    	Set<String> keys = submission.keySet();
    	
    	for(String key: keys) {
    		if (key.contains("_")) {
    			parameters.put("submission[" + key.substring(0, key.indexOf("_")) + "][" + key.substring(key.indexOf("_") + 1) + "]", submission.get(key));
    		} else {
    			parameters.put("submission[" + key + "]", submission.get(key));
    		}
    	}
    	
    	return executePostRequest("/form/" + formID +"/submissions", parameters);
    }
    
    /**
     * Submit data to this form using the API
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param submissions Submission data with question IDs.
     * @return Returns posted submission ID and URL.
     */
    public JSONObject createFormSubmissions(long formID, JSONObject submissions) {
    	return executePutRequest("/form/" + formID + "/submissions", submissions);
    }

    /**
     * List of files uploaded on a form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns uploaded file information and URLs on a specific form.
     */
    public JSONObject getFormFiles(long formID) {
        return executeGetRequest("/form/" + formID + "/files", null);
    }

    /**
     * Get list of webhooks for a form
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns list of webhooks for a specific form.
     */
    public JSONObject getFormWebhooks(long formID) {
        return executeGetRequest("/form/" + formID + "/webhooks", null);
    }

    /**
     * Add a new webhook
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param webhookURL Webhook URL is where form data will be posted when form is submitted.
     * @return Returns list of webhooks for a specific form.
     */
    public JSONObject createFormWebhook(long formID, String webhookURL) {
    	
    	HashMap<String,String> params = new HashMap<String,String>();
    	params.put("webhookURL", webhookURL);
    	
    	return executePostRequest("/form/" + formID + "/webhooks", params);
    }

    /**
     * Delete a specific webhook of a form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param webhookID You can get webhook IDs when you call /form/{formID}/webhooks.
     * @return Returns remaining webhook URLs of form.
     */
    public JSONObject deleteFormWebhook(long formID, long webhookID) {
    	return executeDeleteRequest("/form/" + formID + "/webhooks/" + webhookID, null);
    }
    
    /**
     * Get submission data
     * @param sid You can get submission IDs when you call /form/{id}/submissions.
     * @return Returns information and answers of a specific submission.
     */
    public JSONObject getSubmission(long sid) {
        return executeGetRequest("/submission/" + sid, null);
    }

    /**
     * Get report details
     * @param reportID You can get a list of reports from /user/reports.
     * @return Returns properties of a specific report like fields and status.
     */
    public JSONObject getReport(long reportID) {
        return executeGetRequest("/report/" + reportID, null);
    }

    /**
     * Get folder details
     * @param folderID You can get a list of folders from /user/folders.
     * @return Returns a list of forms in a folder, and other details about the form such as folder color.
     */
    public JSONObject getFolder(String folderID) {
        return executeGetRequest("/folder/" + folderID, null);
    }
    
    /**
     * Get a list of all properties on a form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns form properties like width, expiration date, style etc.
     */
    public JSONObject getFormProperties(long formID) {
        return executeGetRequest("/form/" + formID + "/properties", null);
    }
    
    /**
     * Get a specific property of the form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param propertyKey You can get property keys when you call /form/{id}/properties.
     * @return Returns given property key value.
     */
    public JSONObject getFormProperty(long formID, String propertyKey ) {
        return executeGetRequest("/form/" + formID + "/properties/" + propertyKey, null);
    }
    
    /**
     * Get all the reports of a form, such as excel, csv, grid, html, etc.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns list of all reports in a form, and other details about the reports such as title.
     */
    public JSONObject getFormReports(long formID) {
    	return executeGetRequest("/form/" + formID + "/reports", null);
    }
    
    /**
     * Create new report of a form
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param report Report details. List type, title etc.
     * @return Returns report details and URL.
     */
    public JSONObject createReport(long formID, HashMap<String, String> report){
    	return executePostRequest("/form/" + formID + "/reports", report);
    }
    
    /**
     * Delete a single submission.
     * @param sid You can get submission IDs when you call /user/submissions.
     * @return Returns status of request.
     */
    public JSONObject deleteSubmission(long sid ) {
        return executeDeleteRequest("/submission/" + sid, null);
    }
    
    /**
     * Edit a single submission.
     * @param sid You can get submission IDs when you call /form/{id}/submissions.
     * @param submission New submission data with question IDs.
     * @return Returns status of request.
     */
    public JSONObject editSubmission(long sid, HashMap<String, String> submission ) {
    	HashMap<String, String> parameters = new HashMap<String, String>();
    	
    	Set<String> keys = submission.keySet();
    	
    	for(String key: keys) {
    		if (key.contains("_") && !key.equals("created_at")) {
    			parameters.put("submission[" + key.substring(0, key.indexOf("_")) + "][" + key.substring(key.indexOf("_") + 1) + "]", submission.get(key));
    		} else {
    			parameters.put("submission[" + key + "]", submission.get(key));
    		}
    	}
    	
        return executePostRequest("/submission/" + sid, parameters);
    }
    
    /**
     * Clone a single form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Returns status of request.
     */
    public JSONObject cloneForm(long formID ) {
        return executePostRequest("/form/" + formID + "/clone", null);
    }
    
    /**
     * Delete a single form question.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param qid Identifier for each question on a form. You can get a list of question IDs from /form/{id}/questions.
     * @return Returns status of request.
     */
    public JSONObject deleteFormQuestion(long formID, long qid ) {
        return executeDeleteRequest("/form/" + formID + "/question/" + qid, null);
    }
    
    /**
     * Add new question to specified form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param question New question properties like type and text.
     * @return Returns properties of new question.
     */
    public JSONObject createFormQuestion(long formID, HashMap<String, String> question ) {
    	HashMap<String, String> params = new HashMap<String, String>();
    	
    	Set<String> keys = question.keySet();
    	
    	for(String key: keys) {
    		params.put("question[" + key + "]", question.get(key));
    	}
    	
        return executePostRequest("/form/" + formID + "/questions", params);
    }
    
    /**
     *  Add new questions to specified form.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param questions New question properties like type and text.
     * @return Returns properties of new questions.
     */
    public JSONObject createFormQuestions(long formID, JSONObject questions) {
        return executePutRequest("/form/" + formID + "/questions", questions);
    }
    
    /**
     * Edit a single question properties.
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param qid Identifier for each question on a form. You can get a list of question IDs from /form/{id}/questions.
     * @param questionProperties New question properties like text and order.
     * @return Returns edited property and type of question.
     */
    public JSONObject editFormQuestion(long formID, long qid, HashMap<String, String> questionProperties ) {
    	HashMap<String, String> question = new HashMap<String, String>();
    	
    	Set<String> keys = questionProperties.keySet();
    	
    	for(String key: keys) {
    		question.put("question[" + key + "]", questionProperties.get(key));
    	}
    	
        return executePostRequest("/form/" + formID + "/question/" + qid, question);
    }
    
    /**
     * Add or edit properties of a specific form
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param formProperties New properties like label width.
     * @return Returns edited properties.
     */
    public JSONObject setFormProperties(long formID, HashMap<String, String> formProperties) {
    	HashMap<String, String> properties = new HashMap<String, String>();
    	
    	Set<String> keys = formProperties.keySet();
    	
    	for(String key: keys) {
    		properties.put("properties[" + key + "]", formProperties.get(key));
    	}
    	
        return executePostRequest("/form/" + formID + "/properties", properties);
    }
    
    /**
     * Add or edit properties of a specific form
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @param formProperties New properties like label width.
     * @return Returns edited properties.
     */
    public JSONObject setMultipleFormProperties(long formID, JSONObject formProperties) {
    	return executePutRequest("/form/" + formID + "/properties", formProperties);
    	
    }
    
    /**
     * Create a new form
     * @param form Questions, properties and emails of new form.
     * @return Returns new form.
     */
    public JSONObject createForm(Map form) {
		HashMap<String, String> params = new HashMap<String, String>();
		
		Set<String> formKeys = form.keySet();
		
		for(String formKey: formKeys) {
			if(formKey.equals("properties")) {
				HashMap<String, String> properties = (HashMap<String, String>) form.get(formKey);
				Set<String> propertyKeys = properties.keySet();
				for(String propertyKey: propertyKeys) {
					params.put(formKey + "[" + propertyKey + "]", properties.get(propertyKey));
				}
			}
			else {
				Map formItem = (Map) form.get(formKey);
				Set<String> formItemKeys = formItem.keySet();
				
				for(String formItemKey: formItemKeys) {
					HashMap<String, String> fi = (HashMap<String, String>) formItem.get(formItemKey);
					Set<String> fiKeys = fi.keySet();
					
					for(String fiKey: fiKeys) {
						params.put(formKey + "[" + formItemKey + "][" + fiKey + "]", fi.get(fiKey));
					}
				}
			}
			
		}
		
    	return executePostRequest("/user/forms", params);
    }
    
    /**
     * Create a new form
     * @param form Questions, properties and emails of new form.
     * @return Returns new form.
     */
    public JSONObject createForms(JSONObject form) {
    	return executePutRequest("/user/forms", form);
    }
    
    /**
     * Delete a single form
     * @param formID Form ID is the numbers you see on a form URL. You can get form IDs when you call /user/forms.
     * @return Properties of deleted form.
     */
    public JSONObject deleteForm(long formID) {
    	return executeDeleteRequest("/form/" + formID, null);
    }
    
    /**
     * Register with username, password and email
     * @param userDetails Username, password and email to register a new user
     * @return Returns new user's details
     */
    public JSONObject registerUser(HashMap<String, String> userDetails) {
    	return executePostRequest("/user/register", userDetails);
    }
    
    /**
     * Login user with given credentials
     * @param credentials Username, password, application name and access type of user
     * @return Returns logged in user's settings and app key
     */
    public JSONObject loginUser(HashMap<String, String> credentials) {
    	return executePostRequest("/user/login", credentials);
    }
    
    /**
     * Logout user
     * @return Returns status of request
     */
    public JSONObject logoutUser() {
    	return executeGetRequest("/user/logout", null);
    }
    
    /**
     * Get details of a plan
     * @param planName Name of the requested plan. FREE, PREMIUM etc.
     * @return Returns details of a plan
     */
    public JSONObject getPlan(String planName) {
    	return executeGetRequest("/system/plan/" + planName, null);
    }

    /**
     * Delete a single report
     * @param reportID You can get a list of reports from /user/reports.
     * @return Returns status of request.
     */
    public JSONObject deleteReport(long reportID) {
        return executeDeleteRequest("/report/" + reportID, null);
    }
}

