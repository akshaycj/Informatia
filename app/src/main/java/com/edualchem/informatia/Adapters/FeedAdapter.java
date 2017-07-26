package com.edualchem.informatia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.edualchem.informatia.FeedData;
import com.edualchem.informatia.R;

import java.util.ArrayList;

/**
 * Created by User on 2/20/2017.
 */

public class FeedAdapter extends ArrayAdapter<FeedData>{
    Context context;
    ArrayList<FeedData> dataList;
    int res;

    public FeedAdapter(Context context,int res, ArrayList<FeedData> dataList){
        super(context,res,dataList);
        this.context = context;
        this.dataList = dataList;
        this.res = res;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.feed_item,null);
        }
        final FeedData data = dataList.get(position);

        TextView title,desc;
        final ImageView img;
        final ProgressBar bar = (ProgressBar)view.findViewById(R.id.img_progress);
        title = (TextView)view.findViewById(R.id.feed_title);
        desc = (TextView)view.findViewById(R.id.feed_msg);
        img = (ImageView)view.findViewById(R.id.feed_img);



        title.setText(data.getTitle());
        Log.e("Set text",data.getTitle());
        desc.setText(data.getDesc());
        //img.setImageBitmap(getImage(data.getKey()));
        bar.setVisibility(View.VISIBLE);
        if(data.getKey().contains(".jpg")||data.getKey().contains(".png")){
            StorageReference ref  = FirebaseStorage.getInstance().getReferenceFromUrl(data.getKey());
            Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(ref)
                    .listener(new RequestListener<StorageReference, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                            bar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            bar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(img);

        }
        else if(data.getKey().contains("youtu")){
            bar.setVisibility(View.GONE);
            img.setImageResource(R.drawable.youtube);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getKey())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            bar.setVisibility(View.GONE);
        }



        return view;


    }

    public Bitmap getImage(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return  decodedByte;
    }

}
