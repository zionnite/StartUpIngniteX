package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.EventAdapter;
import xyz.esuku.startupingnitex.ItemClicked.EventCheckListener;
import xyz.esuku.startupingnitex.ItemClicked.EventItemClickListener;
import xyz.esuku.startupingnitex.Model.BusinessListingModel;
import xyz.esuku.startupingnitex.Model.EventModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity implements EventItemClickListener {

    ImageView back_btn,search_btn;

    RecyclerView evet_recyclerView;
    EventAdapter eventAdapter;
    List<EventModel> eventModelList = new ArrayList<>();

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        myProgressDialog    = new MyProgressDialog(EventActivity.this);
        userDbHelper = new UserDb_Helper(EventActivity.this);

        evet_recyclerView       = findViewById(R.id.event_RecyclerView);
        back_btn       = findViewById(R.id.back_btn);
        //search_btn       = findViewById(R.id.search_btn);

//        eventModelList.add(new EventModel("StartUp Ignite is the Shooting Star","Benin City","5mins Ago","content goes here","5 Likes",true));
//        eventModelList.add(new EventModel("MaryDel Hub","Edo State City","15mins Ago","content goes here here offcourse","10 Likes",false));


        eventAdapter = new EventAdapter(getApplicationContext(), eventModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        evet_recyclerView.setHasFixedSize(true);
        evet_recyclerView.setLayoutManager(linearLayoutManager);
        evet_recyclerView.setAdapter(eventAdapter);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Search Button Clicked", Toast.LENGTH_LONG).show();
//            }
//        });

        getEvent();

    }

    @Override
    public void OnItemClicked(EventModel eventModel) {

    }

    @Override
    public void OnBook(EventModel eventModel, int select_item) {
        int position    = select_item;
        HashMap<String, String> user = userDbHelper.getUserDetails();

        int event_id        = eventModel.getId();
        String user_name    = user.get("user_name");
        final String[] event_count = new String[1];
        final String[] event_check = new String[1];


        book_event(event_id, user_name, new EventCheckListener() {
            @Override
            public void bookChecker(String event_counter, String event_checker) {
                event_count[0] = event_counter;
                event_check[0] = event_checker;
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<String>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(String s) {
                        if(event_check[0].equals("unbooked")){
                            //eventModel.setEvent_checker("booked");
                            Toast.makeText(EventActivity.this,"You have Successfully Book this event",Toast.LENGTH_LONG).show();
                        }
                        else if(event_check[0].equals("booked")){
                            Toast.makeText(EventActivity.this,"You have UnBook this event",Toast.LENGTH_LONG).show();
                        }
                        eventAdapter.update_item(position,event_check[0],event_count[0]);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxAndroid",e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("RxAndroid","Completed");
                    }
                });
    }

    @Override
    public void OnUnbook(EventModel eventModel) {

    }


    private void performSearch(String search_term) {
        String requestUrl = appLinks.search_business;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("search_term", search_term);
        myProgressDialog.setMessage("Searching ...");

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        myProgressDialog.dismiss();


                        Log.e("BusSA",response.toString());
                        try {

                            List<BusinessListingModel> listingModels = new ArrayList<>();
                            JSONArray detail = response.getJSONArray("detail");
                            for (int i=0; i < detail.length(); i++) {

                                JSONObject detailObj = detail.getJSONObject(i);

                                String bus_logo     = detailObj.getString("bus_logo");
                                String bus_name     = detailObj.getString("bus_name");
                                String bus_phone     = detailObj.getString("bus_phone");
                                String bus_email     = detailObj.getString("bus_email");
                                String bus_address     = detailObj.getString("bus_address");

                                listingModels.add(new BusinessListingModel(bus_logo,bus_name,bus_phone,bus_email,bus_address));
                            }



//                            businessListingModelList.clear();
//                            businessListingModelList.addAll(listingModels);
//
//                            // refreshing recycler view
//                            businessListingAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("BusSB",e.toString());
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("BusSC",error.toString());
                myProgressDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void getEvent() {

        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_event+"/"+user_name;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.e("getEvent",response.toString());
                        List<EventModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<EventModel>>() {
                        }.getType());



                        eventModelList.clear();
                        eventModelList.addAll(list);

                        // refreshing recycler view
                        eventAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("BusinessListing",error.toString());

            }
        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }

    Observable<String> book_event(final int event_id, final String user_name, EventCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.toogle_event_booking;

                Map<String, String> postMap = new HashMap<>();
                postMap.put("event_id", String.valueOf(event_id));
                postMap.put("user_name", user_name);

                JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                String event_checker;
                                try {
                                    String msg = response.getString("msg");
                                    String status = response.getString("status");

                                    switch (status) {
                                        case "error_1":
                                            Log.e("RxAndroidServer1",response.toString());

                                            break;
                                        case "error_2":
                                            Log.e("RxAndroidServer2",response.toString());
                                            break;
                                        case "success":
                                            Log.e("RxAndroidServer3",response.toString());

                                            String event_counter = response.getString("event_counter");
                                            event_checker = response.getString("event_checker");

                                            checkListener.bookChecker(event_counter,event_checker);

//                                            EventModel eModel   = new EventModel();
//                                            eModel.setEvent_checker(event_checker);


                                            emitter.onNext(event_counter);
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    emitter.onError(e);
                                    Log.e("RxAndroidServer4",e.toString());
                                }

                                emitter.onComplete();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        emitter.onError(error);
                        Log.e("RxAndroidServer5",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };


                req.setRetryPolicy(new DefaultRetryPolicy(
                        16 * 1000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.getInstance().addToRequestQueue(req);



            }
        });
    };
}
