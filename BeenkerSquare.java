package AmmannWrite;

import java.awt.*;
import java.util.List;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;
import java.io.*;

public class BeenkerSquare extends JPanel {
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
                return type == t.type && x == t.x && y == t.y && angle == t.angle;
            }
            return false;
        }
    }

    enum Type {
        Fat, Thin
    }

    static final double G = (1 + sqrt(2)); // generate factor
    static final double T = toRadians(45); // theta

    List<Tile> tiles = new ArrayList<>();

    public BeenkerSquare() {
        int w = 700, h = 450;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.white);

        tiles = deflateTiles(setupPrototiles(w, h), 3); // change the generation
    }

    List<Tile> setupPrototiles(int w, int h) {
        List<Tile> proto = new ArrayList<>();

        // sun
        proto.add(new Tile(Type.Fat, w / 2, h / 2, toRadians(0), w / 10));
        // proto.add(new Tile(Type.Thin, w / 2, h / 2, toRadians(70), w / 10));

        return proto;
    }

    List<Tile> deflateTiles(List<Tile> tls, int generation) {
        if (generation <= 0)
            return tls;

        List<Tile> next = new ArrayList<>();

        for (Tile tile : tls) {
            double x = tile.x, y = tile.y, a = tile.angle, nx, ny, mx, my, ox, oy, px, py;
            double size = tile.size / G;

            if (tile.type == Type.Fat) {
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    nx = x + cos(a + 2 * T) * tile.size * G;
                    ny = y - sin(a + 2 * T) * tile.size * G;
                    next.add(new Tile(Type.Thin, nx, ny, a - 2 * T, size));
                    mx = x + cos(a + 2 * T) * tile.size * G;
                    my = y - sin(a + 2 * T) * tile.size * G;
                    next.add(new Tile(Type.Thin, mx, my, a + T, size));
                    next.add(new Tile(Type.Thin, x, y, a + 3 * T, size));
                    next.add(new Tile(Type.Thin, x, y, a - 4 * T, size));
                }
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    nx = x + sqrt(2) * cos(a + 2 * T) * G * tile.size;
                    ny = y - sqrt(2) * sin(a + 2 * T) * G * tile.size;
                    next.add(new Tile(Type.Fat, nx, ny, a + 3 * T * sign, size));
                    mx = x + cos(a + 2 * T) * tile.size * G;
                    my = y - sin(a + 2 * T) * tile.size * G;
                    next.add(new Tile(Type.Fat, mx, my, a + 4 * T, size));
                    ox = x - sin(a - T * sign) * tile.size * G;
                    oy = y - cos(-a + T * sign) * tile.size * G;
                    next.add(new Tile(Type.Fat, ox, oy, a + 3 * sign * T, size));
                }

            } else {
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    next.add(new Tile(Type.Thin, x, y, a, size));
                    nx = x - cos(a + 2 * T) * tile.size * G;
                    ny = y + sin(a + 2 * T) * tile.size * G;
                    next.add(new Tile(Type.Thin, nx, ny, a + 2 * T, size));
                    next.add(new Tile(Type.Fat, nx, ny, a, size));
                    next.add(new Tile(Type.Fat, nx, ny, a - 3 * T, size));
                    mx = x + cos(a - T) * tile.size * G;
                    my = y - sin(a - T) * tile.size * G;
                    next.add(new Tile(Type.Fat, mx, my, a - 4 * T, size));
                    next.add(new Tile(Type.Fat, mx, my, a + T, size));
                    ox = x + 2 * cos(T / 2) * cos(a - 3 * T / 2) * G * tile.size;
                    oy = y - 2 * cos(T / 2) * sin(a - 3 * T / 2) * G * tile.size;
                    next.add(new Tile(Type.Thin, ox, oy, a - 4 * T, size));
                }
            }
        }

        // remove duplicates
        tls = next.stream().distinct().collect(toList());

        return deflateTiles(tls, generation - 1);
    }

    void drawTiles(Graphics2D g) {
        double[][] dist = {{-G, -2 * G * cos(T), -G}, {G, 2 * G * cos(T / 2), G}};
        int Count = 0;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("D:\\S3.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Tile tile : tiles) {
            double angle = tile.angle - 3 * T;
            double CHEK;
            CHEK = tile.angle - 2 * T;
            Path2D path = new Path2D.Double();
            path.moveTo(tile.x, tile.y);
            writer.printf("%.4f %.4f%n", tile.x, tile.y);

            int ord = tile.type.ordinal();
            // System.out.printf("%d%n",ord);
            if (ord == 0) { // Fat
                double x4 = tile.x + dist[ord][0] * tile.size * cos(angle);
                double y4 = tile.y - dist[ord][0] * tile.size * sin(angle);
                path.lineTo(x4, y4);
                writer.printf("%.4f %.4f%n", x4, y4);
                angle += T;
                double x5 = tile.x + dist[ord][1] * tile.size * cos(angle);
                double y5 = tile.y - dist[ord][1] * tile.size * sin(angle);
                path.lineTo(x5, y5);
                writer.printf("%.4f %.4f%n", x5, y5);
                angle += T;
                double x6 = tile.x + dist[ord][2] * tile.size * cos(angle);
                double y6 = tile.y - dist[ord][2] * tile.size * sin(angle);
                path.lineTo(x6, y6);
                writer.printf("%.4f %.4f%n", x6, y6);
                angle += T;

            } else if (ord == 1) { // Thin

                double x1 = tile.x + dist[ord][0] * tile.size * cos(CHEK);
                double y1 = tile.y - dist[ord][0] * tile.size * sin(CHEK);
                path.lineTo(x1, y1);
                writer.printf("%.4f %.4f%n", x1, y1);
                CHEK += T / 2;
                double x2 = tile.x + dist[ord][1] * tile.size * cos(CHEK);
                double y2 = tile.y - dist[ord][1] * tile.size * sin(CHEK);
                path.lineTo(x2, y2);
                writer.printf("%.4f %.4f%n", x2, y2);
                CHEK += T / 2;
                double x3 = tile.x + dist[ord][2] * tile.size * cos(CHEK);
                double y3 = tile.y - dist[ord][2] * tile.size * sin(CHEK);
                path.lineTo(x3, y3);
                writer.printf("%.4f %.4f%n", x3, y3);
                CHEK += T;
            }
            path.closePath();
            g.setColor(ord == 0 ? Color.orange : Color.yellow);
            g.fill(path);
            g.setColor(Color.darkGray);
            g.draw(path);
            Count++;
        }

        // writer.println("The number of tiles is: " + Count);
        writer.close();
        System.out.println("The number of tiles is: " + Count);
    }

    @Override
    public void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(-200, 100);
        g.scale(2, 2);
        drawTiles(g);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Ammann-Beenker Tiling");
            f.setResizable(true);
            f.add(new BeenkerSquare(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
