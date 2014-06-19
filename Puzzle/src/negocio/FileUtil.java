package negocio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Clase de utilidad que contiene las funciones
 * necesarias para manipular archivos de texto.
 */
public class FileUtil {

    /**
     * Retorna el contenido de un archivo, dada una ruta previa
     * @param path Ruta del archivo
     * @return Contenido del archivo
     */
    public static String abrirTexto( String path ) {
        try {
            BufferedReader reader = new BufferedReader( new FileReader( path ) );
            StringBuffer bufferTexto = new StringBuffer();
            String linea = reader.readLine();

            while ( linea != null ) {
                bufferTexto.append( linea );
                bufferTexto.append( System.getProperty( "line.separator" ) );
                linea = reader.readLine();
            }
            reader.close();
            return bufferTexto.toString();
        } catch ( Exception e ) {
            crearTexto( path );
            return abrirTexto( path );
        }
    }

    /**
     * Almacena una secuencia de caracteres dentro de un archivo dado.
     * @param path Ruta del archivo
     * @param contenido Nuevo contenido del archivo
     */
    public static void guardarTexto( String path, String contenido ) {
        try {
            FileWriter file = new FileWriter( path );
            BufferedWriter Br = new BufferedWriter( file );
            Br.write( contenido );
            Br.flush();
            Br.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    /**
     * Crea un fichero
     * @param path Ruta del archivo
     */
    public static void crearTexto( String path ) {
        try {
            FileWriter file = new FileWriter( path );
            BufferedWriter Br = new BufferedWriter( file );
            Br.flush();
            Br.close();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
