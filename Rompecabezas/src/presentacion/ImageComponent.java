package presentacion;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.MediaTracker;

import javax.swing.JComponent;

/**
 * Componente manipulador de imagenes
 * @author Juan Timoteo Ponce Ortiz
 */
public class ImageComponent extends JComponent{
    Image imagen;
    Dimension size;
    
    /**
     * Constructor
     * @param imagen imagen
     */
    public ImageComponent(Image imagen) {
        this.imagen=imagen;
        MediaTracker mt=new MediaTracker(this);
        mt.addImage(imagen,0);
        try  {
            mt.waitForAll();    
        } catch (Exception ex)  {
            ex.printStackTrace();
        }
        size=new Dimension(imagen.getWidth(null),imagen.getHeight(null));
        setSize(size);
    }
    
    /**
     * 
     * @param g 
     */
    public void paint(Graphics g){
        g.drawImage(imagen,0,0,this);
    }
    
    /**
     * 
     * @return 
     */
    public Dimension getPreferredSize(){
        return size;
    }
}
