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

public class GamePanel extends JPanel {

    private int _ind;
    private Arena _ar;
    private Range2Range _w2f;
    private static geo_location prevPos;
    private static int prevImg;

    public GamePanel(Arena ar) {
        this._ar = ar;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateFrame();
        g.drawImage(importImage("data/pictures/background.jpeg"), 0, 0, this.getWidth(), this.getHeight(), this);
        drawHeader(g);
        drawGraph(g);
        drawPokemons(g);
        drawAgants(g);
    }

    private BufferedImage importImage(String img) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(img));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public void drawHeader(Graphics g) {

        Font font = new Font("Copperplate Gothic Bold", Font.PLAIN, 22);
        g.setFont(font);
        g.setColor(Color.black);
        g.drawString("Time left: " + _ar.getTimeToEnd() + " seconds     |     ", 10, 30);
        g.drawString("Level: " + _ar.get_info().get("gameLevel") + "     |     ", 350, 30);
        g.drawString("Score: " + _ar.get_info().get("grade") + "     |     ", 520, 30);
        g.drawString("Moves: " + _ar.get_info().get("moves"), 720, 30);
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
        CL_Pokemon firstPok = _ar.getPokemons().get(0);
        prevPos = this._w2f.world2frame(firstPok.getLocation());
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();

        for (node_data n : gg.getV()) {
            g.setColor(Color.BLACK);
            for (edge_data e : gg.getE(n.getKey())) {
                drawEdge(e, g);
            }
        }

        for (node_data n : gg.getV()) {
            drawNode(n, 5, g);
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        for (int i = 0; i < fs.size(); i++) {
            fs.get(i).setID(i + 1);
        }

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
                    g.drawImage(importImage("data/pictures/" + f.getID() + ".png"), (int) fp.x() - r - 7, (int) fp.y() - r - 7, 4 * r, 4 * r, this);
//                    	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
//                        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> listAgents = _ar.getAgents();
//        g.setColor(Color.red);
        int i = 0;
        while (listAgents != null && i < listAgents.size()) {
            geo_location c = listAgents.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location pos = this._w2f.world2frame(c);
//                g.fillOval((int) pos.x() - r, (int) pos.y() - r, 2 * r, 2 * r);
                g.drawImage(importImage("data/pictures/agent.png"), (int) pos.x() - r - 5, (int) pos.y() - r - 5, 4 * r, 4 * r, this);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.drawImage(importImage("data/pictures/home.png"), (int) fp.x() - r - 10, (int) fp.y() - r - 14, 8 * r, 8 * r, this);
//        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
//        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        g.setColor(new Color(153, 102, 0));
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(6));

    }
}
