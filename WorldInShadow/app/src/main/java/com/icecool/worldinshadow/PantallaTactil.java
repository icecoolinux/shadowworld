package com.icecool.worldinshadow;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Movie;
import android.util.Log;
import android.view.MotionEvent;

public class PantallaTactil {

	static private int TIEMPO_CLICK = 500;
	
	private int ancho;
	private int alto;
	
	public class ObjetoTactil {
		int id;

		// Posicion actual.
		Vector2 pos;

		// Posicion donde comenzo.
		Vector2 posInicial;
		
		//Tiempo de creacion.
		long ms;
		
		public ObjetoTactil(){
			ms = System.currentTimeMillis();
		}
	}

	private ArrayList<ObjetoTactil> objetos;
	
	//Indica que hizo click y solto enseguida.
	private boolean hayAccion;
	
	//Ms que tiene dos dedos presionados, si son mas de 3 segundos es nueva partida.
	private long msDosDedos_NuevaPartida;
	
	private Datos datos;

	static private PantallaTactil instancia = null;

	private PantallaTactil() {
		datos = Datos.getInstancia();
		objetos = new ArrayList<ObjetoTactil>();
		hayAccion = false;
		msDosDedos_NuevaPartida = -1;
	}

	static public PantallaTactil getInstancia() {
		if (instancia == null)
			instancia = new PantallaTactil();
		return instancia;
	}


	public void setDimPantalla(int ancho_, int alto_){
		ancho = ancho_;
		alto = alto_;
	}
	
	
	
	
	
	
	
	
	
	//Retorna true si esta caminando.
	//Camina cuando tiene dos o mas dedos en la pantalla.
	public boolean getCamina(){
		return objetos.size() >= 2;
	}
	
	//Usa cuchillo o accion.
	//Hace click en pantalla y suelta enseguida.
	public boolean getCuchilloAccion(){
		return hayAccion;
	}
	
	//Inicializa accion.
	public void setCuchilloAccion(){
		hayAccion = false;
	}
	
	//En caso de no tener sensores utiliza esto.
	//Retorna 1 si rota a la izquierda, 2 a la derecha y 0 si no rota.
	//Considera 1 click en pantalla que no es accion.
	public int rota(){
		
		//Si hay un dedo mas tiempo que click.
		if(objetos.size() == 1 && (System.currentTimeMillis() - objetos.get(0).ms) > TIEMPO_CLICK){
			
			//A la izquierda.
			if(objetos.get(0).pos.x <= ancho/2) return 1;
			//A la derecha.
			else return 2;
		}
		
		//No hay movimiento.
		return 0;
	}
	
	public boolean getNuevaPartida(){
		
		//No presiono con dos dedos.
		if(msDosDedos_NuevaPartida <= 0) return false;
		
		return (System.currentTimeMillis() - msDosDedos_NuevaPartida) >= 2500;
	}
	
	//Retorna true cuando hay un dedo deslizandose.
	public boolean getDeslizoDedo(){
		
		//Hay un dedo.
		if(objetos.size() == 1){
			//La distancia recorrida es mayor a un cuarto de ancho.
			if(Vector2.distancia(objetos.get(0).posInicial, objetos.get(0).pos) > ancho/4){
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	public void apreta(int id, float x, float y) {

		datos.P();

		y = alto - y;

		ObjetoTactil o = getObjeto(id);

		if (o == null) {

			o = new ObjetoTactil();

			o.id = id;
			o.pos = new Vector2(x, y);
			o.posInicial = new Vector2(x, y);

			objetos.add(o);
		}

		//Tiene dos dedos.
		if(objetos.size() == 2) msDosDedos_NuevaPartida = System.currentTimeMillis();
		//No tiene dos dedos.
		else msDosDedos_NuevaPartida = -1;
		
		datos.V();
	}

	public void suelta(int id, float x, float y) {

		datos.P();

		y = alto - y;

		ObjetoTactil o = getObjeto(id);

		if (o == null) {

			datos.V();
			return;
		}

		// Quita el objeto.
		objetos.remove(o);

		//Fue click.
		if( (System.currentTimeMillis() - o.ms) < TIEMPO_CLICK && Vector2.distancia(o.posInicial, o.pos) < ancho/6 ) hayAccion = true;
		
		//Tiene dos dedos.
		if(objetos.size() == 2) msDosDedos_NuevaPartida = System.currentTimeMillis();
		//No tiene dos dedos.
		else msDosDedos_NuevaPartida = -1;
				
		datos.V();
	}

	public void mueve(int id, float x, float y) {

		datos.P();

		y = alto - y;

		ObjetoTactil o = getObjeto(id);

		if (o == null) {

			datos.V();
			return;
		}
		
		// Actualiza posicion.
		o.pos.x = x;
		o.pos.y = y;
				
		
		datos.V();
	}

	private ObjetoTactil getObjeto(int id) {

		Iterator<ObjetoTactil> it = objetos.iterator();
		while (it.hasNext()) {

			ObjetoTactil o = it.next();

			if (o.id == id)
				return o;
		}

		return null;
	}

}
