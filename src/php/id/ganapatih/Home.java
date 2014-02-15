package php.id.ganapatih;

import helper.peopleData;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Home extends FragmentActivity {

	peopleData p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		 p = new peopleData(getApplicationContext());
	}	
	
	public void goPanic(View v){
		Dialog dialog = new Dialog(Home.this,R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_panic);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.show();
		
		Button goRelawan = (Button) dialog.findViewById(R.id.button1);
		goRelawan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Dialog dialog = new Dialog(Home.this,R.style.mydialogstyle);
				dialog.setContentView(R.layout.dialog_relawan);
				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				dialog.show();
				
				Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
				List<String> list = new ArrayList<String>();
				list.add("Status Korban");
				list.add("Parah");
				list.add("Biasa");
				list.add("Biasa");
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, list);
				dataAdapter.setDropDownViewResource(R.layout.spinner_item_view);
				spinner.setAdapter(dataAdapter);
				
				((EditText) dialog.findViewById(R.id.editText1)).setText(p.getName());
				((EditText) dialog.findViewById(R.id.editText2)).setText(p.getPhone());
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
				
				((EditText) dialog.findViewById(R.id.editText1)).setText(p.getName());
				((EditText) dialog.findViewById(R.id.editText2)).setText(p.getPhone());
			}
		});
	}
	public void goMaps(View v){
	
	}
	public void goHelp(View v){
		Dialog dialog = new Dialog(Home.this,R.style.mydialogstyle);
//		dialog.setContentView(R.layout.dialog_relawan);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.show();
	}	
}