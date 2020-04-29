package pachet;

        import java.awt.Color;
        import java.awt.Graphics;
        import java.awt.Insets;
        import java.math.BigDecimal;
        import java.math.MathContext;
        import java.util.LinkedList;
        import java.util.Random;

        import javax.swing.JPanel;


public class Crossroad extends JPanel {
    private static final long serialVersionUID = 1L;
    private Car[][] cars;
    private LinkedList<TrafficLights> lights;
    private int size = 14;
    private int iteration = 0;
    private int spawn = 4;
    private static final int PINK_COL = 13;
    private static final int BLUE_COL = 14;
    private static final int LIGHT_BLUE_ROW = 14;
    private static final int GRAY_ROW = 15;
    private static int totalSpeed;
    private static int totalGeneratedCars;
    private Random rand = new Random();

    // single iteration
    public void iteration(int blueCars, int lightBlueCars, int grayCars,
                          int pinkCars, int maxSpeed, int conditions, int changeLight) {
        iteration++;

        // lights change
        for (TrafficLights l : lights) {
            if (iteration % changeLight == 0) {
                l.changeLight();
            }
        }

        // car spawn
        if (iteration % spawn == 0) {

            int blueRow = 0;
            for (int i = 0; i < blueCars; i++) {
                cars[blueRow][BLUE_COL].setSpeed(0);
                cars[blueRow][BLUE_COL].setType(1);
                blueRow = blueRow + 2;
                totalGeneratedCars++;
            }

            int lightBlueCol = 0;
            for (int i = 0; i < lightBlueCars; i++) {
                cars[LIGHT_BLUE_ROW][lightBlueCol].setSpeed(0);
                cars[LIGHT_BLUE_ROW][lightBlueCol].setType(2);
                lightBlueCol = lightBlueCol + 2;
                totalGeneratedCars++;
            }

            int pinkCol = 28;
            for (int i = 0; i < pinkCars; i++) {
                cars[pinkCol][PINK_COL].setSpeed(0);
                cars[pinkCol][PINK_COL].setType(3);
                pinkCol = pinkCol - 2;
                totalGeneratedCars++;
            }

            int grayCol = 28;
            for (int i = 0; i < grayCars; i++) {
                cars[GRAY_ROW][grayCol].setSpeed(0);
                cars[GRAY_ROW][grayCol].setType(4);
                grayCol = grayCol - 2;
                totalGeneratedCars++;
            }

        }

        // acceleration
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                if (cars[x][y].getSpeed() != null) {
                    cars[x][y].accelerate(maxSpeed);
                }
            }

