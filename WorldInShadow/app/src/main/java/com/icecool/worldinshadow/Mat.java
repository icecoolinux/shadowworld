package com.icecool.worldinshadow;

public class Mat{
	
	public float a0,  a1,  a2,  a3;
	public float a4,  a5,  a6,  a7;
	public float a8,  a9,  a10, a11;
	public float a12, a13, a14, a15;
	
	public Mat(Mat m){
		a0 = m.a0;
		a1 = m.a1;
		a2 = m.a2;
		a3 = m.a3;
		
		a4 = m.a4;
		a5 = m.a5;
		a6 = m.a6;
		a7 = m.a7;
		
		a8 = m.a8;
		a9 = m.a9;
		a10 = m.a10;
		a11 = m.a11;
		
		a12 = m.a12;
		a13 = m.a13;
		a14 = m.a14;
		a15 = m.a15;
	}
	
	public Mat(){
		a0 = 1;
		a1 = 0;
		a2 = 0;
		a3 = 0;
		
		a4 = 0;
		a5 = 1;
		a6 = 0;
		a7 = 0;
		
		a8 = 0;
		a9 = 0;
		a10 = 1;
		a11 = 0;
		
		a12 = 0;
		a13 = 0;
		a14 = 0;
		a15 = 1;
	}
	
	public Mat(float v[]){
		set(v);
	}
	
	public void set(float v[]){
		a0 = v[0];
		a1 = v[1];
		a2 = v[2];
		a3 = v[3];
		
		a4 = v[4];
		a5 = v[5];
		a6 = v[6];
		a7 = v[7];
		
		a8 = v[8];
		a9 = v[9];
		a10 = v[10];
		a11 = v[11];
		
		a12 = v[12];
		a13 = v[13];
		a14 = v[14];
		a15 = v[15];	
	}
	
	public void set9(float v[]){
		a0 = v[0];
		a1 = v[1];
		a2 = v[2];
		a3 = 0;
		
		a4 = v[3];
		a5 = v[4];
		a6 = v[5];
		a7 = 0;
		
		a8 = v[6];
		a9 = v[7];
		a10 = v[8];
		a11 = 0;
		
		a12 = 0;
		a13 = 0;
		a14 = 0;
		a15 = 1;	
	}
	
	public void setIdentity(){
		a0 = 1;
		a1 = 0;
		a2 = 0;
		a3 = 0;
		
		a4 = 0;
		a5 = 1;
		a6 = 0;
		a7 = 0;
		
		a8 = 0;
		a9 = 0;
		a10 = 1;
		a11 = 0;
		
		a12 = 0;
		a13 = 0;
		a14 = 0;
		a15 = 1;
	}
	
	public Mat transpuesta(){
		
		Mat t = new Mat();
		
		t.a0 = a0;
		t.a1 = a4;
		t.a2 = a8;
		t.a3 = a12;
		
		t.a4 = a1;
		t.a5 = a5;
		t.a6 = a9;
		t.a7 = a13;
		
		t.a8 = a2;
		t.a9 = a6;
		t.a10 = a10;
		t.a11 = a14;
		
		t.a12 = a3;
		t.a13 = a7;
		t.a14 = a11;
		t.a15 = a15;
		
		return t;
	}
	
	public void set(Mat m){
		a0 = m.a0;
		a1 = m.a1;
		a2 = m.a2;
		a3 = m.a3;
		a4 = m.a4;
		a5 = m.a5;
		a6 = m.a6;
		a7 = m.a7;
		a8 = m.a8;
		a9 = m.a9;
		a10 = m.a10;
		a11 = m.a11;
		a12 = m.a12;
		a13 = m.a13;
		a14 = m.a14;
		a15 = m.a15;
	}
	
