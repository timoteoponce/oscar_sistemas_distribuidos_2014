/*
 * ControlServidor.java
 *
 * Created on 17 de abril de 2008, 01:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import negocio.rmi.ServidorRemotoImp;

/**
 *
 * @author Juan TImoteo Ponce Ortiz
 */
public class ControlServidor{
    public static final int PUERTO_RMI          = 5555;
    public static final String OBJETO_REMOTO    = "//localhost:" + PUERTO_RMI +"/puzzleserver";
    
    private ServidorRemotoImp server;
    
    /** Creates a new instance of ControlServidor */
    public ControlServidor() {
        
    }
    
    public void iniciar() {
        try {
            
            LocateRegistry.createRegistry( PUERTO_RMI );
            ServidorRemotoImp server = new ServidorRemotoImp();
            Naming.rebind( OBJETO_REMOTO , server );
            print(" #### RompeCabezasServer : "+ OBJETO_REMOTO + " iniciado ####");
            
        } catch (RemoteException ex) {
            //ex.printStackTrace();
            print( "Ya existe un servidor iniciado ");
        }catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void detener(){
        try {
            Naming.unbind( OBJETO_REMOTO );
            //System.exit( 0 );
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param string 
     */
    private void print(String string) {
        System.out.println( string );
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        new Runnable() {
            public void run() {
                ControlServidor server = new ControlServidor();
                server.iniciar();
            }
        };
    }
    
    
    
}
