package es.studium.Camello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Carrera{
	
	static int META = 0;
    private List<Camello> camellos = new ArrayList<Camello>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    private int primerCamello;
    private boolean llegadaMeta;
   
    public Carrera(int numCamellos, final int pause) {
    	primerCamello = 0;
    	llegadaMeta = false;
  
    	
    
        barrier = new CyclicBarrier(numCamellos, new Runnable() {
            public void run() {
                
               
                for (Camello camello : camellos) {
                	// obtenemos el lider
                	if(camello.getPasos() > primerCamello) {
                		primerCamello = camello.getPasos();
                	}
                }
             
               
                
                for (Camello camello : camellos) {
                	if(camello.getPasos() == primerCamello) {
                		System.out.println("El Camello " + (camello.getId()+1) + " " 
                				+ "lleva: "+camello.getPasos()+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                				+ " Es el primero");
                	}else {
                		System.out.println("El Camello " + (camello.getId()+1) + " " 
                				+ "lleva: "+camello.getPasos()+" y está"
                				+ " a "+(primerCamello-camello.getPasos())
                				+" posiciones del lider");
                	}
                	if (camello.getPasos() >= META) {
                		llegadaMeta = true;
                	} 
                }
                
                /* Si llegadaMeta es cambiado a true, se termina la carrea y se detiene todo, imprimiendose
                las posiciones finales de forma ordenada con la clase Collections.sort
                
                se realiza otra comprobación, donde se busca el camello que ha llegado primero,
                si no es el primero lo que hace es mirar a cuántas posiciones se ha quedado del primero
                */
                if(llegadaMeta) {
                	System.out.println("\n" +"  POSICIONES: " + "\n");
                	// Ordenar las posiciones e imprimir                 	
                	Collections.sort(camellos);
                	int i=1;
                	for (Camello camello : camellos) {
                		if(i==1) {
                			System.out.println(i+"º: Camello "+ (camello.getId()+1));
                		}else {
                			System.out.println(i+"º: Camello "+ (camello.getId()+1)
                					+ " a "+(META - camello.getPasos())+" posiciones");
                		}                		
                		i++;
                	}
                	exec.shutdownNow();
                    return;
                }                	               
                
                
                
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                	System.out.println("barrier-action sleep interrupted");
                }
            }
        });
        // instanciamos el numero de Camellos y creamos sus hilos.

        for (int i = 0; i < numCamellos; i++) {
            Camello camello = new Camello(barrier);
            camellos.add(camello);
            exec.execute(camello);         
        }
    }

	public static void main(String[] args) throws InterruptedException{
		//creamos la variable que va a recoger el número de camellos y la ponemos a 0
		//para más abajo darle el valor que nos ingrese el usuario por pantalla
		int numCamellos = 0;
		
		/*
		 * lo que estamos haciendo aquío es preguntar al usuario cuántos camellos van a corer en la carrera
		 * así como que distancia queremos que corran en total.
		 * */
		
    	BufferedReader lectura = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Cuantos camellos van a correr?");
    	try {
    		numCamellos= Integer.parseInt(lectura.readLine());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	System.out.println("Qué distancia quiere que corran?");
    	try {
    		META = Integer.parseInt(lectura.readLine());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
/*    	
  La variable int pause lo que hace es definir el tiempo que se pausa
   después de cada ciclo, 	
*/    	
        int pause = 700;
        //se ejecuta con los camellos y con las pausas
        new Carrera(numCamellos, pause);      
    }
}
