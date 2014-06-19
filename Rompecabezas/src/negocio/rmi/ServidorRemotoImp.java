/*
 * manejadorTableroImp.java
 *
 * Created on 23 de octubre de 2007, 04:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package negocio.rmi;




import datos.Mensaje;
import datos.Movida;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import negocio.servidor.HiloNotificador;
import negocio.servidor.Partida;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import negocio.juego.Tablero;

/**
 * Clase que implementa la interfaz {@link ServidorRemoto}, manipula
 * todos lo relacionado a el juego y sus partidas
 * @author Juan Timoteo Ponce Ortiz
 */
public class ServidorRemotoImp extends UnicastRemoteObject implements ServidorRemoto,Observer{
    
    private Partida sesion;
    private ArrayList< Partida > partidas;
    
    /**
     * Creates a new instance of manejadorTableroImp
     * @throws java.rmi.RemoteException
     */
    public ServidorRemotoImp() throws RemoteException {
        super();
        partidas = new ArrayList< Partida >();
        crearPartida();
    }
    
    /**
     * Proceso que servira para establecer conexiones entre los clientes
     * @param user nombre usuario
     * @param obj objetoescuchable del cliente
     * @throws java.rmi.RemoteException
     * @return estado
     */
    public boolean conectar( String user, ObjetoRemoto obj ) {
        sesion = getPartida( -1 );
        
        print( " usuario a conectar: " + user );
        String result = sesion.agregarJugador( user , obj );
        
        if( result.contains( "LLENO" ) ){
            enviarMensaje( new Mensaje( Mensaje.LLENO ) , obj );
        }else if( result.contains( "DUPLICADO" ) ){
            enviarMensaje( new Mensaje( Mensaje.DUPLICADO ) , obj );
        }else{
            enviarMensaje( new Mensaje( Mensaje.NOMBRE , result + '@' + sesion.getUID() ) , obj );
            sesion.difundirMensaje( new Mensaje( Mensaje.CONECTADO , result ) , result );
            print( "todo OK: " + result );
            return true;
        }
        
        return false;
    }
    
    /**
     * Proceso de desconexion de clientes
     * @param user nombre usuario
     * @param partida identificador de partida
     * @throws java.rmi.RemoteException
     */
    public void desconectar( String user , int partida ) {
        sesion = getPartida( partida );
        print( "Se quiere desconectar ->" + user + " de la partida :" + partida );
        Mensaje msg = new Mensaje( Mensaje.DESCONECTADO , user );
        sesion.enviarMensaje( msg , user );
        
    }
    
    /**
     * Procesa un mensaje enviado por algun cliente
     * @param user nombre usuario
     * @param partida identificador de partida
     * @param msg Mensaje a procesar
     * @throws java.rmi.RemoteException
     */
    public void procesar( String user , int partida , Mensaje msg  ) {
        sesion = getPartida( partida );
        
        print("procesando -> "+user);
        
        switch (msg.getTipo()){
            case Mensaje.SET_PREFERENCIAS:{
                print( "Me llegaron las preferencias:" + user );
                String[] array = ( String[] ) msg.getDato();
                int diff = Integer.parseInt( array[ 0 ] );
                
                if( diff != -1){
                    sesion.setDificultad( diff  , -1 , -1);
                    sesion.setImg( array[ 1 ] );
                }else{
                    int fils = Integer.parseInt( array[ 1 ].toString() );
                    int cols = Integer.parseInt( array[ 2 ].toString() );
                    sesion.setDificultad( diff  , fils , cols);
                    sesion.setImg( array[ 3 ] );
                }
                sesion.setBloqueado( false );
                print( "Enviando OK ");
                sesion.enviarMensaje( new Mensaje( Mensaje.OK , null) , user );
                
            }break;
            case Mensaje.PREFERENCIAS:{
                print( "Me piden las preferencias" );
                enviarPreferencias( sesion , user , msg );
            }break;
            case Mensaje.PIEZA_AGARRADA:{
                //difundir( msg , user );
                print( "Se agarro una pieza " );
                sesion.difundirMensaje( msg , user );
            }break;
            case Mensaje.PIEZA_SOLTADA:{
                //difundir( msg , user );
                sesion.movidaMala( user );
                sesion.difundirMensaje( msg , user );
            }break;
            case Mensaje.PIEZA_MOVIDA:{
                //difundir( msg , user );
                sesion.difundirMensaje( msg , user );
                moverPieza( sesion , user , msg );
            }break;
            case Mensaje.TABLERO:{
                print( "Me piden el tablero ");
                enviarTablero( sesion , user , msg );
            }break;
            case Mensaje.DESCONECTADO:{
                sesion.eliminarJugador( user );
                msg.setTipo( Mensaje.DESCONECTADO );
                msg.setDato( user );
                sesion.difundirMensaje( msg , user );
                
                if( sesion.getJugadores().size() == 0 ){
                    partidas.remove( sesion );
                    sesion = null;
                    print( "Eliminando sesion finalizada" );
                }
                System.gc();
            }break;
            case Mensaje.MENSAJE:{
                print( "Enviando un mensaje" );
                String destino = msg.getDato().toString();
                sesion.enviarMensaje( msg , destino.substring( destino.indexOf( '&' ) + 1 , destino.indexOf( '@' ) ) );
            }break;
            
        }
        //}
    }
    
