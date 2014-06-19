/*
 * HiloNotificador.java
 *
 * Created on 20 de abril de 2008, 12:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.servidor;

import java.io.Serializable;
import negocio.rmi.ObjetoRemoto;

/**
 * Hilo que se utilizara para enviar mensajes a on {@link ObjetoRemoto}
 * @author Juan TImoteo Ponce Ortiz
 */
public class HiloNotificador extends Thread{
    ObjetoRemoto obj;
    Serializable msg;
    
    /**
     * Constructor
     * @param o objeto
     * @param m mensaje
     */
    public HiloNotificador( ObjetoRemoto o, Serializable m ){
        obj = o;
        msg = m;
        start();
    }
    
    /**
     * Ejecutar el hilo
     */
    public void run() {
        try {
            obj.enviarMensaje( msg );
        } catch (Exception e) {
            System.out.println("excepcion sync");
        }
    }
    
}
