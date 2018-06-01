package com.personal_project.minami.midtermproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Minami on 2018-05-29.
 */

public class YoutubeSearchApi extends AsyncTask<String , Void, ArrayList> {
    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 6;
    private final View view;
    private static Context context;
    private static String apiKey;
    private final String TAG = "Seach: ";
    private String queryTerm;
    private static final String SHA1 = "BD:D8:67:64:9B:6F:AE:38:19:23:2A:B9:4B:E4:E3:81:C2:37:F4:BD";
    private HashMap<String, String> videoIdAndThumbnails = new HashMap<>();
    private ArrayList<String> videoIdArray = new ArrayList<>();
    private ArrayList<String> thumbnailArray = new ArrayList<>();
    private String selectedLength;

    public YoutubeSearchApi(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public static String getApiKey(){
//        this.context = context;
        Properties properties  = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        try {
            in = assetManager.open(PROPERTIES_FILENAME);
            properties.load(in);
        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME +
                    ": " + e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }
        apiKey = properties.getProperty("youtube.apikey");
        return apiKey;
    }

    private List<SearchResult> getVideoList() {
//        youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
//                new GoogleCredential()).build();
        YouTube youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                Log.i(TAG, "initialize: getPackageName-----> " + context.getPackageName());
                request.getHeaders().set("X-Android-Package", context.getPackageName());
                request.getHeaders().set("X-Android-Cert", SHA1);
            }
        }).setApplicationName(String.valueOf(R.string.app_name)).build();
        queryTerm = "hairstyles+for+" + selectedLength + "+hair+easy+and+quick+tutorial";
        YouTube.Search.List search = null;
        try {
            search = youTube.search().list("id, snippet");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("-----2.There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }
        search.setKey(getApiKey());
        search.setQ(queryTerm);
        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
        search.setType("video");
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/high/url)");
        SearchListResponse response = null;
        try {
            response = search.execute();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("-----3.There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }
        List<SearchResult> searchResultList = response.getItems();
        return searchResultList;
    }

    public void videoIdAndThumbnailUrl(List<SearchResult> videoList) {
        if ( videoList != null) {
            Iterator<SearchResult> searchResultIterator = videoList.iterator();
            if (!searchResultIterator.hasNext()){
                System.out.println("There aren't any video.");
                
            }
            while (searchResultIterator.hasNext()){
                SearchResult singleVideo = searchResultIterator.next();
                ResourceId resourceId = singleVideo.getId();
                if (resourceId.getKind().equals("youtube#video")){
                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();
                    String videoId = resourceId.getVideoId();
                    videoIdAndThumbnails.put(videoId, thumbnail.getUrl());
                    videoIdArray.add(videoId);
                    thumbnailArray.add(thumbnail.getUrl());
                }
            }
        }
    }

    @Override
    protected  ArrayList doInBackground(String... selected) {
        selectedLength = selected[0];

        videoIdAndThumbnailUrl(getVideoList());
        ArrayList<ArrayList> videoIdAndThumbnails = new ArrayList<>();
        videoIdAndThumbnails.add(videoIdArray);
        videoIdAndThumbnails.add(thumbnailArray);
        return videoIdAndThumbnails;
    }

    @Override
    protected void onPostExecute(ArrayList videoIdAndThumbnails) {
        videoIdArray = (ArrayList<String>) videoIdAndThumbnails.get(0);
        thumbnailArray = (ArrayList<String>) videoIdAndThumbnails.get(1);
        GridLayout grid_thumbnail_container = view.findViewById(R.id.grid_thumbnail_container);
        for (int i = 0; i < grid_thumbnail_container.getChildCount(); i++) {
            final String videoId = videoIdArray.get(i);
            final ImageButton imageButton = (ImageButton) grid_thumbnail_container.getChildAt(i);
            imageButton.setBackground(null);
            imageButton.setTag(videoId);
            Picasso.get().load(thumbnailArray.get(i)).into(imageButton);
        }
    }


    public HashMap<String, String> getVideoIdAndThumbnails() {
        return videoIdAndThumbnails;
    }

    public ArrayList<String> getVideoIdArray() {
        return videoIdArray;
    }

    public ArrayList<String> getThumbnailArray() {
        return thumbnailArray;
    }

}
