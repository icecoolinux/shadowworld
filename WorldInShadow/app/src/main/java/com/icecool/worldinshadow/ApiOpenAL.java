package com.icecool.worldinshadow;

public class ApiOpenAL {

	class Receptor{
	
		private ActividadOpenAL juego;
		private Vector3 pos;
		private Vector3 dir, up;
		private Vector3 vel;
		
		private Receptor(){
			
		}
		
		public Receptor(ActividadOpenAL juego_){
			juego = juego_;
			pos = new Vector3();
			dir = new Vector3();
			up = new Vector3();
			vel = new Vector3();
		}
		
		public void setPos(Vector3 pos_){
			pos.set(pos_);
			juego.setReceptor(pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, dir.x, dir.y, dir.z, up.x, up.y, up.z);
		}
		
		public void setDir(Vector3 dir_, Vector3 up_){
			dir.set(dir_);
			up.set(up_);
			juego.setReceptor(pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, dir.x, dir.y, dir.z, up.x, up.y, up.z);
		}
		
		public void setVel(Vector3 vel_){
			vel.set(vel_);
			juego.setReceptor(pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, dir.x, dir.y, dir.z, up.x, up.y, up.z);
		}
	}
	
	class Emisor{
		
		private ActividadOpenAL juego;
		private int id;
		private int idBuffer;
		private boolean loop;
		private Vector3 pos;
		private Vector3 vel;
		
		private Emisor(){
			
		}
		
		public Emisor(ActividadOpenAL juego_, int id_, int idBuffer_, boolean loop_){
			juego = juego_;
			id = id_;
			idBuffer = idBuffer_;
			loop = loop_;
			pos = new Vector3();
			vel = new Vector3();
			
			if(loop) juego.crearFuente(id, idBuffer, 1);
			else juego.crearFuente(id, idBuffer, 0);
		}
		
		public int getId(){
			return id;
		}
		
		public void setPos(Vector3 pos_){
			pos.set(pos_);
			juego.setEmisor(id, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
		}

		public void setVel(Vector3 vel_){
			vel.set(vel_);
			juego.setEmisor(id, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
		}
		
		public boolean estaReproduciendo(){
			return juego.estaReproduciendo(id) == 1;
		}
		
		public void reproducir(){
			juego.playFuente(id);
		}
		
		public void pausa(){
			juego.pauseFuente(id);
		}
		
		public void detener(){
			juego.stopFuente(id);
		}
		
		public void liberar(){
			juego.liberarFuente(id);
		}
	}
	
	class Buffer{
		
		private int id;
		private ActividadOpenAL juego;
		
		private Buffer(){
			
		}
		
		public Buffer(ActividadOpenAL juego_, int id_, String ruta_){
			id = id_;
			juego = juego_;
			juego.cargarBuffer(id, ruta_);
		}
		
		public int getId(){
			return id;
		}
		
		public void liberar(){
			juego.liberarBuffer(id);
		}
	}
	
	
	private ActividadOpenAL juego;
	
	static private int MAX_BUFFERS = 40;
	static private int MAX_FUENTES = 40;
	
	private Buffer[] buffers;
	private Emisor[] emisores;
	private Receptor receptor;
	
	static private ApiOpenAL instancia = null;
	
	private ApiOpenAL(ActividadOpenAL juego_){
		buffers = new Buffer[MAX_BUFFERS];
		for(int i=0; i<MAX_BUFFERS; i++) buffers[i] = null;
		emisores = new Emisor[MAX_FUENTES];
		for(int i=0; i<MAX_FUENTES; i++) emisores[i] = null;
		receptor = new Receptor(juego_);
	}
	
	static public ApiOpenAL getInstancia(ActividadOpenAL juego_){
		if(instancia == null)
			instancia = new ApiOpenAL(juego_);
		instancia.juego = juego_;
		return instancia;
	}
	
	
	
	public void init(){
		juego.initOpenAL();
	}
	
	public void liberar(){
		juego.releaseOpenAL();	
	}
	
	public Receptor getReceptor(){
		return receptor;
	}
	
	public Emisor crearEmisor(Buffer b, boolean loop){
		
		//Busco lugar.
		int i=0;
		while(emisores[i] != null) i++;
		
		emisores[i] = new Emisor(juego, i, b.getId(), loop);
		
		return emisores[i];
	}
	
	public void destruirEmisor(Emisor e){
		
		e.liberar();
		emisores[e.getId()] = null;
	}
	
	public Buffer crearBuffer(String ruta){
		
		//Busco lugar.
		int i=0;
		while(buffers[i] != null) i++;
		
		buffers[i] = new Buffer(juego, i, ruta);
		
		return buffers[i];
	}
	
	public void destruirBuffer(Buffer b){
		
		b.liberar();
		buffers[b.getId()] = null;
	}
}
