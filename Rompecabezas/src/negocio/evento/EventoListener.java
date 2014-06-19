/*
 * EventoListener.java
 *
 * Created on 5 de abril de 2008, 11:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.evento;

import java.util.EventListener;

/**
 * Interfaz que debe implementar toda clase que quiera tener la
 * capacidad de esuchar los eventos generados por una clase 
 * {@link ObjetoEscuchable}
 * @author Juan Timoteo Ponce Ortiz
 */
public interface EventoListener extends EventListener{
    
    /**
     * Método invocado desde el o los {@link ObjetoEscuchable} que esta 
     * monitoreando una clase que implemente  esta interfaz
     * @param evt Evento generado por el origen
     */
    public void eventoLanzado( Evento evt );
}
