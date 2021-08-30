package deneme;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;

public class clipping extends JApplet {
    DrawCanvas drawer = new DrawCanvas();

    public void init() {
        JPanel p = new JPanel();
        JButton b = new JButton("Set Line");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Set Line")) {
                    drawer.p1 = null;
                    drawer.p2 = null;
                }
            }
        });
        p.add(b);
        b = new JButton("Set Bounds");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Set Bounds")) {
                    drawer.clip = null;
                    drawer.clipP = null;
                }
            }
        });
        p.add(b);
	    getContentPane().setLayout(new BorderLayout());
	    getContentPane().add(BorderLayout.NORTH, p);
	    getContentPane().add(drawer);
        validate();
    }

    public void destroy() {}
    public void start() {}
    public void stop() {}

    public String getAppletInfo() {
        return "Bresenham Applet";
    }

}

class DrawCanvas extends Canvas {
    Graphics myGraphics;
    Point p1 = new Point(3, 25);
    Point p2 = new Point(95, 60);
    Point clipP = new Point(0, 0);
    Rectangle clip = new Rectangle(0, 0, 100, 100);

    Dimension size = new Dimension(500, 500);

    // Konstruktor, fügt MouseInputAdapter hinzu
    // 1./2. Klick = 1./2. Punkt, 3. Klick = beide Punkte löschen
    public DrawCanvas(){
        setBackground(Color.white);
    	addMouseListener(new MouseInputAdapter(){
    		public void mousePressed(MouseEvent e){
    			if(p1==null || p2==null){
    				p2 = p1;
    				p1 = new Point(e.getX()/5, e.getY()/5);
                } else if (clipP == null) {
                    clipP = new Point(e.getX()/5, e.getY()/5);
    			} else if (clip == null) {
                    int x1 = Math.min(clipP.x, e.getX()/5);
                    int y1 = Math.min(clipP.y, e.getY()/5);
                    int x2 = Math.max(clipP.x, e.getX()/5);
                    int y2 = Math.max(clipP.y, e.getY()/5);
                    clip = new Rectangle(x1, y1, x2-x1, y2-y1);
                }
    			repaint();
    		}
    	});
    }

    public void paint(Graphics g) {
    	myGraphics = g; // damit g nicht an light übergeben werden muss speichert paint() es hier
        g.setColor(Color.black);

        // Falls 2 Punkte vorhanden Linie malen
        if(p1!=null && p2!=null) {
            g.setColor(new Color(208, 208, 208));
            bresenham(p1, p2);
            g.setColor(Color.black);
            if (clip == null) {
                bresenham(p1, p2);
            } else {
                cohenSutherland(p1, p2, clip);
            }
        }

        // Punkte anzeigen
        String str = (p1!=null ? "p1=("+p1.x+","+p1.y+")" : "p1=null" ) + " - " +
                 (p2!=null ? "p2=("+p2.x+","+p2.y+")" : "p2=null" );
        g.drawString(str, 10, 10);
        // Start und Zielpunkt malen
        g.setColor(Color.red);
        if(p1!=null) light(p1.x, p1.y, 3);
        g.setColor(Color.yellow);
        if(p2!=null) light(p2.x, p2.y, 3);

        g.setColor(Color.green);
        if (clip != null) {
            g.drawRect(clip.x*5, clip.y*5, clip.width*5-1, clip.height*5-1);
        }
    }

    // zum malen der 5er-Pixel
    public void light(int x, int y){ light(x, y, 4); }
    public void light(int x, int y, int s){
        myGraphics.drawRect(x*5, y*5, s, s);
        myGraphics.fillRect(x*5, y*5, s, s);
    }

