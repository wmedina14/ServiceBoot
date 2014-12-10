package com.example.serviceboot;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class ActividadPrincipal extends Activity {

	TextView tvHola;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_principal);
		tvHola = (TextView) findViewById(R.id.tvHola);
		tvHola.setText(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));
		
		try {
			getApplicationContext().startService(new Intent(getApplicationContext(), ServicioLocalizacion.class));		
		} catch (Exception e) {
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_actividad_principal, menu);
		return true;
		
	}

}