        // breaking
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                if (cars[x][y].getSpeed() != null)
                    cars[x][y].brake(x, y, cars.clone(), lights);
            }

        // random event
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                if (cars[x][y].getSpeed() != null) {
                    if (conditions == 1 && rand.nextInt(101) % 2 == 0) {
                        cars[x][y].randomBrake();
                    } else if (conditions == 2 && rand.nextInt(101) % 4 == 0) {
                        cars[x][y].randomBrake();
                    } else if (conditions == 3 && rand.nextInt(101) % 10 == 0) {
                        cars[x][y].randomBrake();
                    }

                }
            }

        // movement
        totalSpeed = 0;
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                if (cars[x][y].getSpeed() != null) {
                    int speed = cars[x][y].getSpeed();

                    cars[x][y].setNextSpeed(null);
                    cars[x][y].setDist(null);

                    if (cars[x][y].getType() == 1) {
                        if (cars[x + speed][y].getNextSpeed() == null) {
                            cars[x + speed][y].setNextSpeed(speed);
                            cars[x + speed][y].setType(1);
                        } else {
                            cars[x][y].setNextSpeed(0);
                            speed = 0;
                        }
                    } else if (cars[x][y].getType() == 2) {
                        if (cars[x][y + speed].getNextSpeed() == null) {
                            cars[x][y + speed].setNextSpeed(speed);
                            cars[x][y + speed].setType(2);
                        } else {
                            cars[x][y].setNextSpeed(0);
                            speed = 0;
                        }
                    } else if (cars[x][y].getType() == 3) {
                        if (cars[x - speed][y].getNextSpeed() == null) {
                            cars[x - speed][y].setNextSpeed(speed);
                            cars[x - speed][y].setType(3);
                        } else {
                            cars[x][y].setNextSpeed(0);
                            speed = 0;
                        }
                    } else if (cars[x][y].getType() == 4) {
                        if (cars[x][y - speed].getNextSpeed() == null) {
                            cars[x][y - speed].setNextSpeed(speed);
                            cars[x][y - speed].setType(4);
                        } else {
                            cars[x][y].setNextSpeed(0);
                            speed = 0;
                        }
                    }
                    totalSpeed += cars[x][y].getSpeed();
                }
            }
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                cars[x][y].move();
            }
        this.paintComponent(this.getGraphics());
        // this.repaint();
    }

    // clearing board
    public void clear() {
        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                if (cars[x][y] != null)
                    cars[x][y].remove();
            }
        Car.setTotalNumberOfCars(0);
        Crossroad.setTotalSpeed(0);
        Crossroad.setTotalGeneratedCars(0);

        this.paintComponent(this.getGraphics());
    }

    public void initialize(int length, int height) {
        cars = new Car[length][height];
        lights = new LinkedList<TrafficLights>();
        // for blue cars
        lights.add(new TrafficLights(13, 14, 6, 1, true));
        // for lightblue cars
        lights.add(new TrafficLights(14, 12, 6, 2, false));
        // for pink cars
        lights.add(new TrafficLights(16, 13, 6, 3, true));
        // for gray cars
        lights.add(new TrafficLights(15, 15, 6, 4, false));
        // lights.add(new TrafficLights(15, 9, 4, 4, true));

        for (int x = 0; x < cars.length; ++x)
            for (int y = 0; y < cars[x].length; ++y) {
                cars[x][y] = new Car();
            }

    }

    // paint background and separators between cells
    protected void paintComponent(Graphics g) {

        g.setColor(getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    // draws the background netting
    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        // Cars
        for (x = 0; x < cars.length; ++x) {
            for (y = 0; y < cars[x].length; ++y) {
                if (cars[x][y].getSpeed() != null) {

                    if (cars[x][y].getType() == 1) {
                        g.setColor(Color.BLUE);

                    } else if (cars[x][y].getType() == 2) {
                        g.setColor(Color.cyan);

                    } else if (cars[x][y].getType() == 3) {
                        g.setColor(Color.magenta);

                    } else if (cars[x][y].getType() == 4) {
                        g.setColor(Color.GRAY);
                    }

                    g.fillRect((x * size) + 1, (y * size) + 1, (size - 1),
                            (size - 1));
                    g.setColor(Color.white);
                    g.drawString(cars[x][y].getSpeed().toString(),
                            (x * size) + 1, (y * size) + size);
                } else if (x < 14 && y < 13 || x > 15 && y < 13 || x < 14
                        && y > 14 || x > 15 && y > 14) {
                    g.setColor(Color.black);
                    g.fillRect((x * size) + 1, (y * size) + 1, (size - 1),
                            (size - 1));
                } else {
                    g.setColor(Color.white);
                    g.fillRect((x * size) + 1, (y * size) + 1, (size - 1),
                            (size - 1));

                }
            }
        }

        // Traffic lights
        for (TrafficLights l : lights) {
            g.setColor(l.isGreen() ? Color.GREEN : Color.RED);
            if (l.getType() == 1)
                g.fillRect((l.getLocation() * size) + 1,
                        ((l.getY() + 1) * size) + 1, (size - 1), (size - 1));
            else if (l.getType() == 2)
                g.fillRect(((l.getLocation() - 1) * size) + 1,
                        (l.getY() * size) + 1, (size - 1), (size - 1));
            else if (l.getType() == 3)
                g.fillRect((l.getLocation() * size) + 1,
                        ((l.getY() - 1) * size) + 1, (size - 1), (size - 1));
            else if (l.getType() == 4)
                g.fillRect(((l.getLocation() + 1) * size) + 1,
                        (l.getY() * size) + 1, (size - 1), (size - 1));
        }
    }

    public static int getTotalSpeed() {
        return totalSpeed;
    }

    public static void setTotalSpeed(int totalSpeed) {
        Crossroad.totalSpeed = totalSpeed;
    }

    public static int getTotalGeneratedCars() {
        return totalGeneratedCars;
    }

    public static void setTotalGeneratedCars(int totalGeneratedCars) {
        Crossroad.totalGeneratedCars = totalGeneratedCars;
    }

    public static BigDecimal getAvgSpeed() {
        BigDecimal avg = new BigDecimal(0);
        if (totalGeneratedCars != 0)
            avg = BigDecimal.valueOf(totalSpeed).divide(BigDecimal.valueOf(totalGeneratedCars - Car.getTotalNumberOfCars()), MathContext.DECIMAL32);
        return avg;
    }

    public int getIteration() {
        return iteration;
    }
}
