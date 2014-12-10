package com.example.serviceboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.R.integer;
import android.app.Service;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;


/*
 * @Author: Ing. William Medina Romero
 * @Date: 01 de Octubre 2014
 * @Description: Clase que implementa metodos de localizacion mediante GPS 
 */

public class ServicioLocalizacion extends Service implements LocationListener {

	//private Context ctx;
	private double latitud;
	private double longitud;
	private Location location;
	private boolean gpsActivo;
	private LocationManager locationManager;
	private CapturaLocalizacion localizacion;
	private String idDevice;
	private EmpresaConfig gConfig;
	private AsyncTask<integer, Void, Object> a;
	private Location lLastKnowLocation;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		// inicializarLocalizacion();
		//Toast.makeText(this, "Servicio creado onCreated", Toast.LENGTH_LONG).show();
		Log.d("SERVICEBOOT", "Servicio creado onCreated");
	}

	
	@Override
	public int onStartCommand(Intent intent,int flags, int startId){
		try {
		idDevice = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		a = new DescargarConfiguracion().execute();
		} catch (Exception e) {
			//Toast.makeText(this, "Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
		}		
		return START_STICKY;
	} 
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "Servicio destruido", Toast.LENGTH_LONG).show();
		Log.d("SERVICEBOOT", "Servicio destruido onDestroy");
	}
	

	@Override
	public void onLocationChanged(Location location) {
		Log.e("WILL_APPLICATION","Nuevas Coordenadas: "+location.getLatitude()+", "+location.getLongitude());
		try {			
			if (lLastKnowLocation != null )    {
				Log.e("WILL_APPLICATION","Distancia: "+lLastKnowLocation.distanceTo(location)+" mts");
				if ((lLastKnowLocation.distanceTo(location)) > gConfig.getDistancia()){
					localizacion = new CapturaLocalizacion(location.getLongitude(), location.getLatitude(), idDevice);
					Log.e("WILL_APPLICATION","Onchanged: Lat: "+localizacion.getLatitud()+" Lon: "+localizacion.getLongitud());
					new TareaSegundoPlano().execute(localizacion);					
				}				
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		//locationManager.remo
	}

	@Override
	public void onProviderEnabled(String provider) {
		inicializarLocalizacion();		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	
	/* METODOS PROGRAMADOS */
	public ServicioLocalizacion(Context c){
		super();
	}
	
	public ServicioLocalizacion(){
		super();
	}
	
	public void inicializarLocalizacion(){
		try {
			locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
						
			lLastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
			if ((gConfig != null) && (gConfig.getDistancia() > 0 && gConfig.getDuracion() > 0) ) {				
				if (lLastKnowLocation != null){
					//Toast.makeText(this, "Duracion: "+gConfig.getDuracion()+" Distancia: "+gConfig.getDistancia()+" \nÚltima lon: " +String.valueOf(lLastKnowLocation.getLongitude()) +" lat: "+String.valueOf(lLastKnowLocation.getLatitude()), Toast.LENGTH_LONG  ).show();
					//Log.e("WILL_APPLICATION","Duracion: "+gConfig.getDuracion()+" Distancia: "+gConfig.getDistancia()+" \nÚltima lon: " +String.valueOf(lLastKnowLocation.getLongitude()) +" lat: "+String.valueOf(lLastKnowLocation.getLatitude()));
				}
				if (gpsActivo){					
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*60 * gConfig.getDuracion() , gConfig.getDistancia(), this);
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null){
						latitud = location.getLatitude();
						longitud = location.getLongitude();
					}
				}				
			}		
		} catch (Exception e) {
			Log.e("WILL_APPLICATION","ERROR: "+e.getMessage());
			//Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	
	
	static class TareaSegundoPlano extends AsyncTask<CapturaLocalizacion, Void, Void>{

		CapturaLocalizacion datos;
		String url ="http://www.arcavi.net/arcavi/gpsReceive.php";
		String paramString;
		
		
		@Override
		protected Void doInBackground(CapturaLocalizacion... params) {
			datos = params[0];
			paramString = "";
			
			HttpClient client = new DefaultHttpClient();
			HttpGet envioGet;
			try {
				List<NameValuePair> nombres = new ArrayList<NameValuePair>();
				nombres.add(new BasicNameValuePair("idDevice", datos.getIdDevide()) );
								
				url = url +"?idDevice="+datos.getIdDevide() + "&longitud="+datos.getLongitud()+"&latitud="+datos.getLatitud();
				
				envioGet = new HttpGet();
				envioGet.setURI(new URI(url));	
				HttpResponse response = client.execute(envioGet);				
				HttpEntity entid = response.getEntity();
				
				Log.e("WILL_APPLICATION", entid.toString());
				
			} catch (UnsupportedEncodingException e) {
				Log.e("ERROR_WILL", "UnsupportedEncodingException: "+e.getMessage());
		    } catch (ClientProtocolException e) {
		    	Log.e("ERROR_WILL", "ClientProtocolException: "+e.getMessage());
		    } catch (IOException e) {
		    	Log.e("ERROR_WILL", "IOException: "+e.getMessage());
		    }catch (Exception e) {
		    	Log.e("ERROR_WILL", "Exception: "+e.getMessage());
		    }						
			return null;
		}		
	}
		
	private class DescargarConfiguracion extends AsyncTask<integer, Void, Object>{
		@Override
		protected Object doInBackground(integer... params) {
			try {
				gConfig = EmpresaConfig.getInstance(idDevice);
				return 1;
				
			} catch (Exception e) {
				return 2;
			}
		}		
		
		@Override
		protected void onPostExecute(Object result) {
			inicializarLocalizacion();
		}		
	}
	

}






