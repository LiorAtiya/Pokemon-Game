package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyPanel extends JPanel {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;

    public MyPanel(Arena ar, Range2Range w2f) {
        this._ar = ar;
        this._w2f = w2f;
        this.setBackground(Color.green);
    }

    public BufferedImage importImage(){
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File("pictures/agent.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public BufferedImage importImagePokemon(){
        BufferedImage image = null;
        Random rnd = new Random();
        int r = rnd.nextInt(13)+1;
        try {
            image = ImageIO.read(new File("pictures/"+r+".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public BufferedImage headerImage(){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("pictures/Pokemon-Logo.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public BufferedImage homeImage(){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("pictures/home.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public BufferedImage backgroundImage(){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("pictures/background.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage(),0,0,this);
        drawHeader(g);
        drawGraph(g);
        drawPokemons(g);
        drawAgants(g);
        updateFrame();
    }

    public void drawHeader(Graphics g){
        g.drawImage(headerImage(),250,0,getWidth()-500,getHeight()-300,this);

        Font font = new Font("Copperplate Gothic Bold",Font.PLAIN,22);
        g.setFont(font);
        g.setColor(Color.black);
        g.drawString("Time left: "+_ar.getTimeToEnd()+" seconds     |     ",10,30);
        g.drawString("Level: "+_ar.get_info().get("gameLevel")+"     |     ",350,30);
        g.drawString("Score: "+_ar.get_info().get("grade")+"     |     ",520,30);
        g.drawString("Moves: "+_ar.get_info().get("moves"),700, 30);
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();

        for(node_data n : gg.getV()){
            g.setColor(Color.BLACK);
            for(edge_data e : gg.getE(n.getKey())){
                drawEdge(e,g);
            }
        }

        for(node_data n : gg.getV()){
            drawNode(n,5,g);
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
//                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    g.drawImage(importImagePokemon(),(int) fp.x() - r,(int) fp.y() - r-2,4 * r,4 * r,this);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
//                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                g.drawImage(importImage(),(int) fp.x() - r,(int) fp.y() - r-2,6 * r,6 * r,this);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawImage(homeImage(),(int) fp.x() - r-10, (int) fp.y() - r-14, 8 * r, 8 * r,this);
//        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        g.setColor(new Color(153,102,0));
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
//        g.drawImage(roadImage(),(int) s0.x(), (int) s0.y(), 20, 20,this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(14));

    }

}
