package com.bokoch.flickerapi;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickrFetcher {
    private static final String TAG = FlickrFetcher.class.getSimpleName();
    //Ключ для получения изображений. Чтобы обращаться к сервису FLicr через сервис API.
    private static final String API_KEY = "cfd200d92fe8e3b014af6ebabf65d2b0";
    public String getJSONSTRING(String urlSpec) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //Запрос
        Request request = new Request.Builder()
                .url(urlSpec)
                .build();
        //Получение ответа на зпрос по url. execure - выполнить.
        Response response = client.newCall(request).execute();
        //В Responce будет храниться ответ за запрос. Получает в формате Json.
        String result = response.body().string();
        return result;
    }
    public List<GalleryItem> FetchItems() {
        List<GalleryItem> galleryItems = new ArrayList<>();
        try {
            //Url адрес запроса
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    //Метод, который будет запрошен - читать на офф сайте.
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    //Хотим получить данные в формате json
                    .appendQueryParameter("format", "json")
                    //Не собираемся отвечать серверу после получения данных.
                    .appendQueryParameter("nojsoncallback", "1")
                    //Ссылка на миниатюру фотографии, с целью экономии траффика.
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getJSONSTRING(url);
            JSONObject jsonbody = new JSONObject(jsonString);
            ParseItems(galleryItems, jsonbody);
        } catch (IOException ios) {
            Log.e(TAG, "Ошибка загрузки данных", ios);
        } catch (JSONException e) {
            Log.e(TAG, "Ошибка парсинга JSON", e);
        }
        return galleryItems;
    }
    private void ParseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            if (photoJsonObject.getString("url_s") == null) {
                continue;
            }
            GalleryItem item = new GalleryItem(photoJsonObject.getString("id"),
                                               photoJsonObject.getString("url_s"),
                                               photoJsonObject.getString("title"));
            items.add(item);

        }
    }
}
