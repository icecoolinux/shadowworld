package com.icecool.worldinshadow;

import android.util.Log;

public class Vector2 {

	public float x, y;
	
	public Vector2(){
		x = 0;
		y = 0;
	}
	
	public Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 p){
		this.x = p.x;
		this.y = p.y;
	}
	
	public void set(Vector2 v){
		x = v.x;
		y = v.y;
	}
	
	public void set(float x_, float y_){
		x = x_;
		y = y_;
	}
	
	public void multiplicar(float f){
		x *= f;
		y *= f;
	}
	
	public void sumar(Vector2 v){
		x += v.x;
		y += v.y;
	}
	
	public void restar(Vector2 v){
		x -= v.x;
		y -= v.y;
	}
	
	public void resta(Vector2 p1, Vector2 p2){
		
		x = p1.x - p2.x;
		y = p1.y - p2.y;
	}
	
	public void normalizar(){
		float n = norma();
		x /= n;
		y /= n;
	}
	
	public float norma(){
		
		return (float) Math.sqrt(x*x + y*y);
	}

	//Proyecta el punto a en el vector b y almacena el punto proyectado en el objeto.
	//Retorna un real L tal que: p = b * L siendo p el punto proyectado.
	public float proyectar(Vector2 a, Vector2 b){
			
		float lambda = ( a.x*b.x + a.y*b.y ) / ( b.x*b.x + b.y*b.y);
			
		x = b.x * lambda;
		y = b.y * lambda;
			
		return lambda;
	}
		
	//Proyecta el punto a sobre la recta definida por p1 y p2.
	//Retorna un real L tal que: p = p1 + (p2-p1) * L siendo p el punto proyectado.
	static private Vector2 vectorP1P2 = new Vector2();
	static private Vector2 aPrima = new Vector2();
	static private Vector2 proyaPrima = new Vector2();
	public float proyectar(Vector2 a, Vector2 p1, Vector2 p2){
			
		//Vector de p1 y p2.
		vectorP1P2.resta(p2, p1);
			
		//Proyecto aPrima en el vector P1P2.
		aPrima.resta(a, p1);
		float lambda = proyaPrima.proyectar(aPrima, vectorP1P2);
			
		proyaPrima.sumar(p1);

		set(proyaPrima);
			
		return lambda;
	}
		
	static public float distancia(Vector2 a, Vector2 b){
		return (float) Math.sqrt( (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y));
	}
	
	//Calcula interseccion de una recta y un circulo.
	static private Vector2 proy = new Vector2();
	static boolean calcularInterseccion(Vector2 p, Vector2 q, Vector2 centro, float radio, Vector2 inter){
		
		//Si P esta en el circulo.
		if( Vector2.distancia(p, centro) <= radio){
			
			if(inter != null)
				inter.set(p);
			
			return true;
		}
		
		//Si Q esta en el circulo.
		if( Vector2.distancia(q, centro) <= radio){
			
			if(inter != null)
				inter.set(q);
			
			return true;
		}
		
		//Si p y q son iguales retorno false.
		if(Vector2.distancia(p, q) < 0.001f)
			return false;
		
		//Proyecto punto sobre recta.
		float lambda = proy.proyectar(centro, p, q);
		
		//Si distancia entre centro y punto proyectado es mayor a radio no hay interseccion.
		if( Vector2.distancia(proy, centro) > radio)
			return false;
		
		//Si lambda es menor a 0 o mayor a 1 no hay interseccion ya que ni P ni Q estan dentro del circulo.
		if(lambda < 0 || lambda > 1){
			return false;
		}
		
		if(inter != null)
			inter.set(proy);
		
		return true;
	}
	
	// v1.x tiene que ser diferente a 0.
	// Si dentro es true y la interseccion cae afuera de las rectas entonces no hay interseccion.
	static public boolean calcularInterseccionRecta(Vector2 p1, Vector2 v1, Vector2 p2, Vector2 v2, Vector2 inter, boolean dentro){
		
		float a = p2.y - p1.y;
		float b = v1.y / v1.x;
		
		if( (b * v2.x  - v2.y) > -0.001f && (b * v2.x  - v2.y) < 0.001f) return false;
		
		float l2 =  (a - b * p2.x + b * p1.x) / (b * v2.x  - v2.y);

		if( dentro && (l2 < 0 || l2 > 1) ) return false;
		
		if(dentro){
			float l1 = (p2.x + v2.x * l2 - p1.x) / v1.x;
			
			if(l1 < 0 || l1 > 1) return false;
		}
		
		inter.set(p2);
		inter.x += l2 * v2.x;
		inter.y += l2 * v2.y;
		
		return true;
		
		/*
		pos.x + dirRecta.x * l1 = ptoLado.x + vectorLado.x * l2;
		l1 = (ptoLado.x + vectorLado.x * l2 - pos.x) / dirRecta.x;
		
		pos.y + dirRecta.y * l1 = ptoLado.y + vectorLado.y * l2;
		pos.y + dirRecta.y * ((ptoLado.x + vectorLado.x * l2 - pos.x) / dirRecta.x) = ptoLado.y + vectorLado.y * l2;
		b * (ptoLado.x + vectorLado.x * l2 - pos.x) = vectorLado.y * l2 + a;
		b * ptoLado.x + b * vectorLado.x * l2 -  b * pos.x = vectorLado.y * l2 + a;
		b * vectorLado.x * l2 - vectorLado.y * l2 =  + a - b * ptoLado.x + b * pos.x;
		(b * vectorLado.x  - vectorLado.y) * l2 =  + a - b * ptoLado.x + b * pos.x;
		l2 =  (a - b * ptoLado.x + b * pos.x) / (b * vectorLado.x  - vectorLado.y);
		
		
		
		
		
		a = ptoLado.y - pos.y;
		b = dirRecta.y / dirRecta.x;*/
	}
}