    /**
     *
     * @return
     */
    private int crearPartida(){
        if( partidas.size() < 10 ){
            Partida part = new Partida( 0 , -1 , -1);
            partidas.add( part );
            part.addObserver( this );
            return partidas.indexOf( part );
        }
        return -1;
    }
    
    /**
     *
     * @param index
     * @return
     */
    private Partida getPartida( int index ){
        if( index == -1 ){
            if( partidas.size() > 0)
                return partidas.get( partidas.size() - 1 );
            else{
                crearPartida();
                return getPartida( - 1 );
            }
        }
        for ( Partida elem : partidas ) {
            if( elem.getUID() == index )
                return elem;
        }
        return null;
    }
    
    /*private void difundir( Partida partida , Mensaje msg ){
        Enumeration en = partida.getJugadores().keys();
        String user;
     
        while(en.hasMoreElements()){
            user = (String) en.nextElement();
            enviarMensaje( msg , partida.getJugador( user ).getObjeto() );
        }
     
        if( partida.tableroCompletado() )
            partida.init();
    }*/
    
    /*private void difundir(Mensaje msg,String excepcion ){
        Enumeration en = sesion.getJugadores().keys();
        String user;
        while(en.hasMoreElements()){
            user = (String) en.nextElement();
            if(!user.equalsIgnoreCase( excepcion ))
                enviarMensaje( msg , sesion.getJugador( user ).getObjeto() );
            else
                print("obviando "+user);
        }
    }*/
    
    /**
     *
     * @param msg
     * @param obj
     */
    private void enviarMensaje( Mensaje msg, ObjetoRemoto obj ){
        new HiloNotificador( obj , msg );
    }
    
    /**
     *
     * @param str
     */
    private void print( String str ) {
        System.out.println( str );
    }
    
    /**
     *
     * @param partida
     * @param user
     * @param msg
     */
    private void moverPieza( Partida partida , String user , Mensaje msg ) {
        Movida mov = ( Movida ) msg.getDato();
        partida.moverPieza( user , mov.getOrigen() ,mov.getDestino() );
        
        if( partida.tableroCompletado() ){
            msg.setDato( user );
            msg.setTipo( Mensaje.FINALIZADO );
            partida.difundirMensaje( msg , "" );
            partida.difundirPuntajes();
        }
    }
    
    /**
     * Retorna informacion de algun tipo a cualquiera que lo pida
     * @param tipoInfo tipo de informacion
     * @param partida identificador de partida
     * @throws java.rmi.RemoteException
     * @return informacion requerida
     */
    public String getInfo( int tipoInfo , int partida ) throws RemoteException {
        sesion = getPartida( partida );
        
        if( sesion.isCompleta() &&  partida == -1 ){
            print( "Partida llena , creando nueva partida " );
            crearPartida();
            sesion = getPartida( -1 );
        }
        switch ( tipoInfo){
            case ServidorRemoto.LISTA_JUGADORES:{
                return sesion.getListaJugadores();
            }
        }
        return "";
    }
    
    
    /**
     *
     * @param o
     * @param arg
     */
    public void update( Observable o, Object arg ) {
        sesion = ( Partida ) o;
        int tipo = Integer.parseInt( arg.toString() );
        
        switch (tipo){
            case Partida.DESBLOQUEADO:{
                for (String elem : sesion.getEnEspera() ) {
                    print( "Enviando el tablero ");
                    String[] temp = new String[]{ sesion.getDificultad()+"" , sesion.getImg() };
                    sesion.enviarMensaje( new Mensaje( Mensaje.PREFERENCIAS , temp ) , elem );
                }
                sesion.getEnEspera().clear();
            }break;
        }
    }
    
    /**
     *
     * @param partida
     * @param user
     * @param msg
     */
    private void enviarPreferencias( Partida partida , String user, Mensaje msg ) {
        if( partida.isBloqueado() && partida.getImg().isEmpty() ){
            partida.enviarMensaje( new Mensaje( Mensaje.SET_PREFERENCIAS ) , user );
            return ;
        }else if( !partida.isBloqueado() ){
            String[] temp = new String[]{ partida.getDificultad()+"",sesion.getImg()};
            partida.enviarMensaje( new Mensaje( Mensaje.PREFERENCIAS , temp ) , user);
        }else{
            partida.getEnEspera().add( user );
            partida.enviarMensaje( new Mensaje( Mensaje.ESPERA ) , user );
        }
    }
    
    /**
     *
     * @param partida
     * @param user
     * @param msg
     */
    private void enviarTablero( Partida partida , String user, Mensaje msg ) {
        if( !partida.isBloqueado()){
            Tablero tablero = partida.getTablero();
            partida.enviarMensaje( new Mensaje( Mensaje.TABLERO , tablero ) , user );
        }else{
            partida.getEnEspera().add( user );
            partida.enviarMensaje( new Mensaje( Mensaje.ESPERA ) , user );
        }
    }
    
    
    
}
