/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logointerpretor2;

//----------------------------------------------------------------------------------POINT

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;


interface Vector{
	public void draw(Graphics g); //canvas???
}
interface Platno{
	public void drawLine(Line l);
	public void drawCircle(Circle c);
	public void erase();
}

//----------------------------------------------------------------------------------LINE
class Line implements Vector{
	public Point start;
	public Point end;
	
	//kontruktor:
	public Line(Point start, Point end){
		this.start = start;
		this.end = end;
	}
	
	public void draw(Graphics g){
		g.drawLine((int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY());
	}
        
}
//----------------------------------------------------------------------------------CIRCLE
class Circle implements Vector{
	public Point center;
	public double radius;
	
	//konstruktor:
	public Circle(Point center, double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public void draw(Graphics g){
		g.drawOval((int)center.getX(), (int)center.getY(), (int)radius, (int)radius);
	}
}
//----------------------------------------------------------------------------------POINT
class Point{
	private double x;
	private double y;
	
	public double getX(){
		return x;
	}
	public void setX(double x){
		this.x = x;
	}
	public double getY(){
		return y;
	}
	public void setY(double y){
		this.y = y;
	}
	//kontruktor:
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
        
        @Override
        public String toString()
        {
            return x + ", " + y;
        }
}
//----------------------------------------------------------------------------------TURTLE
class Turtle{
	private double headingDegrees;
	private Point position;
	private boolean penDown;
        
        private MyPanel mp;
        	
	public double getDeg(){
		return headingDegrees;
	}
	public void setDeg(double deg){
		this.headingDegrees = deg;
	}
	public boolean getPen(){
		return penDown;
	}
	public void setPen(boolean pen){
		this.penDown = pen;
	}
	public Point getPosition(){
		return position;
	}
	public void setPosition(double x, double y){
		this.position.setX( x );
		this.position.setY( y );
	}
        
	public Turtle(MyPanel mp){
		this.headingDegrees = Math.PI;
		this.position = new Point(200.0, 200.0);
		this.penDown = true;  
                this.mp = mp;
	}
        
        public double calculateDeg(double deg){

            return deg*Math.PI/180;
        }
        
        public void processInput(String input)
        {
            try
            {
                processCommand(input);                
            }
            catch (Exception e)
            {
                System.out.println("Ne postojeci input/naredba.");
            }
        }
        
        private void processCommand(String cmd)
        {
            String[] pom = cmd.split(" ");
		
		if(pom[0].equals("FD")){
                    double destX = Double.valueOf(cmd.split(":")[1]) * Math.sin(getDeg()) + getPosition().getX();
                    double destY = Double.valueOf(cmd.split(":")[1]) * Math.cos(getDeg()) + getPosition().getY();
                    Point p = new Point(destX, destY);
                    setPosition(destX, destY);
                    if(penDown)
                        mp.promjena = true;
                    mp.add(p);
		}
                else if(pom[0].equals("BK")){
                    double destX = Double.valueOf(cmd.split(":")[1]) * Math.sin(getDeg() + Math.PI) + getPosition().getX();
                    double destY = Double.valueOf(cmd.split(":")[1]) * Math.cos(getDeg() + Math.PI) + getPosition().getY();
                    Point p = new Point(destX, destY);
                    setPosition(destX, destY);
                    if (penDown) 
                        mp.promjena = true;
                    mp.add(p);
		}
                
                else if(pom[0].equals("CIRCLE")){
                    double pomRadius = Double.valueOf(cmd.split(":")[1]);
                    double x = getPosition().getX()-pomRadius/2;
                    double y = getPosition().getY()-pomRadius/2;
                    Point pos = new Point(x,y);
                    Circle c = new Circle(pos, pomRadius);
                    if(penDown)
                        mp.promjena = true;
                    mp.add(c);
		}
                
                else if(pom[0].equals("RT")){
                    double deg = Double.valueOf(cmd.split(":")[1]);
                    mp.promjena = false;
                    headingDegrees -= calculateDeg(deg);
		}
                
                else if(pom[0].equals("LT")){
                    double deg = Double.valueOf(cmd.split(":")[1]);
                    mp.promjena = false;
                    headingDegrees += calculateDeg(deg);
		}
                
                else if(pom[0].equals("PU"))
                {
                    penDown = false;
                    mp.promjena = false;
                }
                
                else if(pom[0].equals("PD"))
                {
                    penDown = true;
                    mp.promjena = false;
                }
                
                else
                    System.out.println("Ne postojeci input/naredba.");
        }
}
//----------------------------------------------------------------------------------------PANEL
class MyPanel extends JPanel{
	ArrayList<Point> listOfPoints;
	ArrayList<Vector> listOfVectors;
        
        boolean promjena;
        
        JFrame f = new JFrame("frame");
        
