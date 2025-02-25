package RhombWrite;

import java.awt.*;
import java.util.List;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;
import java.io.*;

public class PenroseFat extends JPanel {
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
                return type == t.type && x - t.x < 0.000001 && x - t.x > -0.000001 && y - t.y < 0.000001 && y - t.y > -0.000001
                        && angle - t.angle < 0.000001 && angle - t.angle > -0.000001;
            }
            return false;
        }
    }

    enum Type {
        Thin, Fat
    }

    static final double G = (1 + sqrt(5)) / 2; // golden ratio
    static final double T = toRadians(36); // theta

    List<Tile> tiles = new ArrayList<>();

    public PenroseFat() {
        int w = 700, h = 450;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.white);

        tiles = deflateTiles(setupPrototiles(w, h), 6); // change the generation
    }

    List<Tile> setupPrototiles(int w, int h) {
        List<Tile> proto = new ArrayList<>();

       // proto.add(new Tile(Type.Thin, w / 2, h / 2, toRadians(-90), w / 10)); // toRadians(90) change the degree
        proto.add(new Tile(Type.Fat, w / 2, h / 2, toRadians(90), w / 10));

        return proto;
    }

    List<Tile> deflateTiles(List<Tile> tls, int generation) {
        if (generation <= 0)
            return tls;

        List<Tile> next = new ArrayList<>();

        for (Tile tile : tls) {
            double x = tile.x, y = tile.y, a = tile.angle, nx, ny;
            double size = tile.size / G;

            if (tile.type == Type.Fat) {
                a = PI + a;
                x = x + (1 + G) * tile.size * cos(a);
                y = y - (1 + G) * tile.size * sin(a);
                next.add(new Tile(Type.Fat, x, y, 0 * T + a, size));

                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    nx = x + cos(a + 4 * T * sign) * G * tile.size;
                    ny = y - sin(a + 4 * T * sign) * G * tile.size;
                    next.add(new Tile(Type.Thin, nx, ny, 0 * T + a - T * sign, size));
                }
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    nx = x + cos(a + 4 * T * sign) * G * tile.size;
                    ny = y - sin(a + 4 * T * sign) * G * tile.size;
                    next.add(new Tile(Type.Fat, nx, ny, 0 * T + a + T * sign, size));
                }

            } else {
                x = x + tile.size * cos(a);
                y = y - tile.size * sin(a);
                for (int i = 0, sign = 1; i < 2; i++, sign *= -1) {
                    next.add(new Tile(Type.Thin, x, y, PI + a + 4 * T / 2 * sign, size));

                    nx = x + cos(a + 3 * T * sign) * G * tile.size;
                    ny = y - sin(a + 3 * T * sign) * G * tile.size;
                    next.add(new Tile(Type.Fat, nx, ny, a + 2 * T * sign, size));
                }
            }
            // next.add(tile);
        }

        // remove duplicates
        tls = next.stream().distinct().collect(toList());

        return deflateTiles(tls, generation - 1);
    }

    void drawTiles(Graphics2D g, PrintStream printStream) {
        double[][] dist = { { G, 1, G }, { -G, -2 * G * cos(T), -G } };
        int Count = 0;
        for (Tile tile : tiles) {
            double angle = tile.angle - T;
            double CHEK;
            CHEK = tile.angle / 2 - T;
            Path2D path = new Path2D.Double();
            path.moveTo(tile.x, tile.y);
            printStream.printf("%.4f %.4f%n", tile.x, tile.y);

            int ord = tile.type.ordinal();
            if (ord == 0) {// fat
                double x1 = tile.x + dist[ord][0] * tile.size * cos(2 * CHEK);
                double y1 = tile.y - dist[ord][0] * tile.size * sin(2 * CHEK);
                path.lineTo(x1, y1);
                printStream.printf("%.4f %.4f%n", x1, y1);

                CHEK += T;
                double x2 = tile.x + dist[ord][1] * tile.size * cos(2 * CHEK);
                double y2 = tile.y - dist[ord][1] * tile.size * sin(2 * CHEK);
                path.lineTo(x2, y2);
                printStream.printf("%.4f %.4f%n", x2, y2);
                CHEK += T;
                double x3 = tile.x + dist[ord][2] * tile.size * cos(2 * CHEK);
                double y3 = tile.y - dist[ord][2] * tile.size * sin(2 * CHEK);
                path.lineTo(x3, y3);
                printStream.printf("%.4f %.4f%n", x3, y3);
                CHEK += T;

            }
            if (ord == 1) {
                // thin
                for (int i = 0; i < 3; i++) {
                    double x = tile.x + dist[ord][i] * tile.size * cos(angle);
                    double y = tile.y - dist[ord][i] * tile.size * sin(angle);
                    path.lineTo(x, y);
                    printStream.printf("%.4f %.4f%n", x, y);
                    angle += T;
                }
            }
            path.closePath();
            g.setColor(ord == 0 ? Color.orange : Color.yellow);
            g.fill(path);
            g.setColor(Color.darkGray);
            g.draw(path);
            Count++;
        }
        // printStream.println("The number of tiles is: " + Count);
        System.out.println("The number of tiles is: " + Count);
    }

    @Override
    public void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(-300, 100); // change this number to move image around
        g.scale(1.5, 1.5); // change this number to change the size of the image
        try (PrintStream printStream = new PrintStream(
                new FileOutputStream("D:\\FYP\\F6.txt"))) {
            drawTiles(g, printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Penrose Tiling");
            f.setResizable(true);
            f.add(new PenroseFat(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
