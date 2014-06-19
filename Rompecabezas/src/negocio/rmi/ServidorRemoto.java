/*
 * NewClass.java
 *
 * Created on 21 de octubre de 2007, 01:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package negocio.rmi;
import datos.Mensaje;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz a implementar en el controlador
 * del servidor
 * los void deben cambiarse
 * @author Juan Timoteo Ponce Ortiz
 */
public interface ServidorRemoto extends Remote{
    
    public static final int LISTA_JUGADORES = 0;
    
    /**
     * Proceso que servira para establecer conexiones entre los clientes
     * @param user nombre usuario
     * @param obj objetoescuchable del cliente
     * @throws java.rmi.RemoteException
     * @return estado
     */
    public boolean conectar( String user, ObjetoRemoto obj ) throws RemoteException;
    /**
     * Proceso de desconexion de clientes
     * @param user nombre usuario
     * @param partida identificador de partida
     * @throws java.rmi.RemoteException
     */
    public void desconectar( String user , int partida ) throws RemoteException;
    /**
     * Procesa un mensaje enviado por algun cliente
     * @param user nombre usuario
     * @param partida identificador de partida
     * @param msg Mensaje a procesar
     * @throws java.rmi.RemoteException
     */
    public void procesar( String user , int partida , Mensaje msg) throws RemoteException;
    /**
     * Retorna informacion de algun tipo a cualquiera que lo pida
     * @param tipoInfo tipo de informacion
     * @param partida identificador de partida
     * @throws java.rmi.RemoteException
     * @return informacion requerida
     */
    public String getInfo( int tipoInfo , int partida ) throws RemoteException;
}
