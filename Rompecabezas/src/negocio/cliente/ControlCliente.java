/*
 * controlCliente.java
 *
 * Created on 8 de abril de 2008, 02:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.cliente;


import datos.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.StringTokenizer;
import javax.swing.*;
import negocio.evento.Evento;
import negocio.evento.EventoListener;
import negocio.juego.Tablero;
import negocio.juego.FormTablero;
import negocio.rmi.ObjetoRemoto;
import negocio.rmi.ObjetoRemotoImp;
import negocio.rmi.ServidorRemoto;
import presentacion.FormCliente;
import presentacion.FormCliente.ModeloLista;
import presentacion.FormLogin;
import presentacion.FormPreferencias;
import presentacion.FormPuntaje;

/**
 *
 * @author timo
 */
public class ControlCliente implements ActionListener,EventoListener,WindowListener{
    //JFrame ventana;
    private FormTablero tableroJuego;
    private FormPreferencias dialog;
    //rmi
    private ServidorRemoto servidor;
    private String user = "";
    private String remoteServer;
    private int partida;
    //juego
    private String img;
    private int diff;
    private int buenas;
    private int malas;
    //cliente
    private FormCliente formulario;
    private boolean conectado;
    //
    private String aEnviar;
    
    /** Creates a new instance of controlCliente */
    public ControlCliente(String servidor) {
        this.remoteServer = servidor;
    }
    
    public void run() {
        formulario = new FormCliente();
        formulario.addWindowListener( this );
        formulario.initPop( this );
        formulario.getBtnCambiarImg().addActionListener( this );
        formulario.getBtnAyuda().addActionListener( this );
        formulario.getBtnSalir().addActionListener( this );
        init();
    }
    
    /**
     *
     */
    public void init(){
        try {
            partida = -1;
            buenas = 0;
            malas = 0;
            tableroJuego = new FormTablero( );
            tableroJuego.agregarEscuchador( this );
            conectado = false;
            
            servidor = (ServidorRemoto) Naming.lookup( "//"+remoteServer+":5555/puzzleserver" );
            login();
        } catch (Exception e) {
            e.printStackTrace();
            showInfoMessage( "Servidor no encontrado" );
            System.exit( 0 );
        }
    }
    
