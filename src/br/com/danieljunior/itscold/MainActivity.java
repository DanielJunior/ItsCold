package br.com.danieljunior.itscold;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.danieljunior.itscold.models.DataRequestTask;

public class MainActivity extends Activity implements OnItemSelectedListener {
	private String weather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		setTitle("It's Cold?");
		Spinner spinner = (Spinner) findViewById(R.id.cities);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.cities_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String city = parent.getItemAtPosition(position).toString();
		if (!city.equalsIgnoreCase("Selecione uma cidade")) {

			DataRequestTask task = new DataRequestTask(MainActivity.this) {
				@Override
				public void receiveData(Object result) {
					// TODO Auto-generated method stub
					weather = (String) result;
					setImageView();
					showMessage();
				}
			};
			task.execute(city);
		}else{
			ImageView image = (ImageView) findViewById(R.id.image);
			image.setImageResource(R.drawable.termometro);
		}
	}

	private void showMessage() {
		Toast.makeText(getApplicationContext(), "O clima está: " + weather,
				Toast.LENGTH_LONG).show();
	}

	private void setImageView() {
		// TODO Auto-generated method stub

		ImageView image = (ImageView) findViewById(R.id.image);
		int img;
		if (weather.equalsIgnoreCase("FRIO")) {
			img = R.drawable.frio;
		} else if (weather.equalsIgnoreCase("CALOR")) {
			img = R.drawable.calor;
		} else {
			img = R.drawable.normal;
		}

		image.setImageResource(img);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
