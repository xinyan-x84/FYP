package KiteAndDartWrite;

import java.awt.*;
import java.util.List;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;

public class PenroseDart extends JPanel {
    // ignores missing hash code
    class Tile {
        double x, y, angle, size;
        Type type;

        Tile(Type t, double x, double y, double a, double s) {
            type = t;
            this.x = x;
            this.y = y;
            angle = a;
            size = s;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Tile) {
                Tile t = (Tile) o;
                return type == t.type && Math.abs(x - t.x) < 0.000001 && Math.abs(y - t.y) < 0.000001 && Math.abs(angle - t.angle) < 0.000001;
            }
            return false;
        }
    }

    enum Type {
        Kite, Dart
    }

    static final double G = (1 + sqrt(5)) / 2; // golden ratio
    static final double T = toRadians(36); // theta

    List<Tile> tiles = new ArrayList<>();

    public PenroseDart() {
        int w = 700, h = 450;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.white);

        tiles = deflateTiles(setupPrototiles(w, h), 5); //change the generation
    }

    List<Tile> setupPrototiles(int w, int h) {
        List<Tile> proto = new ArrayList<>();
        //proto.add(new Tile(Type.Kite, w / 2, h / 2, toRadians(90), w / 2.5)); //change the amount of dart //toRadians(90) change the degree
        proto.add(new Tile(Type.Dart, w / 2, h / 2, toRadians(90), w / 2.5)); // same
        return proto;
    }

    List<Tile> deflateTiles(List<Tile> tls, int generation) {
        if (generation <= 0)
            return tls;
        List<Tile> next = new ArrayList<>();
        for (Tile tile : tls) {
            double x = tile.x, y = tile.y, a = tile.angle, nx, ny;
            double size = tile.size / G;

            if (tile.type == Type.Dart) {
                next.add(new Tile(Type.Kite, x, y, a + 5 * T, size));
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    nx = x + cos(a - 4 * T * sign) * G * tile.size;
                    ny = y - sin(a - 4 * T * sign) * G * tile.size;
                    next.add(new Tile(Type.Dart, nx, ny, a - 4 * T * sign, size));
                }
            } else {
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    next.add(new Tile(Type.Dart, x, y, a - 4 * T * sign, size));
                    nx = x + cos(a - T * sign) * G * tile.size;
                    ny = y - sin(a - T * sign) * G * tile.size;
                    next.add(new Tile(Type.Kite, nx, ny, a + 3 * T * sign, size));
                }
            }
        }
        // remove duplicates
        tls = next.stream().distinct().collect(toList());
        return deflateTiles(tls, generation - 1);
    }

    void drawTiles(Graphics2D g) {
        double[][] dist = {{G, G, G}, {-G, -1, -G}};
        int Count = 0;
        try {
            FileWriter writer = new FileWriter("D:\\D5.txt");
            for (Tile tile : tiles) {
                double angle = tile.angle - T;

                Path2D path = new Path2D.Double();
                path.moveTo(tile.x, tile.y);
                writer.write(String.format("%.4f %.4f%n", tile.x, tile.y));

                int ord = tile.type.ordinal();
                for (int i = 0; i < 3; i++) {
                    double x = tile.x + dist[ord][i] * tile.size * cos(angle);
                    double y = tile.y - dist[ord][i] * tile.size * sin(angle);
                    path.lineTo(x, y);
                    writer.write(String.format("%.4f %.4f%n", x, y));
                    angle += T;
                }

                path.closePath();
                g.setColor(ord == 0 ? Color.orange : Color.yellow);
                g.fill(path);
                g.setColor(Color.darkGray);
                g.draw(path);
                Count++;
                // System.out.println("The angle of the tile is: " + tile.angle / (2 * PI) + "\n");
            }
            // writer.write("The number of tiles is: " + Count);
            writer.close();
            System.out.println("The number of tiles is: " + Count);
        } catch (IOException e) {
            System.out.println("Write failed: " + e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(300, -100); //change this number to move image around
        g.scale(1.4, 1.4); //change this number to change the size of the image
        drawTiles(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Penrose Tiling");
            f.setResizable(true);
            f.add(new PenroseDart(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