	static private Mat mTempMult = new Mat();
	public void mult(Mat m){

		mTempMult.a0 = a0 * m.a0 + a1 * m.a4 + a2 * m.a8 + a3 * m.a12;
		mTempMult.a4 = a4 * m.a0 + a5 * m.a4 + a6 * m.a8 + a7 * m.a12;
		mTempMult.a8 = a8 * m.a0 + a9 * m.a4 + a10 * m.a8 + a11 * m.a12;
		mTempMult.a12 = a12 * m.a0 + a13 * m.a4 + a14 * m.a8 + a15 * m.a12;
		
		mTempMult.a1 = a0 * m.a1 + a1 * m.a5 + a2 * m.a9 + a3 * m.a13;
		mTempMult.a5 = a4 * m.a1 + a5 * m.a5 + a6 * m.a9 + a7 * m.a13;
		mTempMult.a9 = a8 * m.a1 + a9 * m.a5 + a10 * m.a9 + a11 * m.a13;
		mTempMult.a13 = a12 * m.a1 + a13 * m.a5 + a14 * m.a9 + a15 * m.a13;
		
		mTempMult.a2 = a0 * m.a2 + a1 * m.a6 + a2 * m.a10 + a3 * m.a14;
		mTempMult.a6 = a4 * m.a2 + a5 * m.a6 + a6 * m.a10 + a7 * m.a14;
		mTempMult.a10 = a8 * m.a2 + a9 * m.a6 + a10 * m.a10 + a11 * m.a14;
		mTempMult.a14 = a12 * m.a2 + a13 * m.a6 + a14 * m.a10 + a15 * m.a14;
		
		mTempMult.a3 = a0 * m.a3 + a1 * m.a7 + a2 * m.a11 + a3 * m.a15;
		mTempMult.a7 = a4 * m.a3 + a5 * m.a7 + a6 * m.a11 + a7 * m.a15;
		mTempMult.a11 = a8 * m.a3 + a9 * m.a7 + a10 * m.a11 + a11 * m.a15;
		mTempMult.a15 = a12 * m.a3 + a13 * m.a7 + a14 * m.a11 + a15 * m.a15;
		
		set(mTempMult);
	}
	
	static private Mat rotX = new Mat();
	static private Mat rotY = new Mat();
	static private Mat rotZ = new Mat();
	static private Mat mTemp = new Mat();
	public void rotar(boolean ejeX, boolean ejeY, boolean ejeZ, float grados){
		
		rotX.setIdentity();
		rotY.setIdentity();
		rotZ.setIdentity();
		
		if(ejeX){
			rotX.a0 = 1;
			rotX.a5 = (float) Math.cos(grados);
			rotX.a6 = (float) -Math.sin(grados);
			rotX.a9 = (float) Math.sin(grados);
			rotX.a10 = (float) Math.cos(grados);
		}
		
		if(ejeY){
			rotX.a0 = (float) Math.cos(grados);
			rotX.a2 = (float) Math.sin(grados);
			rotX.a5 = 1;
			rotX.a8 = (float) -Math.sin(grados);
			rotX.a10 = (float) Math.cos(grados);
		}
		
		if(ejeZ){
			rotX.a0 = (float) Math.cos(grados);
			rotX.a1 = (float) -Math.sin(grados);
			rotX.a4 = (float) Math.sin(grados);
			rotX.a5 = (float) Math.cos(grados);
			rotX.a10 = 1;
		}
		

		mTemp.set(this);
		mTemp.mult(rotX);
		mTemp.mult(rotY);
		mTemp.mult(rotZ);
		
		set(mTemp);
	}
    
	static private Mat mTempTras = new Mat();
	public void trasladar(float x, float y, float z){

		mTempTras.setIdentity();
		
		mTempTras.a3 = x;
		mTempTras.a7 = y;
		mTempTras.a11 = z;
		
		mult(mTempTras);
	}
	
	public void getArray(float[] array){
		
		array[0] = a0;
		array[1] = a1;
		array[2] = a2;
		array[3] = a3;
		
		array[4] = a4;
		array[5] = a5;
		array[6] = a6;
		array[7] = a7;
		
		array[8] = a8;
		array[9] = a9;
		array[10] = a10;
		array[11] = a11;
		
		array[12] = a12;
		array[13] = a13;
		array[14] = a14;
		array[15] = a15;
	}
	
	public void multiplicarVector(Vector3 v, Vector3 res){
		res.x = a0*v.x + a1*v.y + a2*v.z;
		res.y = a4*v.x + a5*v.y + a6*v.z;
		res.z = a8*v.x + a9*v.y + a10*v.z;
	}
}
