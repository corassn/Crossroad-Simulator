package pachet;

import java.util.LinkedList;

public class Car {
    private Integer currSpeed;
    private Integer nextSpeed;
    private Integer dist;
    private int type = 0;
    private static int totalNumberOfCars;

    private int avgSpeed;

    public int getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(int avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public static int getTotalNumberOfCars() {
        return totalNumberOfCars;
    }

    public static void setTotalNumberOfCars(int totalNumberOfCars) {
        Car.totalNumberOfCars = totalNumberOfCars;
    }

    public Car() {
        currSpeed = null;
        nextSpeed = null;
        dist = null;
    }

    public Integer getSpeed() {
        return currSpeed;
    }

    public void setSpeed(Integer speed) {
        if (this.currSpeed == null) {
            currSpeed = speed;
        } else {
            this.currSpeed = speed;
        }
    }

    public void setNextSpeed(Integer speed) {
        if (this.nextSpeed == null) {
            this.nextSpeed = 0;
            nextSpeed = speed;
        } else {
            this.nextSpeed = speed;
        }
    }

    public int getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        if (this.dist != null)
            this.dist = dist;
        else {
            this.dist = 0;
            this.dist = dist;
        }
    }

    public void accelerate(int maxSpeed) {
        currSpeed = Math.min(maxSpeed, currSpeed + 1);
    }

    public void randomBrake() {
        currSpeed = Math.max(0, currSpeed - 1);
    }

    public void brake(int x, int y, Car[][] cars,
                      LinkedList<TrafficLights> lights) {
        Integer lightDist = 0;
        TrafficLights nearestLights = null;

        if (type == 1) {
            // end of board
            if (x + currSpeed >= cars.length) {
                this.remove();
                return;
            }

            // calculating distance to nearest vehicle
            for (int i = (x + 1); i < cars.length; i++) {
                if (cars[i][y].getSpeed() != null) {
                    dist = ((i - x) - 1) >= 0 ? (i - x) - 1 : null;
                    break;
                }
            }
            if (dist == null)
                dist = cars.length - x;

            // calculating distance to nearest traffic lights
            int help = -1;
            for (TrafficLights l : lights) {
                if (l.getLocation() >= x && l.getY() == y && l.getType() == 1) {
                    if (help > 0 && help > l.getLocation())
                        nearestLights = l;
                    else if (help < 0) {
                        help = l.getLocation();
                        nearestLights = l;
                    }
                }
            }

            if (nearestLights != null)
                lightDist = nearestLights.getLocation() - x >= 0 ? nearestLights
                        .getLocation() - x
                        : null;
            else
                lightDist = null;

        } else if (type == 2) {
            // end of board
            if (y + currSpeed >= cars[x].length) {
                this.remove();

                return;
            }

            // calculating distance to nearest vehicle
            for (int i = (y + 1); i < cars[x].length; i++) {
                if (cars[x][i].getSpeed() != null) {
                    dist = ((i - y) - 1) >= 0 ? (i - y) - 1 : null;
                    break;
                }
            }
            if (dist == null)
                dist = cars[x].length - y;

            // calculating distance to nearest traffic lights
            int help = -1;
            for (TrafficLights l : lights) {
                if (l.getY() >= y && l.getLocation() == x && l.getType() == 2) {
                    if (help > 0 && help > l.getY())
                        nearestLights = l;
                    else if (help < 0) {
                        help = l.getY();
                        nearestLights = l;
                    }
                }
            }
            if (nearestLights != null)
                lightDist = nearestLights.getY() - y >= 0 ? nearestLights
                        .getY() - y : null;
            else
                lightDist = null;
        } else if (type == 3) {
            // end of board
            if (x - currSpeed <= 0) {
                this.remove();

                return;
            }

            // calculating distance to nearest vehicle
            for (int i = (x - 1); i >= 0; i--) {
                if (cars[i][y].getSpeed() != null) {
                    dist = ((x - i) - 1) >= 0 ? (x - i) - 1 : null;
                    break;
                }
            }
            if (dist == null)
                dist = cars.length - x;

            // calculating distance to nearest traffic lights
            int help = -1;
            for (TrafficLights l : lights) {
                if (l.getLocation() <= x && l.getY() == y && l.getType() == 3) {
                    if (help > 0 && help < l.getLocation())
                        nearestLights = l;
                    else if (help < 0) {
                        help = l.getLocation();
                        nearestLights = l;
                    }
                }
            }

            if (nearestLights != null)
                lightDist = x - nearestLights.getLocation() >= 0 ? x
                        - nearestLights.getLocation() : null;
            else
                lightDist = null;

        } else if (type == 4) {
            // end of board
            if (y - currSpeed <= 0) {
                this.remove();

                return;
            }

            // calculating distance to nearest vehicle
            for (int i = (y - 1); i >= 0; i--) {
                if (cars[x][i].getSpeed() != null) {
                    dist = ((y - i) - 1) >= 0 ? (y - i) - 1 : null;
                    break;
                }
            }
            if (dist == null)
                dist = cars[x].length - y;

            // calculating distance to nearest traffic lights
            int help = -1;
            for (TrafficLights l : lights) {
                if (l.getY() <= y && l.getLocation() == x && l.getType() == 4) {
                    if (help > 0 && help < l.getY())
                        nearestLights = l;
                    else if (help < 0) {
                        help = l.getY();
                        nearestLights = l;
                    }
                }
            }
            if (nearestLights != null)
                lightDist = y - nearestLights.getY() >= 0 ? y
                        - nearestLights.getY() : null;
            else
                lightDist = null;
        }

        // speed modification
        if (lightDist == null
                || (nearestLights != null && nearestLights.isGreen())) {
            if (dist != null) {
                currSpeed = Math.min(dist, currSpeed);
            }
        } else {
            if (dist != null
                    && currSpeed.compareTo(Math.min(dist, lightDist)) > 0) {
                currSpeed = Math.min(dist, lightDist);
            }
        }
    }

    public void move() {
        currSpeed = nextSpeed;
    }

    public void remove() {
        totalNumberOfCars++;
        dist = null;
        currSpeed = null;
        nextSpeed = null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getNextSpeed() {
        return nextSpeed;
    }
}
