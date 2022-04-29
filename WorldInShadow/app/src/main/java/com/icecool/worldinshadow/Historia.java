package com.icecool.worldinshadow;

import java.util.ArrayList;

/*

Tutorial arranca hablando sobre lo que paso, por una causa que no se sabe todavia
la luz desaparecio, quedo todo a oscuras. Hay que saber que fue lo que paso.
Para eso los cientificos enviaron a no videntes con aparatos los cuales le indica
por medio de sonido el lugar donde sospechan esta la causa.
Se utiliza el dispositivo para apuntar hacia donde quiere dirigirse.
Presionando dos dedos camina hacia adelante, presionando una vez se realiza accion.
Para probar el dispositivo se lo envia a seguir lugares.
Por las dudas que halla algun enemigo se puede defender con un cuchillo.

Mision 1.
Tienes que dirigirte afuera de la base, sigue los indicadores sonoros.

Mision 2.
Un compa�ñero tuyo a perdido la señal, ve hacia alla para averiguar que sucedio.
En el camino descubre enemigos que te quieren matar.
En la base dicen que no sabian nada de eso.
Al llegar descubre que esta muerto.

Mision 3.
La base avisa que cerca de ahi hay una zona de mucha energia, hay que ir.
Mas enemigos atacando.
Cuando llega lo atrapa, eran compa�eros y se ponen a hablar.
- Estamos en una zona segura, al parecer es algo grande lo que esta sucediendo.
- Ellos deben saber que es todo esto, esos animales no son animales normales.
- Sospechamos que son mutaciones geneticas o seres fuera de este planeta.

En eso entran bichos y todo es un caos. 

*/

/*
	## Tutorial ##
	1 - Dialogo_01: (Bienvenido. 
		Como sabr�s todo el mundo que conocemos a desaparecido.
		En realidad nuestro mundo. 
		Por alguna extra�a raz�n que no logramos comprender toda la luz ha desaparecido literalmente.
		Al parecer una fuerza extra�a absorve la luz inmediatamente despu�s de emitirse por cualquier fuente luminosa.
		Nuestros cient�ficos no saben que ha sucedido pero tienen una idea en que lugar se origin� todo. 
		Por esta raz�n precisamos de ustedes. 
		Ya saben como moverse en un mundo a oscuras.
		Nuestros cient�ficos les brindar�n unos dispositivos que les ayudar� a dirigirlos hacia el objetivo.
		Hacia donde piensan que esta la respuesta a este suceso.
		Te ense�are a usarlo.)
	2 - Setea 3 objetivos.
	    Dialogo_02: (Eso sera un punto hacia donde debes dirigirte, te iremos
	    marcando puntos hasta llegar al objetivo. )
	3 - Dialogo_03: (Caminas presionando dos o mas dedos en la pantalla.) 
	    Repite dialogo hasta que camina. Pasa a la siguiente parte cuando camine y agarre los objetivos.
	4 - Dialogo_04: (Te brindaremos un cuchillo. Por si aparece alg�n enemigo o animal salvaje.)
	5 - Dialogo_05: (Presionando 1 vez en la pantalla puedes interactuar con el mundo o
	    utilizar el cuchillo si tienes un enemigo en frente.)
	6 - Aparece un enemigo a atacar.
	    Dialogo_06: (Prueba matar al enemigo utilizando el cuchillo, presiona con un solo dedo la pantalla.)
	    Repite hasta que mata al enemigo.
	7 - Dialogo_07: (Felicidades. Ya estas listo para la mision. Mucha suerte.)
	
	## Nivel 1  ##
	1 - Dialogo_08: (Sigue los objetivos sonoros para dirigirte fuera de la base.)
		Setea objetivos.
	2 - Cuando termina de seguir los objetivos.
		Dialogo_09: (Perfecto. Te encuentras fuera de la base. De inmediato te enviaremos nuevos objetivos.)
		
	## Nivel 2 ##
	1 - Dialogo_10: (Cambio de planes. Hemos detectado que un compa�ero ha perdido la se�al. Por suerte est� cerca tuyo.
		Te enviamos su �ltima posici�n para que investigues.)
		Setea objetivos.
	2 - Aparece un animal.
	3 - Dialogo_11: (Parece que hay animales salvajes en tu zona. Es la primera vez que los encontramos.
		No te preocupes. Deben de estar desorientados.)
	4 - Aparecen dos animales cuando quedan 3 objetivos.
	5 - Cuando termina de seguir los objetivos y matar los enemigos.
		Dialogo_12: (Ya has llegado al punto donde ha desaparecido tu compa�ero. Al parecer no se
		encuentra m�s ah�. Te enviaremos nuevas coordenadas.)
		
	## Nivel 3 ##
	1 - Dialogo_13: (Hemos detectado una zona de mucha actividad energ�tica. Tienes que ir. Te estamos enviando las coordenadas.)
	2 - Nuevos objetivos.
	3 - Van apareciendo 2 enemigos mas luego de 30 seg de matarlos.
	4 - Dialogo_14: (Ya estas en la zona. Ten cuidado. No se sabe qu� o qui�n est� ah�.)
	5 - Ruido.
	
	## Nivel 99 ##
	1 - Dialogo_99: (Continuar�)
		
*/

