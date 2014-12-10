package com.example.serviceboot;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

/*
 * @Author: Ing. William Medina Romero
 * @Date: 07 de Agosto, 2014
 */

public class ArrancaInicio extends BroadcastReceiver{

	private ServicioLocalizacion s;
	private EmpresaConfig gConfig;

	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
		}
	};
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Iniciar el servicio
		//Toast.makeText(context, "Código ejecutandose en la clase inicio - Will", Toast.LENGTH_LONG).show();
		
		try {
			// cargar los datos preliminares			
			context.startService(new Intent(context, ServicioLocalizacion.class));			
		} catch (Exception e) {
			//Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}
	
	

	

}
