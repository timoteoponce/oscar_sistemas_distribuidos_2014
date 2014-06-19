/*
 * Evento.java
 *
 * Created on 5 de abril de 2008, 11:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.evento;

import java.util.EventObject;

/**
 * Clase base para la manipulacion de eventos entre {@link ObjetoEscuchable} y 
 * {@link EventoListener}
 * @author timo
 */
public class Evento extends EventObject{
    private Object mensaje;
    
    /**
     * Creates a new instance of Evento
     * @param source Objeto generador del mensaje
     */
    public Evento( Object source ) {
        super( source );
    }

    /**
     * Retorna el mensaje contenido dentro del evento
     * @return Mensaje del evento
     */
    public Object getMensaje() {
        return mensaje;
    }

    /**
     * Asigna un contenido al evento
     * @param mensaje Contenido
     */
    public void setMensaje( Object mensaje ) {
        this.mensaje = mensaje;
    }
    

    
}
