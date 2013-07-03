package com.jotform.api;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.json.*;


import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JotForm {
    
    private static String baseUrl = "https://api.jotform.com/";
    public static String version = "v1";
    private String apiKey;
    private boolean debugMode;

    public JotForm(String apiKey, boolean debugMode){

        this.apiKey = apiKey;
        this.debugMode = debugMode;
    }
    
    public JotForm(String apiKey){
    	this(apiKey, false);
    }

    private void Log(String message){

        if (this.debugMode){
            System.out.println(message);
        }
    }

    public JSONObject executeHttpRequest(String path, HashMap<String,String> params, String method) throws UnsupportedEncodingException {

        DefaultHttpClient client = new DefaultHttpClient();
        
        HttpUriRequest req;
        HttpResponse resp;

        if (method.equals("GET")){
            req = new HttpGet(JotForm.baseUrl + JotForm.version + path);
            req.addHeader("apiKey", this.apiKey);
        } else if (method.equals("POST")) {
            req = new HttpPost(JotForm.baseUrl + JotForm.version + path);
            req.addHeader("apiKey", this.apiKey);
            
            /*
            Set<String> keys = params.keySet();
            
            for(String key:keys) {
            	HttpParams parameter = new BasicHttpParams();
            	parameter.setParameter(key, params.get(key));
            	
            	System.out.println(parameter.toString());
            	
            	req.setParams(parameter);
            }
            */
            
            Set<String> keys = params.keySet();
            
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
            
            for(String key : keys) {
            	parameters.add(new BasicNameValuePair(key, params.get(key)));
            }
            
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            
            ((HttpPost) req).setEntity(entity);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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

    public JSONObject executeGetRequest(String path, HashMap<String,String> params) {
        try {
			return executeHttpRequest(path, params, "GET");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public JSONObject executePostRequest(String path, HashMap<String,String> params) {
        try {
			return executeHttpRequest(path, params, "POST");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public JSONObject getUser() {
        return executeGetRequest("/user", null);
    }

    public JSONObject getUsage() {
        return executeGetRequest("/user/usage", null);
    }
    
    public JSONObject getForms() {
    	return executeGetRequest("/user/forms", null);
    }

    // TODO: add submission queries
    public JSONObject getSubmissions() {
        return executeGetRequest("/user/submissions", null);
    }

    public JSONObject getSubUsers() {
        return executeGetRequest("/user/subusers", null);
    }

    public JSONObject getFolders() {
        return executeGetRequest("/user/folders", null);
    }

    public JSONObject getReports() {
        return executeGetRequest("/user/reports", null);
    }

    public JSONObject getSettings() {
        return executeGetRequest("/user/settings", null);
    }

    public JSONObject getHistory() {
        return executeGetRequest("/user/history", null);
    }

    public JSONObject getForm(long formID) {
        return executeGetRequest("/form/" + formID, null);
    }

    public JSONObject getFormQuestions(long formID) {
        return executeGetRequest("/form/" + formID + "/questions", null);
    }

    public JSONObject getFormQuestion(long formID, long qid) {
        return executeGetRequest("/form/" + formID + "/question/" + qid, null);
    }

    public JSONObject getFormSubmissions(long formID) {
        return executeGetRequest("/form/" + formID + "/submissions", null);
    }

    public JSONObject createFormSubmissions(long formID, HashMap<String, String> submission) {
    	HashMap<String, String> parameters = new HashMap<String, String>();
    	
    	Set<String> keys = submission.keySet();
    	
    	for(String key : keys) {
    		if(key.contains("first")){
    			parameters.put("submission[" + key.substring(0, key.indexOf("_")) + "][first]", submission.get(key));
    		} else if (key.contains("last")) { 
    			parameters.put("submission[" + key.substring(0, key.indexOf("_")) + "][last]", submission.get(key));
    		} else {
    			parameters.put("submission[" + key + "]", submission.get(key));	
    		}
    	}
    	
    	return executePostRequest("/form/" + formID +"/submissions", parameters);
    }

    public JSONObject getFormFiles(long formID) {
        return executeGetRequest("/form/" + formID + "/files", null);
    }

    public JSONObject getFormWebhooks(long formID) {
        return executeGetRequest("/form/" + formID + "/webhooks", null);
    }

    public JSONObject createFormWebhook(long formID, String webhookURL) {
    	
    	HashMap<String,String> params = new HashMap<String,String>();
    	params.put("webhookURL", webhookURL);
    	
    	return executePostRequest("/form/" + formID + "/webhooks", params);
    }

    public JSONObject getSubmission(long sid) {
        return executeGetRequest("/submission/" + sid, null);
    }

    public JSONObject getReport(long reportID) {
        return executeGetRequest("/report/" + reportID, null);
    }

    public JSONObject getFolder(long folderID) {
        return executeGetRequest("/folder/" + folderID, null);
    }
    
    public JSONObject getFormProperties(long formID) {
        return executeGetRequest("/form/" + formID + "/properties", null);
    }

}

