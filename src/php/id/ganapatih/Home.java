package php.id.ganapatih;

import helper.KorbanAsync;
import helper.RelawanAsync;
import helper.peopleData;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends FragmentActivity {

	peopleData p;
	private Context c;
	private Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		act = getParent();
		c = this;
		p = new peopleData(getApplicationContext());
	}

	public void goPanic(View v) {
		Dialog dialog = new Dialog(Home.this, R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_panic);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.show();

		Button goRelawan = (Button) dialog.findViewById(R.id.button1);
		goRelawan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Dialog dialog = new Dialog(Home.this, R.style.mydialogstyle);
				dialog.setContentView(R.layout.dialog_relawan);
				dialog.getWindow().setBackgroundDrawableResource(
						android.R.color.transparent);
				dialog.show();

				final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
				List<String> list = new ArrayList<String>();
				list.add("Status Darurat");
				list.add(getString(R.string.status_korban1));
				list.add(getString(R.string.status_korban2));
				list.add(getString(R.string.status_korban3));
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						getApplicationContext(), R.layout.spinner_item, list);
				dataAdapter.setDropDownViewResource(R.layout.spinner_item_view);
				spinner.setAdapter(dataAdapter);
				
				final EditText etUsername 	= ((EditText) dialog.findViewById(R.id.editText1));
				final EditText etPhone 		= ((EditText) dialog.findViewById(R.id.editText2));
				final EditText etDesc 		= ((EditText) dialog.findViewById(R.id.editText3));
				
				etUsername.setText(p.getName());
				etPhone.setText(p.getPhone());
				
				Button btKirim = (Button) dialog.findViewById(R.id.button1);
				btKirim.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						RelawanAsync R1 = new RelawanAsync(c, act) {
							@Override
							public void onResponseReceived(String result) {
								
								boolean success = Boolean.parseBoolean(result);
								if(success){
									Toast.makeText(getApplicationContext(),
											getString(R.string.submit_success), Toast.LENGTH_SHORT).show();

									p.setName(etUsername.getText().toString());
									p.setPhone(etPhone.getText().toString());
								}else{
									Toast.makeText(getApplicationContext(),getString(R.string.submit_success) , Toast.LENGTH_SHORT).show();
								}

								Intent intent = new Intent(Home.this,Home.class);
								startActivity(intent);
								finish();

							}
						};
						R1.execute(etUsername.getText().toString(), 
									etPhone.getText().toString(), 
									etDesc.getText().toString(),
									getGPS(),spinner.getSelectedItem().toString());

					}
				});
			}
		});

		Button goKorban = (Button) dialog.findViewById(R.id.button2);
		goKorban.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(Home.this, R.style.mydialogstyle);
				dialog.setContentView(R.layout.dialog_korban);
				dialog.getWindow().setBackgroundDrawableResource(
						android.R.color.transparent);
				dialog.show();
				
				final EditText etUsername 	= ((EditText) dialog.findViewById(R.id.editText1));
				final EditText etPhone 		= ((EditText) dialog.findViewById(R.id.editText2));
				
				etUsername.setText(p.getName());
				etPhone.setText(p.getPhone());


				Button btKirim = (Button) dialog.findViewById(R.id.button1);
				btKirim.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						KorbanAsync R2 = new KorbanAsync(c, act) {
							@Override
							public void onResponseReceived(String result) {
								
								boolean success = Boolean.parseBoolean(result);
								if(success){
									Toast.makeText(getApplicationContext(),
											getString(R.string.submit_success), Toast.LENGTH_SHORT).show();

									p.setName(etUsername.getText().toString());
									p.setPhone(etPhone.getText().toString());
								}else{
									Toast.makeText(getApplicationContext(),getString(R.string.submit_success) , Toast.LENGTH_SHORT).show();
								} 

								Intent intent = new Intent(Home.this,Home.class);
								startActivity(intent);
								finish();

							}
						};
						R2.execute(etUsername.getText().toString(), 
									etPhone.getText().toString(),
									getGPS());

					}
				});
			}
		});
	}

	public void goMaps(View v) {
		startActivity(new Intent(Home.this, Maps.class));

	}

	public void goHelp(View v) {
		Dialog dialog = new Dialog(Home.this, R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_help);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.show();

		((TextView) dialog.findViewById(R.id.tv_desc)).setText(Html
				.fromHtml(getString(R.string.desc_help)));
	}

	private String getGPS() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;

		for (int i = providers.size() - 1; i >= 0; i--) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}

		String r = "";
		if (l != null) {
			r += Double.toString(l.getLatitude())+",";
			r += Double.toString(l.getLongitude());
		}
		return r;
	}
}