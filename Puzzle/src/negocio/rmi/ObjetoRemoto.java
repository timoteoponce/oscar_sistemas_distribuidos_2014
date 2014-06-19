/*
 * tableroRemoto.java
 *
 * Created on 23 de octubre de 2007, 04:08 PM
 *
 * Esta es la interfaz para todo objeto remoto
 * que necesite ejecutarse en el servidor, generalmente
 */

package negocio.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Objeto que servira de nexo entre el servidor-cliente, debido a la 
 * falta de un camino de comunicacion entre estos, se utiliza esta clase
 * como un puente
 * @author Juan Timoteo Ponce Ortiz
 */
public interface ObjetoRemoto extends Remote{
    /**
     * Envia un mensaje al objeto remoto implementado
     * @param o contenido
     * 
     */
    public void enviarMensaje( Serializable o )throws RemoteException;    
}
