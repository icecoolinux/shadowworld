package com.icecool.worldinshadow;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.text.InputFilter;
import android.util.TypedValue;
import android.widget.EditText;

public class Imagen {

	//Variables comunes.
	private ShortBuffer indices;
	private FloatBuffer verticesCuadrado1x1;
	private FloatBuffer verticesCuadrado1x1_Rotacion;
	private FloatBuffer texCoord_3D;
	private FloatBuffer texCoord_2D;
	
	static private Imagen instancia = null;
	
	private Imagen(){
		
		indices = null;
				
				
		//Coordenadas de texturas.
		float coordTex_[] = new float[4*2];
		coordTex_[0] = 0;
		coordTex_[1] = 1;
		coordTex_[2] = 1;
		coordTex_[3] = 1;
		coordTex_[4] = 1;
		coordTex_[5] = 0;
		coordTex_[6] = 0;
		coordTex_[7] = 0;
		
		ByteBuffer tcb = ByteBuffer.allocateDirect(coordTex_.length * 4);
		tcb.order(ByteOrder.nativeOrder());
		texCoord_2D = tcb.asFloatBuffer();
		texCoord_2D.put(coordTex_);
		texCoord_2D.position(0);
		
		//Indices.
		short indicesTemp[] = new short[6];
		indicesTemp[0] = 0;
		indicesTemp[1] = 1;
		indicesTemp[2] = 3;
		indicesTemp[3] = 1;
		indicesTemp[4] = 2;
		indicesTemp[5] = 3;
				
		ByteBuffer ibb = ByteBuffer.allocateDirect(indicesTemp.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indices = ibb.asShortBuffer();
		indices.put(indicesTemp);
		indices.position(0);
				
		//Vertices de cuadrado de 1x1.
		float vertices[] = new float[4*3];
		vertices[0] = 0;
		vertices[1] = 0;
		vertices[2] = 0;
		vertices[3] = 1;
		vertices[4] = 0;
		vertices[5] = 0;
		vertices[6] = 1;
		vertices[7] = 1;
		vertices[8] = 0;
		vertices[9] = 0;
		vertices[10] = 1;
		vertices[11] = 0;
						
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesCuadrado1x1 = vbb.asFloatBuffer();
		verticesCuadrado1x1.put(vertices);
		verticesCuadrado1x1.position(0);
		
		//Vertices de cuadrado de 1x1.
		vertices = new float[4*3];
		vertices[0] = -0.5f;
		vertices[1] = -0.5f;
		vertices[2] = 0;
		vertices[3] = 0.5f;
		vertices[4] = -0.5f;
		vertices[5] = 0;
		vertices[6] = 0.5f;
		vertices[7] = 0.5f;
		vertices[8] = 0;
		vertices[9] = -0.5f;
		vertices[10] = 0.5f;
		vertices[11] = 0;

		vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesCuadrado1x1_Rotacion = vbb.asFloatBuffer();
		verticesCuadrado1x1_Rotacion.put(vertices);
		verticesCuadrado1x1_Rotacion.position(0);
	}
	
	static public Imagen getInstancia(){
		if(instancia == null)
			instancia = new Imagen();
		return instancia;
	}

	public void dibujarRectangulo(GL10 gl, float x, float y, float dimx, float dimy, int idTex, float xTex, float yTex, float anchoTex, float altoTex, float rotar){
		
		//La guardo en el stack.
		gl.glPushMatrix();
		
		// Reinicia la matriz de la vista del modelo
		gl.glLoadIdentity();
		
		
		// Habilitar el buffer de vertices para la escritura y cuales se usaran para el renderizado
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//Habilita buffer de coordenadas de texturas.
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
						
		// Desabilita el buffer para el color del grafico
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, idTex);
		
		//Traslado y escalo.
		if(rotar > -500){
			gl.glTranslatef(x+dimx/2, y+dimy/2, 0);
			gl.glScalef(dimx, dimy, 1);
			gl.glRotatef(rotar, 0, 0, 1);
		}
		else{
			gl.glTranslatef(x, y, 0);
			gl.glScalef(dimx, dimy, 1);
		}
		
		//Modifico indices de texturas.
		texCoord_2D.position(0);
		texCoord_2D.put(xTex);
		texCoord_2D.put(1.0f-yTex);
		texCoord_2D.put(xTex+anchoTex);
		texCoord_2D.put(1.0f-yTex);
		texCoord_2D.put(xTex+anchoTex);
		texCoord_2D.put(1.0f-yTex-altoTex);
		texCoord_2D.put(xTex);
		texCoord_2D.put(1.0f-yTex-altoTex);
		texCoord_2D.position(0);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoord_2D);
		
		//Dibujo.
		if(rotar > -500) gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesCuadrado1x1_Rotacion);
		else gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesCuadrado1x1);
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indices); 
		
		// Deshabilita el buffer de vertices para la escritura y cuales se usaran para el renderizado
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		// Deshabilita buffer de coordenadas de texturas.
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				
		//Restauro matriz de vista.
		gl.glPopMatrix();
		
	}

}
