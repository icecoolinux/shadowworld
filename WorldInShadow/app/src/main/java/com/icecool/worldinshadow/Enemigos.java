package com.icecool.worldinshadow;

import java.util.ArrayList;
import java.util.Iterator;

import com.icecool.worldinshadow.ApiOpenAL.Emisor;
import com.icecool.worldinshadow.BaseDato.StructEnemigo;

public class Enemigos {

	static private float DISTANCIA_PEGA_ENEMIGO = 3.0f;
	static private float DISTANCIA_CERCANA_ENEMIGO = 2.0f;
	static private float VELOCIDAD_ENEMIGO_SEGUNDOS = 1.5f;
	static private int MS_ENTRE_ATAQUES = 3000;
	
	class Enemigo{
		
		//Posicion.
		Vector3 pos;
		
		//Sonido.
		int idSonidoEnemigoCamina;
		
		//Vida.
		int vida;
		
		//Ultimo ataque.
		long msAtaque;
		
		public Enemigo(Vector3 pos_, int idSonidoEnemigoCamina_){
			pos = new Vector3();
			pos.set(pos_);
			idSonidoEnemigoCamina = idSonidoEnemigoCamina_;
			vida = 100;
			
			reproducirSonido(emisorEnemigoCamina);
		}
		
		public void setPos(Vector3 pos_, Emisor emisorEnemigoCamina[]){
			
			pos.set(pos_);
			
			if(idSonidoEnemigoCamina >= 0) emisorEnemigoCamina[idSonidoEnemigoCamina].setPos(pos);
		}
		
		public void detenerSonido(Emisor emisorEnemigoCamina[]){
			if(idSonidoEnemigoCamina >= 0) emisorEnemigoCamina[idSonidoEnemigoCamina].detener();
		}
		
		public void reproducirSonido(Emisor emisorEnemigoCamina[]){
			if(idSonidoEnemigoCamina >= 0) emisorEnemigoCamina[idSonidoEnemigoCamina].reproducir();
		}
	}
	
	private ArrayList<Enemigo> enemigos;
	
	//Sonidos.
	private Emisor emisorEnemigoMurio;
	private Emisor emisorCuchilladaEnemigo;
	private Emisor emisorEnemigoAtaca;
	private Emisor emisorEnemigoCamina[];
	
	//Para saber que caminata es de que enemigo.
	private boolean caminaOcupado[] = null;
	
	static private Enemigos instancia = null;
	
	private Enemigos(){
	
		enemigos = new ArrayList<Enemigo>();
	}
	
	static public Enemigos getInstancia(){
		if(instancia == null)
			instancia = new Enemigos();
		return instancia;
	}
	
	public void setSonidos(Emisor emisorEnemigoMurio_, Emisor emisorCuchilladaEnemigo_, Emisor emisorEnemigoAtaca_, Emisor emisorEnemigoCamina_[]){
		
		emisorEnemigoMurio = emisorEnemigoMurio_;
		emisorCuchilladaEnemigo = emisorCuchilladaEnemigo_;
		emisorEnemigoAtaca = emisorEnemigoAtaca_;
		emisorEnemigoCamina = emisorEnemigoCamina_;
		
		caminaOcupado = new boolean[emisorEnemigoCamina.length];
		for(int i=0; i<caminaOcupado.length; i++) caminaOcupado[i] = false;
	}
	
	public int getCantEnemigos(){
		return enemigos.size();
	}
	
	public void limpiar(){
		enemigos.clear();
	}
	
	//Setea enemigos de la base de dato.
	public void setEnemigos(StructEnemigo[] es){
		
		limpiar();
		
		if(es != null){
			
			for(int i=0; i<es.length; i++){
				
				Enemigo e = new Enemigo(es[i].pos, getEnemigoCaminaLibre());
				e.vida = es[i].vida;
				enemigos.add(e);
			}
		}
	}
	
	public StructEnemigo[] getEnemigosBD(BaseDato bd){

		if(enemigos.size() == 0) return null;
		
		else{
			
			StructEnemigo[] es = new StructEnemigo[enemigos.size()];
			Iterator<Enemigo> it = enemigos.iterator();
			int i=0;
			while(it.hasNext()){
				
				Enemigo e = it.next();
				
				es[i] = new StructEnemigo();
				
				es[i].pos = new Vector3();
				es[i].pos.set(e.pos);
				es[i].vida = e.vida;
				
				i++;
			}
			
			return es;
		}
	}
	
