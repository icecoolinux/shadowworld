package com.icecool.worldinshadow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import com.icecool.worldinshadow.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLUtils;

public class Texturas {

	//TODO hacer un uso eficiente de las texturas, cargo una sola vez cada textura
	//y retorno el id cuando me pide cargar por segunda vez la misma textura
	//tener una variable de la cantidad de veces que se esta usando cada textura
	//y cuando se libera se libera realmente en la ultima invocacion.
	
	static public int[] cargarTexturas(GL10 gl, Context context, String[] nombreTexturas){

		int[] textures = new int[nombreTexturas.length];
		
		// generate one texture pointer
		gl.glGenTextures(nombreTexturas.length, textures, 0);
		
		//Para cada textura.
		for(int i=0; i<nombreTexturas.length; i++){

			if(nombreTexturas[i] == null){
				textures[i] = -1;
			}
			else{

				// Carga la imagen.
				Bitmap bitmap = getBitmap(nombreTexturas[i], context);
				
				if(bitmap != null){

					// and bind it to our array
					gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
	 
					// create nearest filtered texture
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
				    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	 
					ByteBuffer imageBuffer = ByteBuffer.allocateDirect(bitmap.getHeight() * bitmap.getWidth() * 4);
					imageBuffer.order(ByteOrder.nativeOrder());
					byte buffer[] = new byte[4];
					for(int j=0; j<bitmap.getHeight(); j++){
						for(int k=0; k<bitmap.getWidth(); k++){
							int color = bitmap.getPixel(k, j);
							buffer[0] = (byte) Color.red(color);
							buffer[1] = (byte) Color.green(color);
							buffer[2] = (byte) Color.blue(color);
							buffer[3] = (byte) Color.alpha(color);
							imageBuffer.put(buffer);
						}
					}
					imageBuffer.position(0);
					gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, imageBuffer);
					
					//Use Android GLUtils to specify a two-dimensional texture image from our bitmap
					//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	 
					// Clean up
					bitmap.recycle();
				}
				else{
					textures[i] = -1;
				}
			}
		}
		
		return textures;
	}
	
	//Retorna largo y alto.
	static public void getDimension(String nombreTextura, Context context, int[] dim){
		
		Bitmap bitmap = getBitmap(nombreTextura, context);
		
		if(bitmap != null){
			dim[0] = bitmap.getWidth();
			dim[1] = bitmap.getHeight();
			bitmap.recycle();
		}
		else{
			dim[0] = -1;
			dim[1] = -1;
		}
	}
	
	//Libera textura.
	static public void liberar(GL10 gl, int[] idTextura){
		
		if(idTextura == null)
			return;
		
		gl.glDeleteTextures(idTextura.length, idTextura, 0);
		
	}
	
	static private Bitmap getBitmap(String nombreTextura, Context context){
		
		Bitmap bitmap = null;
		
		//Opcion para no escalar la imagen.
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
				
		if(nombreTextura.equals("vortice")) bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.vortice, opts);
		
		
		return bitmap;
	}
}
