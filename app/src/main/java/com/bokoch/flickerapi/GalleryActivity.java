package com.bokoch.flickerapi;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mItems = new ArrayList<>();
        mPhotoRecyclerView = (RecyclerView) findViewById(R.id.photo_gallery_recyclerview);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //Пустим на вполнение чтобы в фоне подключился к серверу и в фоне подключил лист
        new FlickItemTask().execute();
        SetupAdapter();
    }
    private void SetupAdapter() {
        mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }
    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;
        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = itemView.findViewById(R.id.photo_gallery_item_imageview);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;
        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }
        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(GalleryActivity.this);
            View v = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            //Наполняет ViewHolder Данными. Для этого указана позиция.
            GalleryItem galleryItem = mGalleryItems.get(position);
            //Вступает в дело библиотека пикассо!
            //Загрузка изображения. Передаем Url и одновременно связываем его с ImgeView.
            //В одну блять строчку! Не 100, как раньше.
            Picasso.with(GalleryActivity.this).load(galleryItem.getUrl())
                    .into(holder.mItemImageView);

        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
    //API_KEY нужен, чтобы избежать DDOS.
    //Размер кэша розняся от девайса к девайсу. Определяется производителем!
    // Пикассо использует кэширование автоматическое.
    //Позволяет выполнять операции в отдельном потоке.
    private class FlickItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickrFetcher().FetchItems();
        }
        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            SetupAdapter();
        }
    }
}
