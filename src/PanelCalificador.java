import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class PanelCalificador extends JPanel{
	

    private final String FONDO_NAME = "fC.class";
	private final String [] TIPO_CALIFICACION = {"RITMO","VOCALIZACION","AFINACION"};
	
	private final int [] CALIFICACION;
	private final LabelBarra [] BARRA;
	private final JLabel [] TITULO_BARRA;
	private final Contador [] CONTADOR;
	
	private final Image IMAGEN_FONDO;
    private final Font fuente;
	private final JLabel TITULO_PROMEDIO;
	
	private Contador CONTADOR_PROMEDIO;
	private LabelBarra GENERAL;
	
	private int PROMEDIO;
	private int X;
	private int Y;
	private int ANCHO;
	private int ALTO;
	private final Random R = new Random();
	
	public PanelCalificador(){
		setLayout(null);
		setBounds(0,0,1024,768);
		
		X = 300;
		Y = 250;
		ANCHO = 0;
		ALTO = 60;
		
		CALIFICACION = new int [TIPO_CALIFICACION.length];
		BARRA = new LabelBarra[TIPO_CALIFICACION.length];
		TITULO_BARRA = new JLabel[TIPO_CALIFICACION.length];
		CONTADOR = new Contador[TIPO_CALIFICACION.length];
		
		IMAGEN_FONDO = Toolkit.getDefaultToolkit().getImage (getClass().getResource( FONDO_NAME ));
		fuente = new Font("Chiller", Font.BOLD, 30);
		TITULO_PROMEDIO = new JLabel("PROMEDIO");

		TITULO_PROMEDIO.setFont(fuente);
		TITULO_PROMEDIO.setForeground(Color.WHITE);
		add(TITULO_PROMEDIO).setBounds(X,600,200,ALTO);
	//	califica();
	}
	
	
	public boolean isAlive()	{
		boolean alive = false;
		for (int i = 0; i < BARRA.length; i++)
			alive |= BARRA[i].isAlive();
		
		alive |= GENERAL.isAlive();
		
		for (int i = 0; i < CONTADOR.length; i++)
			alive |= CONTADOR[i].isAlive();

		alive |= CONTADOR_PROMEDIO.isAlive();
		
		return	alive;
	}
	
	public void paint(Graphics g) {
        g.drawImage(IMAGEN_FONDO, 0, 0, getWidth(), getHeight(), this);
        setOpaque(false);
        super.paint(g);
    }
	
	public void califica(){

		X = 300;
		Y = 250;
		ANCHO = 0;
		ALTO = 60;
		
		for(int i=0; i<BARRA.length; i++){
			TITULO_BARRA[i]= new JLabel(TIPO_CALIFICACION[i]);
			TITULO_BARRA[i].setForeground(Color.BLUE);
			TITULO_BARRA[i].setBounds(X, Y-30, 300, 30);
			TITULO_BARRA[i].setFont(fuente);
			add(TITULO_BARRA[i]);
			
			CALIFICACION[i] = R.nextInt(101);
			BARRA[i] = new LabelBarra(10, "",CALIFICACION[i],X,Y,ANCHO,ALTO);
			add(BARRA[i]);
			
			CONTADOR[i] = new Contador(10,CALIFICACION[i]);
			add(CONTADOR[i]).setBounds(X-80,Y,100,ALTO);
			
			Y=Y+100;
			PROMEDIO = PROMEDIO + CALIFICACION[i];
		}
		
		PROMEDIO /= 3;
		PROMEDIO = Math.max(PROMEDIO, 1);
		
		GENERAL = new LabelBarra(15,"",PROMEDIO,X,650,ANCHO,ALTO);
		add(GENERAL);
		
		CONTADOR_PROMEDIO = new Contador(15, PROMEDIO);
		add(CONTADOR_PROMEDIO).setBounds(X-80,650,100,ALTO);
	}
	public int getPromedio()	{
		return	PROMEDIO;
	}
	
	
	
	
	
}



class Contador extends JLabel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8327383542257810852L;
	private int Tope;
	private int Cont;
    private Font fuente = new Font("Chiller", Font.BOLD, 50);
    private Timer timer;
	private boolean alive;
	public Contador(int Velocidad, int MAX){
		Tope = MAX;
		setText("0");
		setFont(fuente);
		timer = new Timer (Velocidad*5, this) ;
		timer.start();
		alive = true;
	}
	public boolean isAlive()	{
		return	alive;
	}
	public void actionPerformed(ActionEvent e){
		if (Cont >= Tope) {
			timer.stop();
			alive = false;
			return;
		}
		if (Cont < 50)
			setForeground(Color.RED);
		else if (Cont < 70)
			setForeground(Color.YELLOW);
		else
			setForeground(Color.GREEN);
		Cont++;
		setText(Cont + "");
		validate();
	}
}


class LabelBarra extends JLabel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6902505127204015001L;
	private int Maximo;
	private Timer timer;
	private boolean alive;
	public LabelBarra(final int Velocidad, String Cadena, int  Max,int X,int Y,int ANCHO,int ALTO){
		setOpaque(true);
		setHorizontalTextPosition(SwingConstants.CENTER);
		Maximo=Max*5;
		setBounds(X,Y,ANCHO,ALTO);
		setText(Cadena);
		timer = new Timer (Velocidad, this) ;
		timer.start();
		alive = true;
	}
	
	
	public boolean isAlive()	{
		return	alive;
	}
	public void actionPerformed(ActionEvent e){
		if(getWidth()<Maximo){
			if(getWidth()<50*5)
				setBackground(Color.RED);
			else{
				if(getWidth()<70*5)
					setBackground(Color.YELLOW);
				else 
					setBackground(Color.GREEN);
			}
			setSize(getWidth()+1,getHeight());
			validate();
		}
		else {
			timer.stop();
			alive = false;
		}
    }
}