    public void bresenham(Point u, Point v){
    	Point p1, p2;
    	// Punkte sortieren, dann nur 4 Fälle statt 8
    	if(u.x<=v.x){ p1 = u; p2 = v; }
    	else        { p1 = v; p2 = u; }

    	// Variablen initialisieren
    	int dx=p2.x-p1.x, dy=p2.y-p1.y, x=p1.x, y=p1.y, s=0;

    	if(Math.abs(dx)>=Math.abs(dy)){ // dx>dy, Fall 1 u. 2
    		if(dy>=0){ // Fall 1, y positiv
    			System.out.println("Fall 1");
    			while(x<=p2.x){
    				light(x, y);
    				x++;
    				if(s>=dx){
    					s -= 2*dx;
    					y++;
    				}
    				s += 2*dy;
    			}
    		} else if(dy<0){ // Fall 2, y negativ
    			System.out.println("Fall 2");
    			while(x<=p2.x){
    				light(x, y);
    				x++;
    				if(Math.abs(s)>=dx){
    					s += 2*dx;
    					y--;
    				}
    				s += 2*dy;
    			}
    		}
    	} else { // dy>dx, Fall 3 u. 4
    		if(dy>=0){ // Fall 3, y positiv
    			System.out.println("Fall 3");
    			while(y<=p2.y){
    				light(x, y);
    				y++;
    				if(s>=dy){
    					s -= 2*dy;
    					x++;
    				}
    				s += 2*dx;
    			}
    		} else if(dy<0){ //Fall 4, y negativ
    			System.out.println("Fall 4");
    			while(y>=p2.y){
    				light(x, y);
    				y--;
    				if(s>=Math.abs(dy)){
    					s += 2*dy;
    					x++;
    				}
    				s += 2*dx;
    			}
    		}
    	}
    }// END bresenham()

    public static final int CODE_TOP = 1;
    public static final int CODE_BOTTOM = 2;
    public static final int CODE_LEFT = 4;
    public static final int CODE_RIGHT = 8;

    public void cohenSutherland(Point p1, Point p2, Rectangle bounds) {
        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);
        System.out.println("bounds: " + bounds);

        int[] b = { bounds.y,
                    bounds.y + bounds.height - 1,
                    bounds.x,
                    bounds.x + bounds.width - 1};

        int code1 = 0;
        if (p1.y < b[0]) code1 |= CODE_TOP;
        if (p1.y > b[1]) code1 |= CODE_BOTTOM;
        if (p1.x < b[2]) code1 |= CODE_LEFT;
        if (p1.x > b[3]) code1 |= CODE_RIGHT;
        System.out.println("code1: " + code1);

        int code2 = 0;
        if (p2.y < b[0]) code2 |= CODE_TOP;
        if (p2.y > b[1]) code2 |= CODE_BOTTOM;
        if (p2.x < b[2]) code2 |= CODE_LEFT;
        if (p2.x > b[3]) code2 |= CODE_RIGHT;
        System.out.println("code2: " + code2);

        if ((code1 & code2) != 0) {
            System.out.println("all outside");
            return;
        }

        int clip = code1 | code2;
        System.out.println("clip: " + clip);

        if (clip == 0) {
            System.out.println("all inside");
            bresenham(p1, p2);
            return;
        }

        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        double alpha1 = 0;
        double alpha2 = 1;
        int mask = 1;
        int div, sub;

        for (int i=0; i<4; i++) {
            if ((clip & mask) != 0) {
                System.out.println("executing for i=" + i + ", mask=" + mask);
                if (i < 2) {
                    sub = p1.y;
                    div = dy;
                } else {
                    sub = p1.x;
                    div = dx;
                }
                System.out.println("b[i]: " + b[i]);
                System.out.println("sub: " + sub);
                System.out.println("div: " + div);
                double alphai = (double)(b[i] - sub) / div;
                System.out.println("alphai: " + alphai);
                if ((mask & code1) != 0) {
                    if (alphai > alpha1) alpha1 = alphai;
                } else {
                    if (alphai < alpha2) alpha2 = alphai;
                }
                System.out.println("alpha1: " + alpha1);
                System.out.println("alpha2: " + alpha2);
            }
            mask += mask;
        }
        if (alpha1 > alpha2) return;

        Point p1p = new Point((int)(p1.x + alpha1 * dx + 0.5),
                              (int)(p1.y + alpha1 * dy + 0.5));
        Point p2p = new Point((int)(p1.x + alpha2 * dx + 0.5),
                              (int)(p1.y + alpha2 * dy + 0.5));
        System.out.println("p1: " + p1p);
        System.out.println("p2: " + p2p);
        bresenham(p1p, p2p);
    }

    public Dimension getMinimumSize() {
        return size;
    }

    public Dimension getMaximumSize() {
        return size;
    }

    public Dimension getPreferredSize() {
        return size;
    }

}