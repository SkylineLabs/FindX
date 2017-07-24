package in.skylinelabs.digiPune.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import in.skylinelabs.digiPune.R;

public class SOS_Contacts extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

	FloatingActionButton btn;
	ImageButton c1,c2,c3,c4;
	EditText edt1, edt2, edt3, edt4;
	Boolean one,two,three,four = false;

	AlertDialog alertDialog;

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_contacts);


		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setElevation(5);
		getSupportActionBar().setTitle("digiPune");
		drawerFragment = (FragmentDrawer)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);
		displayView(7);

		edt1 = (EditText)  findViewById(R.id.editText1);
        edt2 = (EditText)  findViewById(R.id.editText2);
        edt3 = (EditText)  findViewById(R.id.editText3);
        edt4 = (EditText)  findViewById(R.id.editText4);
        
        edt1.setHint("Enter phone number 1");
        edt2.setHint("Enter phone number 2");
        edt3.setHint("Enter phone number 3");
        edt4.setHint("Enter phone number 4");
        
        final String PREFS_NAME = "GeoPreferences";
      	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        edt1.setText(settings.getString("sos_1", ""));
        edt2.setText(settings.getString("sos_2", ""));
        edt3.setText(settings.getString("sos_3", ""));
        edt4.setText(settings.getString("sos_4", ""));
        
        c1 =  (ImageButton)  findViewById(R.id.con1);
        c2 =  (ImageButton)  findViewById(R.id.con2);
        c3 =  (ImageButton)  findViewById(R.id.con3);
        c4 =  (ImageButton)  findViewById(R.id.con4);
        
        c1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				 one = true;
				 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		         intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		         startActivityForResult(intent, 1);
			
			}
		});
        
 c2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				 two = true;
				 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		         intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
		         startActivityForResult(intent, 1);

			}
		});
 c3.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 
			 three = true;
			 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	         intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	         startActivityForResult(intent, 1);
		
		}
	});
 c4.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 
			 four = true;
			 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	         intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	         startActivityForResult(intent, 1);
		
		}
	});
 
       
        
        
         btn = (FloatingActionButton)  findViewById(R.id.button1);
		 btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					String one1 = edt1.getText().toString();
					settings.edit().putString("sos_1", one1).commit();
					
					String two1 = edt2.getText().toString();
					settings.edit().putString("sos_2", two1).commit();
					
					String three1 = edt3.getText().toString();
					settings.edit().putString("sos_3", three1).commit();
					
					String four1 = edt4.getText().toString();
					settings.edit().putString("sos_4", four1).commit();
					
					
					
					
					  AlertDialog.Builder builder = new AlertDialog.Builder(SOS_Contacts.this);
				        builder.setMessage("Contacts Set")
				               .setCancelable(true)
				               .setTitle("digiPune SOS Contacts")
				               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                	  
				                   }
				               });
				        AlertDialog alert1 = builder.create();
				        alert1.show();
				
				}
			});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_CANCELED){
			if (data != null) {
				Uri uri = data.getData();

				if (uri != null) {
					Cursor c = null;
					try {
						c = getContentResolver().query(uri, new String[]{
										ContactsContract.CommonDataKinds.Phone.NUMBER,
										ContactsContract.CommonDataKinds.Phone.TYPE },
								null, null, null);

						if (c != null && c.moveToFirst()) {
							String number = c.getString(0);
							int type = c.getInt(1);
							if(one)
							{
								showSelectedNumber1(type, number);
							}
							else if(two)
							{
								showSelectedNumber2(type, number);
							}
							else if(three)
							{
								showSelectedNumber3(type, number);
							}

							else if(four)
							{
								showSelectedNumber4(type, number);
							}

						}
						else
						{
							Toast.makeText(SOS_Contacts.this, "c is null",
									Toast.LENGTH_LONG).show();
						}
					}

					catch(Exception e)
					{
						Toast.makeText(SOS_Contacts.this, "Something went wrong. Try again",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

					finally {
						if (c != null) {
							c.close();
						}
					}

				}
			}}
	}
	

	public void showSelectedNumber1(int type, String number) {
	    edt1.setText(number);
	    one = false;
	}

	public void showSelectedNumber2(int type, String number) {
	    edt2.setText(number);
	    two = false;
	}


	public void showSelectedNumber3(int typ , String number) {
	    edt3.setText(number);
	    three=false;
	}


	public void showSelectedNumber4(int type, String number) {
	    edt4.setText(number);
	    four = false;
	}


	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	private void displayView(int position) {
		//Fragment fragment = null;
		String title = getString(R.string.app_name);
		Intent i;
		switch (position) {
			case 0:
				finish();
				Pre_launch_activity.post=0;
				break;
			case 1:
				finish();
				Intent i2 = new Intent(this, ComplaintPortal.class);
				startActivity(i2);
				break;
			case 2:
				finish();
				i = new Intent(this, Bus_Fetch.class);
				startActivity(i);
				break;
			case 3:
				finish();
				i = new Intent(this, FindGov.class);
				startActivity(i);
				break;
			case 4:
				finish();
				i = new Intent(this, MyLocation.class);
				startActivity(i);
				break;
			case 5:
				finish();
				i = new Intent(this, History.class);
				startActivity(i);
				break;
			case 6:
				finish();
				i = new Intent(this, Setting.class);
				startActivity(i);
				break;
			case 7:

				break;
			case 8:
				finish();
				i = new Intent(this, Favourites.class);
				startActivity(i);
				break;

			default:
				break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_log_in, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if(id == R.id.info) {
			AlertDialog.Builder alertadd = new AlertDialog.Builder(
					SOS_Contacts.this);
			LayoutInflater factory = LayoutInflater.from(SOS_Contacts.this);

			final View view = factory.inflate(R.layout.dialog_main, null);

			ImageView image = (ImageView) view.findViewById(R.id.imageView);
			image.setImageResource(R.drawable.sos_help);

			alertadd.setView(view);


			alertadd.setPositiveButton("ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dlg, int sumthin) {

				}
			});
			alertDialog =  alertadd.create();
			alertDialog.show();


		}

		return super.onOptionsItemSelected(item);
	}

}
