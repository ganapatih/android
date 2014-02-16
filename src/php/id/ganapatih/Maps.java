package php.id.ganapatih;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class Maps extends Activity {

	static final LatLng JOGJA = new LatLng(-7.797483,110.36821);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		
		initData();
	}
	
	public void initData(){
		Marker jogja = map.addMarker(new MarkerOptions()
				.position(JOGJA)
				.title("Helloo :D")
				.snippet("jogja is cool"));
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(JOGJA, 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}
}