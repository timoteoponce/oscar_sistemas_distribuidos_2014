/*
 * Tablero.java
 *
 * Created on 4 de abril de 2008, 08:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package negocio.juego;


import datos.Mensaje;
import datos.Movida;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputListener;
import negocio.evento.Evento;
import negocio.evento.EventoListener;
import negocio.evento.ObjetoEscuchable;



/**
 *
 * @author timo
 */
public class FormTablero extends JComponent implements ObjetoEscuchable{
    
    private BufferedImage imagen;
    private Pieza[] piezas;
    
    private Dimension size;
    private int filas;
    private int columnas;
    private int ancho;
    private int alto;
    private int width;
    private int height;
    
    private Tablero tablero;
    private boolean finalizado;
    
    private Point anterior;
    
    
    //para el drag
    private boolean agarrado;
    private Point posAgarrado;
    private ArrayList<Point> bloqueados;
    
    //para eventos
    private transient EventListenerList listaEscuchadores;

    /** Creates a new instance of Tablero */
    public FormTablero( Image img , int fils , int cols ) {
        MediaTracker tracker = new MediaTracker( this );
        imagen = toBufferedImage( img );
        filas = fils;
        columnas = cols;
        
        listaEscuchadores = new EventListenerList();
        tablero = new Tablero( filas , columnas );
        
        tracker.addImage( imagen , 0 );
        try {
            tracker.waitForAll();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        
        addMouseListener( new AdaptadorRaton() );
        addMouseMotionListener( new AdaptadorMotion() );
        
        
        size = new Dimension( imagen.getWidth()*2 + 40 , imagen.getHeight( ) + 40 );
        //init();
    }
    
    public FormTablero() {
        listaEscuchadores = new EventListenerList();
        
        addMouseListener( new AdaptadorRaton() );
        addMouseMotionListener( new AdaptadorMotion() );
    }
    
    public void init(){
        anterior = new Point(-1,-1);
        posAgarrado = new Point(-1,-1);
        tablero.init();
        finalizado = false;
        
        agarrado = false;
        anterior = null;
        bloqueados = new ArrayList<Point>();
        size = new Dimension( imagen.getWidth(null)*2 + 40 , imagen.getHeight(null) + 40 );
        
        setSize( size );
        crearSubImgs();
        repaint();
    }
    
    
    
    public void setImagen( Image imagen ) {
        if( imagen == null)
            print("Imagen nula");
        this.imagen = toBufferedImage( imagen );
        MediaTracker tracker = new MediaTracker( this );
        tracker.addImage( imagen , 0 );
        try {
            tracker.waitForAll();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
    
    public void setColumnas( int columnas ) {
        this.columnas = columnas;
        init();
    }
    
    public void setFilas( int filas ) {
        this.filas = filas;
    }
    
    public void setAlto( int alto ) {
        this.alto = alto;
    }
    
    public void setAncho( int ancho ) {
        this.ancho = ancho;
    }
    
    public void setColocados( MatrizDispersa colocados ) {
        tablero.setColocados( colocados );
        repaint();
    }
    
    public void setMatriz( MatrizDispersa mat ) {
        print( mat.toString() );
        filas =  mat.getFilas();
        columnas =  mat.getColums();
        tablero = new Tablero( filas , columnas );
        tablero.init();
        tablero.setAleatorios( mat );
        
        anterior = new Point( -1 , -1 );
        posAgarrado = new Point( -1 , -1 );
        agarrado = false;
        
        anterior = null;
        bloqueados = new ArrayList< Point >();
        
        finalizado = false;
        size = new Dimension( imagen.getWidth()*2 + 40 , imagen.getHeight() + 40 );
        
        setSize( size );
        crearSubImgs();
        repaint();
    }
    
    /*public boolean isAgarrado() {
        return agarrado;
    }*/
    
    public MatrizDispersa getMatriz() {
        return tablero.getColocados();
    }
    
    public Point getPosAgarrado() {
        return posAgarrado;
    }
    
    public Image getImagen() {
        return imagen;
    }
    
    public int getFilas() {
        return filas;
    }
    
    public int getColumnas() {
        return columnas;
    }
    
    public ArrayList< Point > getBloqueados() {
        return bloqueados;
    }
    
    public MatrizDispersa getColocados() {
        return tablero.getColocados();
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public int getAncho() {
        return ancho;
    }
    
    public int getAlto() {
        return alto;
    }
    
    
    public void crearSubImgs(){
        piezas = new Pieza[ filas*columnas ];
        width = imagen.getWidth( );
        height = imagen.getHeight(  );
        ancho = width / columnas;
        alto = height /filas;
        int index = 0;
        
        Area shape = new Area();
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                shape.reset();
                shape.add( new Area(new Rectangle( j*ancho , i*alto , ancho , alto ) ) );
                //shape.subtract( new Area( new Arc2D.Double( j*ancho , i*alto , ancho/2 , alto/2 , 45 , 180,1 ) ) );
                piezas[ index ] = new Pieza();
                piezas[ index ].imagen = getShapedImage( shape , imagen );
                //shape.setBounds( 0 , 0 , ancho , alto );
                //piezas[ index ].forma = shape;
                index++;
            }
        }
        System.gc();
    }
    
    private void paintShapes( Image img , int x , int y , String msg, Graphics g ){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON );
        
        g2.setColor( Color.GRAY );
        g2.drawImage( img , x , y, this);
        
        if( msg != null)
            g2.drawString( msg , x+ancho/2 , y+alto/2 );
        g2.drawRect( x, y , ancho , alto );
    }
    
    protected void paintComponent(Graphics g) {
        //super.paintComponent( g );
        try {
            //pintamos las fichas colocadas
            //pintamos el tablero de fichas
            paintPiezas( g );
            paintBloqueados( g );
            if( agarrado &&  tablero.getAleatorios().getDato( anterior.y , anterior.x ) != -1)
                g.drawImage( piezas[ tablero.getAleatorios().getDato( anterior.y , anterior.x )].imagen , posAgarrado.y , posAgarrado.x , this );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void paintPiezas( Graphics g ){
        int temp;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                temp = tablero.getColocados().getDato(i,j);
                if( temp != -1)
                    paintShapes( piezas[temp].imagen , j*ancho , i*alto  , temp+"" , g);
                else{
                    temp = tablero.getColocados().getN( i ,j );
                    paintFigura( j*ancho , i*alto , g);
                }
                
                temp = tablero.getAleatorios().getDato(i,j);
                if( agarrado ){
                    if( (anterior.x != j || anterior.y != i)  && temp != -1){
                        paintShapes( piezas[temp].imagen , j*ancho + width +10, i*alto  ,temp+"", g);
                    }
                }else if( temp != -1)
                    paintShapes( piezas[temp].imagen ,j*ancho + width +10, i*alto ,temp+"", g);
            }
        }
    }
    
    private void paintBloqueados( Graphics g ){
        Graphics2D g2d = (Graphics2D) g;
        if( bloqueados.size() > 0){
            Point tempP;
            g2d.setColor( Color.GRAY );
            int index;
            for (int i = 0; i < bloqueados.size(); i++) {
                tempP = bloqueados.get( i );
                g2d.fillRect( tempP.x*ancho + width + 10 , tempP.y*alto , ancho ,alto );
            }
        }
    }
    
    private void paintFigura( int x , int y, Graphics g ){
        g.setColor( Color.GRAY );
        g.drawRect( x , y , ancho , alto );
    }
    
    public void bloquear( Point p ){
        if( !bloqueados.contains( p ) )
            bloqueados.add( p );
        repaint();
    }
    
    
    public void desbloquear( Point p ){
        if( bloqueados.contains( p) )
            bloqueados.remove( p );
        repaint();
    }
    
    public void desbloquearTodos(){
        bloqueados.clear();
    }
    
    public Dimension getPreferredSize() {
        return size;
    }
    
    class AdaptadorRaton implements MouseInputListener{
        public void mouseClicked(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
            if( ! finalizado ){
                
                if( anterior == null )
                    anterior = new Point();
                anterior.y = e.getY() / alto;
                anterior.x = ( e.getX() - imagen.getWidth( null )-10 ) / ancho;
                
                if( (e.getX() / ancho ) > -1 && tablero.getColocados().getDato( anterior.y , e.getX() / ancho ) != -1 ){
                    anterior = null;
                    posAgarrado = null;
                    return;
                }
                
                //para dragging
                agarrado = true;
                if( posAgarrado == null )
                    posAgarrado = new Point();
                posAgarrado.y = anterior.y * alto;
                posAgarrado.x = anterior.x * ancho;
                
                print( "origen -> fil=" + anterior.y + " col=" + anterior.x );
                
                Evento evt = new Evento( e );
                evt.setMensaje( new Mensaje( Mensaje.PIEZA_AGARRADA , anterior ));
                lanzarEvento( evt );
            }else{
                anterior = null;
                posAgarrado = null;
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            if( !finalizado && anterior != null){
                Evento evt = new Evento( e );
                
                print( "destino -> fil=" + ( e.getY() / alto ) + " col=" + ( e.getX() / ancho ) );
                int fil = e.getY() / alto;
                int col = e.getX() / ancho;
                
                if(( fil < filas  && fil > -1 ) && ( col < columnas && col > -1) ){
                    
                    print( "pos inicial [ " + anterior.y + " , " + anterior.x + "]=" + tablero.getAleatorios().getDato( anterior.y , anterior.x ) );
                    print( "pos destino [ " + fil + " , " + col + "]=" + tablero.getAleatorios().getN( fil , col ) );
                    
                    if( tablero.getAleatorios().getDato( anterior.y ,anterior.x ) == tablero.getAleatorios().getN( fil , col )){
                        
                        Point destino = new Point( col , fil );
                        tablero.mover( anterior , destino );
                        
                        print( "entro" );
                        //testing
                        
                        evt.setMensaje( new Mensaje( Mensaje.PIEZA_MOVIDA , new Movida( anterior , destino ) ));
                        
                        if( tablero.getColocados().ordenada() && !tablero.getColocados().existe( -1 )){
                            print(" acabado ");
                            finalizado = true;
                        }
                        
                    }else{
                        print("Posicion invalida");
                        evt.setMensaje( new Mensaje( Mensaje.PIEZA_SOLTADA , anterior ));
                    }
                    
                    lanzarEvento( evt );
                    agarrado = false;
                    repaint();
                    return;
                }else
                    print("Posicion invalida");
                evt.setMensaje( new Mensaje( Mensaje.PIEZA_SOLTADA , anterior ));
                lanzarEvento( evt );
                repaint();
            }
        }
        
        public void mouseEntered(MouseEvent e) {        }
        
        public void mouseExited(MouseEvent e) {        }
        
        public void mouseDragged(MouseEvent e) {        }
        
        public void mouseMoved(MouseEvent e) {        }
    }
    
    class AdaptadorMotion extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent e) {
            if( posAgarrado != null){
                posAgarrado.y = e.getX();
                posAgarrado.x = e.getY();
                repaint();
            }
        }
        
    }
    