        Turtle turtle;
        
        public void add(Point p)
        {
            if(turtle.getPen() == false)
            {
                listOfPoints.add(p);
                return;
            }
            else
            {                
                Line l = new Line(listOfPoints.get(listOfPoints.size()-1), p);
                listOfVectors.add(l);
                listOfPoints.add(p);
                
            }
        }
        
        public void add(Circle c)
        {
            if(turtle.getPen() == false)
            {
                return;
            }
            listOfVectors.add(c);
        }
	
	public MyPanel(){
		listOfPoints = new ArrayList<Point>();
                listOfVectors = new ArrayList<Vector>();
                promjena = false;
                
                turtle = new Turtle(this);
                Point start = new Point(turtle.getPosition().getX(), turtle.getPosition().getY());
                listOfPoints.add(start);
                
                f.add(BorderLayout.CENTER, this);
                f.setSize(800, 800);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for( Vector i : listOfVectors )
			i.draw(g);
	}

        public Turtle getTurtle()
        {
            return turtle;
        }
}

class CrtacePlatno implements Platno
{
    MyPanel mp;
        
    public CrtacePlatno(MyPanel mp)
    {
        this.mp = mp;
    }
    
    public void drawAll()
    {   
        for (Vector c : mp.listOfVectors) {
            if(c instanceof Line)
                drawLine((Line)c);
            else if(c instanceof Circle)
                drawCircle((Circle)c);
        }
    }
    
    @Override
    public void drawCircle(Circle c) {
        c.draw(mp.getGraphics());
    }

    @Override
    public void drawLine(Line l) {
        l.draw(mp.getGraphics());
    }

    @Override
    public void erase() {
    }
}

class VectorPlatno implements Platno{

    MyPanel mp;
        
    public VectorPlatno(MyPanel mp)
    {
        this.mp = mp;
    }
    
    public void drawAll()
    {   
        for (Vector c : mp.listOfVectors) {
            if(c instanceof Line)
                drawLine((Line)c);
            else if(c instanceof Circle)
                drawCircle((Circle)c);
        }
    }
    
    public void drawLast()
    {
        if(mp.promjena == false)
            return;
        Vector c = mp.listOfVectors.get(mp.listOfVectors.size() - 1);
        if(c instanceof Line)
                drawLine((Line)c);
            else if(c instanceof Circle)
                drawCircle((Circle)c);
    }
    
    @Override
    public void drawCircle(Circle c) {
        //Implementirati drugačiji ispis vektora po želji
        System.out.println("Krug -> srediste: " + c.center.getX() + "." + c.center.getY() + "; radius:" + c.radius);
    }

    @Override
    public void drawLine(Line l) {
        //Implementirati drugačiji ispis vektora po želji
        System.out.println("Linija -> pocetak: " + l.start.getX() + "." + l.start.getY() + "; kraj:" + l.end.getX() + "." + l.end.getY());
    }

    @Override
    public void erase() {
        
    }
   
}

public class LogoInterpretor2 {

    public static void main(String[] args) {
        MyPanel p = new MyPanel();
        CrtacePlatno cp = new CrtacePlatno(p);
        VectorPlatno vp = new VectorPlatno(p);
        
        Scanner s = new Scanner(System.in);
        

        /*
        ///UNOS PODATAKA U KONZOLU
        while(true)
        {
            String cmd = s.nextLine();
            if(cmd.equals("Q") || cmd.equals("q") || cmd.equals("quit") )
                System.exit(0);
             if(cmd.equals("flush") || cmd.equals("FLUSH") )
             {
                 vp.drawAll(); // ispisuje sve dosadasnje linije i kruznice
                 continue;
             }
            p.getTurtle().processInput(cmd);
            cp.drawAll();
            vp.drawLast(); //ispisuje samo zadnju promjenu
        }
        */
        
        ///TEST PODACI
        ///unaprijed upisani podaci
        p.getTurtle().processInput("RT : 180");
        p.getTurtle().processInput("FD : 100");
        p.getTurtle().processInput("PU");
        p.getTurtle().processInput("FD : 50");
        p.getTurtle().processInput("PD");
        p.getTurtle().processInput("FD : 50");
        p.getTurtle().processInput("CIRCLE : 20");
        p.getTurtle().processInput("PU");
        p.getTurtle().processInput("LT : 90");
        p.getTurtle().processInput("FD : 200");
        p.getTurtle().processInput("PD");
        int n = 6;
        for (int i = 0; i < n; i++) {
            p.getTurtle().processInput("FD : 100");
            p.getTurtle().processInput("LT : " + 360/n);
        }
        n = 8;
        for (int i = 0; i < n; i++) {
            p.getTurtle().processInput("FD : 50");
            p.getTurtle().processInput("LT : " + 360/n);
        }
    }
    
}
