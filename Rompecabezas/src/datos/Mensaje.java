/*
 * Mensaje.java
 *
 * Created on 8 de abril de 2008, 01:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package datos;

import java.io.Serializable;

/**
 * Clase base para la comunicaci�n entre partes,
 * absolutamente todo lo que se quiera comunicar,
 * debe ser un mensaje.
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class Mensaje implements Serializable{

    public static final byte PIEZA_AGARRADA     = 0;
    public static final byte PIEZA_MOVIDA       = 1;
    public static final byte PIEZA_SOLTADA      = 2;
    public static final byte TABLERO            = 3;
    public static final byte FINALIZADO         = 4;
    public static final byte DUPLICADO          = 5;
    public static final byte LLENO              = 6;
    public static final byte NOMBRE             = 7;
    public static final byte PREFERENCIAS       = 8;
    public static final byte SET_PREFERENCIAS   = 9;
    public static final byte OK                 = 10;
    public static final byte ESPERA             = 11;
    public static final byte DESCONECTADO       = 12;
    public static final byte CONECTADO          = 13;
    public static final byte MENSAJE            = 14;
    public static final byte PUNTAJE            = 15;

    byte tipo;
    Object dato;
    
    /**
     * Crea un nuevo mensaje, con algun contenido.
     * @param dat Contenido del mensaje
     */
    public Mensaje( byte tip) {
        this.tipo = tip;
    }
    
    /**
     * Crea un nuevo mensaje, de algun tipo, con algun contenido.
     * @param tip Tipo de Mensaje
     * @param dat Contenido del mensaje
     */
    public Mensaje( byte tip , Object dat ) {       
        tipo = tip;
        dato = dat;
    }
    
    /**
     * Retorna el contenido del mensaje
     * @return Contenido del mensaje
     */
    public Object getDato() {
        return dato;
    }
    
    /**
     * Tipo de mensaje, de entre los listados al inicio
     * @return Tipo de mensaje
     */
    public byte getTipo() {
        return tipo;
    }
    
    /**
     * Asigna algun contenido al mensajes
     * @param dato Contenido del mensaje
     */
    public void setDato( Object dato ) {
        this.dato = dato;
    }
    
    /**
     * Tipo de mensaje que contiene, listados arriba
     * @param tipo Tipo de mensaje
     */
    public void setTipo( byte tipo ) {
        this.tipo = tipo;
    }
    
    /**
     * Retorna una descripcion del tipo de mensaje
     * @return Descripcion mensaje
     * @param  tipo a describir
     */
    public static String getMensaje( int tipo ){
        String str = "";
        switch (tipo){
            case PIEZA_AGARRADA:
                str = "PIEZA_AGARRADA";
                break;
            case PIEZA_MOVIDA:
                str = "PIEZA_MOVIDA";
                break;
            case PIEZA_SOLTADA:
                str = "PIEZA_SOLTADA";
                break;
            case TABLERO:
                str = "TABLERO";
                break;
            case FINALIZADO:
                str = "FINALIZADO";
                break;
            case DUPLICADO:
                str = "DUPLICADO";
                break;
            case LLENO:
                str = "LLENO";
                break;
            case NOMBRE:
                str = "NOMBRE";
                break;
            case PREFERENCIAS:
                str = "PREFERENCIAS";
                break;
            case SET_PREFERENCIAS:
                str = "SET_PREFERENCIAS";
                break;
            case OK:
                str = "OK";
                break;
            case ESPERA:
                str = "ESPERA";
                break;
            case DESCONECTADO:
                str = "DESCONECTADO";
                break;
            case CONECTADO:
                str = "CONECTADO";
                break;
            case MENSAJE:
                str = "MENSAJE";
                break;
        }
        return str;
    }
    
}