    /**
     *
     * @return
     */
    private boolean conectar(){
        try {
            ObjetoRemotoImp obj = new ObjetoRemotoImp();
            obj.agregarEscuchador( this );
            
            return servidor.conectar( user , obj );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     *
     */
    private void desconectar(){
        try {
            servidor.desconectar( user , partida );
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
    
    /**
     *
     */
    private void login(){
        try {
            String usados = servidor.getInfo( ServidorRemoto.LISTA_JUGADORES , -1 );
            print( "Lista de jugadores: " + usados );
            FormLogin dialog = new FormLogin( null , true , usados);
            int result = dialog.showDialog();
            
            if( result == dialog.OK ){
                
                user = dialog.getResult();
                if( conectar() ){
                    print("conectado");
                    conectado = true;
                    cargarJugadores();
                }/*else{
                    print( "Jugador duplicado ");
                    login();
                }*/
                dialog = null;
            }else
                System.exit( 0 );
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }
    
    
    /**
     *
     */
    private void initJuego(){
        cargarJugadores();
        formulario.getPanelJuego().setViewportView( tableroJuego );
        formulario.setSize( Toolkit.getDefaultToolkit().getScreenSize() );
        formulario.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        formulario.setVisible( true );
    }
    
    /**
     *
     */
    private void setPreferencias() {
        dialog = new FormPreferencias( null , true );
        dialog.getBtnAceptar().setActionCommand( "preferencias" );
        dialog.getBtnAceptar().addActionListener( this );
        dialog.setVisible( true );
    }
    
    /**
     *
     * @param path
     * @return
     */
    private Image getImagenTablero( String path ) {
        return getImagen( getClass().getResource( "/images/tablero/"+ path ) );
    }
    
    /**
     *
     * @param path
     * @return
     */
    private Image getImagen( URL path ){
        return new ImageIcon( path ).getImage(  );
    }
    
    
    
    /**
     *
     */
    private void cargarJugadores() {
        try {
            formulario.getPanelEquip0().removeAll();
            formulario.getPanelEquip1().removeAll();
            
            String jugadores = servidor.getInfo( ServidorRemoto.LISTA_JUGADORES , partida );
            print("Jugadores "+jugadores );
            StringTokenizer stoken = new StringTokenizer( jugadores , "," );
            String temp;
            
            int equipo;
            
            while( stoken.hasMoreTokens() ){
                temp = stoken.nextToken();
                equipo = Integer.parseInt( temp.substring( temp.indexOf( '#' )+1 , temp.length() ) );
                temp = temp.substring( 0 , temp.indexOf( '#' ) );
                
                JButton button = new JButton();
                button.setIcon( new ImageIcon( getClass().getResource("/images/ids/"+temp+"small.png") ) );
                button.addActionListener( this );
                button.setActionCommand( "users"+ temp + "#" + equipo);
                
                if( equipo % 2 == 0)
                    formulario.getPanelEquip0().add( button );
                else
                    formulario.getPanelEquip1().add( button );
            }
            
            formulario.getPanelEquip0().updateUI();
            formulario.getPanelEquip1().updateUI();
            
            //formulario.repaint();
            
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        System.gc();
    }
    
    /**
     *
     * @param remit
     * @param img
     */
    private void addMensaje( String remit , String img ){
        FormCliente.ModeloLista modelo = (ModeloLista) formulario.getListaMsg().getModel();
        CompMsg obj = new CompMsg( remit, img );
        modelo.add( obj );
        //formulario.getListaMsg().setCellRenderer( obj );
        formulario.getListaMsg().updateUI();
    }
    
    /**
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if( e.getActionCommand().equals( "preferencias" )){
                img = dialog.getImg();
                print( "La imagen es -> "+img+".png");
                tableroJuego.setImagen( getImagenTablero( img+".png" ) );
                String[] prefs;
                
                if( !dialog.getBtnEdit().isSelected() ){
                    diff = dialog.getDiff();
                    prefs = new String[]{ diff+"",img };
                }else{
                    diff = -1;
                    int fils = Integer.parseInt( dialog.getTxtFilas().getValue().toString() );
                    int cols = Integer.parseInt( dialog.getTxtColumnas().getValue().toString() );
                    prefs = new String[]{ diff+"", fils+"", cols+"",img };
                }
                
                print("Enviando las preferencias: "+user);
                servidor.procesar( user , partida ,  new Mensaje( Mensaje.SET_PREFERENCIAS , prefs ));
                dialog.dispose();
                dialog = null;
                return;
            }
            
            if( e.getSource() instanceof JButton ){
                if( e.getActionCommand().contains( "users" )){
                    aEnviar = e.getActionCommand();
                    aEnviar = aEnviar.substring( 5 , aEnviar.length() );
                    int x = ( (JButton) e.getSource() ).getLocation().x;
                    int y = ( (JButton) e.getSource() ).getLocation().y;
                    formulario.getMenuPop().show( (Component) e.getSource() , x , y );
                    
                    return;
                    
                } else if( e.getActionCommand().contains( "ops" )){
                    if( e.getActionCommand().equals( "opsCambiarImg" )){
                        if( img.contains( "0" ))
                            img = "img1";
                        else if( img.contains( "1" ))
                            img = "img2";
                        else if( img.contains( "2" ))
                            img = "img0";
                        tableroJuego.setImagen( getImagenTablero( img+".png" ) );
                        tableroJuego.crearSubImgs();
                        tableroJuego.repaint();
                        return;
                    }
                    if( e.getActionCommand().equals( "opsAyuda" )){
                        
                        return;
                    }
                    if( e.getActionCommand().equals( "opsSalir" )){
                        desconectar();
                        return;
                    }
                }
            }
            
            if( e.getSource() instanceof JMenuItem){
                if( e.getActionCommand().contains( "alegre" ) ){
                    print( "Mensaje 1" );
                    servidor.procesar( user , partida , new Mensaje( Mensaje.MENSAJE , user + "&" + aEnviar + "@alegre" ));
                }else if( e.getActionCommand().contains( "furioso" ) ){
                    print( "Mensaje 2" );
                    servidor.procesar( user , partida , new Mensaje( Mensaje.MENSAJE , user + "&" + aEnviar + "@furioso" ));
                }else if( e.getActionCommand().contains( "llorando" ) ){
                    print( "Mensaje 3" );
                    servidor.procesar( user , partida , new Mensaje( Mensaje.MENSAJE , user + "&" + aEnviar + "@llorando" ));
                }
                aEnviar = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.gc();
    }
    
    /**
     *
     * @param evt
     */
    public void eventoLanzado(Evento evt) {
        Mensaje msg = (Mensaje) evt.getMensaje();
        
        try {
            if( evt.getSource() instanceof MouseEvent){//desde el tablero de juego
                
                print("Enviado a procesar ");
                if( msg.getTipo() == Mensaje.PIEZA_MOVIDA ){
                    buenas ++ ;
                    formulario.setBuenas( buenas );
                }else if( msg.getTipo() == Mensaje.PIEZA_SOLTADA ){
                    malas ++;
                    formulario.setMalas( malas );
                }
                
                servidor.procesar( user , partida , msg );
                return;
                
            } else if(evt.getSource() instanceof ObjetoRemoto ){
                print("Recibiendo mensaje "+Mensaje.getMensaje( msg.getTipo() ) );
                
                switch( msg.getTipo() ){
                    case Mensaje.OK:{
                        servidor.procesar( user ,partida , new Mensaje( Mensaje.TABLERO ) );
                    }break;
                    case Mensaje.DUPLICADO:{
                        login();
                        break;
                    }
                    case Mensaje.NOMBRE:{
                        nombreRecibido( msg );
                        break;
                    }
                    case Mensaje.SET_PREFERENCIAS:{
                        setPreferencias();
                    }break;
                    case Mensaje.PREFERENCIAS:{
                        preferenciasRecibidas( msg );
                        break;
                    }case Mensaje.ESPERA:{
                        showInfoMessage( "Por favor espere" );
                    }break;
                    case Mensaje.TABLERO:{
                        Tablero tablero =  (Tablero) msg.getDato();
                        tableroJuego.setMatriz( tablero.getAleatorios() );
                        tableroJuego.setColocados( tablero.getColocados() );
                        initJuego();
                    }break;
                    case Mensaje.PIEZA_AGARRADA:{
                        tableroJuego.bloquear( (Point) msg.getDato() );
                    }break;
                    case Mensaje.PIEZA_SOLTADA:{
                        tableroJuego.desbloquear( (Point) msg.getDato() );
                    }break;
                    case Mensaje.PIEZA_MOVIDA:{
                        Movida mov = (Movida) msg.getDato();
                        tableroJuego.getTablero().mover( mov.getOrigen() , mov.getDestino() );
                        tableroJuego.desbloquear( mov.getOrigen() );
                    }break;
                    case Mensaje.FINALIZADO:{
                        /*String ganador = msg.getDato().toString();
                         
                        if( ganador.equals( user ))
                            showInfoMessage("Usted es el ganador "+user);
                        else{
                            showInfoMessage("El ganador fue: "+ ganador);
                        }*/
                        conectado = false;
                    }break;
                    case Mensaje.PUNTAJE:{
                        puntajeRecibido( msg );
                    }
                    case Mensaje.CONECTADO:{
                        print( "Se conecto: "+ msg.getDato().toString());
                        cargarJugadores();
                    }break;
                    case Mensaje.DESCONECTADO:{
                        print( "Se desconecto " +msg.getDato().toString() );
                        if( user.equals( msg.getDato().toString() )){
                            servidor.procesar( user , partida , new Mensaje( Mensaje.DESCONECTADO) );
                            try {
                                //System.exit( 0 );
                                this.finalize();
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }else
                            cargarJugadores();
                    }break;
                    case Mensaje.LLENO:{
                        showInfoMessage( "El servidor contiene muchas partidas, eliga otro servidor " );
                        System.exit( 0 );
                    }break;
                    case Mensaje.MENSAJE:{
                        mensajeRecibido( msg );
                    }break;
                }
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     *
     * @param msg
     * @throws java.rmi.RemoteException
     */
    private void nombreRecibido( Mensaje msg ) throws RemoteException {
        user = msg.getDato().toString();
        partida = Integer.parseInt( user.substring( user.indexOf( '@' )+1, user.length() ) );
        user = user.substring(0 , user.indexOf( '@' ) );
        formulario.getLblID().setIcon( new ImageIcon( getClass().getResource("/images/ids/"+user.substring( 0 , user.indexOf('#') )+"small.png") ) );
        print( "Mis datos, nombre: " + user + " partida =" + partida );
        servidor.procesar( user , partida , new Mensaje( Mensaje.PREFERENCIAS ) );
    }
    
    /**
     *
     * @param msg
     * @throws java.rmi.RemoteException
     */
    private void preferenciasRecibidas( Mensaje msg ) throws RemoteException {
        String[] prefs = (String[]) msg.getDato();
        diff = Integer.parseInt( prefs[ 0 ] );
        img = prefs[ 1 ];
        tableroJuego.setImagen( getImagenTablero( img+".png" ) );
        servidor.procesar( user , partida , new Mensaje( Mensaje.TABLERO ) );
    }
    
    /**
     *
     * @param msg
     */
    private void mensajeRecibido( Mensaje msg ) {
        String temp = msg.getDato().toString();
        String remit = temp;
        
        temp = temp.substring( temp.indexOf('@')+1 , temp.length() );
        remit = remit.substring( 0 ,remit.indexOf( '&' ) );
        
        print( "yo: "+user+" remitente:"+remit+" msg:"+temp);
        addMensaje( remit.substring( 0 , remit.indexOf( '#') ) , temp );
    }
    
    /**
     *
     * @param msg
     */
    private void puntajeRecibido( Mensaje msg ) {
        String[] data = (String[]) msg.getDato();
        int punt0 = Integer.parseInt( data[0] );
        int punt1 = Integer.parseInt( data[1] );
        
        int total0 = Integer.parseInt( data[2] );
        int total1 = Integer.parseInt( data[3] );
        
        int miPuntaje =  buenas - malas/2 ;
        if( user.substring( user.indexOf( '#' )+1 , user.length() ).contains( "0" ) )
            miPuntaje =  miPuntaje *100/total0;
        else
            miPuntaje =  miPuntaje *100/total1;
        
        FormPuntaje form = new FormPuntaje( formulario , true );
        form.getBtnID().setText( "" );
        form.getBtnID().setIcon( formulario.getLblID().getIcon() );
        
        form.getLblPuntaje().setText("<html><h1>" +  miPuntaje + "</h1></html>");
        
        form.getProgEquipo0().setValue( punt0 );
        form.getProgEquipo1().setValue( punt1 );
        
        
        form.addWindowListener( this );
        form.setVisible( true );
    }
    
    
    public void windowOpened(WindowEvent e) {
    }
    
    public void windowClosing(WindowEvent e) {
    }
    
    public void windowClosed(WindowEvent e) {
        if( e.getSource() instanceof FormCliente )
            desconectar();
        else if( e.getSource() instanceof FormPuntaje ){
            desconectar();
        }
    }
    
    public void windowIconified(WindowEvent e) {
    }
    
    public void windowDeiconified(WindowEvent e) {
    }
    
    public void windowActivated(WindowEvent e) {
    }
    
    public void windowDeactivated(WindowEvent e) {
    }
    
    
    /**
     *
     * @param msg
     * @return
     */
    private String showInputMessage(String msg){
        return JOptionPane.showInputDialog( formulario , msg , "Usuario");
    }
    
    /**
     *
     * @param string
     */
    private void print(String string) {
        System.out.println(string);
    }
    
    /**
     *
     * @param string
     */
    private void showInfoMessage(String string) {
        JOptionPane.showMessageDialog( formulario , string );
    }
    
    protected void finalize() throws Throwable {
        formulario.dispose();
        formulario = null;
        dialog = null;
        servidor = null;
        tableroJuego = null;
        System.gc();
    }
    
    
    
    
    public class CompMsg extends JComponent{
        /**
         *
         * @param origen
         * @param msg
         */
        public CompMsg( String origen , String msg){
            try {
                setLayout( new FlowLayout() );
                JLabel lbl1 = new JLabel();
                JLabel lbl2 = new JLabel();
                JLabel lbl3 = new JLabel();
                
                lbl1.setIcon( new ImageIcon( getClass().getResource("/images/ids/" + origen + "small.png" )) );
                add( lbl1 );
                
                lbl2.setIcon( new ImageIcon( getClass().getResource("/images/msg/next.png" ) ));
                add( lbl2 );
                
                lbl3.setIcon( new ImageIcon( getClass().getResource("/images/msg/" + msg + ".png" ) ) );
                add( lbl3 );
                setBackground( Color.WHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new ControlCliente("localhost").run();
    }
    
}
