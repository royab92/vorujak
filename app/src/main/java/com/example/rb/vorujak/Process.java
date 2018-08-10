package com.example.rb.vorujak;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bahrampashootan on 7/14/2018 AD.
 */

public class Process {
    private Context context;
    public Process(Context context){
        this.context=context;
    }
    public interface ProcessResponse{
        void getProcess(String code,String question,String answer1,String answer2,String answer3,int correct,int StatusCode, String Message);
    }
    public void onProcess(JSONObject requestja, final Process.ProcessResponse osc ){
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,"Http://baziigram.com/api/webAPI/UpdateUserAndLocation",requestja,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject nresponse;
                //  Log.i("Get_Response: ", response.toString());
                try {
                    nresponse= response.getJSONObject("Data");
                     String code = nresponse.getString("code");
                    String question = nresponse.getString("question");
                    String answer1 = nresponse.getString("answer1");
                    String answer2 = nresponse.getString("answer2");
                    String answer3 = nresponse.getString("answer3");
                    int correct=nresponse.getInt("correctAnswer");
                    int status=nresponse.getInt("StatusCode");
                    String message=nresponse.getString("Message");
                    osc.getProcess(code,question,answer1,answer2,answer3,correct,status,message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //  osc.onSignUp(false);
                //Log.i("Get_Error: ", error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(18000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(request);

    }

}
