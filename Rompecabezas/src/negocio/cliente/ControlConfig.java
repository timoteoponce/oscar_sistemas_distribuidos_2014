/*
 * ControlConfig.java
 *
 * Created on 23 de abril de 2008, 22:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import negocio.FileUtil;
import negocio.rmi.ServidorRemoto;
import presentacion.FormConfig;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class ControlConfig implements ActionListener{
    FormConfig config;

    /** Creates a new instance of ControlConfig */
    public ControlConfig() {
        config = new FormConfig();
        config.getBtnAceptar().addActionListener( this );
        config.getBtnCancelar().addActionListener( this );
        init();
        config.setVisible( true );
        config.setLocationRelativeTo( null );
    }
    
    public void init(){
        StringTokenizer stoken = new StringTokenizer( FileUtil.abrirTexto( "config.conf" ) );
        while ( !stoken.nextToken().equals( "server" ) );
        config.getTxtServidor().setText( stoken.nextToken() );
    }

    /**
     * 
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        if( e.getSource().equals( config.getBtnAceptar() )){

            if ( verificar( config.getTxtServidor().getText() ) ){
                FileUtil.guardarTexto( "config.conf" , "server  "+config.getTxtServidor().getText() );
                JOptionPane.showMessageDialog( config , "Dirección valida", "Informacion",JOptionPane.INFORMATION_MESSAGE );
                System.exit( 0 );
            }else
                JOptionPane.showMessageDialog( config , "Dirección invalida", "Advertencia",JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        if( e.getSource().equals( config.getBtnCancelar() )){
            System.exit( 0 );
            return;
        }
    }

    /**
     * 
     * @param string 
     * @return 
     */
    private boolean verificar(String string) {
        try {
            ServidorRemoto servidor = (ServidorRemoto) Naming.lookup( "//"+string+":5555/puzzleserver" );
            return true;
        } catch (Exception e) {
            
        }
        return false;
    }
    
    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        new ControlConfig();
    }
    
}
