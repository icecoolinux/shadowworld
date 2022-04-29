package com.icecool.worldinshadow;

import java.util.concurrent.Semaphore;

import com.icecool.worldinshadow.ApiOpenAL.Emisor;
import com.icecool.worldinshadow.ApiOpenAL.Receptor;

import android.content.Context;
import android.media.AudioManager;

public class Datos {

	static public float AVANCE_USUARIO = 1.0f;
	static public float VEL_ANGULO_ROTAR_SEGUNDO = 0.5f;
	
	static public float DISTANCIA_CERCANA_OBJETIVO = 2.0f;

	static public int VIDA_QUITADA_ENEMIGO = 5;
	
	static public long MS_LUEGO_ATAQUE_RECUPERA_VIDA = 6000;
	
	
	//Obtengo si utiliza auriculares o no.
	private AudioManager audioMan = null;
    
    
	//Usar sensor.
	private boolean usarSensor;
	
	//Tiene sensor.
	private boolean tieneSensor;
	
	//Vista.
	private Vector3 vista;
	
	//Posicion.
	private Vector3 pos;
	
	//Vida.
	private int vida;
	
	//Tiempo que me atacaron, es para recuperar vida.
	private long tiempoMeAtacaron;
	
	//Puede caminar.
	private boolean puedeCaminar;
	
	//Utiliza cuchillo.
	private boolean tieneCuchillo;
	
	private Semaphore sem;
	
	static private Datos instancia = null;
	
	private Datos(){
	
		sem = new Semaphore(1);
		
		usarSensor = true;
		tieneSensor = false;
		vista = new Vector3(1, 0, 0);
		pos = new Vector3(0, 0, 0);
		tiempoMeAtacaron = System.currentTimeMillis();
	}
	
	static public Datos getInstancia(){
		if(instancia == null)
			instancia = new Datos();
		return instancia;
	}
	
	public boolean getTieneAuriculares(Context context){
		
		if(audioMan == null) audioMan = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        return audioMan.isWiredHeadsetOn();
		//return (audioMan.getRouting(audioMan.getMode()) & AudioManager.ROUTE_HEADSET) == AudioManager.ROUTE_HEADSET;
	}
	
	public boolean getUsarSensor(){
		//Si quiere usar sensor y tiene sensores.
		return usarSensor && tieneSensor;
	}
	
	//Cambia entre usar sensores o pantalla.
	public void cambiarModoMovimiento(){
		
		usarSensor = !usarSensor;
		
		//Sin sensor, coloco horizontal la vista.
		if(!usarSensor){
			vista.z = 0;
			vista.normalizar();
		}
	}
	
	//Setea si tiene sensores.
	public void setTieneSensor(boolean tiene){
		tieneSensor = tiene;
	}
	
	public void setVista(Vector3 vista_){
		vista.set(vista_);
	}
	
	public void getVista(Vector3 vista_){
		vista_.set(vista);
	}
	
	public void setPosicion(Vector3 pos_){
		pos.set(pos_);
	}
	
	public void getPosicion(Vector3 pos_){
		pos_.set(pos);
	}
	
	public void setPuedeCaminar(boolean puede){
		puedeCaminar = puede;
	}
	
	public boolean getPuedeCaminar(){
		return puedeCaminar;
	}
	
	public void setTieneCuchillo(boolean tiene){
		tieneCuchillo = tiene;
	}
	
	public boolean getTieneCuchillo(){
		return tieneCuchillo;
	}
	
	public void setVida(int vida_){
		vida = vida_;
	}
	
	public int getVida(){
		return vida;
	}
	
	//Seteo tiempo que ataco, esto es para recuperar vida luego de tanto tiempo.
	public void setUltimaVezAtacaron(long tiempoAtacaron){
		tiempoMeAtacaron = tiempoAtacaron;
	}
	
	public long getUltimaVezAtacaron(){
		return tiempoMeAtacaron;
	}
	
	
	
	
	public void P(){
		try {sem.acquire();} catch (InterruptedException e) {}
	}
	
	public void V(){
		sem.release();
	}
}
