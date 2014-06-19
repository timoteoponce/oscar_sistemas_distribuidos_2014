/*
 * Sesion.java
 *
 * Created on 9 de abril de 2008, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.servidor;

import datos.*;
import datos.DatosJugador;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import negocio.juego.MatrizDispersa;
import negocio.juego.Tablero;
import negocio.juego.FormTablero;
import negocio.rmi.ObjetoRemoto;

/**
 * Estructura básica de una sesión, las sesiones
 * contienen datos referentes a las partidas,
 * cantidad de usuarios, tablero, ...
 * @author Juan Timoteo Ponce Ortiz
 */
public class Partida extends Observable{
    
    private int UID;
    private int MAX_JUGADORESxEQUIPO ;
    
    public static final int DIFICULTAD_FACIL    = 0;
    public static final int DIFICULTAD_NORMAL   = 1;
    public static final int DIFICULTAD_DIFICIL  = 2;
    public static final int DIFICULTAD_PERSONALIZADA  = -1;
    
    public static final int DESBLOQUEADO        = 3;
    
    private Hashtable< String , DatosJugador > jugadores;
    private Tablero tablero;
    private int dificultad;
    private boolean bloqueado;
    //
    private int puntajeEquipo0;
    private int puntajeEquipo1;
    private int totalEquipo0;
    private int totalEquipo1;
    //
    private String img;
    private ArrayList<String> enEspera;
    
    
    
    
    /**
     * Creates a new instance of Sesion
     * @param diff
     * @param filas
     * @param columnas
     */
    public Partida( int diff , int filas , int columnas) {
        
        jugadores = new Hashtable< String , DatosJugador >();
        enEspera = new ArrayList<String>();
        UID = this.hashCode();
        
        setDificultad( diff ,filas , columnas );
        //init();
    }
    
    /**
     * Inicializa los valores de la clase
     */
    public void init(){
        tablero.init();
        //jugadores.clear();
        bloqueado = true;
        img = "";
        puntajeEquipo0 = -1;
        puntajeEquipo1 = -1;
    }
    
