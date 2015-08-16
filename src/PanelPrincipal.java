/**
 * @author nikki
 *
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.cdg.swing.BasicPlayerPanel;

public class PanelPrincipal extends JPanel
	implements KeyListener, MyPlayerListener  {
    /**
     * Esta sera la imagen de fondo
    **/
    private  ImageIcon imgFondo;
    /**
     * Ancho y alto de la ventana
    **/
    private  int width, height;
    /**
     * Directorio raiz donde se buscara toda la musica
    **/
    public  String DIR_ROOT;
    /**
     * Directorio del genero actual
    **/
    private String dirGenero;
    /**
     * Directorio de artista actual
    **/
    private String dirArtista;
    /**
     * especifica en que lugar estamos
     * 0 - root
     * 1 - genero
     * 2 - artista
    **/
    private int currentDirID;
    /**
     * Constantes para identificar el lugar donde estamos
    **/
    public  final int ID_ROOT = 0;
    public  final int ID_GENERO = 1;
    public  final int ID_ARTISTA = 2;
    /**
     * caracter separador de directorios
    **/
    public  final char DIR_SEP = File.separatorChar;
    /**
     * Lista con el contenido general
    **/
    private JList<String> lista;
    /**
     * contenido de la lista actual
    **/
    private Vector<String> listData;
    private JScrollPane scrollPaneLista;
    /**
     * 
    **/
    /**
     * Nombre que tendran todas las imagenes de portada (leidas desde el disco, NO CAMBIAR)
    **/
    private  final String COVER_NAME = "portada.jpg";
    private  final String COVER_NAME_GIF = "portada.gif";

    /**
     * nombres de las imagenes usadas internamente para visualizar la interfaz grafica
    **/
    private  final String FONDO_NAME = "resources/fondo";
    private  final String MARCO_NAME = "aF.class";
    private  final String GENERO_DEFAULT_NAME = "bC.class";
    private  final String ARTISTA_DEFAULT_NAME = "Bc.class";
    private  final String VOLUMEN_NAME = "vL.class";
    private  final String VOLUMEN2_NAME = "Vl.class";
    private  final String USB_NAME = "ee.class";
    private  final String USB1_NAME = "eD.class";

    private  final String ABC_ON_NAME = "ao.class";
    private  final String ABC_OFF_NAME = "af.class";
    
    
    
    /**
     * Pila para mantener el cursor en la su posicion anterior al regresar al anterior directorio
    **/
    private Stack<Integer> pilaSelectedIndex;
    /**
     * Contenedores de las imagenes de artista y genero
    **/
    private JLabel lblGeneroIcon, lblArtistaIcon, lblCurrentPlayingIcon,
    		lblMarcoIcon, lblVolumenIcon, lblVolumenText, lblExternalIcon, lblABCIcon;
    /**
     * Panel donde se muestra la reproduccion actual (karaoke)
     */
    private BasicPlayerPanel panelKaraoke;
    private JPanel panelVideo;
    private Comparator<String> comparatorIgnoreCase;
    /**
     * Posiciones absolutas para ubicar los componentes
    **/
    // icono genero
    private  final int ICON_GENERO_POS_X = 68;
    private  final int ICON_GENERO_POS_Y = 55;
    private  final int ICON_GENERO_SIZE_X = 265 - ICON_GENERO_POS_X;
    private  final int ICON_GENERO_SIZE_Y = 226 - ICON_GENERO_POS_Y;
    // icono artista
    private  final int ICON_ARTISTA_POS_X = 755;
    private  final int ICON_ARTISTA_POS_Y = 55;
    private  final int ICON_ARTISTA_SIZE_X = 960 - ICON_ARTISTA_POS_X;
    private  final int ICON_ARTISTA_SIZE_Y = 228 - ICON_ARTISTA_POS_Y;
    // icono reproduciendo
    private  final int ICON_PLAYING_POS_X = 413;
    private  final int ICON_PLAYING_POS_Y = 76;
    private  final int ICON_PLAYING_SIZE_X = 206;
    private  final int ICON_PLAYING_SIZE_Y = 179;
    // icono de volumen
    private  final int ICON_VOL_POS_X = 31;
    private  final int ICON_VOL_POS_Y = 526;
    private  final int ICON_VOL_SIZE_X = 177 - ICON_VOL_POS_X - 40;
    private  final int ICON_VOL_SIZE_Y = 614 - ICON_VOL_POS_Y + 10;
    // icono de memoria externa
    private  final int ICON_USB_POS_X = 14;
    private  final int ICON_USB_POS_Y = 336;
    private  final int ICON_USB_SIZE_X = 177 - ICON_USB_POS_X - 15;
    private  final int ICON_USB_SIZE_Y = 495 - ICON_USB_POS_Y - 30;
    // icono del modo busqueda
    private  final int ICON_ABC_POS_X = 821;
    private  final int ICON_ABC_POS_Y = 362;
    private  final int ICON_ABC_SIZE_X = 1017 - ICON_ABC_POS_X;
    private  final int ICON_ABC_SIZE_Y = 542 - ICON_ABC_POS_Y;
    
    
    // posicion de la lista de elementos
    private  final int LISTA_POS_X = 250;
    private  final int LISTA_POS_Y = 338;
    private  final int LISTA_SIZE_X = 790 - LISTA_POS_X;
    private  final int LISTA_SIZE_Y = 603 - LISTA_POS_Y;
    // label de reproduccion actual
    private  final int PLAYING_POS_X = 20;
    private  final int PLAYING_POS_Y = 650;
    private  final int PLAYING_SIZE_X = 280;
    private  final int PLAYING_SIZE_Y = 30;
    // label de reproduccion en cola
    private  final int PLAYING_NEXT_POS_X = 335;
    private  final int PLAYING_NEXT_POS_Y = 650;
    private  final int PLAYING_NEXT_SIZE_X = 670;
    private  final int PLAYING_NEXT_SIZE_Y = 30;
    
    /**
     * Apuntador a el objeto encargado de controlar la reproduccion
    **/
    private MyPlayer player;
    /**
     * label para reproduccion en cola
     */
    private String StrPlayingNextDefault  =  "          No Hay Mas Pistas Agregadas!!!       ".toUpperCase();
    private JLabelRotary lblPlayingNext = new JLabelRotary( 350, StrPlayingNextDefault );
    /**
     * Fuente usada para los labels
     */
    private Font mFont;
    
    
    /**
     * label para el archivo en reproduccion
     */
    private JLabel lblSongName;
    /**
     * tabla de simbolos donde se guardaran las imagenes ya cargadas en memoria
     */
   // private Map<File,ImageIcon> hashImages;
    /**
     * Iconos para mostrar en gui
     */
    private ImageIcon iconArtistDefault, iconGeneroDefault, iconMarco, 
    	iconVolumen, iconVolumen2, iconUsb, iconUsb1, iconABCON, iconABCOFF;
    /**
     * clase encargada de otorgar una calilficacion despues de cantar
     */
    private Calificador calificador;
    

    private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    
    
    private ExternalDevice externalDevice;
    private final String EXTERNAL_DEVICE_NAME = "1Cliente USB";
    
    /**
     * Constructor
     * @throws Exception 
    **/
    public PanelPrincipal(String root) throws Exception {
    	// save root directory
        DIR_ROOT = root;
        
        // collection for containing images already loaded
    //    hashImages = Collections.synchronizedMap( new HashMap<File,ImageIcon>() );
        
        // comparator for sorting with case insensitive
        comparatorIgnoreCase = new ComparatorIgnoreCase();
        
        // remove layout to use absolute bounds in components
        setLayout(null);
        
        // load menu icons
        try	{
        	imgFondo = new ImageIcon( getClass().getResource( FONDO_NAME ) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen de fondo");
        	System.exit(0);
        }

        try	{
        	iconGeneroDefault = new ImageIcon( getClass().getResource( GENERO_DEFAULT_NAME ) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para genero");
        	System.exit(0);
        }

        try	{
        	iconArtistDefault = new ImageIcon( getClass().getResource( ARTISTA_DEFAULT_NAME ) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para artista");
        	System.exit(0);
        }
        

        try	{
        	iconMarco = new ImageIcon( getClass().getResource(MARCO_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada como marco" );
        	System.exit(0);
        }

        try	{
        	iconVolumen = new ImageIcon( getClass().getResource(VOLUMEN_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar volumen" );
        	System.exit(0);
        }

        try	{
        	iconVolumen2 = new ImageIcon( getClass().getResource(VOLUMEN2_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar volumen en silencio" );
        	System.exit(0);
        }

        try	{
        	iconUsb = new ImageIcon( getClass().getResource(USB_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar memoria externa" );
        	System.exit(0);
        }


        try	{
        	iconUsb1 = new ImageIcon( getClass().getResource(USB1_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar memoria externa" );
        	System.exit(0);
        }

        try	{
        	iconABCOFF = new ImageIcon( getClass().getResource(ABC_OFF_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar modo ABC" );
        	System.exit(0);
        }
        try	{
        	iconABCON = new ImageIcon( getClass().getResource(ABC_ON_NAME) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen usada para mostrar modo ABC" );
        	System.exit(0);
        }

        
        try	{
        	mFont = Font.createFont( Font.TRUETYPE_FONT, getClass().getResourceAsStream("fuente.otf") );
        	mFont = mFont.deriveFont( Font.PLAIN, 30 );
        }	catch (Exception e)	{
        	System.out.println("No se encontro la fuente, se usara la default");
        	mFont = new Font( Font.MONOSPACED, Font.PLAIN, 30);
        }
        // set dimension like background icon
        width = imgFondo.getIconWidth();
        height = imgFondo.getIconHeight();
        setSize( width, height );
        debug( " width: " + width );
        debug( " height: " + height );
        
        // vector for containing data in current directory
        listData = new Vector<String>();
        
        // stack for saving selected derectories
        pilaSelectedIndex = new Stack<Integer>();
        
        // player
        player = new MyPlayer();
        
        // listen player events
        player.setPlayerListener(this);
        
        // current menus are empty
        dirArtista = new String();
        dirGenero = new String();
        
        // current directry is root
        currentDirID = ID_ROOT;
        
        // karaoke player panel
        panelKaraoke = player.getPanel();
       
        
        // video player panel
        panelVideo = player.getVideoPanel();

        // calificador app
        calificador = new Calificador();
        
        // create label to view genero icon
        lblGeneroIcon = KaraokeUtils.createJLabel(ICON_GENERO_POS_X, ICON_GENERO_POS_Y, ICON_GENERO_SIZE_X, ICON_GENERO_SIZE_Y);
        lblGeneroIcon.setFocusable(false);
        lblGeneroIcon.setOpaque(false);
        lblGeneroIcon.setBackground( BACKGROUND_COLOR );

        // create label to view artista icon
        lblArtistaIcon = KaraokeUtils.createJLabel(ICON_ARTISTA_POS_X, ICON_ARTISTA_POS_Y, ICON_ARTISTA_SIZE_X, ICON_ARTISTA_SIZE_Y);
        lblArtistaIcon.setFocusable(false);
        lblArtistaIcon.setOpaque(false);
        lblArtistaIcon.setBackground( BACKGROUND_COLOR );

        // create label to view artista icon
        lblMarcoIcon = new JLabel();
        lblMarcoIcon.setFocusable(false);
        lblMarcoIcon.setOpaque(false);
        lblMarcoIcon.setIcon( iconMarco );

        // create label to view volumen icon
        lblVolumenIcon = KaraokeUtils.createJLabel(ICON_VOL_POS_X - 2, ICON_VOL_POS_Y, ICON_VOL_SIZE_X, ICON_VOL_SIZE_Y);
        lblVolumenIcon.setFocusable(false);
        lblVolumenIcon.setOpaque(false);
        iconVolumen = KaraokeUtils.boundsIconLikeComponent( lblVolumenIcon, iconVolumen );
        iconVolumen2 = KaraokeUtils.boundsIconLikeComponent( lblVolumenIcon, iconVolumen2 );
        lblVolumenIcon.setIcon( iconVolumen );
        lblVolumenIcon.setBackground( BACKGROUND_COLOR );
        // create label to view volume percent
        lblVolumenText = KaraokeUtils.createJLabel(ICON_VOL_POS_X + 25, ICON_VOL_POS_Y - 20, 70, 50);
        lblVolumenText.setFocusable(false);
        lblVolumenText.setOpaque(false);
		lblVolumenText.setForeground( Color.BLUE );
		lblVolumenText.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 22) );

        // create label to view external icon
        lblExternalIcon = KaraokeUtils.createJLabel(ICON_USB_POS_X, ICON_USB_POS_Y, ICON_USB_SIZE_X, ICON_USB_SIZE_Y);
        lblExternalIcon.setFocusable(false);
        lblExternalIcon.setOpaque(false);
        iconUsb = KaraokeUtils.boundsIconLikeComponent( lblExternalIcon, iconUsb );
        iconUsb1 = KaraokeUtils.boundsIconLikeComponent( lblExternalIcon, iconUsb1 );
        lblExternalIcon.setIcon( iconUsb1 );
		

        // create label to view ABC Mode icon
        lblABCIcon = KaraokeUtils.createJLabel(ICON_ABC_POS_X, ICON_ABC_POS_Y, ICON_ABC_SIZE_X, ICON_ABC_SIZE_Y);
        lblABCIcon.setFocusable(false);
        lblABCIcon.setOpaque(false);
        iconABCOFF = KaraokeUtils.boundsIconLikeComponent( lblABCIcon, iconABCOFF );
        iconABCON = KaraokeUtils.boundsIconLikeComponent( lblABCIcon, iconABCON );
        lblABCIcon.setIcon( iconUsb1 );
        
        
        // create label to view current song icon
        lblCurrentPlayingIcon = KaraokeUtils.createJLabel(ICON_PLAYING_POS_X, ICON_PLAYING_POS_Y, ICON_PLAYING_SIZE_X, ICON_PLAYING_SIZE_Y);
        lblCurrentPlayingIcon.setFocusable(false);
        lblCurrentPlayingIcon.setOpaque(false);
        lblCurrentPlayingIcon.setBackground( BACKGROUND_COLOR );

        // create label to view current song name
        lblSongName = KaraokeUtils.createJLabel( PLAYING_POS_X, PLAYING_POS_Y, PLAYING_SIZE_X, PLAYING_SIZE_Y );
        lblSongName.setFocusable(false);
        lblSongName.setOpaque(false);
        lblSongName.setHorizontalAlignment( SwingConstants.CENTER );
        lblSongName.setBackground( BACKGROUND_COLOR );
        lblSongName.setFont(mFont);

        // create label to view songs on queue
        lblPlayingNext.setBounds( PLAYING_NEXT_POS_X, PLAYING_NEXT_POS_Y, PLAYING_NEXT_SIZE_X, PLAYING_NEXT_SIZE_Y );
		lblPlayingNext.setForeground(Color.RED);
        lblPlayingNext.setHorizontalAlignment( SwingConstants.CENTER );
        lblPlayingNext.setFocusable(false);
        lblPlayingNext.setOpaque(false);
        lblPlayingNext.setBackground( BACKGROUND_COLOR );
        lblPlayingNext.setFont(mFont);

        // create JList to lists items
        lista = new JList<String>();
        lista.setFocusable(false);
        lista.setFont(mFont);
        lista.setBackground( BACKGROUND_COLOR );
        lista.setCellRenderer( new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground( Color.BLUE );
                    c.setForeground( Color.WHITE );
                    c.setFont( c.getFont().deriveFont(Font.PLAIN, 48) );
                }
                return c;
            }
       } );
        
        // create Scroll Pane to handle scrolls in lista
        scrollPaneLista = new JScrollPane(lista);
        scrollPaneLista.setFocusable(false);
        scrollPaneLista.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneLista.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneLista.setLocation(LISTA_POS_X, LISTA_POS_Y);
        scrollPaneLista.setSize(LISTA_SIZE_X, LISTA_SIZE_Y);

        // listen key events
        this.addKeyListener(this);
        
        // request focus to handle key events
		this.setFocusable(true);
		this.requestFocusInWindow();
		

        externalDevice = new ExternalDevice( root, new ActionListener() {
        	public synchronized void actionPerformed(ActionEvent e)	{
    			if	( ExternalDevice.CMD_CONNECTED.equals( e.getActionCommand() ) )	{
    				lblExternalIcon.setIcon( iconUsb );
    			}	else	{
    				lblExternalIcon.setIcon( iconUsb1 );
    			}
    			if	( currentDirID == ID_ROOT )	{
    				System.out.println(" menu list should be updated");
    				try	{
    				updateList();
    				updateIcons();
    				}	catch (Exception ex)	{
    				}
    			}
    			
    			requestFocus();
    			requestFocusInWindow();
        	}
        });
        
		// load first time list content and icons
        updateList();
    	updateIcons();
        
        // hide mouse pointer
        setCursor( KaraokeUtils.getBlankCursor() );
        
        /*
        // timer for updating volume and ABC mode
        configUpdater = new Timer(200, new ActionListener() {
        	public void actionPerformed(ActionEvent e)	{
        		
        		// update volume
        		String vol = KaraokeUtils.getVolPercent();
        		if	( vol.equals("0%") )	{
        			lblVolumenIcon.setIcon(iconVolumen2);
        			vol = "";
        		}	else	{
        			lblVolumenIcon.setIcon(iconVolumen);
        		}
        		lblVolumenText.setText( vol );
        		
        		// update ABC mode
        		
        		if	( isSearchModeEnabled() )	{
        			lblABCIcon.setIcon( iconABCON );
        		}	else	{
        			lblABCIcon.setIcon( iconABCOFF );
        		}
        	}
        });
        configUpdater.start();
        */

        
        //
        updateABCMode();
        updateVolumeLabel( KaraokeUtils.getVolPercent() );
        // show menu when starts
        showMenuGui();
        
        add(panelVideo);

		lblSongName.setText( "Nada en reproduccion actual".toUpperCase() );	// update current song label
		lblSongName.setForeground(Color.RED);
    }
    
    private void updateABCMode()	{
		if	( isSearchModeEnabled() )	{
			lblABCIcon.setIcon( iconABCON );
		}	else	{
			lblABCIcon.setIcon( iconABCOFF );
		}
    }
    
    private void updateVolumeLabel(String vol)	{
		// update volume
		if	( vol.equals("0%") )	{
			lblVolumenIcon.setIcon(iconVolumen2);
			vol = "";
		}	else	{
			lblVolumenIcon.setIcon(iconVolumen);
		}
		lblVolumenText.setText( vol );
    }
    
    /**
     * Muestra en lblPlayingNext el texto de las pistas en la cola de reproduccion
     */
    public void updatePlayingTextNext(){
    	String name = player.getNextSongs();
    	if ( name == null )	{
    		lblPlayingNext.setForeground(Color.RED);
    		name = StrPlayingNextDefault;
    	}
    	else 
    		lblPlayingNext.setForeground(Color.BLACK);
    	
    	lblPlayingNext.setText(name.toUpperCase().toUpperCase());
    }
    
    /**
     * Draw a background image in panel
    **/
    public void paintComponent(Graphics g)    {
        g.drawImage( imgFondo.getImage(), 0, 0, width, height, null);
        setOpaque(false);
        super.paintComponent(g);
    }

    /**
     * go to the parent directory (if is possible)
    **/
    private void back() {
        switch  (currentDirID)  {
            case ID_GENERO:
            	// if we are in genero directory, go to root directory
	            currentDirID = ID_ROOT;
            break;
            
            case ID_ARTISTA:
            	// if we are in artista directory, go to genero directory
                currentDirID = ID_GENERO;
            break;
            
            default:
            return;
        }
        
        updateList();
        
        // restore selected index from stack
        int index;
        if ( pilaSelectedIndex.isEmpty() ) {
            index = 0;
        }   else    {
            index = pilaSelectedIndex.pop();
        }
        
        selectIndexFromList(index);

    	updateIcons();

        // if search mode is enabled, update search
        if	( isSearchModeEnabled() )	{
    		search = new SearchByFirstChar(listData);
    		search.selectChar( listData.get(index).charAt(0) );
        }

    }
    /**
     * open directory or play selected file
    **/
    private void forward() {
        int index = lista.getSelectedIndex();
        
        switch  (currentDirID)  {
        
        case ID_ROOT:
        	// if we are in root directory, go to genero directory
            currentDirID = ID_GENERO;
            dirGenero = listData.elementAt( index );
        break;
        
        case ID_GENERO:
        	// if we are in genero directory, go to artista directory
            currentDirID = ID_ARTISTA;
            dirArtista = listData.elementAt( index );
        break;
        
        case ID_ARTISTA:
        	// if we are in artista directory, play selected file
            String s = getWorkingirectory();
            for (String ext : SUPPORTED_FILES)	{
            	File f = new File( s + 
                    DIR_SEP + listData.elementAt( lista.getSelectedIndex() ) + ext
            	);
            	if	 ( f.exists() )	{
            		debug( " file found; " + f.getAbsolutePath() );
                    player.addToPlaylist( f.getAbsolutePath() );
            	}
            }
            // affter play the file, go to next item (to avoid playing same track several times )
            lista.ensureIndexIsVisible(lista.getSelectedIndex());
            selectIndexFromList(  (lista.getSelectedIndex() + 1) % listData.size() );

    		updatePlayingTextNext(  );		// update queue songs label
            return;
           
        default:    return;
        }
        
        pilaSelectedIndex.push(index);	// add selected item to the start, to recovery when we go back
        updateList();
    	updateIcons();

        // if search mode is enabled, update search
        if	( isSearchModeEnabled() )	{
    		search = new SearchByFirstChar(listData);
        }
    }
    
    /**
     * update icons in menu gui
     */
    private void updateIcons()  {
        if (lista.getSelectedIndex() < 0) {
            return;
        }
        /*
         * cargar imagenes solo al entrar a genero o artista 
         * lo comentado es para cargar al iluminar genero o artista
        */
        File f = new File("");
        if	( currentDirID == ID_ROOT || currentDirID == ID_GENERO )	{
        	f = new File( getWorkingirectory() + DIR_SEP + listData.get( lista.getSelectedIndex() ) + DIR_SEP + COVER_NAME_GIF );	
        	if	( !f.exists() )
        		f = new File( getWorkingirectory() + DIR_SEP + listData.get( lista.getSelectedIndex() ) + DIR_SEP + COVER_NAME );
        }
        
        debug(" get icon from: " + f);
        String path = f.getAbsolutePath();
        
        ImageIcon icon = KaraokeUtils.loadImageIcon( path );
        switch (currentDirID)   {
        case ID_ROOT:
        	// no redimensionar para aumentar rendimiento
            // icon = KaraokeUtils.boundsIconLikeComponent( lblGeneroIcon, icon );
            if	( icon == null )
            	icon = iconGeneroDefault;
            lblGeneroIcon.setIcon(icon);
            lblArtistaIcon.setIcon( null );	// set white icon to artista
        break;
        
        case ID_GENERO:
        	// no redimensionar para aumentar rendimiento
            // icon = KaraokeUtils.boundsIconLikeComponent( lblArtistaIcon, icon );
            if	( icon == null )
            	icon = iconArtistDefault;
            lblArtistaIcon.setIcon(icon);
        break;
        }
        
        
        // update marco position
        switch (currentDirID)   {
    
        case ID_ROOT:
        	// no redimensionar para aumentar rendimiento
            // icon = KaraokeUtils.boundsIconLikeComponent( lblGeneroIcon, icon );
            if	( icon == null )
            	icon = iconGeneroDefault;
            lblGeneroIcon.setIcon(icon);
            lblArtistaIcon.setIcon( null );	// set white icon to artista
            lblMarcoIcon.setBounds(17, 6, 322-21, 266-2);
        break;
        
        case ID_GENERO:
        	// no redimensionar para aumentar rendimiento
            // icon = KaraokeUtils.boundsIconLikeComponent( lblArtistaIcon, icon );
            if	( icon == null )
            	icon = iconArtistDefault;
            lblArtistaIcon.setIcon(icon);
            lblMarcoIcon.setBounds(710, 5, 1013-713, 267-5);
        break;
        
        }
    }
    /**
     * update JList content according current directory info
    **/
    private final String [] SUPPORTED_FILES = { ".mp3", ".avi", ".mpg", ".mp4", ".wmv", ".mkv" };
    private void updateList() {
    	debug( "updating list" );
        File dir = new File( getWorkingirectory() );
        debug(" loading dir: " + dir);
        lista.removeAll();
        // update list from content in current directory
        
        listData.clear();
        switch ( currentDirID )	{
        
        case ID_ROOT:
        	// agregar solo directorios
            for ( File f  :   listFiles(dir) )    {
            	if	( f.isDirectory() )	{
                    listData.add( f.getName() );
                }
            }
            if ( externalDevice.hasExternalDevice() )	{
            	listData.add( 0, EXTERNAL_DEVICE_NAME );
            	debug("external added to list");
            }
        break;
        case ID_GENERO:
        	// agregar solo directorios
            for ( File f  :   listFiles(dir) )    {
            	if	( f.isDirectory() )	{
                    listData.add( f.getName() );
                }
            }
        break;
        
        case ID_ARTISTA:
        	// agregar archivos con extension valida
            for ( String s  :   dir.list() )	{
            	if	( KaraokeUtils.isSupportedFile(s, SUPPORTED_FILES) )	{
                    listData.add( KaraokeUtils.removeExtension(s) );
                }
            }
        break;
        }
        
        // sort data
        // no se requiere ordenar pues la lista ya debe estar ordenada
        // Collections.sort(listData, comparatorIgnoreCase);
        
        //
        debug( " " + listData.size() + " elements were added" );
        lista.setListData(listData);
        selectIndexFromList(0);
        
        lista.updateUI();
        
        debug( " dir loaded" );
    }

    /**
     * get a String of working directory
    **/
    private String getWorkingirectory()  {
        StringBuilder currentDir = new StringBuilder( getRootDir() );
        if ( currentDirID > ID_ROOT ) {
            currentDir.append(DIR_SEP);
        	currentDir.append( getGeneroDir() );
        }
        if ( currentDirID > ID_GENERO ) {
            currentDir.append(DIR_SEP);
            currentDir.append(dirArtista);
        }
        return  currentDir.toString();
    }
    private String getGeneroDir()	{
    	if	( getRootDir().startsWith(DIR_ROOT) )	{
    		return	dirGenero;
    	}
		return	externalDevice.getName();
    }
    private String getRootDir()	{
    	if	( currentDirID != ID_ROOT )	{
        	if	( dirGenero.equals(EXTERNAL_DEVICE_NAME) && externalDevice.hasExternalDevice() )	{
        		return	externalDevice.getRootDir();
        	}
    	}
		return	DIR_ROOT;
    }
    /**
     * checks if search mode is enabled
     * @return true is search mode is enabled
    **/
    private boolean isSearchModeEnabled()	{
    	return	(screenMode & SCREEN_MODE_SEARCH) != 0;
    }
    /**
     * enable / disable search mode
    **/
    private synchronized void switchSearchMode()	{
    	screenMode ^= SCREEN_MODE_SEARCH;
    	if	( isSearchModeEnabled() )	{
    		// search was enabled
    		search = new SearchByFirstChar(listData);
    		search.selectChar( listData.get( lista.getSelectedIndex() ).charAt(0) );
    		debug( "search enabled");
    	}	else	{
    		// search was disabled
    		debug( "search disabled");
    	}
    }

    /**
     * select index in lista and ensure it's visible
     */
    private void selectIndexFromList(int index)	{
		lista.setSelectedIndex( index );
		lista.ensureIndexIsVisible(index);
    }
    
    /**
     * checks if full screen mode is enabled
     * @return true is window is in full screen
    **/
    private boolean isFullScreenMode()	{
    	return	(
    		screenMode & (SCREEN_MODE_VIDEO | SCREEN_MODE_KARAOKE | SCREEN_MODE_EVALUATION)
    	)	!= 0 ;
    }
    /**
     * checks if is evaluating people
     * @return true is window is in full screen
    **/
    private boolean isEvaluating()	{
		return	(screenMode & SCREEN_MODE_EVALUATION) != 0;
    }

    

    /**
     ****************************************************
     * GUIs
     ****************************************************
    **/

    // modos en que estamos trabajando actualmente
    private  final int SCREEN_MODE_MENU = 1;
    private  final int SCREEN_MODE_VIDEO = 2;
    private  final int SCREEN_MODE_SEARCH = 4;
    private  final int SCREEN_MODE_EVALUATION = 8;
    private  final int SCREEN_MODE_KARAOKE = 16;
    
    // modo en el que esta trabajando el sistema
    private int screenMode = SCREEN_MODE_MENU;
    
    private SearchByFirstChar search;
    
    private void removeComponents()	{
    	remove(lblMarcoIcon);
    	remove(lblGeneroIcon);
    	remove(lblArtistaIcon);
    	remove(lblVolumenIcon);
    	remove(lblVolumenText);
    	remove(lblExternalIcon);

    	remove(panelKaraoke);
    	remove(lblCurrentPlayingIcon);

    	remove(lblSongName);
    	remove(lblPlayingNext);
    	remove(scrollPaneLista);
    	
    	remove(lblABCIcon);

        panelVideo.setBounds(0,0,0,0);
    }
    
    /**
     * Mostrar el menu en la pantalla
     */
    private void showMenuGui()	throws Exception	{
    	removeComponents();
    	// add icons
    	add(lblMarcoIcon);
        add(lblGeneroIcon);
        add(lblArtistaIcon);
        add(lblVolumenIcon);
        add(lblVolumenText);
        add(lblExternalIcon);
        add(lblABCIcon);

        panelKaraoke.setBounds(ICON_PLAYING_POS_X, ICON_PLAYING_POS_Y, ICON_PLAYING_SIZE_X, ICON_PLAYING_SIZE_Y);
        panelKaraoke.setFocusable(false);

        panelVideo.setFocusable(false);
        
        if	( player.isPlayingVideo() )	{
            panelVideo.setBounds(ICON_PLAYING_POS_X, ICON_PLAYING_POS_Y, ICON_PLAYING_SIZE_X, ICON_PLAYING_SIZE_Y);
            validate();
        }
        else if	( panelKaraoke.isPlayingCdg() )	{
            add(panelKaraoke);
        }
        else	{
            add( lblCurrentPlayingIcon );
        }

        
        add( lblSongName );
        add( lblPlayingNext );
        
        add(scrollPaneLista);
        
        screenMode = SCREEN_MODE_MENU;
        
        repaint();
        debug("   screen: " + screenMode);
        
        
        // check if we are in a valid computer
		byte [] arr = { 42, -97, 9, -74, 80, -94, 9, 70, -114, 14, 1, -9, -53, -31, 111, 57, -126, -89, -103, 6, 36, -118, 59, -50, 102, -58, 40, -42, 79, -32, -128, -84, -36, 114, -22, 112, 35, -70, 66, 110, 87, -60, -78, -109, 123, 10, 5, -51, -98, -105, 100, -29, -24, -71, 47, -81, 54, -65, 28, 109, -77, 94, -3, -86, -20, 65, -2, -87, 31, -125, -90, 28, 120, 127, 27, 89, -96, 74, 18, 74, 78, 88, -45, 92, -69, -126, -71, -34, 120, 115, 126, 50, 49, 14, 27, 74, -105, 48, -91, 119, -90, -42, -25, -126, -51, -99, 2, -114, 37, -84, -119, 47, -88, -18, -32, 51, -49, -54, 25, 22, 119, 56, -30, 10, -3, 16, -7, 29, -25 };
		byte [] key = new byte [ arr.length - 2 ];
		System.arraycopy(arr, 1, key, 0, key.length);

	//	InputStream cipherStream = new FileInputStream("msg");
		InputStream cipherStream = this.getClass().getResourceAsStream( KaraokeUtils.decipher( new int [] {128, 149, 73, 126, 135, 124, 142, 142} ) );
		ByteArrayOutputStream plainStream = new ByteArrayOutputStream();
		javax.crypto.spec.DESKeySpec dks = new javax.crypto.spec.DESKeySpec(key);
		javax.crypto.SecretKeyFactory skf = javax.crypto.SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey desKey = skf.generateSecret(dks);
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, desKey);
		javax.crypto.CipherOutputStream cos = new javax.crypto.CipherOutputStream(plainStream, cipher);
		
		byte[] bytes = new byte[1024];
		int numBytes;
		while ((numBytes = cipherStream.read(bytes)) != -1) {
			cos.write(bytes, 0, numBytes);
		}
		cos.flush();
		cos.close();
		cipherStream.close();
		byte [] msg = plainStream.toByteArray();
		
		// read serial from computer

		StringBuilder sb = new StringBuilder();
		

		final String PATH = KaraokeUtils.decipher( new int [] {74, 142, 148, 142, 74, 126, 135, 124, 142, 142, 74, 137, 128, 143, 74} );
		File root = new File(PATH);
		TreeSet<String> set = new TreeSet<String>();
		// read all folders in PATH whose have a file name as 'address'
		File [] folders = listFiles(root);
		// if root have not at least one archive or folder, finish

		for (File folder : folders)	{
			// iterate over all files and folders into root
			if	( !folder.isDirectory() )
				continue;
			//
			File address = new File( folder.getAbsolutePath() + "/" + KaraokeUtils.decipher( new int [] {124, 127, 127, 141, 128, 142, 142} ) );
			if	( !address.exists() )	continue;

			Scanner in = new Scanner(address);
			StringTokenizer st = new StringTokenizer( in.nextLine(), ":" );
			String s = "";
			while	( st.hasMoreTokens() )
				s += st.nextToken();
			set.add( s );
			in.close();
		}
		
		for ( String s : set.descendingSet() )
			sb.insert( sb.length() >> 1, s );
		
		byte [] src = sb.toString().getBytes();
		if	( !Arrays.equals(msg, src) )	{
			src[0] /= (byte) 0;
		}
		
        
    }
    /**
     * Mostrar la lirica de la pista actual en pantalla
     */
    private void showKaraokeGui()	{
    	removeComponents();
    	
        panelKaraoke.setBounds( 0, 0, this.getWidth(), this.getHeight() );
    	
        add( panelKaraoke );
        
        screenMode = SCREEN_MODE_KARAOKE;
        
        validate();
        debug("   screen: " + screenMode);
    }
    /**
     * Mostrar video en pantalla
     */
    private void showVideoGui()	{
    	removeComponents();
        
    	panelVideo.setBounds( 0, 0, this.getWidth(), this.getHeight() );
        
    	screenMode = SCREEN_MODE_VIDEO;
        
        validate();
        debug("   screen: video fullscreen");
    }

    private void showCalificationGui() throws Exception	{
    //	removeComponents();
    	this.removeAll();
    	
        screenMode = SCREEN_MODE_EVALUATION;
        calificador.showInPanel( this, new Rectangle( 0, 0, this.getWidth(), this.getHeight() ) );
        //
        showMenuGui();
        
        add(panelVideo);	// restore video panel, it fix video repaint bug
        panelVideo.setBounds(0,0,0,0);
		// fix the gray square after show calification, in lblCurrentPlayingIcon
        lblCurrentPlayingIcon.setIcon(null);
        add( lblCurrentPlayingIcon );
    }
    

    /**
     ****************************************************
     * KeyListener Events
     ****************************************************
    **/
    
    /**
     * called when a char was typed, unused for this app
    **/
    public void keyTyped(KeyEvent ke) {
    	ke.consume();
    }

    
    /**
     * called when a key was pressed, if keep pressed, multiple calls to this method would be
    **/
    public void keyPressed(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        debug("keypressed: " + keyCode);
        

        if	( keyCode == KeyboardConfig.KEY_VOLUME_UP )	{
        	// increase volume
        	new SwingWorker<String, Void>() {

				@Override
				protected String doInBackground() throws Exception {
		        	KaraokeUtils.volUp();
					return KaraokeUtils.getVolPercent();
				}
				protected void done()	{
					try {
						String value = get();
						updateVolumeLabel(value);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.execute();
            return;
        }
        if	( keyCode == KeyboardConfig.KEY_VOLUME_DOWN )	{
        	// decrease volume

        	new SwingWorker<String, Void>() {

				@Override
				protected String doInBackground() throws Exception {
		        	KaraokeUtils.volDown();
					return KaraokeUtils.getVolPercent();
				}
				protected void done()	{
					try {
						String value = get();
						updateVolumeLabel(value);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
        		
			}.execute();
            return;
        }

        // evaluation can not be canceled
        if	( isEvaluating() )	{
        	debug( " key pressed, exit cause is evaluating" );
        	return;
        }
        
        if	( keyCode == KeyboardConfig.KEY_CANCEL )	{
        	// stop the current file
        	player.remove();
        	return;
        }
        
        if	( isFullScreenMode() )	{
        	debug( " key pressed, exit cause is full screen" );
            // only volume and cancel keys work in full screen mode
        	return;
        }
        

        if	( keyCode == KeyboardConfig.KEY_LEFT )	{
        	// go back
            back();
        	return;
        }
        
        // is current list is empty, we have nothing to do
        if	( listData.size() == 0 )	{
        	return;
        }
        
        if	( keyCode == KeyboardConfig.KEY_RIGHT )	{
        	// forward
            forward();
        	return;
        }
        
        
        if	( keyCode == KeyboardConfig.KEY_UP )	{
        	int index;
        	if	( isSearchModeEnabled() )	{
        		index = search.prev();
        	}	else	{
                index = (listData.size() + lista.getSelectedIndex() - 1) % listData.size();
        	}
    		selectIndexFromList(index);
        	return;
        }
        
        if	( keyCode == KeyboardConfig.KEY_DOWN )	{
        	int index;
        	if	( isSearchModeEnabled() )	{
        		index = search.next();
        	}	else	{
                index = (lista.getSelectedIndex() + 1) % listData.size();
        	}
    		selectIndexFromList(index);
        	return;
        }
        ke.consume();
    }
    
    
    /**
     * called when a key was released
    **/
    public void keyReleased(KeyEvent ke) {
        int keyCode = ke.getKeyCode();
        debug("keyreleased");

        // evaluation can not be minimized
        if	( isEvaluating() )	{
        	debug( " key released, exit cause is evaluating" );
        	return;
        }
        
        if	( keyCode == KeyboardConfig.KEY_MINIMIZE )	{
        	// minimize / restore screen ( works when a cdg or video is playing )
        	if	( isFullScreenMode() )	{
        		debug("trying to minimize");
        		try {
					showMenuGui();
				} catch (Exception e) {
				}
        	}	else if ( panelKaraoke.isPlayingCdg() )	{
        		showKaraokeGui();
        	}	else if ( player.isPlayingVideo() )	{
        		debug("trying to maximize");
        		showVideoGui();
        	}
        	return;
        }
        
        if	( isFullScreenMode() )	{
        	debug( " key released, exit cause is full screen" );
        	// for full screen modes, only minimize key is working
        	return;
        }
        
        if	( keyCode == KeyboardConfig.KEY_SEARCH )	{
            // enable / disable search mode

        	new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {
		        	switchSearchMode();
		        	return	null;
				}
				protected void done()	{
					updateABCMode();
				}
			}.execute();
        	return;
        }
        
        // load new icons only when the move is stopped
        if	( keyCode == KeyboardConfig.KEY_DOWN || keyCode == KeyboardConfig.KEY_UP )	{
        	updateIcons();
        	return;
        }
        ke.consume();
    }
    /**
     ****************************************************
     * Events from MyPlayer
     ****************************************************
    **/

    /**
     * Called when MyPlayer begins playing a file
     * 
     * Here we gonna update the current gui on top, set menu or full screen content when needed
    **/
	public synchronized void playerStarted(MyPlayerEvent e) {
		debug( " player started, type: " + e.getFileType() );

		lblSongName.setText( player.getCurrentSongName().toUpperCase() );	// update current song label
		lblSongName.setForeground(Color.BLACK);
		updatePlayingTextNext();		// update queue songs label
		
		if	( e.isMp3() )	{
			// a mp3 begins, show menu and load an icon for the current track
			try {
				showMenuGui();
			} catch (Exception e1) {
			}
			
			// load icon from current file
	    	File f = e.getFile();
	    	Image img = null;
	    	if ( f != null )	{
	            img = KaraokeUtils.getMP3AlbumArt( f.getAbsolutePath() );
	    	}
	        ImageIcon icon = null;
	        if	( img == null )	{
	        	// if file doesn't have image, set icon like artista icon
	        	icon = KaraokeUtils.loadImageIcon( f.getParentFile().getAbsolutePath() + DIR_SEP + COVER_NAME );
            	// no redimensionar para aumentar rendimiento
	            //	if	( icon != null )
	        		// icon = KaraokeUtils.boundsIconLikeComponent(lblCurrentPlayingIcon, icon);
	        }	else	{
	        	icon = KaraokeUtils.boundsIconLikeComponent(lblCurrentPlayingIcon, new ImageIcon(img) );
	        }
	        // set the current icon
	        lblCurrentPlayingIcon.setIcon(icon);
			return;
		}
		
		if	( e.isCdg() )	{
			// a mp3 containing liryc starts, show liryc in full screen
			showKaraokeGui();
			return;
		}
		
		if	( e.isVideo() )	{
			// a video starts, show video in full screen
			System.out.println( "video Started, showing video gui");
			showVideoGui();
		}
	}
    /**
     * Called when MyPlayer ends playing a file
    **/
	public synchronized void playerEnded(MyPlayerEvent e) {
		debug( " player ended, type: " + e.getFileType() );
		
		lblSongName.setText( "Nada en reproduccion actual".toUpperCase() );	// update current song label
		lblSongName.setForeground(Color.RED);
		updatePlayingTextNext();			// update queue songs label
		lblCurrentPlayingIcon.setIcon(null);						// remove current track icon
		
		if	( e.isMp3() )	{
			// al terminar de reproducir un mp3
		}
		
		if	( e.isCdg() )	{
			// if was playing a mp3 with liryc
			if	( e.playerCanceled() )
				try {
					showMenuGui();
				} catch (Exception e1) {
				}
			else
				try {
					showCalificationGui();
				} catch (Exception e1) {
				}
			
			return;
		}
		
		if	( e.isVideo() )	{
			// if was playing a video with liryc
			debug( " video if from karaoke: "  + KaraokeUtils.isFileFromKaraokeFolder( e.getFile() )  );
			if	( e.playerCanceled() || !KaraokeUtils.isFileFromKaraokeFolder( e.getFile() ) )	{
				// if was canceled or it don't came from karaoke files, show menu
				try {
					showMenuGui();
				} catch (Exception e1) {
				}
				// fix the gray square affter play a video in lblCurrentPlayingIcon
		        panelVideo.setBounds(0,0,0,0);
	            add( lblCurrentPlayingIcon );
			} else
				try {
					showCalificationGui();
				} catch (Exception e1) {
				}
			
			return;
		}
		
	}
	
	/**
	 * Funciones auxiliares
	**/

	// ordenar primero por directorios, despues por archivos, no es case sensitive
	final private Comparator<File> CMP_BY_NAME_NO_CASE = new Comparator<File>() {
		
		public int compare(File a, File b)	{
			if	( a.isDirectory() ^ b.isDirectory() )
				return	a.isDirectory() ? -1 : 1;
			return	a.getName().toLowerCase().compareTo( b.getName().toLowerCase() );
		}
	};
	private File[] listFiles(File dir)	{
		
		// checar si existe el archivo list.txt
		File f = new File( dir.getAbsolutePath() + DIR_SEP + "list.txt" );
		if	( !f.exists() )	{
			File [] list = dir.listFiles();
			if	( list == null )
				list = new File [] {};
			
			// ordenar la list
			Arrays.sort(list, CMP_BY_NAME_NO_CASE);
			return	list;
		}
		
		// cargar desde archivo, se asume la lista esta ordenada
		// la lista debe estar ordenada
		ArrayList<File> result = new ArrayList<File>();
		BufferedReader br = null;
		try	{
			br = new BufferedReader( new InputStreamReader( new FileInputStream(f) ) );
			String s;
			while	( (s = br.readLine()) != null )	{
				s = s.trim();
				// acepta comentarios
				if	( s.startsWith("#") )
					continue;
				if	( !s.isEmpty() )	{
					f = new File(s);
					if	( f.exists() )
						result.add( new File(s) );
				}
			}
			
		}	catch (Exception e)	{
		}	finally	{
			if ( br != null )
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return	result.toArray( new File [] {} );
	}
	
	
    /**
     * Mostrar msg por la consola
    **/
    public void debug(Object s)  {
    //    System.out.println( "(PanelPrincipal) " + s);
    }
}