/* Dialogos en ingles
 * 

Dialogo_01: 
Welcome.
As everyone know that we know disappeared.
Actually our world.
For some reason we fail to grasp all the light has literally disappeared.
It seems a strange force absorbs light immediately following the issuance by any light source.
Our scientists do not know has happened but have an idea that originated everything place.
For this reason we need you.
You know how to move in a dark world.
Our scientists will give them a device that will help lead them to their goal.
Towards where they think is the answer to this event.
I'll show you how to use it.

Dialogo_02:
That will be a point to where you should go, you go ticking points to reach the target.

Dialogo_03:
You walk by pressing two or more fingers on the screen.

Dialogo_04:
We'll provide a knife.
For if an enemy or wild animal appears.

Dialogo_05:
Pressing once on the screen you can interact with the world or use the knife if you have an enemy in front.

Dialogo_06:
Try to kill the enemy with the knife, one finger pressed the screen.

Dialogo_07:
Congratulations.
You are now ready for the mission.
Best of luck.

Dialogo_08:
Follow the sound objectives to guide you out of the base.

Dialogo_09:
Perfect.
You are off base.
Immediately will send new targets.

Dialogo_10:
Change of plans.
We have detected that a partner has lost the signal.
Luckily it is near you.
We sent your last position to investigate.

Dialogo_11:
It seems that there are wild animals in your area.
It is the first time that we found.
Do not worry.
They must be confused.

Dialogo_12:
You've reached the point where your partner is gone.
Apparently there is more there.
We'll send new coordinates.

Dialogo_13:
We detected a very energetic activity area.
You have to go.
We are sending the coordinates.

Dialogo_14:
Whether you're in the area.
Be careful.
It is not known what or who is there.

Dialogo_99:
To be continued


*/

public class Historia {

	static private Vector3 vTemp = new Vector3();
	
	//Setea posicion de dialogos.
	//Hay dialogos que se debe actualizar siempre la posicion (por ejemplo la voz en la radio),
	//otras no cuando es una persona en el mundo, la variable primeraVez indica que es la primera vez que se setea.
	//Retorna true si la posicion se actualiza, false si no.
	static public boolean setPosDialogoExplicacion(int nivel, int parte, Vector3 miPos, boolean primeraVez){
		
		boolean posActualiza = true;
		
		vTemp.set(0, 0, 0);
		
		if(nivel == 0){
		
			if(parte == 1) vTemp.set(2, 1, 1.5f);
			else if(parte == 2) vTemp.set(2, 1, 1.5f); 
			else if(parte == 3) vTemp.set(2, 1, 1.5f); 
			else if(parte == 4) vTemp.set(2, 1, 1.5f);
			else if(parte == 5) vTemp.set(2, 1, 1.5f); 
			else if(parte == 6) vTemp.set(2, 1, 1.5f); 
			else if(parte == 7) vTemp.set(2, 1, 1.5f); 
		}
		
		if(posActualiza) miPos.sumar(vTemp);
		
		return posActualiza;
	}
	
	
	//Seteo objetivos segun nivel y parte.
	static public void setObjetivos(int nivel, int parte, ArrayList<Vector3> objetivos){
		
		if(nivel == 0){
			if(parte == 2){
				objetivos.add(new Vector3(7, 7, 0));
				objetivos.add(new Vector3(10, 3, 0));
			}
		}
		else if(nivel == 1){
			if(parte == 1){
				objetivos.add(new Vector3(10, 10, 0));
				objetivos.add(new Vector3(25, 10, 0));
				objetivos.add(new Vector3(40, 15, 0));
				objetivos.add(new Vector3(50, 20, 0));
				objetivos.add(new Vector3(40, 25, 0));
				objetivos.add(new Vector3(30, 30, 0));
				objetivos.add(new Vector3(40, 33, 0));
				objetivos.add(new Vector3(30, 40, 0));
			}
		}
		else if(nivel == 2){
			if(parte == 1){
				objetivos.add(new Vector3(40, 40, 0));
				objetivos.add(new Vector3(55, 30, 0));
				objetivos.add(new Vector3(60, 40, 0));
				objetivos.add(new Vector3(70, 50, 0));
				objetivos.add(new Vector3(85, 40, 0));
				objetivos.add(new Vector3(90, 30, 0));
				objetivos.add(new Vector3(100, 30, 0));
				objetivos.add(new Vector3(110, 20, 0));
				objetivos.add(new Vector3(120, 15, 0));
			}
		}
		else if(nivel == 3){
			if(parte == 2){
				objetivos.add(new Vector3(130, 15, 0));
				objetivos.add(new Vector3(140, 10, 0));
				objetivos.add(new Vector3(135, 18, 0));
				objetivos.add(new Vector3(145, 22, 0));
				objetivos.add(new Vector3(155, 15, 0));
				objetivos.add(new Vector3(163, 25, 0));
				objetivos.add(new Vector3(171, 22, 0));
				objetivos.add(new Vector3(182, 10, 0));
				objetivos.add(new Vector3(195, 18, 0));
				objetivos.add(new Vector3(205, 28, 0));
			}
		}
	}
}