    /**
     *
     * @param bloqueado
     */
    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
        setChanged();
        notifyObservers( DESBLOQUEADO );
    }
    
    /**
     *
     * @return
     */
    public boolean isBloqueado() {
        return bloqueado;
    }
    
    /**
     *
     * @return
     */
    public String getImg() {
        return img;
    }
    
    /**
     *
     * @return
     */
    public int getUID() {
        return UID;
    }
    
    
    
    /**
     *
     * @param img
     */
    public void setImg(String img) {
        this.img = img;
    }
    
    /**
     *
     * @return
     */
    public ArrayList<String> getEnEspera() {
        return enEspera;
    }
    
    /**
     *
     * @return
     */
    public int getDificultad() {
        return dificultad;
    }
    
    /**
     *
     * @param diff
     * @param filas
     * @param columnas
     */
    public void setDificultad( int diff , int filas , int columnas) {
        dificultad = diff;
        if( dificultad != DIFICULTAD_PERSONALIZADA ){
            switch ( dificultad ){
                case 0:
                    MAX_JUGADORESxEQUIPO = 3*3/3;
                    tablero = new Tablero( 3, 3);
                    break;
                case 1:
                    MAX_JUGADORESxEQUIPO = 3*4/3;
                    tablero = new Tablero( 3, 4);
                    break;
                case 2:
                    MAX_JUGADORESxEQUIPO = 4*4/3;
                    tablero = new Tablero( 4, 4);
                    break;
            }
        }else{
            MAX_JUGADORESxEQUIPO = filas * columnas / 3;
            tablero = new Tablero( filas , columnas );
        }
        init();
    }
    
    /**
     *
     * @param user
     * Nombre jugador
     * @param obj
     * Objeto remoto del jugador
     * @return
     * Si el jugador se pudo agregar
     */
    public synchronized String agregarJugador(String user,ObjetoRemoto obj){
        if( isCompleta() )
            return "LLENO";
        
        user = user+"#"+ ( jugadores.size() );
        if( existe( user ) )
            return "DUPLICADO";
        
        DatosJugador dat = new DatosJugador( obj );
        dat.setEquipo( jugadores.size() % 2 );
        jugadores.put( user , dat );
        
        return user;
    }
    
    /**
     *
     * @return
     */
    public boolean isCompleta(){
        return ( jugadores.size() == MAX_JUGADORESxEQUIPO );
    }
    
    /**
     *
     * @param user
     * Nombre jugador
     * @return
     * Si el jugador esta registrado
     */
    public boolean existe(String user) {
        //String key = user.substring( 0 , user.indexOf('#') );
        //System.out.println("Existira? : "+key);
        return jugadores.containsKey( user );
    }
    
    /**
     *
     * @param user
     * Nombre del jugador
     */
    public void eliminarJugador(String user){
        print( "Partida: Eliminando jugador " + user );
        if( user.indexOf( '#' ) >= 0)
            jugadores.remove( user );        
    }
    
    /**
     *
     * @return
     * Lista de jugadores y sus datos
     */
    public Hashtable getJugadores() {
        return jugadores;
    }
    
    /**
     *
     * @return
     * Cantidad de jugadores registrados
     */
    public int getCantJugadores(){
        return jugadores.size();
    }
    
    /**
     *
     * @param user
     * Nombre jugador
     * @return
     * Datos del jugador
     */
    public DatosJugador getJugador(String user){
        //System.out.println ( getListaJugadores () );
        return ( jugadores.get( user ));
    }
    
    /**
     *
     * @param user
     * Nombre jugador
     * @return
     * Objeto remoto del jugador
     */
    public ObjetoRemoto getObjeto(String user){
        DatosJugador tmp =  jugadores.get( user );
        if( tmp != null)
            return tmp.getObjeto();
        
        return null;
    }
    
    /**
     *
     * @return
     * Tablero de la partida
     */
    public Tablero getTablero() {
        return tablero;
    }
    
    /**
     *
     * @return
     */
    public String getListaJugadores(){
        if( jugadores.size() > 0){
            Enumeration en = jugadores.keys();
            StringBuffer str = new StringBuffer();
            while( en.hasMoreElements() )
                str.append( en.nextElement().toString()+"," );
            return str.toString();
        }
        return "";
    }
    
    
    
    /**
     *
     * @return
     */
    public boolean tableroCompletado(){
        return ( tablero.getColocados().ordenada() && !tablero.getColocados().existe( -1 ));
    }
    
    /**
     *
     * @param user
     * @param origen
     * @param destino
     */
    public void moverPieza( String user , Point origen, Point destino ) {
        tablero.mover( origen , destino );
        DatosJugador temp = getJugador( user );
        temp.setMovidasBuenas( temp.getMovidasBuenas() + 1 );
        //print( user + ":" + temp.toString() );
    }
    
    /**
     *
     * @param user
     */
    public void movidaMala( String user ) {
        DatosJugador temp = getJugador( user );
        temp.setMovidasMalas( temp.getMovidasMalas() - 1 );
        //print( user + ":" + temp.toString() );
    }
    
    /**
     *
     * @param msg
     * @param usr
     */
    public void enviarMensaje( Mensaje msg , String usr){
        if( existe( usr ) ){
            ObjetoRemoto obj = ( (DatosJugador) jugadores.get( usr ) ).getObjeto();
            new HiloNotificador(  obj , msg );
        }else
            print( "No existe usuario : "+usr );
        //setChanged ();
        //notifyObservers ( MENSAJE_ENVIADO );
    }
    
    /**
     *
     * @param msg
     * @param excepcion
     */
    public void difundirMensaje( Mensaje msg, String excepcion){
        Enumeration en = getJugadores().keys();
        String user;
        while( en.hasMoreElements() ){
            user = (String) en.nextElement();
            if( !excepcion.isEmpty() ){
                if( !user.equalsIgnoreCase( excepcion ) )
                    enviarMensaje( msg , user );
            }else
                enviarMensaje( msg , user );
        }
        
    }
    
    public void difundirPuntajes() {
        calcularPuntajes();
        Mensaje msg = new Mensaje( Mensaje.PUNTAJE , null );
        DatosJugador temp;
        String[] data = new String[ 4 ];
        
        Enumeration en = getJugadores().keys();
        String user;
        while( en.hasMoreElements() ){
            user = (String) en.nextElement();
            temp = jugadores.get( user );
            data[ 0 ] = puntajeEquipo0 + "";
            data[ 1 ] = puntajeEquipo1 + "";
            data[ 2 ] = totalEquipo0 + "";
            data[ 3 ] = totalEquipo1 + "";
            msg.setDato( data );
            enviarMensaje( msg , user );
        }
    }
    
    private void calcularPuntajes(){
        int buenasEquip0 = 0;
        int buenasEquip1 = 0;
        int malasEquip0  = 0;
        int malasEquip1  = 0;
        
        DatosJugador temp;
        Enumeration en = getJugadores().keys();
        String user;
        while( en.hasMoreElements() ){
            user = (String) en.nextElement();
            temp = jugadores.get( user );
            if( temp.getEquipo() == 0 ){
                buenasEquip0 = buenasEquip0 + temp.getMovidasBuenas();
                malasEquip0 = malasEquip0 + temp.getMovidasMalas();
            }else{
                buenasEquip1 = buenasEquip1 + temp.getMovidasBuenas();
                malasEquip1 = malasEquip1 + temp.getMovidasMalas();
            }
        }
        temp = null;
        buenasEquip0 =  buenasEquip0 - malasEquip0/2 ;
        buenasEquip1 =  buenasEquip1 - malasEquip1/2 ;
        
        totalEquipo0 = buenasEquip0;
        totalEquipo1 = buenasEquip1;
        
        puntajeEquipo0 = (100 / ( buenasEquip0 + buenasEquip1 ) ) * buenasEquip0;
        puntajeEquipo1 = (100 / ( buenasEquip0 + buenasEquip1 ) ) * buenasEquip1;
    }
    
    
    
    /**
     *
     * @param string
     */
    private void print(String string) {
        System.out.println( string );
    }
    
    
}
