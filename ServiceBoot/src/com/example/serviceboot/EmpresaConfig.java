package com.example.serviceboot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.provider.Settings.Secure;
import android.util.Log;

/*
 * Ing. William Medina Romero
 * Grupo Arcavi | Departamento de Sistemas
 * 30 de Setiembre 2014
 * wmedina14@gmail.com 
 */

// Clase que se encarga de cargar la configuración de la empresa.
public class EmpresaConfig {
	
	private static EmpresaConfig admEmpresa = null;
	private static int gDuracionRefresco;
	private static int gDistanciaMinima;
	private static String gIdDevice;
	
	
	public static synchronized EmpresaConfig getInstance(String pIdDevice) throws Exception{
		if (null == admEmpresa){
			gIdDevice = pIdDevice;
			admEmpresa = new EmpresaConfig();			
		}
		return admEmpresa;
	}
	
	
	protected EmpresaConfig () throws Exception{
		try {
			for (int i=0; i < 15000;i++){
				if (cargaConfigEmpresa()){
					return;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Boolean cargaConfigEmpresa(){
		String result = "";
		//gIdDevice  = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		try {
			HttpClient httpCliente = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://www.somepage.com/configEmpresa.php?idDispositivo="+gIdDevice);
			
			HttpResponse response = httpCliente.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line+"\n");
			}// fin del while
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag","Error en la conexion http: "+e.toString());
			return false;
		}
		
		
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i<jArray.length(); i++){
				JSONObject json_data = jArray.getJSONObject(i);
				
				gDuracionRefresco = Integer.parseInt(json_data.getString("periodicidad_actualizacion")) ;
				gDistanciaMinima = Integer.parseInt(json_data.getString("distancia_actualizacion")) ;
				
			}
			return true;
		} catch (Exception e) {
			Log.e("log_tag","Error convirtiendo datos: "+e.toString());
			return false;
		}
		
	}// fin del metodo 	cargaConfigEmpresa
	
	
	
	public int getDuracion(){
		return gDuracionRefresco;
	}
	
	public int getDistancia(){
		return gDistanciaMinima;
	}
	
	public String getIdDevice(){
		return gIdDevice;
	}
	
		
}