    void print(String msg){
        System.out.println( msg );
    }
    
    
    public void agregarEscuchador(EventoListener listener) {
        listaEscuchadores.add( EventoListener.class , listener );
    }
    
    public void lanzarEvento(Evento evt) {
        EventoListener[] array = listaEscuchadores.getListeners( EventoListener.class );
        for (int i = 0; i < array.length; i++) {
            array[ i ].eventoLanzado( evt );
        }
    }
    
    //para las imagenes
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        
        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        
        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);
        
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        return bimage;
    }
    
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }
        
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
    
    public BufferedImage getShapedImage(Shape forma, BufferedImage original ) {
        Rectangle rect = forma.getBounds();
        
        int x = rect.x;
        int y = rect.y;
        
        BufferedImage bi = new BufferedImage( original.getWidth() , original.getHeight() , BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //print( rect.toString() );
        
        g2d.setClip( forma );
        //g2d.translate( x , y );
        g2d.setColor( new Color( 255,255,255,0) );
        g2d.fillRect( 0, 0 , rect.width ,rect.height );
        g2d.drawImage( original , 0 , 0 ,null);
        
        g2d.setColor( Color.GRAY );
        g2d.draw( forma );
        
        g2d.dispose();
        
        return bi.getSubimage( x , y , rect.width , rect.height );
    }
    
    public static void main(String[] args) {
        JFrame frm = new JFrame();
        FormTablero tab = new FormTablero( Toolkit.getDefaultToolkit().getImage("ardilla.jpg") ,4,3);
        tab.init();
        frm.getContentPane().add( tab  );
        frm.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frm.setSize( tab.getSize() );
        frm.setVisible( true );
    }
    
}
