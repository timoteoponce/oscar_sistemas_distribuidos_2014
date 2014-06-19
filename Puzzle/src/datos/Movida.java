/*
 * Movida.java
 *
 * Created on 9 de abril de 2008, 10:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datos;

import java.awt.Point;
import java.io.Serializable;

/**
 * Clase de utilidad, usada para enviar
 * información sobre las movidas realizadas 
 * por los jugadores.
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class Movida implements Serializable{
    Point origen;
    Point destino;
    
    /**
     * Creates a new instance of Movida
     * @param orig Punto origen de la movida
     * @param dest Punto destino de la movida
     */
    public Movida( Point orig, Point dest ) {
        origen = orig;
        destino = dest;
    }
    
    /**
     * Retorna el punto de origen de la movida
     * @return Punto origen de la movida
     */
    public Point getOrigen() {
        return origen;
    }
    
    /**
     * Retorna el punto destino de la movida
     * @return Punto destino de la movida
     */
    public Point getDestino() {
        return destino;
    }
    
    /**
     * Asigna el destino
     * @param destino Punto destino de la movida
     */
    public void setDestino( Point destino ) {
        this.destino = destino;
    }
    
    /**
     * Asigna el origen
     * @param origen Punto origen de la movida
     */
    public void setOrigen( Point origen) {
        this.origen = origen;
    }
    
    
    
}