	public void crearEnemigo(Vector3 pos){
		
		Enemigo e = new Enemigo(pos, getEnemigoCaminaLibre());
		enemigos.add(e);
	}
	
	//Ataca a enemigos.
	static private Vector3 vTemp = new Vector3();
	public void atacar(Vector3 pos, Vector3 dir){
		
		//Para cada enemigo.
		Iterator<Enemigo> it = enemigos.iterator();
		while(it.hasNext()){
			
			Enemigo e = it.next();
			
			//Le pego, el producto escalar es el coseno del angulo entre las dos direcciones, si es mayor a 0.4 es un angulo cerrado.
			vTemp.resta(e.pos, pos);
			vTemp.normalizar();
			if( Vector3.distancia(e.pos, pos) < DISTANCIA_PEGA_ENEMIGO && Vector3.prodEscalar(dir, vTemp) > 0.4f){
				
				e.vida -= 40;
				
				//Murio enemigo.
				if(e.vida <= 0){
					
					e.detenerSonido(emisorEnemigoCamina);
					
					//Quito enemigo.
					it.remove();
					
					//Sonido de que murio.
					emisorEnemigoMurio.setPos(e.pos);
					emisorEnemigoMurio.reproducir();
				}
				
				//No ha muerto.
				else{
					
					//Sonido de que pega.
					emisorCuchilladaEnemigo.setPos(e.pos);
					emisorCuchilladaEnemigo.reproducir();
				}
			}
		}
	}
	
	//Ajusta posicion.
	static private Vector3 miPos = new Vector3();
	static private Vector3 posEnemigo = new Vector3();
	public void ajustar(int ms){
		
		Datos.getInstancia().getPosicion(miPos);
		
		//Para cada enemigo.
		Iterator<Enemigo> it = enemigos.iterator();
		while(it.hasNext()){
			
			Enemigo e = it.next();
			
			//Ajusto posicion.
			vTemp.resta(miPos, e.pos);
			float distanciaEnemigo = vTemp.norma();
			
			//Estoy lejos.
			if(distanciaEnemigo > DISTANCIA_CERCANA_ENEMIGO){
				
				//Distancia a acercarme.
				float distAcercarme = VELOCIDAD_ENEMIGO_SEGUNDOS * ((float)ms) / 1000.0f; 
					
				//Me acerco.
				vTemp.normalizar();
				vTemp.multiplicar(distAcercarme);
				posEnemigo.set(e.pos);
				posEnemigo.sumar(vTemp);
				e.setPos(posEnemigo, emisorEnemigoCamina);
			}
			
			//Esta cerca.
			else{
				
				//Es hora de atacar.
				if( (System.currentTimeMillis() - e.msAtaque) > MS_ENTRE_ATAQUES){
					
					//Ataco enemigo, le quita vida al usuario.
					Datos.getInstancia().setVida(Datos.getInstancia().getVida() - Datos.VIDA_QUITADA_ENEMIGO);
					
					//Seteo tiempo que ataco, esto es para recuperar vida luego de tanto tiempo.
					Datos.getInstancia().setUltimaVezAtacaron(System.currentTimeMillis());
					
					//Sonido.
					emisorEnemigoAtaca.setPos(e.pos);
					emisorEnemigoAtaca.reproducir();
					
					//Actualizo.
					e.msAtaque = System.currentTimeMillis();
				}
			}
		}
	}
	
	
	//Retorna un sonido de enemigo camina libre.
	private int getEnemigoCaminaLibre(){
		
		for(int i=0; i<emisorEnemigoCamina.length; i++){
			if(!caminaOcupado[i]){
				caminaOcupado[i] = true;
				return i;
			}
		}
		
		return -1;
	}
	
	//Libera enemigo camina libre.
	private void liberarEnemigoCaminaLibre(int id){
		
		if(id == -1) return;
		
		caminaOcupado[id] = false;
	}
}
