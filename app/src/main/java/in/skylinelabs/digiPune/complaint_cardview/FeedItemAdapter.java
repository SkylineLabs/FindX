package in.skylinelabs.digiPune.complaint_cardview;

/**
 * Created by Reshul Dani on 07-10-2015.
 */

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.skylinelabs.digiPune.R;


public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.FeedViewHolder> {
    private Activity activity;
    private List<FeedItem> feedList;
    public ImageLoader imageLoader;

    Geocoder geocoder;
    List<Address> addresses;

    public FeedItemAdapter(Activity a, List<FeedItem> feedList) {
        activity = a;
        this.feedList = feedList;
        imageLoader = new ImageLoader(activity.getApplicationContext());

        geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());


    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public void onBindViewHolder(FeedViewHolder feedViewHolder, int i) {
        FeedItem fi = feedList.get(i);
        feedViewHolder.title.setText(fi.title);
        feedViewHolder.url.setText(fi.imgurl);
        feedViewHolder.id.setText(fi.id);
        feedViewHolder.description.setText(fi.description);
        feedViewHolder.latitude.setText(fi.latitude);
        feedViewHolder.longitude.setText(fi.longitude);
        feedViewHolder.b.setText("# " +fi.id);
        imageLoader.DisplayImage(fi.imgurl, feedViewHolder.img);

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(fi.latitude), Double.parseDouble(fi.longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();


        if(city == null)
        {
            city="";
        }

        if(address == null)
        {
            address="";
        }
        if(postalCode == null)
        {
            postalCode="";
        }

        feedViewHolder.location.setText(address+ " "+city+" " +postalCode);

    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new FeedViewHolder(itemView);
    }


    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView url;
        protected TextView id;
        protected TextView description;
        protected ImageView img;
        protected TextView location;
        protected TextView latitude;
        protected TextView longitude;
        protected Button b;
        public FeedViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            url =  (TextView) v.findViewById(R.id.url);
            description = (TextView)  v.findViewById(R.id.description);
            img = (ImageView) v.findViewById(R.id.img);
            id =  (TextView) v.findViewById(R.id.ID);
            location =  (TextView) v.findViewById(R.id.location);
            latitude =  (TextView) v.findViewById(R.id.latitude);
            longitude =  (TextView) v.findViewById(R.id.logitude);
            b = (Button) v.findViewById(R.id.idbutton);
        }
    }


}