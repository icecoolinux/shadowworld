package com.icecool.worldinshadow;

import android.util.Log;

public class Vector3{
	float x, y, z;
	
	public Vector3(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 pos){
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
	}
	
	public void set(float x_, float y_, float z_){
		x = x_;
		y = y_;
		z = z_;
	}
	
	static public float prodEscalar(Vector3 p1, Vector3 p2){
		return p1.x*p2.x + p1.y*p2.y + p1.z*p2.z;
	}
	
	public float norma(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public void productoVectorial(Vector3 u, Vector3 v){
		
		x = u.y * v.z - u.z * v.y;
		y = - (u.x * v.z - v.x * u.z);
		z = u.x * v.y - u.y * v.x;
	}
	
	//Setea al objeto con el valor a - b
	public void resta(Vector3 a, Vector3 b){
		x = a.x - b.x;
		y = a.y - b.y;
		z = a.z - b.z;
	}
	
	public void restar(Vector3 a){
		x -= a.x;
		y -= a.y;
		z -= a.z;
	}
	
	public void normalizar(){
		float n = norma();
		x /= n;
		y /= n;
		z /= n;
	}
	
	public void multiplicar(float a){
		x *= a;
		y *= a;
		z *= a;
	}
	
	public void dividir(float a){
		x /= a;
		y /= a;
		z /= a;
	}
	
	public void sumar(Vector3 a){
		x += a.x;
		y += a.y;
		z += a.z;
	}
	
	public void set(Vector3 p){
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	static public float distancia(Vector3 a, Vector3 b){
		return (float) Math.sqrt( (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y) + (b.z - a.z)*(b.z - a.z) );
	}
	
	//Proyecta el punto a en el vector b y almacena el punto proyectado en el objeto.
	//Retorna un real L tal que: p = b * L siendo p el punto proyectado.
	public float proyectar(Vector3 a, Vector3 b){
		
		float lambda = ( a.x*b.x + a.y*b.y + a.z*b.z ) / ( b.x*b.x + b.y*b.y + b.z*b.z);
		
		x = b.x * lambda;
		y = b.y * lambda;
		z = b.z * lambda;
		
		return lambda;
	}
	
	public void print(){
		Log.e("mitag", x+" "+y+" "+z);
	}
}
