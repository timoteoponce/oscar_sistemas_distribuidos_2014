/*
 * @(#)MatrizDispersa.java
 *
 * Created on 5 de junio de 2007, 14:06
 *
 * Copyright 2006 Juan Timoteo Ponce Ortiz, All rights reserved.
 * Use is subject to license terms.
 */

package negocio.juego;

import java.io.Serializable;


/**
 * Matriz de elementos enteros, que simboliza su funcionamiento
 * mediante la utilizacion de un arreglo simple
 */
public class MatrizDispersa implements Serializable{
    
    private byte[] V;
    
    private byte filas, colums;
    
    private byte defect;
    
    /**
     * Creates a new instance of MatrizDispersa
     */
    public MatrizDispersa() {
    }
    
    /**
     * Creates a new instance of MatrizDispersa
     * @param filas filas
     * @param columns columnas
     */
    public MatrizDispersa( int filas, int columns ) {
        this.filas = (byte) filas;
        this.colums = (byte) columns;
        defect = 0;
    }
    
    /**
     * Inicializa todas las variables
     */
    public void Init() {
        V = new byte[ this.filas * this.colums ];
    }
    
    /**
     * Asigna la cantidad de filas
     * @param filas filas
     */
    public void setFilas( int filas ) {
        this.filas = (byte) filas;
    }
    
    /**
     * Rtorna la cantidad de columnas
     * @return columnas
     */
    public byte getColums() {
        return colums;
    }
    
    /**
     * Retorna el valor por defecto de la matriz
     * @return valor por defecto
     */
    public byte getDefect() {
        return defect;
    }
    
    /**
     * Retorna la cantidad de filas
     * @return filas
     */
    public byte getFilas() {
        return filas;
    }
    
    /**
     * Asigna la cantidad de columnas
     * @param colum columnas
     */
    public void setColum( int colum ) {
        this.colums = (byte)colum;
    }
    
    /**
     * Asigna el valor por defecto de la matriz
     * @param defect valor por defecto
     */
    public void setDefect( int defect ) {
        this.defect = (byte) defect;
        for ( int i = 0 ; i < V.length ; i++ ) {
            V[ i ] = (byte)defect;
        }
    }
    
    /**
     * Asigna el contenido de una celda determinada
     * @param ele contenido
     * @param fil fila
     * @param col columna
     */
    public void setDato( int ele, int fil, int col ) {
        if (fil < filas && col < colums) {
            int pos =  (fil * colums + col);
            V[pos] = (byte)ele;
        }
    }
    
    /**
     * Retorna el valor numerico escalar de una celda
     * @param fil fila
     * @param col columna
     * @return valor escalar
     */
    public int getN( int fil , int col ){
        return (fil * colums + col);
    }
    
    /**
     * Llena la matriz con datos aleatorios
     */
    public void setRandom(){
        boolean[] marcados = new boolean[ filas*colums ];
        int num = -1;
        for ( int i = 0; i < filas; i++ ) {
            for ( int j = 0; j < colums; j++ ) {
                if( num == -1)
                    num = (int)( Math.random()*( filas*colums ) );
                else{
                    while( marcados[num] )
                        num = (int)( Math.random()*( filas*colums ) );
                }
                setDato( num , i , j);
                marcados[num] = true;
            }
        }
    }
    
    /**
     * Retorna el contenido de una celda
     * @param fil fila
     * @param col columna
     * @return contenido
     */
    public int getDato(int fil, int col) {
        if ( fil < filas && col < colums ) {
            int pos =  (fil * colums + col);
            if( pos >= 0)
                return (V[pos] != defect ? V[pos] : defect);
        }
        return -1;
    }
    
    /**
     * Retorna la cantidad de celdas que contienen un valor similar
     * @param data contenido
     * @return cantidad de repeticiones
     */
    public int getCant( byte data ){
        int i = 0;
        int cont=0;
        while( i < filas*colums ){
            if( V[i] == data )
                cont++;
            i++;
        }
        return cont;
    }
    
    /**
     * Intercambia el contenido de dos celdas
     * @param x1 fila1
     * @param y1 col1
     * @param x2 fila2
     * @param y2 col2
     */
    public void swap(int x1,int y1,int x2,int y2){
        int dat1 = getDato( x1 , y1 );
        int dat2 = getDato( x2 , y2 );
        setDato( dat2 , x1 , y1 );
        setDato( dat1 , x2 , y2 );
        System.out.println( "[" + x1 + "," + y1 + "]" + "[" + x2 + "," + y2 + "]" );
    }
    
    /**
     * Verifica si la matriz esta completa y ordenada
     * @return ordenada
     */
    public boolean ordenada(){
        int dat = -1;        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < colums; j++) {
                if(dat < 0)
                    dat = getDato( i ,j );
                else{
                    if(dat < getDato( i , j ) )
                        dat = getDato( i ,j );
                    else
                        return false;
                }
            }
        }
        return ( dat >= 0);
    }
    
    
    /**
     * Concatena el contenido de la matriz a modo texto
     * @return matriz
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        int i = 0;
        byte j = 0;
        while (i < filas * colums) {
            str.append(" ");
            str.append(V[i]);
            j++;
            if (j == colums) {
                str.append(System.getProperty("line.separator"));
                j = 0;
            }
            i++;
        }
        return str.toString();
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        MatrizDispersa mat = new MatrizDispersa( 6, 8);
        mat.Init();
        mat.setSerie();
        System.out.println( mat.toString() +" "+mat.ordenada());
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.println( mat.getN( i,j ));
            }
        }
    }
    
    void setSerie() {
        int index = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < colums; j++) {
                setDato( index , i , j);
                index++;
            }
        }
    }

    /**
     * Verifica la existencia de un elemento
     * @param i contenido
     * @return existencia
     */
    public boolean existe(int i) {
        for (int j = 0; j < V.length; j++) {
            if( V[ j ] == i)
                return true;
        }
        return false;
    }
}
