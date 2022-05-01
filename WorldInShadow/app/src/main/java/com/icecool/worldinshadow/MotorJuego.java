package com.icecool.worldinshadow;

import java.util.ArrayList;

import com.icecool.worldinshadow.ApiOpenAL.Buffer;
import com.icecool.worldinshadow.ApiOpenAL.Emisor;
import com.icecool.worldinshadow.BaseDato.StructEnemigo;
import com.icecool.worldinshadow.BaseDato.StructPartida;
import com.icecool.worldinshadow.PantallaTactil.ObjetoTactil;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class MotorJuego {

	
	// Estados del juego.
	// 1 - Menu.
	// 2 - Jugando.
	private int estado;
	
	// Nivel.
	// 0 - Tutorial.
	// 1 - Nivel 1.
	// 2 - Nivel 2.
	// 3 - Nivel 3.
	private int nivel;
	
	//Parte del nivel.
	private int parteNivel; 
	
	//Indica que camino, util para tutorial.
	private boolean camino;
	
	//Uso cuchillo.
	private boolean usoCuchillo;
	
	//Posicion de los objetivos
	private ArrayList<Vector3> objetivos;
	
	//Posicion de dialogo.
	private Vector3 posDialogo;
	
	//Enemigos.
	private Enemigos enemigos;
	
	//Buffers.
	private Buffer bufferObjetivo;
	private Buffer bufferEnemigo;
	private Buffer bufferCuchillo;
	private Buffer bufferDialogo;
	private Buffer bufferEnemigoAtaca;
	private Buffer bufferCuchilladaEnemigo;
	private Buffer bufferEnemigoMurio;
	private Buffer bufferMenuVolverMenu;
	private Buffer bufferMenuSalir;
	private Buffer bufferMenuJugarUltimaPartida;
	private Buffer bufferMenuJugarNuevaPartida;
	// Comenzando nueva partida.
	// Starting new game.
	private Buffer bufferMenuMensajeNuevaPartida;
	// Comenzando partida cargada.
	// Beginning Game loaded.
	private Buffer bufferMenuMensajePartidaCargada;
	// Partida guardada.
	// Saved game.
	private Buffer bufferMensajePartidaGuardada;
	private Buffer bufferMenuCambiarModoMovimiento;
	// Modo sensor activado.
	// Mode sensor activated.
	private Buffer bufferMenuModoSensor;
	// Modo pantalla activado.
	// Enabled screen mode.
	private Buffer bufferMenuModoPantalla;
	// Conecte los auriculares.
	// Connect the headphones.
	private Buffer bufferMensajeConectarAuriculares;
	private Buffer bufferAmbiente;
	private Buffer bufferCaminando;
	private Buffer bufferMeAtraparon;
	private Buffer bufferAlcanceObjetivo;
	private Buffer bufferCorazon;
	private Buffer bufferMuero;
	
	//Emisores.
	private Emisor emisorObjetivo;
	private Emisor emisorEnemigo[];
	private Emisor emisorCuchillo;
	private Emisor emisorDialogo;
	private Emisor emisorEnemigoMurio;
	private Emisor emisorCuchilladaEnemigo;
	private Emisor emisorEnemigoAtaca;
	private Emisor emisorDialogoMenu;
	private Emisor emisorAmbiente;
	private Emisor emisorCaminando;
	private Emisor emisorAlcanceObjetivo;
	private Emisor emisorMeAtraparon;
	private Emisor emisorCorazon;
	private Emisor emisorMuero;
	
	//Dialogo reproducido del menu.
	// Espanol:
	// 1 - Para volver al menu mientras juega presiona el boton de atras. 
	// 2 - Para salir presione atras.
	// 3 - Para jugar desde la ultima partida guardada haga click con un dedo en la pantalla.
	// 4 - Para comenzar una nueva partida mantenga presionada la pantalla con dos dedos.
	// 5 - Para cambiar de modo de movimiento.
	// Ingles:
	// 1 - To return to the menu while playing press back button.
	// 2 - To exit press back.
	// 3 - To play from the last saved game click with one finger on the screen.
	// 4 - To start a new game hold the screen with two fingers.
	// 5 - To change mode of motion.
	private int dialogoMenu;
	
	//Milisegundos del frame anterior.
	private long msAnt;
	
	//Milisegundo del ultimo cambio de modo de movimiento.
	private long msCambioModoMovimiento;
	
	//Sonido ambiente que esta reproduciendo.
	private int sonidoAmbiente;
	
	//Utilizado en los niveles para contar tiempo.
	private long contadorTiempo;
	
	private Datos datos;
	private ApiOpenAL openal;
	
	static private ActividadOpenAL juego = null;
	
	static private MotorJuego instancia = null;
	
	private MotorJuego(){
	
		datos = Datos.getInstancia();
		enemigos = Enemigos.getInstancia();
		objetivos = new ArrayList<Vector3>();
		posDialogo = new Vector3();
		bufferDialogo = null;
		emisorDialogo = null;
		emisorDialogoMenu = null;
		dialogoMenu = 1;
		sonidoAmbiente = -1;
	}
	
	static public MotorJuego getInstancia(ActividadOpenAL juego_){
		juego = juego_;
		if(instancia == null)
			instancia = new MotorJuego();
		instancia.openal = ApiOpenAL.getInstancia(juego_);
		return instancia;
	}
	
	//Inicializa.
	public void init(){
		
		msAnt = -1;
		
		//Seteo estado en menu principal.
		estado = 1;
		
		//Inicializa OpenAL.
		openal.init();
		
		//Carga buffers.
		bufferObjetivo = openal.crearBuffer(juego.getPathFile("objetivo"));
		bufferEnemigo = openal.crearBuffer(juego.getPathFile("enemigo"));
		bufferCuchillo = openal.crearBuffer(juego.getPathFile("cuchillo"));
		bufferEnemigoMurio = openal.crearBuffer(juego.getPathFile("enemigo_muere"));
		bufferCuchilladaEnemigo = openal.crearBuffer(juego.getPathFile("cuchillada_enemigo"));
		bufferEnemigoAtaca = openal.crearBuffer(juego.getPathFile("enemigo_ataca"));
		bufferMenuVolverMenu = openal.crearBuffer(juego.getPathFile("volver_menu"));
		bufferMenuSalir = openal.crearBuffer(juego.getPathFile("menu_salir"));
		bufferMenuJugarUltimaPartida = openal.crearBuffer(juego.getPathFile("menu_jugar_ultima_partida"));
		bufferMenuJugarNuevaPartida = openal.crearBuffer(juego.getPathFile("menu_jugar_nueva_partida"));
		bufferMenuMensajeNuevaPartida = openal.crearBuffer(juego.getPathFile("menu_mensaje_nueva_partida"));
		bufferMenuMensajePartidaCargada = openal.crearBuffer(juego.getPathFile("menu_mensaje_partida_cargada"));
		bufferMensajePartidaGuardada = openal.crearBuffer(juego.getPathFile("mensaje_partida_guardada"));
		bufferMenuCambiarModoMovimiento = openal.crearBuffer(juego.getPathFile("menu_modo_movimiento"));
		bufferMenuModoSensor = openal.crearBuffer(juego.getPathFile("menu_modo_sensor"));
		bufferMenuModoPantalla = openal.crearBuffer(juego.getPathFile("menu_modo_pantalla"));
		bufferMensajeConectarAuriculares = openal.crearBuffer(juego.getPathFile("conectar_auriculares"));
		bufferCaminando = openal.crearBuffer(juego.getPathFile("caminando"));
		bufferAmbiente = null;
		bufferMeAtraparon = openal.crearBuffer(juego.getPathFile("me_atraparon"));
		bufferAlcanceObjetivo = openal.crearBuffer(juego.getPathFile("alcance_objetivo"));
		bufferCorazon = openal.crearBuffer(juego.getPathFile("corazon"));
		bufferMuero = openal.crearBuffer(juego.getPathFile("muero"));
		
		//Creo fuentes.
		//Objetivo.
		emisorObjetivo = openal.crearEmisor(bufferObjetivo, true);
		//Enemigos.
		emisorEnemigo = new Emisor[4];
		emisorEnemigo[0] = openal.crearEmisor(bufferEnemigo, true);
		emisorEnemigo[1] = openal.crearEmisor(bufferEnemigo, true);
		emisorEnemigo[2] = openal.crearEmisor(bufferEnemigo, true);
		emisorEnemigo[3] = openal.crearEmisor(bufferEnemigo, true);
		//Cuchillo.
		emisorCuchillo = openal.crearEmisor(bufferCuchillo, false);
		//Cuando un enemigo muere.
		emisorEnemigoMurio = openal.crearEmisor(bufferEnemigoMurio, false);
		//Cuando le pego con un cuchillo a un enemigo.
		emisorCuchilladaEnemigo = openal.crearEmisor(bufferCuchilladaEnemigo, false);
		//Cuando un enemigo me ataca.
		emisorEnemigoAtaca = openal.crearEmisor(bufferEnemigoAtaca, false);
		//Caminando.
		emisorCaminando = openal.crearEmisor(bufferCaminando, true);
		emisorAmbiente = null;
		emisorAlcanceObjetivo = openal.crearEmisor(bufferAlcanceObjetivo, false);
		emisorMeAtraparon = openal.crearEmisor(bufferMeAtraparon, false);
		emisorCorazon = openal.crearEmisor(bufferCorazon, true);
		emisorMuero = openal.crearEmisor(bufferMuero, false);
		
		//Seteo sonidos de enemigo.
		enemigos.setSonidos(emisorEnemigoMurio, emisorCuchilladaEnemigo, emisorEnemigoAtaca, emisorEnemigo);
	}
	
	//Indica que apreto el boton de atras.
	//Retorna true si tiene que salir.
	public boolean botonAtras(){
	
		if(estado == 2){
			
			abrirMenu();
			return false;
		}

		return true;
	}
	
	public void release(){
		
		//Libero fuentes.
		openal.destruirEmisor(emisorObjetivo);
		openal.destruirEmisor(emisorEnemigo[0]);
		openal.destruirEmisor(emisorEnemigo[1]);
		openal.destruirEmisor(emisorEnemigo[2]);
		openal.destruirEmisor(emisorEnemigo[3]);
		openal.destruirEmisor(emisorCuchillo);
		openal.destruirEmisor(emisorCaminando);
		if(emisorAmbiente != null) openal.destruirEmisor(emisorAmbiente);
		openal.destruirEmisor(emisorAlcanceObjetivo);
		openal.destruirEmisor(emisorMeAtraparon);
		openal.destruirEmisor(emisorCorazon);
		openal.destruirEmisor(emisorMuero);
		
		//Libero buffers.
		openal.destruirBuffer(bufferCuchillo);
		openal.destruirBuffer(bufferEnemigo);
		openal.destruirBuffer(bufferObjetivo);
		openal.destruirBuffer(bufferEnemigoMurio);
		openal.destruirBuffer(bufferCuchilladaEnemigo);
		openal.destruirBuffer(bufferEnemigoAtaca);
		openal.destruirBuffer(bufferMenuVolverMenu);
		openal.destruirBuffer(bufferMenuSalir);
		openal.destruirBuffer(bufferMenuJugarUltimaPartida);
		openal.destruirBuffer(bufferMenuJugarNuevaPartida);
		openal.destruirBuffer(bufferMenuMensajeNuevaPartida);
		openal.destruirBuffer(bufferMenuMensajePartidaCargada);
		openal.destruirBuffer(bufferMensajePartidaGuardada);
		openal.destruirBuffer(bufferMenuCambiarModoMovimiento);
		openal.destruirBuffer(bufferMenuModoSensor);
		openal.destruirBuffer(bufferMenuModoPantalla);
		openal.destruirBuffer(bufferMensajeConectarAuriculares);
		openal.destruirBuffer(bufferCaminando);
		if(bufferAmbiente != null) openal.destruirBuffer(bufferAmbiente);
		openal.destruirBuffer(bufferMeAtraparon);
		openal.destruirBuffer(bufferAlcanceObjetivo);
		openal.destruirBuffer(bufferCorazon);
		openal.destruirBuffer(bufferMuero);
		
		//Libero openal.
		openal.liberar();
	}
	
	
	static private float angulo = 0;
	static private Vector3 vista = new Vector3();
	static private Vector3 vTemp = new Vector3();
	static private Vector3 pos = new Vector3();
	static private Vector3 vectorUp = new Vector3(0, 0, 1);
	public void ajustar(){
		
		//Calculo tiempo entre frame.
		long ms = 0;
		if(msAnt < 0){//Inicializo.
			msAnt = System.currentTimeMillis();
			return;
		}
		else{
			ms = System.currentTimeMillis() - msAnt;
			msAnt = System.currentTimeMillis();
		}
		
		//No paso tiempo suficiente, retorno.
		if(ms == 0) return;
		
		//No tiene auriculares conectado.
		if(!datos.getTieneAuriculares(juego)){
			
			//No esta reproduciendo.
			if(emisorDialogoMenu == null || !emisorDialogoMenu.estaReproduciendo()){
				
				//Borro dialogo.
				if(emisorDialogoMenu != null){
					openal.destruirEmisor(emisorDialogoMenu);
					emisorDialogoMenu = null;
				}

				//Reproduzco.
				emisorDialogoMenu = openal.crearEmisor(bufferMensajeConectarAuriculares, false);
				datos.getPosicion(vTemp);
				emisorDialogoMenu.setPos(vTemp);
				emisorDialogoMenu.reproducir();
			}
			return;
		}
		
		//Menu.
		if(estado == 1){
			
			//Hay dialogo del juego.
			if(emisorDialogo != null){

				//Detengo dialogo.
				emisorDialogo.detener();
				openal.destruirEmisor(emisorDialogo);
				emisorDialogo = null;
			}
			
			//No hay dialogo reproduciendo.
			if( emisorDialogoMenu == null || !emisorDialogoMenu.estaReproduciendo()){
				
				//Borro dialogo.
				if(emisorDialogoMenu != null){
					openal.destruirEmisor(emisorDialogoMenu);
					emisorDialogoMenu = null;
				}
				
				//Reproduzco.
				if(dialogoMenu == 1){
					emisorDialogoMenu = openal.crearEmisor(bufferMenuVolverMenu, false);
					dialogoMenu = 2;
				}
				else if(dialogoMenu == 2){
					emisorDialogoMenu = openal.crearEmisor(bufferMenuSalir, false);
					dialogoMenu = 3;
				}
				else if(dialogoMenu == 3){
					emisorDialogoMenu = openal.crearEmisor(bufferMenuJugarUltimaPartida, false);
					dialogoMenu = 4;
				}
				else if(dialogoMenu == 4){
					emisorDialogoMenu = openal.crearEmisor(bufferMenuJugarNuevaPartida, false);
					dialogoMenu = 5;
				}
				else if(dialogoMenu == 5){
					emisorDialogoMenu = openal.crearEmisor(bufferMenuCambiarModoMovimiento, false);
					dialogoMenu = 1;
				}
				
				datos.getPosicion(pos);
				emisorDialogoMenu.setPos(pos);
				emisorDialogoMenu.reproducir();
			}
			
			//Hizo accion, carga ultima partida.
			if(PantallaTactil.getInstancia().getCuchilloAccion()){
				
				//Borro dialogo.
				if(emisorDialogoMenu != null){
					openal.destruirEmisor(emisorDialogoMenu);
					emisorDialogoMenu = null;
				}
				
				//Reproduzco.
				emisorDialogoMenu = openal.crearEmisor(bufferMenuMensajePartidaCargada, false);
				datos.getPosicion(pos);
				emisorDialogoMenu.setPos(pos);
				emisorDialogoMenu.reproducir();
				
				//Carga partida.
				cargarPartida();
				
				estado = 2;
				
				//Borro.
				PantallaTactil.getInstancia().setCuchilloAccion();
			}
			
			//Quiere nueva partida.
			else if(PantallaTactil.getInstancia().getNuevaPartida()){
				
				//Borro dialogo.
				if(emisorDialogoMenu != null){
					openal.destruirEmisor(emisorDialogoMenu);
					emisorDialogoMenu = null;
				}
				
				//Reproduzco.
				emisorDialogoMenu = openal.crearEmisor(bufferMenuMensajeNuevaPartida, false);
				datos.getPosicion(pos);
				emisorDialogoMenu.setPos(pos);
				emisorDialogoMenu.reproducir();
				
				//Carga datos de nueva partida.
				nuevaPartida();
				
				estado = 2;
			}
			
			//Quiere cambiar de modo de movimiento.
			else if(PantallaTactil.getInstancia().getDeslizoDedo()){
				
				//Hace mas de 2 segundos que cambio.
				if( (System.currentTimeMillis() - msCambioModoMovimiento) > 2000){
					
					//Cambio modo.
					datos.cambiarModoMovimiento();
					msCambioModoMovimiento = System.currentTimeMillis();
					
					//Sonido.
					emisorDialogoMenu.detener();
					openal.destruirEmisor(emisorDialogoMenu);
					if(datos.getUsarSensor()) emisorDialogoMenu = openal.crearEmisor(bufferMenuModoSensor, false);
					else emisorDialogoMenu = openal.crearEmisor(bufferMenuModoPantalla, false);
					datos.getPosicion(pos);
					emisorDialogoMenu.setPos(pos);
					emisorDialogoMenu.reproducir();
				}
			}
		}
		
		//Jugando.
		else if(estado == 2){
			
			//Si esta reproduciendo sonido de menu espera.
			if(emisorDialogoMenu.estaReproduciendo()) return;
			
			//Obtengo posicion, vista.
			datos.getPosicion(pos);
			datos.getVista(vista);
			
			//No tiene sensores.
			if(!datos.getUsarSensor()){
				
				//Angulo.
				float alfa = (float) Math.acos(vista.x);
				if(vista.y < 0) alfa = (float) (2.0*Math.PI - alfa);

				//Utilizo movimiento de pantalla.
				//Hacia la izquierda.
				if(PantallaTactil.getInstancia().rota() == 1){
					
					alfa += (Datos.VEL_ANGULO_ROTAR_SEGUNDO * ms) / 1000.0f;
				}
				
				//Hacia la derecha.
				else if(PantallaTactil.getInstancia().rota() == 2){
					
					alfa -= (Datos.VEL_ANGULO_ROTAR_SEGUNDO * ms) / 1000.0f;
				}
				
				vista.x = (float) Math.cos(alfa);
				vista.y = (float) Math.sin(alfa);
				
				//Seteo vista.
				datos.setVista(vista);
			}
			
			//Si puede caminar.
			if(datos.getPuedeCaminar()){

				//Si esta caminando.
				if(PantallaTactil.getInstancia().getCamina()){

					//Calculo vector de caminata.
					vTemp.set(vista);
					vTemp.z = 0;
					vTemp.normalizar();
					vTemp.multiplicar( ((float)ms) * Datos.AVANCE_USUARIO / 1000.0f);

					//Seteo nueva posicion.
					pos.sumar(vTemp);
					setPosicion(pos);
					
					camino = true;
					
					//Sonido.
					vTemp.set(pos);
					vTemp.z = -2;
					emisorCaminando.setPos(vTemp);
					if(!emisorCaminando.estaReproduciendo()) emisorCaminando.reproducir();
					
					//Tengo que actualizar dialogo.
					posDialogo.set(pos);
					if( emisorDialogo != null & emisorDialogo.estaReproduciendo() && Historia.setPosDialogoExplicacion(nivel, parteNivel, posDialogo, false)) emisorDialogo.setPos(posDialogo); 
				}
				
				//No esta caminando.
				else{
					
					//Pauso si esta caminando.
					if(emisorCaminando.estaReproduciendo()) emisorCaminando.pausa();
				}
			}
			
			//No esta caminando.
			else{
				
				//Pauso si esta caminando.
				if(emisorCaminando.estaReproduciendo()) emisorCaminando.pausa();
			}

			//Seteo propiedad de receptor.
			openal.getReceptor().setDir(vista, vectorUp);

			//Seteo sonido ambiente.
			if(emisorAmbiente != null) emisorAmbiente.setPos(pos);
			
			//Accion.
			if(PantallaTactil.getInstancia().getCuchilloAccion()){

				//Puede usar arma.
				if(datos.getTieneCuchillo()){
				
					//Utiliza cuchillo.
					usoCuchillo = true;
					vTemp.set(vista);
					vTemp.sumar(pos);
					emisorCuchillo.setPos(vTemp);
					emisorCuchillo.reproducir();
					
					//Aviso a enemigos que ataque.
					enemigos.atacar(pos, vista);
				}
				
				//Inicializa la accion.
				PantallaTactil.getInstancia().setCuchilloAccion();
			}
			
			//Objetivos.
			if(objetivos.size() > 0){
				
				//Esta cerca del primero.
				if( Vector3.distancia(objetivos.get(0), pos) < Datos.DISTANCIA_CERCANA_OBJETIVO ){
					
					//Reproduzco alcance de objetivo.
					emisorAlcanceObjetivo.setPos(objetivos.get(0));
					
					//Lo quito.
					objetivos.remove(0);
					
					//Si hay otro, seteo posicion.
					if(objetivos.size() > 0) emisorObjetivo.setPos(objetivos.get(0));
				}
			}
			
			//Enemigos.
			enemigos.ajustar((int) ms);
			
			//No tengo vida al 100 porciento y estoy a tiempo de recuperarme.
			if( datos.getVida() < 100 && (System.currentTimeMillis() - datos.getUltimaVezAtacaron()) > datos.MS_LUEGO_ATAQUE_RECUPERA_VIDA ){
				
				//Recupero vida.
				datos.setVida(100);
				
				//Quito sonido de corazon.
				if(emisorCorazon.estaReproduciendo()) emisorCorazon.detener();
			}
			
			//Tengo que poner sonido de corazon.
			if(datos.getVida() < 100){
				
				emisorCorazon.setPos(pos);
				if(!emisorCorazon.estaReproduciendo()) emisorCorazon.reproducir();
			}
			
			//A reproducir sonido.
			if(objetivos.size() > 0 && !emisorObjetivo.estaReproduciendo()){
				emisorObjetivo.setPos(objetivos.get(0));
				emisorObjetivo.reproducir();
			}
			//A no reproducir.
			else if(objetivos.size() == 0 && emisorObjetivo.estaReproduciendo())
				emisorObjetivo.detener();
			
			//Sonido ambiente.
			iniciarSonidoAmbiente();
			
			//Tutorial.
			if(nivel == 0){
				
				if(parteNivel == 0){

					//No camina ni tiene cuchillo.
					datos.setPuedeCaminar(false);
					datos.setTieneCuchillo(false);
					
					//Abre primer dialogo.
					setDialogo(pos, nivel, parteNivel+1, "dialogo1");
					
					//Siguiente parte.
					parteNivel++;
				}
			
				else if(parteNivel == 1){
					
					//Termino el dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Coloco objetivos.
						Historia.setObjetivos(nivel, parteNivel+1, objetivos);
						
						//Creo nuevo dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo2");
						
						//Camina.
						datos.setPuedeCaminar(true);
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 2){
					
					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Genero dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo3");
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 3){

					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Ya camino.
						if(camino){
							
							//Llego a los 2 objetivos.
							if(objetivos.size() == 0){

								//Genero dialogo.
								setDialogo(pos, nivel, parteNivel+1, "dialogo4");
								
								//Siguiente parte.
								parteNivel++;
							}
						}
						
						//No ha caminado, de nuevo dialogo 3.
						else emisorDialogo.reproducir();
					}
				}

				else if(parteNivel == 4){

					//Termino de reproducir.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Genero dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo5");
						
						//Tiene arma.
						datos.setTieneCuchillo(true);
						
						//Siguiente parte.
						parteNivel++;
					}
				}

				else if(parteNivel == 5){

					//Termino de reproducir.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Genero dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo6");
						
						//Genero 1 enemigo.
						vTemp.set(10, 10, 0);
						vTemp.sumar(pos);
						enemigos.crearEnemigo(vTemp);
						
						//Inicializo contador.
						contadorTiempo = 0;
						
						//Siguiente parte.
						parteNivel++;
					}
				}

				else if(parteNivel == 6){

					//Mato enemigo.
					if(enemigos.getCantEnemigos() == 0){
						
						//Hace mas de 3 segundos lo mato.
						if(contadorTiempo > 3000){
						
							//No reproduce mas el dialogo.
							if(!emisorDialogo.estaReproduciendo()){
								
								//Genero dialogo.
								setDialogo(pos, nivel, parteNivel+1, "dialogo7");

								//Siguiente parte.
								parteNivel++;
							}
						}
						
						//Sumo contador.
						contadorTiempo += ms;
					}
					
				}
				
				else if(parteNivel == 7){

					//Termina dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Listo, al siguiente nivel.
						nivel++;
						parteNivel = 0;
						
						//Guardo.
						guardarPartida();
					}
				}
			}
			
			//Nivel 1.
			else if(nivel == 1){
				
				if(parteNivel == 0){

					//Camina y con cuchillo.
					datos.setPuedeCaminar(true);
					datos.setTieneCuchillo(true);
					
					//Abre dialogo.
					setDialogo(pos, nivel, parteNivel+1, "dialogo8");

					//Setea objetivos.
					Historia.setObjetivos(nivel, parteNivel+1, objetivos);
					
					//Siguiente parte.
					parteNivel++;
				}
			
				else if(parteNivel == 1){
					
					//Termino de buscar los objetivos.
					if(objetivos.size() == 0){
						
						//Creo nuevo dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo9");
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 2){
					
					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Listo, al siguiente nivel.
						nivel++;
						parteNivel = 0;
						
						//Guardo.
						guardarPartida();
					}
				}
			}
			
			//Nivel 2.
			else if(nivel == 2){
				
				if(parteNivel == 0){

					//Camina y con cuchillo.
					datos.setPuedeCaminar(true);
					datos.setTieneCuchillo(true);
					
					//Abre dialogo.
					setDialogo(pos, nivel, parteNivel+1, "dialogo10");

					//Setea objetivos.
					Historia.setObjetivos(nivel, parteNivel+1, objetivos);
					
					//Siguiente parte.
					parteNivel++;
				}
			
				else if(parteNivel == 1){
					
					//Aparece 1 animal.
					if(!emisorDialogo.estaReproduciendo()){
					
						vTemp.set(pos);
						vTemp.x += -20;
						vTemp.y += 20;
						enemigos.crearEnemigo(vTemp);
						
						//Inicializo tiempo.
						contadorTiempo = 0;
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 2){
					
					//Murio animal.
					if(enemigos.getCantEnemigos() == 0){
						
						//Hace 3 segundos que murio.
						if(contadorTiempo > 3000){
							
							//Dialogo.
							setDialogo(pos, nivel, parteNivel+1, "dialogo11");
							
							//Inicializo tiempo.
							contadorTiempo = 0;
							
							//Siguiente parte.
							parteNivel++;
						}
						
						contadorTiempo += ms;
					}
				}
				
				else if(parteNivel == 3){
				
					//Termino dialogo y quedan 3 objetivos o menos.
					if(!emisorDialogo.estaReproduciendo() && objetivos.size() <= 3){
						
						//Aparecen dos animales.
						vTemp.set(pos);
						vTemp.x += -20;
						vTemp.y += 20;
						enemigos.crearEnemigo(vTemp);
						vTemp.set(pos);
						vTemp.x += 15;
						vTemp.y += 20;
						enemigos.crearEnemigo(vTemp);
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 4){
				
					//No hay enemigos ni objetivos.
					if(enemigos.getCantEnemigos() == 0 && objetivos.size() == 0){
						
						//Dialogo.
						setDialogo(pos, nivel, parteNivel+1, "dialogo12");
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 5){

					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Listo, al siguiente nivel.
						nivel++;
						parteNivel = 0;
						
						//Guardo.
						guardarPartida();
					}
				}
			}
			
			//Nivel 3.
			else if(nivel == 3){
				
				if(parteNivel == 0){

					//Camina y con cuchillo.
					datos.setPuedeCaminar(true);
					datos.setTieneCuchillo(true);
					
					//Abre dialogo.
					setDialogo(pos, nivel, parteNivel+1, "dialogo13");
					
					//Siguiente parte.
					parteNivel++;
				}
			
				else if(parteNivel == 1){
					
					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
					
						//Objetivos.
						Historia.setObjetivos(nivel, parteNivel+1, objetivos);
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 2){
					
					//Seteo tiempo de mostruos.
					contadorTiempo = 20000;
					
					//Siguiente parte.
					parteNivel++;
				}
				
				else if(parteNivel == 3){
				
					//No hay enemigos.
					if(enemigos.getCantEnemigos() == 0){
						
						//Termino objetivos.
						if(objetivos.size() == 0){
							
							//Abre dialogo.
							setDialogo(pos, nivel, parteNivel+1, "dialogo14");
							
							//Siguiente parte.
							parteNivel++;
						}
						
						//Hay objetivos todavia.
						else{
							
							//Tiempo de 2 animales mas.
							if( contadorTiempo <= 0 ){

								//Aparecen dos animales.
								vTemp.set(pos);
								vTemp.x += Math.random()*20 + 10;
								vTemp.y += Math.random()*20 + 10;
								enemigos.crearEnemigo(vTemp);
								vTemp.set(pos);
								vTemp.x += Math.random()*20 + 10;
								vTemp.y += Math.random()*20 + 10;
								enemigos.crearEnemigo(vTemp);

								contadorTiempo = 30000;
							}
							//Baja contador.
							else contadorTiempo -= ms;
						}
					}
					
					//Hay enemigos.
					else{ }
				}
				
				else if(parteNivel == 4){

					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
					
						//No camina ni usa cuchillo.
						datos.setPuedeCaminar(false);
						datos.setTieneCuchillo(false);
						
						//Ruido de que me atraparon.
						emisorMeAtraparon.setPos(pos);
						emisorMeAtraparon.reproducir();
						
						//Siguiente parte.
						parteNivel++;
					}
				}
				
				else if(parteNivel == 5){
					
					//Termino ruido de que me atraparon.
					if(!emisorMeAtraparon.estaReproduciendo()){
					
						//Listo, termina por ahora.
						nivel = 99;
						parteNivel = 0;

						//Guardo.
						guardarPartida();
					}
				}
			}
			
			//Nivel 99.
			else if(nivel == 99){
				
				if(parteNivel == 0){

					//No camina y no usa cuchillo.
					datos.setPuedeCaminar(false);
					datos.setTieneCuchillo(false);
					
					//Abre dialogo de fin.
					setDialogo(pos, nivel, parteNivel+1, "dialogo99");
					
					//Siguiente parte.
					parteNivel++;
				}
				
				else if(parteNivel == 1){
					
					//Termino dialogo.
					if(!emisorDialogo.estaReproduciendo()){
						
						//Me voy.
						abrirMenu();
					}
				}
			}
		}
	}
	
	
	
	
	private void borrarDialogo(){
		if(emisorDialogo != null){
			openal.destruirEmisor(emisorDialogo);
			emisorDialogo = null;
		}
		if(bufferDialogo != null){
			openal.destruirBuffer(bufferDialogo);
			bufferDialogo = null;
		}
	}
	
	private void setDialogo(Vector3 miPos, int nivel, int parte, String file){
		
		borrarDialogo();
		
		bufferDialogo = openal.crearBuffer(juego.getPathFile(file));
		emisorDialogo = openal.crearEmisor(bufferDialogo, false);
		posDialogo.set(miPos);
		Historia.setPosDialogoExplicacion(nivel, parte, posDialogo, true);
		emisorDialogo.setPos(posDialogo);
		emisorDialogo.reproducir();
	}
	
	//Nueva partida.
	public void nuevaPartida(){
		
		//Posicion.
		pos.set(0, 0, 0);
		setPosicion(pos);
		
		
		//Nivel y parte.
		nivel = 0;
		parteNivel = 0;
		datos.setPuedeCaminar(false);
		datos.setTieneCuchillo(false);
		camino = false;
		usoCuchillo = false;
		
		//Limpio objetivos.
		objetivos.clear();
		
		//Vida al maximo.
		datos.setVida(100);
		
		posDialogo.set(0, 0, 0);
		enemigos.limpiar();
	}
	
	//Carga partida.
	public void cargarPartida()
	{
		StructPartida p = BaseDato.getBaseDato(juego).cargarPartida();
		
		//Existe partida.
		if(p != null)
			cargarPartidaFromStruct(p);
		
		//No existe partida, nueva partida.
		else nuevaPartida();
	}

	private void cargarPartidaFromStruct(StructPartida p){
		setPosicion(p.miPos);
		nivel = p.nivel;
		parteNivel = p.parte;
		datos.setPuedeCaminar(p.puedeCaminar);
		datos.setTieneCuchillo(p.tieneCuchillo);
		camino = p.camino;
		usoCuchillo = p.usoCuchillo;
		objetivos.clear();
		if(p.objetivos != null){
			for(int i=0; i<p.objetivos.length; i++)
				objetivos.add(p.objetivos[i]);
		}
		datos.setVida(p.vida);
		posDialogo.set(p.posDialogo);
		enemigos.setEnemigos(p.enemigos);
	}
	
	//Guarda partida.
	public void guardarPartida(){
		
		BaseDato bd = BaseDato.getBaseDato(juego);
		
		StructPartida p = new StructPartida();
		
		p.miPos = new Vector3();
		datos.getPosicion(p.miPos);
		p.nivel = nivel;
		p.parte = parteNivel;
		p.puedeCaminar = datos.getPuedeCaminar();
		p.tieneCuchillo = datos.getTieneCuchillo();
		p.camino = camino;
		p.usoCuchillo = usoCuchillo;
		p.objetivos = null;
		if(objetivos.size() > 0){
			p.objetivos = new Vector3[objetivos.size()];
			for(int i=0; i<objetivos.size(); i++){
				p.objetivos[i] = new Vector3(objetivos.get(i).x, objetivos.get(i).y, objetivos.get(i).z);
			}
		}
		p.vida = datos.getVida();
		p.posDialogo = new Vector3();
		p.posDialogo.set(posDialogo);
		p.enemigos = enemigos.getEnemigosBD(bd);
		
		bd.guardarPartida(p);
		
		
		//Borro dialogo.
		if(emisorDialogoMenu != null){
			openal.destruirEmisor(emisorDialogoMenu);
			emisorDialogoMenu = null;
		}
		
		//Reproduzco.
		emisorDialogoMenu = openal.crearEmisor(bufferMensajePartidaGuardada, false);
		datos.getPosicion(vTemp);
		emisorDialogoMenu.setPos(vTemp);
		emisorDialogoMenu.reproducir();
	}
	
	private void quitarSonidoAmbiente(){
		
		if(emisorAmbiente != null){
			openal.destruirEmisor(emisorAmbiente);
			emisorAmbiente = null;
		}
		if(bufferAmbiente != null){
			openal.destruirBuffer(bufferAmbiente);
			bufferAmbiente = null;
		}
		
		sonidoAmbiente = -1;
	}
	
	private void iniciarSonidoAmbiente(){
		
		//Ya esta reproduciendo.
		if(sonidoAmbiente == nivel) return;
		
		quitarSonidoAmbiente();
		
		//Reproduzco.
		if(nivel == 0)
			bufferAmbiente = openal.crearBuffer(juego.getPathFile("ambiente_espacio"));
		else if(nivel == 1)
			bufferAmbiente = openal.crearBuffer(juego.getPathFile("ambiente_espacio"));
		else if(nivel == 2)
			bufferAmbiente = openal.crearBuffer(juego.getPathFile("ambiente_bosque2"));
		else if(nivel == 3)
			bufferAmbiente = openal.crearBuffer(juego.getPathFile("ambiente_bosque"));

		if(bufferAmbiente != null) {
			emisorAmbiente = openal.crearEmisor(bufferAmbiente, true);
			emisorAmbiente.reproducir();
		}

		sonidoAmbiente = nivel;
	}
	
	private void abrirMenu(){
		
		estado = 1;
		
		quitarSonidoAmbiente();
		borrarDialogo();
		emisorObjetivo.detener();
	}
	
	private void setPosicion(Vector3 pos_){
		
		datos.setPosicion(pos_);
		openal.getReceptor().setPos(pos_);
		if(emisorCaminando != null) emisorCaminando.setPos(pos_);
	}

	private void setForDebug(int _nivel)
	{
		StructPartida p = new StructPartida();

		if(_nivel == 1) {
			p.miPos = new Vector3(8.731397f, 3.722984f, 0.0f);
			p.nivel = 1;
			p.parte = 0;
			p.puedeCaminar = true;
			p.tieneCuchillo = true;
			p.camino = true;
			p.usoCuchillo = true;
			p.vida = 100;
			p.posDialogo = new Vector3(10.731397f, 4.7229843f, 1.5f);
		}
		else if(_nivel == 2){
			p.miPos = new Vector3(29.259993f, 38.56343f, 0.0f);
			p.nivel = 2;
			p.parte = 0;
			p.puedeCaminar = true;
			p.tieneCuchillo = true;
			p.camino = true;
			p.usoCuchillo = true;
			p.vida = 100;
			p.posDialogo = new Vector3(29.259993f, 38.56343f, 0.0f);
		}
		else if(_nivel == 3)
		{
			p.miPos = new Vector3(121.19921f, 13.92817f, 0.0f);
			p.nivel = 3;
			p.parte = 0;
			p.puedeCaminar = true;
			p.tieneCuchillo = true;
			p.camino = true;
			p.usoCuchillo = true;
			p.vida = 100;
			p.posDialogo = new Vector3(121.19921f, 13.92817f, 0.0f);
		}
		else if(_nivel == 99)
		{
			p.miPos = new Vector3(205.9107f, 31.037321f, 0.0f);
			p.nivel = 99;
			p.parte = 0;
			p.puedeCaminar = false;
			p.tieneCuchillo = false;
			p.camino = true;
			p.usoCuchillo = true;
			p.vida = 100;
			p.posDialogo = new Vector3(205.9107f, 31.037321f, 0.0f);
		}

		cargarPartidaFromStruct(p);
	}
}
