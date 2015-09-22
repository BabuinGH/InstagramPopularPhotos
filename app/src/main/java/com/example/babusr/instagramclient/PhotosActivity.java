package com.example.babusr.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "43b3babb1cd244dab0e6922a8c25f60d";
    private ArrayList<InstagramPhoto>photos;
    private InstagramPhotosAdapter aPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //Initialize the photos(arraylist)
        photos = new ArrayList<>();
        //Create the adapter to link to the source
        aPhotos = new InstagramPhotosAdapter(this,photos);
        //Find the ListView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //Set the adapter binding it to the Listview
        lvPhotos.setAdapter(aPhotos);
        //Send out API request for Popular Photos
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos(){
        /*Client-ID ->  43b3babb1cd244dab0e6922a8c25f60d
          - Popular Media: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        //Trigger the Get Request
        client.get(url,null,new JsonHttpResponseHandler() {
            //OnSuccess (worked,200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                /* Expecting a JSON Object
                - Response
                - Type: {“data” => [Array of Items] => “type”} (“Image” or “Video”)
                */
                //Iterate each of the photo item and decode the item into a Java Object

                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //Array of posts
                    //Iterate Array of posts
                    for (int i=0; i < photosJSON.length(); i++ ){
                        //Get JSON object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //Decode the attributes of json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // Author Name: {“data” => [Array of Items] => “user” => “username”}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        //Caption: {“data” => [Array of Items] => “caption” => “text”}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        //URL: {“data” => [Array of Items] => “Images” => “standard_resolution” => “url” }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        //Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        //No.of likes
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        //Add all the decoded objects to the photos arraylist
                        photos.add(photo);


                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();


            }

/*            //onFailure (fail)
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable) {

            }*/
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
