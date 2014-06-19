/*
 * DatosJugador.java
 *
 * Created on 9 de abril de 2008, 10:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datos;


import negocio.rmi.ObjetoRemoto;

/**
 * Clase base para almacenar los datos referentes
 * a un jugador, cantidad de movidas, fallos , etc.
 * Además que contiene el objeto remoto relativo.
 * @author Juan Timoteo Ponce Ortiz
 */
public class DatosJugador {
    /**
     * Objeto remoto de nexo con el cliente
     */
    private ObjetoRemoto objeto;
    /**
     * Cantidad de movidas buenas
     */
    private int movidasBuenas;
    /**
     * Cantidad de movidas malas
     */
    private int movidasMalas;
    /**
     * Bando o equipo
     */
    private int equipo;
    
    /**
     * Clase base que contiene todos los datos de un jugador, incluyendo
     * su objeto remoto y sus datos estadisticos.
     * @param obj Objeto remoto del jugador
     */
    public DatosJugador( ObjetoRemoto obj ) {
        objeto = obj;
        movidasBuenas = 0;
        movidasMalas = 0;
    }
    
    /**
     * Retorna el objeto remoto del jugador.
     * @return Objeto remoto
     */
    public ObjetoRemoto getObjeto() {
        return objeto;
    }
    
    /**
     * Asigna el valor al objeto remoto.
     * @param objeto Objeto remoto
     */
    public void setObjeto( ObjetoRemoto objeto ) {
        this.objeto = objeto;
    }
    
    /**
     * Retorna la cantidad de movidas buenas realizadas por el 
     * jugador.
     * @return movidas correctas realizadas
     */
    public int getMovidasBuenas() {
        return movidasBuenas;
    }
    
    /**
     * Retorna las movidas incorrectas.
     * @return Movidas incorrectas
     */
    public int getMovidasMalas() {
        return movidasMalas;
    }
    
    /**
     * Asigna un nuevo valor a las movidas erroneas
     * @param movidasBuenas Movidas incorrectas
     */
    public void setMovidasBuenas( int movidasBuenas ) {
        this.movidasBuenas = movidasBuenas;
    }
    
    /**
     * Asigna el valor a las movidas incorrectas
     * @param movidasMalas Movidas incorrectas
     */
    public void setMovidasMalas( int movidasMalas ) {
        this.movidasMalas = movidasMalas;
    }

    /**
     * Retorna el bando al que pertenece el jugador
     * @return Bando
     */
    public int getEquipo() {
        return equipo;
    }

    /**
     * Asigna el bando al jugador
     * @param equipo bando
     */
    public void setEquipo( int equipo ) {
        this.equipo = equipo;
    }
    


    /**
     * Muestra un resumen del objeto, como texto
     * @return Resumen objeto
     */
    public String toString() {
        return ( "Buenas = " + movidasBuenas + " , Malas = " + movidasMalas );
    }    
    
}

