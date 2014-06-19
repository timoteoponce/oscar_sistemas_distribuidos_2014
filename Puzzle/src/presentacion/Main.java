/*
 * Main.java
 *
 * Created on 17 de abril de 2008, 02:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package presentacion;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import negocio.cliente.ControlCliente;
import negocio.servidor.ControlServidor;

/**
 *
 * @author Juan TImoteo Ponce Ortiz
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        try {
            Runnable hilo1 = new Runnable() {
                public void run() {
                    ControlServidor server = new ControlServidor();
                    server.iniciar();
                }
            };
            hilo1.run();
            
            Runnable hilo2 = new Runnable() {
                public void run() {
                    ControlCliente cliente = new ControlCliente();
                }
            };
            hilo2.run();
        } catch (Exception e) {
			e.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new NimRODLookAndFeel() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new Main();
    }
    
}
