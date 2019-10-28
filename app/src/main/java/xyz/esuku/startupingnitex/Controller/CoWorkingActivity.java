package xyz.esuku.startupingnitex.Controller;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Model.GrantModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class CoWorkingActivity extends AppCompatActivity {

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    boolean grant_selector =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working);
    }

//    private void getGrantByCategory(String item) {
//        String requestUrl = appLinks.get_accelerator_wort_by_category;
//
//
//
//        myProgressDialog.setMessage("Please wait ...");
//
//        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
//
//
//            myProgressDialog.dismiss();
//
//
//
//            List<GrantModel> listingModels = new ArrayList<>();
//
//            try {
//
//
//                if(response.contains("status")){
//                    JSONObject jsonObject  = new JSONObject(response);
//                    String status   = jsonObject.getString("status");
//                    String msg   = jsonObject.getString("msg");
//
//                    Toast.makeText(GrantActivity.this, msg, Toast.LENGTH_LONG).show();
//
//                    grantModelList.clear();
//                    grantAdapter.notifyDataSetChanged();
//                }else{
//                    JSONArray jsonArray   = new JSONArray(response);
//                    for (int i=0; i < jsonArray.length(); i++) {
//                        JSONObject  dataObj     = jsonArray.getJSONObject(i);
//                        Log.e("Grant", String.valueOf(i));
//
//                        String grant_id         = dataObj.getString("grant_id");
//                        String grant_name         = dataObj.getString("grant_name");
//                        String grant_desc         = dataObj.getString("grant_desc");
//                        String grant_img         = dataObj.getString("grant_img");
//
//                        listingModels.add(new GrantModel(grant_id,grant_name,grant_desc,grant_img));
//                    }
//
//
//
//                    grantModelList.clear();
//                    grantModelList.addAll(listingModels);
//                    // refreshing recycler view
//                    grantAdapter.notifyDataSetChanged();
//
//                }
//
//            } catch (JSONException e) {
//                myProgressDialog.dismiss();
//                e.printStackTrace();
//            }
//
//        }, error -> {
//            myProgressDialog.dismiss();
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> postMap = new HashMap<>();
//                postMap.put("grant_cat_name", item);
//
//                return postMap;
//            }
//        };
//
//
//        req.setRetryPolicy(new DefaultRetryPolicy(
//                16 * 1000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MyApplication.getInstance().addToRequestQueue(req);
//    }
//
//    private void getGrant() {
//
//        myProgressDialog.setMessage("Please wait ...");
//        HashMap<String, String> user = userDbHelper.getUserDetails();
//        String user_name    = user.get("user_name");
//
//        String requestUrl = appLinks.get_grant;
//        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
//                response -> {
//
//                    myProgressDialog.dismiss();
//                    if (response == null) {
//                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    Log.e("getEvent",response.toString());
//                    List<GrantModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<GrantModel>>() {
//                    }.getType());
//
//
//
//                    grantModelList.clear();
//                    grantModelList.addAll(list);
//
//                    // refreshing recycler view
//                    grantAdapter.notifyDataSetChanged();
//
//                }, error -> {
//            // error in getting
//            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//            myProgressDialog.dismiss();
//
//        });
//
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                16 * 1000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }
//    private void getGrantCategory() {
//
//        HashMap<String, String> user = userDbHelper.getUserDetails();
//        String user_name    = user.get("user_name");
//
//        String requestUrl = appLinks.get_grant_category;
//        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
//                response -> {
//                    if (response == null) {
//                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    List<String> arrayList = new ArrayList<>();
//                    String first_category_name  ="All Grant Type";
//                    arrayList.add(first_category_name);
//
//                    for (int i=0; i < response.length(); i++) {
//
//                        try {
//                            JSONObject detailObj = response.getJSONObject(i);
//                            int grant_cat_id        = detailObj.getInt("grant_cat_id");
//                            String grant_cat_name        = detailObj.getString("grant_cat_name");
//                            arrayList.add(grant_cat_name);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                    dataset = new LinkedList<>(arrayList);
//                    niceSpinner.attachDataSource(dataset);
//
//                }, error -> {
//            // error in getting
//            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//            Log.e("BusinessListing",error.toString());
//
//        });
//
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                16 * 1000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        MyApplication.getInstance().addToRequestQueue(request);
//    }

}
