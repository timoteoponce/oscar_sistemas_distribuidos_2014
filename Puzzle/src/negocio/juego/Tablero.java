/*
 * Tablero.java
 *
 * Created on 20 de abril de 2008, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.juego;

import java.awt.Point;
import java.io.Serializable;

/**
 * Clase que contiene los datos de las piezas colocadas, y las no colocadas,
 * esta clase tambien define los metodos para verificar si el mismo ya esta
 * completado
 * @author Juan TImoteo Ponce Ortiz
 */
public class Tablero implements Serializable{

    private MatrizDispersa colocados;
    private MatrizDispersa aleatorios;    
    
    /**
     * Creates a new instance of Tabler
     * @param fil Filas del tablero
     * @param col Columnas del tablero
     */
    public Tablero( int fil, int col ){
        colocados = new MatrizDispersa( fil , col );
        aleatorios = new MatrizDispersa( fil , col );
    }
    
    /**
     * Inicializa todos los atributos y datos necesarios
     */
    public void init(){
        colocados.Init();
        colocados.setDefect( -1 );
        aleatorios.Init();
        aleatorios.setRandom();
    }
    
    /**
     * Retorna la matriz de elementos no posicionados
     * @return No posicionados
     */
    public MatrizDispersa getAleatorios() {
        return aleatorios;
    }
    
    /**
     * Retorna la matriz de elementos posicionados
     * @return Posicionados
     */
    public MatrizDispersa getColocados() {
        return colocados;
    }
    
    /**
     * Asigna los elementos no posicionados
     * @param aleatorios No posicionados
     */
    public void setAleatorios( MatrizDispersa aleatorios ) {
        this.aleatorios = aleatorios;
    }
    
    /**
     * Asigna los elementos posicionados
     * @param colocados Posicionados
     */
    public void setColocados( MatrizDispersa colocados ) {
        this.colocados = colocados;
    }
    
    
    /**
     * Realiza el intercambio de dos posiciones entre las dos matrices
     * de elementos, simbolizando un movimiento de pieza
     * @param origen origen
     * @param destino destino
     */
    public void mover( Point origen , Point destino){
        int temp = getAleatorios().getDato( origen.y , origen.x );
        getAleatorios().setDato( -1 , origen.y , origen.x );
        getColocados().setDato( temp , destino.y , destino.x );
    }
    
    
    
}
