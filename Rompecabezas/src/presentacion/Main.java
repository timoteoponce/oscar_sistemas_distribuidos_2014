/*
 * Main.java
 *
 * Created on 17 de abril de 2008, 02:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package presentacion;

import negocio.cliente.ControlCliente;
import negocio.servidor.ControlServidor;

/**
 *
 * @author Juan TImoteo Ponce Ortiz
 */
public class Main {

    /**
     * Creates a new instance of Main
     */
    public Main(final String servidor) {
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
                    new ControlCliente(servidor).run();
                }
            };
            hilo2.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String servidor = args == null || args.length == 0 ? "localhost" : args[0];
        new Main(servidor);
    }
}
