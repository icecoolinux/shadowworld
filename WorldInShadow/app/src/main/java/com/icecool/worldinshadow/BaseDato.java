package com.icecool.worldinshadow;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDato extends SQLiteOpenHelper{

	private String sqlCreatePartida = "CREATE TABLE Partida (posx REAL, posy REAL, posz REAL, nivel INTEGER, parte INTEGER,	puedeCaminar INTEGER, tieneCuchillo INTEGER,  camino INTEGER, usoCuchillo INTEGER, vida INTEGER, posxDialogo REAL, posyDialogo REAL, poszDialogo REAL)";
	private String sqlCreateEnemigos = "CREATE TABLE Enemigo (idenemigo INTEGER, posx REAL, posy REAL, posz REAL, vida INTEGER)";
	private String sqlCreateObjetivos = "CREATE TABLE Objetivos (idobjetivo INTEGER, posx REAL, posy REAL, posz REAL)";
	
	
	static private String nombreBaseDato = "AudioJuego_DB";
	
	private SQLiteDatabase db;
	
	static private BaseDato instancia = null;
	
	static public BaseDato getBaseDato(Context context){
		
		if(instancia == null)
			instancia = new BaseDato(context, nombreBaseDato, null, 1);
		return instancia;
	}
	
	private BaseDato(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		
		db = null;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		crearTablas(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// En la practica deberemos migrar datos de la tabla antigua
		// a la nueva, por lo que este método debería ser más elaborado.
		//Se elimina la versión anterior de la tabla
		borrarTablas(db);
		
		//Se crea la nueva versi�n de la tabla
		crearTablas(db);
	}

	
	
	
	
	static public class StructEnemigo{
		Vector3 pos;
		int vida;
	}
	
	static public class StructPartida{
		Vector3 miPos; 
		int nivel;
		int parte; 
		boolean puedeCaminar; 
		boolean tieneCuchillo;
		boolean camino;
		boolean usoCuchillo;
		Vector3[] objetivos;
		int vida;
		Vector3 posDialogo;
		StructEnemigo[] enemigos;

		public String getString(){
			String res = "miPos: "+miPos.x+", "+miPos.y+", "+miPos.z+"\n";
			res += "nivel: "+nivel+"\n";
			res += "parte: "+parte+"\n";
			res += "nivel: "+nivel+"\n";
			res += "puedeCaminar: "+puedeCaminar+"\n";
			res += "tieneCuchillo: "+tieneCuchillo+"\n";
			res += "camino: "+camino+"\n";
			res += "usoCuchillo: "+usoCuchillo+"\n";
			res += "vida: "+vida+"\n";
			res += "posDialogo: "+posDialogo.x+", "+posDialogo.y+", "+posDialogo.z+"\n";
			if(objetivos != null)
				for(int i=0; i<objetivos.length; i++)
					res += "objetivo "+i+": "+objetivos[i].x+", "+objetivos[i].y+", "+objetivos[i].z+"\n";
			if(enemigos != null)
				for(int i=0; i<enemigos.length; i++) {
					res += "enemigos pos" + i + ": " + enemigos[i].pos.x + ", " + enemigos[i].pos.y + ", " + enemigos[i].pos.z + "\n";
					res += "enemigos vida" + i + ": " + enemigos[i].vida + "\n";
				}
			return res;
		}
	}

	public void guardarPartida(StructPartida p){

		if(db == null) db = getWritableDatabase();
		
		//Borro.
		if(db != null) {
			
			db.execSQL("DELETE FROM Partida");
			db.execSQL("DELETE FROM Enemigo");
			db.execSQL("DELETE FROM Objetivos");
			
			int puedeCaminar_ = 0;
			int tieneCuchillo_ = 0;
			int camino_ = 0;
			int usoCuchillo_ = 0;
			if(p.puedeCaminar) puedeCaminar_ = 1;
			if(p.tieneCuchillo) tieneCuchillo_ = 1;
			if(p.camino) camino_ = 1;
			if(p.usoCuchillo) usoCuchillo_ = 1;
			
			//Guardo partida.
			db.execSQL("INSERT INTO Partida (posx, posy, posz, nivel, parte, puedeCaminar, tieneCuchillo, camino, usoCuchillo, vida, posxDialogo, posyDialogo, poszDialogo) VALUES ("+ p.miPos.x +", "+ p.miPos.y +", "+ p.miPos.z +", "+ p.nivel +", "+ p.parte +", "+ puedeCaminar_ +", "+ tieneCuchillo_ +", "+ camino_ +", "+ usoCuchillo_ +", "+ p.vida +", "+ p.posDialogo.x +", "+ p.posDialogo.y +", "+ p.posDialogo.z +")");
			
			//Guardo enemigos.
			if(p.enemigos != null){
				for(int i=0; i<p.enemigos.length; i++)
					db.execSQL("INSERT INTO Enemigo (idenemigo, posx, posy, posz, vida) VALUES ("+ i +", "+ p.enemigos[i].pos.x +", "+ p.enemigos[i].pos.y +", "+ p.enemigos[i].pos.z +", "+ p.enemigos[i].vida +")");
			}
			
			//Guardo objetivos.
			if(p.objetivos != null){
				for(int i=0; i<p.objetivos.length; i++)
					db.execSQL("INSERT INTO Objetivos (idobjetivo, posx, posy, posz) VALUES ("+ i +", "+ p.objetivos[i].x +", "+ p.objetivos[i].y +", "+ p.objetivos[i].z +")");
			}
		}
	}
			
			
	public StructPartida cargarPartida(){

		if(db == null) db = getWritableDatabase();
		
		StructPartida p = null;
		
		//Si hemos abierto correctamente la base de datos
		if(db != null) {
			
			Cursor c = db.rawQuery("SELECT * FROM Partida", null);
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {

				p = new StructPartida();

				//Recorremos el cursor hasta que no haya m�s registros
				do {
					
					p.miPos = new Vector3();
					p.miPos.x = Float.parseFloat(c.getString(0));
					p.miPos.y = Float.parseFloat(c.getString(1));
					p.miPos.z = Float.parseFloat(c.getString(2));
					p.nivel = Integer.parseInt(c.getString(3));
					p.parte = Integer.parseInt(c.getString(4));
					p.puedeCaminar = Integer.parseInt(c.getString(5)) == 1;
					p.tieneCuchillo = Integer.parseInt(c.getString(6)) == 1;
					p.camino = Integer.parseInt(c.getString(7)) == 1;
					p.usoCuchillo = Integer.parseInt(c.getString(8)) == 1;
					p.vida = Integer.parseInt(c.getString(9));
					p.posDialogo = new Vector3();
					p.posDialogo.x = Float.parseFloat(c.getString(10));
					p.posDialogo.y = Float.parseFloat(c.getString(11));
					p.posDialogo.z = Float.parseFloat(c.getString(12));
					
				} while(c.moveToNext());
			}
			
			//Obtenemos objetivos.
			if(p != null){
				
				c = db.rawQuery("SELECT * FROM Objetivos ORDER BY idobjetivo ASC", null);
				
				if(c.getCount() == 0)
					p.objetivos = null;
				
				else{
				
					p.objetivos = new Vector3[c.getCount()];
				
					int pos = 0;
					do {
						p.objetivos[pos] = new Vector3();
						p.objetivos[pos].x = Float.parseFloat(c.getString(0));
						p.objetivos[pos].y = Float.parseFloat(c.getString(1));
						p.objetivos[pos].z = Float.parseFloat(c.getString(2));
						pos++;
					} while(c.moveToNext());
				}
			}
			
			//Obtenemos enemigos.
			if(p != null){
				
				c = db.rawQuery("SELECT * FROM Enemigo", null);
				
				if(c.getCount() == 0)
					p.enemigos = null;
				
				else{
				
					p.enemigos = new StructEnemigo[c.getCount()];
				
					int pos = 0;
					do {
						p.enemigos[pos] = new StructEnemigo();
						p.enemigos[pos].pos = new Vector3();
						p.enemigos[pos].pos.x = Float.parseFloat(c.getString(0));
						p.enemigos[pos].pos.y = Float.parseFloat(c.getString(1));
						p.enemigos[pos].pos.z = Float.parseFloat(c.getString(2));
						p.enemigos[pos].vida = Integer.parseInt(c.getString(3));
						pos++;
					} while(c.moveToNext());
				}
			}
		}
		
		return p;
	}
	

	private void borrarTablas(SQLiteDatabase db_){
		
		db_.execSQL("DROP TABLE IF EXISTS Partida");
		db_.execSQL("DROP TABLE IF EXISTS Enemigos");
		db_.execSQL("DROP TABLE IF EXISTS Objetivos");
	}
	
	private void crearTablas(SQLiteDatabase db_){
		
		db_.execSQL(sqlCreatePartida);
		db_.execSQL(sqlCreateEnemigos);
		db_.execSQL(sqlCreateObjetivos);
	}
}
