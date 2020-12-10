package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        drawHeader(g);
        updateFrame();
    }

    public void drawHeader(Graphics g){
        g.drawImage(headerImage(),250,0,500,200,this);

        Font font = new Font("Ariel", Font.BOLD, 14);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString("Time to end: "+_ar.getTimeToEnd()+" seconds  | ",250,200);
        g.drawString("Level:       |   ",450,200);
        g.drawString("Score: ",550,200);
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }

    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();

        for(node_data n : gg.getV()){
            g.setColor(Color.BLACK);
            drawNode(n,5,g);

            for(edge_data e : gg.getE(n.getKey())){
                g.setColor(Color.gray);
                drawEdge(e,g);
            }
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
                    g.drawImage(importImagePokemon(),(int) fp.x() - r,(int) fp.y() - r,3 * r,3 * r,this);
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
                g.drawImage(importImage(),(int) fp.x() - r,(int) fp.y() - r,4 * r,4 * r,this);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

}
