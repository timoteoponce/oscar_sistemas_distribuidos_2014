/*
 * ObjetoEscuchable.java
 *
 * Created on 5 de abril de 2008, 11:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.evento;

/**
 * Interfaz que debe implementar toda clase que quiera tener
 * la capacidad de generar eventos para ser escuchados
 * por uno o varios {@link EventoListener}
 * @author Juan Timoteo Ponce Ortiz
 */
public interface ObjetoEscuchable {    

    /**
     * Agregar un escuchador de eventos para esta interfaz
     * @param listener Escuchador a agregar
     */
    public void agregarEscuchador( EventoListener listener );
    /**
     * Genera un evento para ser escuchado por todos sus observadores
     * @param evt Evento a lanzar
     */
    public void lanzarEvento( Evento evt );
}
