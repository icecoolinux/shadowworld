package com.icecool.worldinshadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.icecool.worldinshadow.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class ActividadOpenAL extends Activity {

	//Surface que dibuja opengl.
	private GLSurfaceView view;
	private MyRenderer renderer;
	
	private boolean salir;
	private boolean destruyoTodo;
	
	private int ancho;
	private int alto;
	
	private int idTexVortice;
	private float rotacionVortice;
	private long msCambioRotacionVortice;
	
	//Para usar sensores.
	private MiVista miVista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//esto quita el título de la activity en la parte superior
		requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		//y esto para pantalla completa (oculta incluso la barra de estado)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		setContentView(R.layout.main);

		
		salir = false;
		destruyoTodo = false;
		
	    RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout);

	    view = new GLSurfaceView(this);
	    view.setEGLConfigChooser(false);
	    renderer = new MyRenderer(this);
	    view.setRenderer(renderer);
	    view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);    

	    layout1.addView(view);
	    
	    
	    
	    
	    //Copio ficheros a la tarjeta.
	    if(!copiarAudioA_SD()){
	    	
	    	Toast.makeText(this, getString(R.string.errorficherosaudio), Toast.LENGTH_LONG).show();
	    	
	    	finish();
	    	
	    	return;
	    }

	    
	    
	    
	    //Cargo libreria openal.
		System.loadLibrary("openal");
		System.loadLibrary("openaltest");

		//Inicializo motor.
		MotorJuego.getInstancia(this).init();
		
		
		//Setea datos de sensores.
		// En esta clase recibir� los eventos y usar� el resultado para lo que quiera
		miVista = null;
		miVista = new MiVista(this);
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Datos.getInstancia().setTieneSensor(true);
		if(mSensorManager != null){

			// Cada sensor se registra por separado
			List<Sensor>  listSensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			if(listSensors != null && listSensors.size() > 0){
				Sensor acelerometerSensor = listSensors.get(0);
				mSensorManager.registerListener(miVista, acelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
			}
			else
				Datos.getInstancia().setTieneSensor(false);

			listSensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			if(listSensors != null && listSensors.size() > 0){
				Sensor magneticSensor = listSensors.get(0);
				mSensorManager.registerListener(miVista, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
			}
			else
				Datos.getInstancia().setTieneSensor(false);
		}
		else
			Datos.getInstancia().setTieneSensor(false);
	}
	
	
	
	
	
	public class MyRenderer implements Renderer{
		 
		private Datos datos;
		private ActividadOpenAL juego;
		
		private Vector3 vistaTemp = new Vector3();
		
		private boolean recargarTexturas;
		
		public MyRenderer(ActividadOpenAL juego_){
			super();
			
			datos = Datos.getInstancia();
			juego = juego_;
			recargarTexturas = true;
		}
		
		@Override
		public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig arg1) {

			if(salir) return;
			
			gl.glClearColor(0, 0, 0, 1);
			
			// Habilita el sombreado suave
			gl.glShadeModel(GL10.GL_SMOOTH);

			// Calculo de perspectivas
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
			
			//Transparencia.
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		}
		  
		 
		@Override
		public void onDrawFrame(GL10 gl) {

			//Sale del juego.
			if(salir){
				
				//No ha destruido todo.
				if(!destruyoTodo){
					
					destruir(gl);
					
					destruyoTodo = true;

			        finish();
				}

				return;
			}
			
			// Limpia la pantalla y el buffer de profundidad
			//gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			// Reemplaza la matriz actual con la matriz identidad
			gl.glLoadIdentity();
			
			//Necesita cargar texturas.
			if(recargarTexturas){

				recargarTexturas = false;

				//Cargo textura de logo.
				String nombreTexturas[] = new String[1];
				nombreTexturas[0] = new String("vortice");
				int[] idTexturas = Texturas.cargarTexturas(gl, juego, nombreTexturas);

				idTexVortice = idTexturas[0];
			}

			//Dibujo vortice.
			if( (System.currentTimeMillis() - msCambioRotacionVortice) > 150){
				rotacionVortice = (float) (Math.random()*360);
				msCambioRotacionVortice = System.currentTimeMillis();
			}
			gl.glColor4f(1, 1, 1, 1);
			Imagen.getInstancia().dibujarRectangulo(gl, (ancho/2) -128, (alto/2)-128, 256, 256, idTexVortice, 0, 0, 1, 1, rotacionVortice);
			

			datos.P();
			
			//Ajusta.
			MotorJuego.getInstancia(juego).ajustar();
			
			datos.V();
		}
		 
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {

			if(salir) return;

			ancho = width;
			alto = height;
			
			PantallaTactil.getInstancia().setDimPantalla(ancho, alto);
			
			// Establece el puerto de vista actual  al nuevo tama�o
			gl.glViewport(0, 0, width, height);
			 
			// Selecciona la matriz de proyeccion
			gl.glMatrixMode(GL10.GL_PROJECTION);
			 
			// Reinicia la matriz de proyeccion
			gl.glLoadIdentity();
			 
			// Calcula la proporcion del aspecto de la ventana
			gl.glOrthof(0, width, 0, height, -1000, 1000);
			 
			// Selecciona la matriz de la vista del modelo
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			 
			// Reinicia la matriz de la vista del modelo
			gl.glLoadIdentity();
		}
		
		public void setRecargarTexturas(){
			recargarTexturas = true;
		}
	}
	
	//Destruyo todo antes de salir.
	private void destruir(GL10 gl){
		
		MotorJuego.getInstancia(this).release();
		
		//Quito escucha de sensores.
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.unregisterListener(miVista);
		
		//Destruyo texturas.
		int idTex[] = new int[1];
		idTex[0] = idTexVortice;
		Texturas.liberar(gl, idTex);
	}

	@Override
	public void onBackPressed(){
		
		//Si me dice que salga, mando a salir.
		if(MotorJuego.getInstancia(this).botonAtras()) salir = true;
	}
	
	@Override
	public void onDestroy(){
	
		super.onDestroy();
	}
	
	@Override
	public void onPause(){
		
		//view.setVisibility(View.GONE);
		view.onPause();
	
		super.onPause();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		view.onResume();
		renderer.setRecargarTexturas();
	}
	
	
	//Calcula cuando toca la pantalla.
	@Override 
	public boolean onTouchEvent(MotionEvent e) {

		int action = e.getAction() & MotionEvent.ACTION_MASK;

		switch(action) {

		case MotionEvent.ACTION_DOWN : {

			int id = e.getPointerId(getIndex(e));
			int x = (int) e.getX(getIndex(e));
			int y = (int) e.getY(getIndex(e));

			PantallaTactil.getInstancia().apreta(id, x, y);

			break;
		}

		case MotionEvent.ACTION_MOVE : {

			int touchCounter = e.getPointerCount();
			for (int t = 0; t < touchCounter; t++) {
				int id = e.getPointerId(t);
				int x = (int) e.getX(t);
				int y = (int) e.getY(t);
				PantallaTactil.getInstancia().mueve(id, x, y);
			}
			break;
		}

		case MotionEvent.ACTION_POINTER_DOWN : {

			int id = e.getPointerId(getIndex(e));
			int x = (int) e.getX(getIndex(e));
			int y = (int) e.getY(getIndex(e));
			PantallaTactil.getInstancia().apreta(id, x, y);

			break;
		}

		case MotionEvent.ACTION_POINTER_UP : {

			int id = e.getPointerId(getIndex(e));
			int x = (int) e.getX(getIndex(e));
			int y = (int) e.getY(getIndex(e));
			PantallaTactil.getInstancia().suelta(id, x, y);

			break;
		}

		case MotionEvent.ACTION_UP : {

			int id = e.getPointerId(getIndex(e));			    
			int x = (int) e.getX(getIndex(e));
			int y = (int) e.getY(getIndex(e));
			PantallaTactil.getInstancia().suelta(id, x, y);

			break;
		}   
		}

		return true;
	}
		
	private int getIndex(MotionEvent e){
		return (e.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	}
	
	
	
	//Clase que calcula direccion del celular.
	class MiVista extends View implements SensorEventListener {

		//Valores leidos.
		private float[] campoMagnetico;
		private float[] acelerometro;

		//Variable temporal.
		private Vector3 init;
		private float[] orientacion;
		private float[] R;
		private Vector3 vista;
		private Mat m;

		public MiVista(Context context) {
			super(context);

			campoMagnetico = null;
			acelerometro = null;
			
			init = new Vector3(0, 1, 0);
			orientacion = new float[3];
			R = new float[9];
			vista = new Vector3();
			m = new Mat();
		}

		@Override
		public void onAccuracyChanged(Sensor s, int arg1) {

		}


		@Override
		public void onSensorChanged(SensorEvent event) {

			//No quiere usar sensores, retorno.
			if(!Datos.getInstancia().getUsarSensor()) return;

			Datos.getInstancia().P();
			
			switch(event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:

				acelerometro = event.values.clone();
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:

				campoMagnetico = event.values.clone();
				break;
			}


			if (acelerometro != null && campoMagnetico != null) {

				if (SensorManager.getRotationMatrix(R, null, acelerometro, campoMagnetico)) {


					/*
						//Calcula la vista.
						//Eje X tangente al suelo y apunta hacia el oeste.
						//Eje Y tangente al suelo y apunta al norte.
						//Eje Z es perpendicular al cielo y apunta hacia el centro de la tierra.
						// orientacion[0]: angulo de rotacion alrededor del eje Z.
						// orientacion[1]: angulo de rotacion alrededor del eje X.
						// orientacion[2]: angulo de rotacion alrededor del eje Y.
						SensorManager.getOrientation(R, orientacion);
						float anguloVista = (float) (Math.PI + orientacion[0]);
						vista.x = (float) -Math.cos(anguloVista);
						vista.y = (float) Math.sin(anguloVista);
						vista.z = (float) Math.sin(-orientacion[2] - Math.PI/2.0f);

						//Arreglo la vista en caso que gire la pantalla.
						if(campoMagnetico[0] < 0){
							vista.x *= -1.0f;
							vista.y *= -1.0f;
						}

						vista.normalizar();

					 */


					m.set9(R);
					m.rotar(true, false, false, (float) (-Math.PI/2.0));
					m.multiplicarVector(init, vista);

					Datos.getInstancia().setVista(vista);
				}
			}


			Datos.getInstancia().V();
			
		}

	}
		
	
	
	
	
	
	public String getPath(){
		return getFilesDir().getAbsolutePath() + "/";
		/*
		//Tiene tarjeta SD.
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/worldinshadow/";

		//No tiene tarjeta SD.
		else
			return getFilesDir().getAbsolutePath() + "/";
		*/
	}
	
	public String getFileName(String file){
		return file + ".wav";
	}
	
	//Retorna el path del fichero.
	public String getPathFile(String file){
		
		return getPath() + getFileName(file);
		//return new String("/sdcard/wav/"+file);
	}
	
	
	//Retorna false si hay error.
	public boolean copiarAudioA_SD_AUX(int recurso, String dir, String nombre){
		
		try {

			InputStream in = getResources().openRawResource(recurso); 
			File outDir = new File(dir);
Log.e("K", dir);
			outDir.mkdirs();
			File outFile = new File(outDir, nombre);
			OutputStream out = new FileOutputStream(outFile);
			
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != -1){
				out.write(buffer, 0, read);
			}

			in.close();

			out.flush();
			out.close();
		} catch (IOException e) {
			Log.e("JJ", e.getMessage());
			return false;
		}
		
		return true;
	}
		
	//Retorna false si hay error.
	public boolean copiarAudioA_SD(){

		boolean yaEstaCopiado = true;
		
		InputStream in = null;
		
		//Abre el primero a ver si existe.
		File outFile = new File(getPathFile("alcance_objetivo"));
		try {
			
			in = new FileInputStream(outFile);
			
		//No esta copiado.
		} catch (FileNotFoundException e) {
			yaEstaCopiado = false;
		}
		
		//Ya estan.
		if(yaEstaCopiado){

			try {
				if(in != null) in.close();
			} catch (IOException e) {}
			
			return true;
		}
		
		
		//Espanol.
		if(getString(R.string.lenguaje).equals("es")){
			
			if(!copiarAudioA_SD_AUX(R.raw.conectar_auriculares, getPath(), getFileName("conectar_auriculares"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo1, getPath(), getFileName("dialogo1"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo2, getPath(), getFileName("dialogo2"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo3, getPath(), getFileName("dialogo3"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo4, getPath(), getFileName("dialogo4"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo5, getPath(), getFileName("dialogo5"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo6, getPath(), getFileName("dialogo6"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo7, getPath(), getFileName("dialogo7"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo8, getPath(), getFileName("dialogo8"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo9, getPath(), getFileName("dialogo9"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo10, getPath(), getFileName("dialogo10"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo11, getPath(), getFileName("dialogo11"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo12, getPath(), getFileName("dialogo12"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo13, getPath(), getFileName("dialogo13"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo14, getPath(), getFileName("dialogo14"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo99, getPath(), getFileName("dialogo99"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.mensaje_partida_guardada, getPath(), getFileName("mensaje_partida_guardada"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_jugar_nueva_partida, getPath(), getFileName("menu_jugar_nueva_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_jugar_ultima_partida, getPath(), getFileName("menu_jugar_ultima_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_mensaje_nueva_partida, getPath(), getFileName("menu_mensaje_nueva_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_mensaje_partida_cargada, getPath(), getFileName("menu_mensaje_partida_cargada"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_movimiento, getPath(), getFileName("menu_modo_movimiento"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_pantalla, getPath(), getFileName("menu_modo_pantalla"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_sensor, getPath(), getFileName("menu_modo_sensor"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_salir, getPath(), getFileName("menu_salir"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.volver_menu, getPath(), getFileName("volver_menu"))) return false;
		}
		//Ingles.
		else{
			
			if(!copiarAudioA_SD_AUX(R.raw.conectar_auriculares_en, getPath(), getFileName("conectar_auriculares"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo1_en, getPath(), getFileName("dialogo1"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo2_en, getPath(), getFileName("dialogo2"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo3_en, getPath(), getFileName("dialogo3"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo4_en, getPath(), getFileName("dialogo4"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo5_en, getPath(), getFileName("dialogo5"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo6_en, getPath(), getFileName("dialogo6"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo7_en, getPath(), getFileName("dialogo7"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo8_en, getPath(), getFileName("dialogo8"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo9_en, getPath(), getFileName("dialogo9"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo10_en, getPath(), getFileName("dialogo10"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo11_en, getPath(), getFileName("dialogo11"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo12_en, getPath(), getFileName("dialogo12"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo13_en, getPath(), getFileName("dialogo13"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo14_en, getPath(), getFileName("dialogo14"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.dialogo99_en, getPath(), getFileName("dialogo99"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.mensaje_partida_guardada_en, getPath(), getFileName("mensaje_partida_guardada"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_jugar_nueva_partida_en, getPath(), getFileName("menu_jugar_nueva_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_jugar_ultima_partida_en, getPath(), getFileName("menu_jugar_ultima_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_mensaje_nueva_partida_en, getPath(), getFileName("menu_mensaje_nueva_partida"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_mensaje_partida_cargada_en, getPath(), getFileName("menu_mensaje_partida_cargada"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_movimiento_en, getPath(), getFileName("menu_modo_movimiento"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_pantalla_en, getPath(), getFileName("menu_modo_pantalla"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_modo_sensor_en, getPath(), getFileName("menu_modo_sensor"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.menu_salir_en, getPath(), getFileName("menu_salir"))) return false;
			if(!copiarAudioA_SD_AUX(R.raw.volver_menu_en, getPath(), getFileName("volver_menu"))) return false;
		}
		
		if(!copiarAudioA_SD_AUX(R.raw.alcance_objetivo, getPath(), getFileName("alcance_objetivo"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.ambiente_bosque, getPath(), getFileName("ambiente_bosque"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.ambiente_bosque2, getPath(), getFileName("ambiente_bosque2"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.ambiente_espacio, getPath(), getFileName("ambiente_espacio"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.ambiente_horror, getPath(), getFileName("ambiente_horror"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.caminando, getPath(), getFileName("caminando"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.cuchillada_enemigo, getPath(), getFileName("cuchillada_enemigo"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.cuchillo, getPath(), getFileName("cuchillo"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.enemigo, getPath(), getFileName("enemigo.wav"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.enemigo_ataca, getPath(), getFileName("enemigo_ataca"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.enemigo_muere, getPath(), getFileName("enemigo_muere"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.me_atraparon, getPath(), getFileName("me_atraparon.wav"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.objetivo, getPath(), getFileName("objetivo"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.corazon, getPath(), getFileName("corazon"))) return false;
		if(!copiarAudioA_SD_AUX(R.raw.muero, getPath(), getFileName("muero"))) return false;
		
		
		return true;
	}

	
	
	
	public native int initOpenAL();
	public native int releaseOpenAL();
		 
	public native int cargarBuffer(int idBuffer, String filename);
	public native int liberarBuffer(int idBuffer);
		 
	public native int crearFuente(int idFuente, int idBuffer, int loop);
	public native int playFuente(int idFuente);
	public native int pauseFuente(int idFuente);
	public native int stopFuente(int idFuente);
	public native int liberarFuente(int idFuente);
	public native int estaReproduciendo(int idFuente);
	
	public native int setReceptor(float posx, float posy, float posz, float velx, float vely, float velz, float dirx, float diry, float dirz, float upx, float upy, float upz);
	public native int setEmisor(int idEmisor, float posx, float posy, float posz, float velx, float vely, float velz);
}
