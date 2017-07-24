package in.skylinelabs.digiPune.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import in.skylinelabs.digiPune.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class Upload_Complaint extends Activity {
    ProgressDialog prgDialog;
    String encodedString;
 //   RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;

    GPSTracker gps;

    int an=0;

    public static Double lat =null, lon=null;

    GoogleMap map;
    MarkerOptions marker = new MarkerOptions().position(new LatLng(10, 10));

    Marker mrk;

    AlertDialog alertDialog;

    String networkTS;
    long date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    RequestParams params = new RequestParams();

    View.OnClickListener snackaction,snackaction1;

    EditText titleedt, messageedt;
    Switch anonymous;

   ImageView imgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_complaint);

        gps = new GPSTracker(this);



        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        imgb = (ImageView) findViewById(R.id.imageLocation);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Upload_Complaint.this, SelectHome.class);
                startActivity(i);
            }
        });

        titleedt = (EditText) findViewById(R.id.editTextTitle);
        messageedt = (EditText) findViewById(R.id.editTextDescription);

        anonymous = (Switch) findViewById(R.id.switchAn);
        anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anonymous.isChecked()) {
                    anonymous.setText("Anonymous");
                    an = 1;

                } else {
                    anonymous.setText("Non-Anonymous");
                    an = 0;
                }
            }
        });

    }




    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        final CharSequence[] options = {"Click A Photo", "Select From Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Complaint.this);
        builder.setTitle("Add Photo From Gallery");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Select From Gallery")) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                } else if (options[item].equals("Click A Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
            }
        });
        builder.show();

    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imgView2);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

                Calendar cal = Calendar.getInstance();
                int millisecond = cal.get(Calendar.MILLISECOND);
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR);

                TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String imei = tMgr.getDeviceId();

                params.put("filename",imei + fileName + hour + minute + second + millisecond);

            }
            else if(requestCode == 1)
            {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for(File temp: f.listFiles())
                    if(temp.getName().equals("temp.jpg")){
                        f = temp;
                        break;
                    }
                imgPath = f.getAbsolutePath();
                ImageView imgView = (ImageView) findViewById(R.id.imgView2);
                // Set the Image in ImageView
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

                Calendar cal = Calendar.getInstance();
                int millisecond = cal.get(Calendar.MILLISECOND);
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR);

                TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String imei = tMgr.getDeviceId();

                params.put("filename", imei + fileName + hour + minute + second + millisecond);
            }

            else {
                Snackbar.make(findViewById(android.R.id.content), "Please select an image", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
            }
        }

        catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Please try again", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage(View v) {
        String Title = titleedt.getText().toString();
        String Description = messageedt.getText().toString();

        if(Title.equals(null) || Title.equals("") ||  Description.equals(null) || Description.equals("")) {


            if(Title.equals(null) || Title.equals(""))
            {
                Snackbar.make(findViewById(android.R.id.content), "Title can't be empty", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
            }

            else if(Description.equals(null) || Description.equals(""))
            {
                Snackbar.make(findViewById(android.R.id.content), "Description can't be empty", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
            }
        }
        else
        {
            params.put("title", Title);
            params.put("description", Description);
            if(an == 1){
                params.put("user_name","anonymous");
            }
            else{
                final String PREFS_NAME = "GeoPreferences";
                final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String user_name = settings.getString("user_name","anonymous");
                params.put("user_name",user_name);
            }

            if(lat == null || lon == null)
            {
                lat = gps.getLatitude();
                lon = gps.getLongitude();

                if(lat == null || lon == null) {
                    Snackbar.make(findViewById(android.R.id.content), "Location not available", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
                else
                {
                    Toast.makeText(Upload_Complaint.this, "Location set to current", Toast.LENGTH_SHORT).show();
                    params.put("latitude", lat.toString());
                    params.put("longitude", lon.toString());
                    if (imgPath != null && !imgPath.isEmpty()) {
                        prgDialog.setMessage("Converting Image to Binary Data");
                        prgDialog.show();
                        encodeImagetoString();

                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), "Please select an image", Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                    }
                }

            }
            else
            {
                params.put("latitude", lat.toString());
                params.put("longitude", lon.toString());

                if (imgPath != null && !imgPath.isEmpty()) {
                    prgDialog.setMessage("Converting Image to Binary Data");
                    prgDialog.show();
                    encodeImagetoString();

                }
                else {
                    Snackbar.make(findViewById(android.R.id.content), "Please select an image", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }






        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {

        try {
            new AsyncTask<Void, Void, String>() {

                protected void onPreExecute() {

                }

                ;

                @Override
                protected String doInBackground(Void... params) {
                    BitmapFactory.Options options = null;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeFile(imgPath,
                            options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    encodedString = Base64.encodeToString(byte_arr, 0);
                    return "";
                }

                @Override
                protected void onPostExecute(String msg) {
                    prgDialog.setMessage("Calling Upload");
                    // Put converted Image string into Async Http Post param
                    params.put("image", encodedString);
                    // Trigger Image upload
                    triggerImageUpload();
                }
            }.execute(null, null, null);
        }
        catch (Exception e)
        {
            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Please try again", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();
        }
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }


    public void makeHTTPCall() {
        try {

            prgDialog.setMessage("Uploading image");
            AsyncHttpClient client = new AsyncHttpClient();
            // Don't forget to change the IP address to your LAN address. Port no as well.
            client.post("http://www.skylinelabs.in/Compalaint_Portal/upload_app.php",
                    params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            prgDialog.hide();
                            Snackbar.make(findViewById(android.R.id.content), "Complaint successfully registered", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            prgDialog.hide();
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Please try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                        }

                    });
        }
        catch (Exception e)
        {
            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Please try again", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();

            prgDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

}
