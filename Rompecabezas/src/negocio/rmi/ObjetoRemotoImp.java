/*
 * tableroRemotoImp.java
 *
 * Created on 23 de octubre de 2007, 04:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package negocio.rmi;


import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import negocio.evento.Evento;
import negocio.evento.EventoListener;
import negocio.evento.ObjetoEscuchable;

/**
 * Clase que implementa las interfaces {@link ObjetoRemoto} y {@link ObjetoEscuchable}
 * , esta clase servira como nexo entre el servidor y el cliente
 * @author Juan Timoteo Ponce Ortiz
 */
public class ObjetoRemotoImp extends UnicastRemoteObject implements ObjetoRemoto,ObjetoEscuchable{

    private transient EventListenerList listeners = new EventListenerList();
    
    
    /**
     * Creates a new instance of tableroRemotoImp
     * @throws java.rmi.RemoteException 
     */
    public ObjetoRemotoImp() throws RemoteException {
        super();
    }
    
    /**
     * Agrega un observador de eventos
     * @param listener observador
     */
    public void agregarEscuchador( EventoListener listener ){
        listeners.add( EventoListener.class , listener );
    }
    
    
    /**
     * Genera un evento para los observadores
     * @param evt evento a lanzar
     */
    public void lanzarEvento( Evento evt ){
        EventoListener[] array = listeners.getListeners( EventoListener.class );
        
        for (int i = 0 ; i < array.length ; i++)
            ( ( EventoListener ) array[i] ).eventoLanzado( evt );
    }
    
    
    /**
     * Recibe un mensaje poara ser enviado al cliente
     * @param o conteniod
     * @throws java.rmi.RemoteException 
     */
    public void enviarMensaje( Serializable o ) throws RemoteException {
        Evento evt = new Evento( this );
        evt.setMensaje( o );
        lanzarEvento( evt );
    }
}
