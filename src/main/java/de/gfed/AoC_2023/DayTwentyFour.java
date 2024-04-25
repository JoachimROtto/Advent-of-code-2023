package de.gfed.AoC_2023;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DayTwentyFour extends Day{
    long[] boundaries;
    private static final int PRECISION_100 = 100;
    int[] maxes = new int[3];
    int[] mins = new int[3];
    DayTwentyFour(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 24);
        expectations=new long[]{11246,716599937560103L};

        boundaries= debugMode?new long[]{7,27}:new long[]{200000000000000L, 400000000000000L};
        example = Arrays.asList(
                "19, 13, 30 @ -2,  1, -2",
                "18, 19, 22 @ -1, -1, -2",
                "20, 25, 34 @ -2, -2, -4",
                "12, 31, 28 @ -1, -2, -1",
                "20, 19, 15 @  1, -5, -3"); //array: 7-27 ->2

        /*
        The water in the air does not turn into snow but into hail. Whether you have to do something depends on whether
        the hailstones collide. You have a list of their starting position and movement. How many hailstones will collide
        in a xy-square from 200000000000000 to 400000000000000? (only consider x and y)
        Example: In a given testarray from 7-27 (x and y) the paths of 19, 13, 30 @ -2,  1, -2 and
        18, 19, 22 @ -1, -1, -2 cross at 14.333/15,333 (only path not hailstone).

        Part 2:
        Upon further analysis, it doesn't seem like any hailstones will naturally collide. It's up to you to throw a stone
        just right, so you hit every hailstone! It seems extremely unlikely but it's possible.
        Add the initial position of your rock.
        Example: 24, 13, 10 @ -3, 1, 2 ->47
        */
    }

    protected long evalInput(boolean bPart2) {
        AtomicLong result= new AtomicLong();
        List<Hailstone> hailstones = input.stream().map(Hailstone::new).toList();
        if (!bPart2) {
            buildIntersections(hailstones, boundaries);
            hailstones.forEach(hailstone -> result.addAndGet(hailstone.intersections));
            return result.get()/2;
        }
        BigDecimal[][]  matrix = createMatrix(findThreeCandidates(hailstones));
        solveGauss(matrix);
        return matrix[0][6].add(matrix[1][6]).add(matrix[2][6]).setScale(0, RoundingMode.HALF_EVEN).longValue();
    }

    private void buildIntersections(List <Hailstone> hailstones, long[] boundaries){
        for(int i=0; i<hailstones.size(); i++){
            Hailstone hailstone= hailstones.get(i);
            for (int j = i;j<hailstones.size(); j++){
                hailstone.intersects(hailstones.get(j), boundaries);
            }
        }
    }

    private static List<Hailstone> findThreeCandidates(List<Hailstone> hailstones) {
        List<Hailstone> result = new ArrayList<>();
        for(int aI = 0; aI < hailstones.size() - 2 && result.size() < 3; ++aI) {
            Hailstone a = hailstones.get(aI);
            for(int bI = aI+1; bI<hailstones.size()-1 && result.size()<3; ++bI) {
                Hailstone b = hailstones.get(bI);
                if(!a.velocityEquals(b)) {
                    for(int cI=bI+1; cI < hailstones.size()&&result.size()<3; ++cI) {
                        Hailstone c = hailstones.get(cI);
                        if(!a.velocityEquals(c) && !b.velocityEquals(c)) {
                            result.add(a);
                            result.add(b);
                            result.add(c);
                        }
                    }
                }
            }
        }
        return result;
    }

    private  BigDecimal[][] createMatrix(List<Hailstone> hailstones) {

        BigDecimal zero = BigDecimal.ZERO;
        BDCoord ap = BDCoord.BDCoordFromHS(hailstones.get(0));
        BDCoord bp = BDCoord.BDCoordFromHS(hailstones.get(1));
        BDCoord cp = BDCoord.BDCoordFromHS(hailstones.get(2));
        BDCoord av = BDCoord.BDVelFromHS(hailstones.get(0));
        BDCoord bv = BDCoord.BDVelFromHS(hailstones.get(1));
        BDCoord cv = BDCoord.BDVelFromHS(hailstones.get(2));

        return new BigDecimal[][]{
                {av.y.subtract(bv.y), av.x.subtract(bv.x).negate(), zero, ap.y.subtract(bp.y).negate(), ap.x.subtract(bp.x), zero, bp.y.multiply(bv.x).subtract(bp.x.multiply(bv.y)).subtract(ap.y.multiply(av.x).subtract(ap.x.multiply(av.y)))},
                {av.y.subtract(cv.y), av.x.subtract(cv.x).negate(), zero, ap.y.subtract(cp.y).negate(), ap.x.subtract(cp.x), zero, cp.y.multiply(cv.x).subtract(cp.x.multiply(cv.y)).subtract(ap.y.multiply(av.x).subtract(ap.x.multiply(av.y)))},
                {av.z.subtract(bv.z).negate(), zero, av.x.subtract(bv.x), ap.z.subtract(bp.z), zero, ap.x.subtract(bp.x).negate(), bp.x.multiply(bv.z).subtract(bp.z.multiply(bv.x)).subtract(ap.x.multiply(av.z).subtract(ap.z.multiply(av.x)))},
                {av.z.subtract(cv.z).negate(), zero, av.x.subtract(cv.x), ap.z.subtract(cp.z), zero, ap.x.subtract(cp.x).negate(), cp.x.multiply(cv.z).subtract(cp.z.multiply(cv.x)).subtract(ap.x.multiply(av.z).subtract(ap.z.multiply(av.x)))},
                {zero, av.z.subtract(bv.z), av.y.subtract(bv.y).negate(), zero, ap.z.subtract(bp.z).negate(), ap.y.subtract(bp.y), bp.z.multiply(bv.y).subtract(bp.y.multiply(bv.z)).subtract(ap.z.multiply(av.y).subtract(ap.y.multiply(av.z)))},
                {zero, av.z.subtract(cv.z), av.y.subtract(cv.y).negate(), zero, ap.z.subtract(cp.z).negate(), ap.y.subtract(cp.y), cp.z.multiply(cv.y).subtract(cp.y.multiply(cv.z)).subtract(ap.z.multiply(av.y).subtract(ap.y.multiply(av.z)))}
        };
    }

    private static void solveGauss(BigDecimal[][] c) {
        for (int row = 0; row < c.length; row++) {
            BigDecimal factor = c[row][row];
            for (int col = 0; col < c[row].length; col++) {
                c[row][col] = c[row][col].divide(factor, PRECISION_100, RoundingMode.HALF_EVEN);
            }

            for (int row2 = 0; row2 < c.length; row2++) {
                if (row2 != row) {
                    BigDecimal factor2 = c[row2][row].negate();
                    for (int col = 0; col < c[row2].length; col++) {
                        c[row2][col] = c[row2][col].add(factor2.multiply(c[row][col]));
                    }
                }
            }
        }
    }

    private record BDCoord(BigDecimal x, BigDecimal y, BigDecimal z) {
        static BDCoord BDCoordFromHS(Hailstone hailstone) {
            return new BDCoord(BigDecimal.valueOf(hailstone.x), BigDecimal.valueOf(hailstone.y), BigDecimal.valueOf(hailstone.z));
        }
        static BDCoord BDVelFromHS(Hailstone hailstone) {
            return new BDCoord(BigDecimal.valueOf(hailstone.velX), BigDecimal.valueOf(hailstone.velY), BigDecimal.valueOf(hailstone.velZ));
        }
    }



    public class Hailstone{
        long x;
        long y;
        long z;
        int velX;
        int velY;
        int velZ;
        List<Hailstone> intersect;
        int intersections=0;

        Hailstone(String line){
            String[] input =  line.replace("@", ",").split(",");
            x= Long.parseLong(input[0].trim());
            y= Long.parseLong(input[1].trim());
            z= Long.parseLong(input[2].trim());
            velX= Integer.parseInt(input[3].trim());
            velY= Integer.parseInt(input[4].trim());
            velZ= Integer.parseInt(input[5].trim());
            intersect=new ArrayList<>();
            maxes[0] = Math.max(maxes[0], velX);
            maxes[1] = Math.max(maxes[1], velY);
            maxes[2] = Math.max(maxes[2], velZ);
            mins[0] = Math.min(mins[0], velX);
            mins[1] =  Math.min(mins[1], velY);
            mins[2] = Math.min(mins[2], velZ);

        }
        public boolean intersects(Hailstone candidate, long[]boundaries){
            if (intersect.contains(candidate))
                return true;

            double d = -this.velY * candidate.velX + candidate.velY * this.velX;
            if (d != 0) {
                double a = this.velY * this.x - this.velX * this.y;
                double b = candidate.velY * candidate.x - candidate.velX * candidate.y;
                double x = (-candidate.velX * a + this.velX * b) / d;
                double y = (-candidate.velY * a + this.velY * b) / d;
                if (isInBoundaries(x,y,boundaries) && isInThePast(candidate, x,y)){
                    addIntersection(candidate);
                    candidate.addIntersection(this);
                    return true;
                }
            }
            return false;
        }
        private boolean isInBoundaries(double x, double y, long[] boundaries){
            return x > boundaries[0] && y > boundaries[0] &&
                    x < boundaries[1] && y < boundaries[1];
        }
        public boolean velocityEquals(Hailstone candidate){
            return (this.velX==candidate.velX &&
                    this.velY==candidate.velY &&
                    this.velZ==candidate.velZ);
        }
        public void addIntersection(Hailstone hailstone){
            intersect.add(hailstone);
            intersections++;
        }
        private boolean isInThePast(Hailstone candidate, double x, double y){
            return   (((x - this.x)  * this.velX)>0) &&
                    (((y - this.y)  * this.velY)>0) &&
                    (((x - candidate.x)  * candidate.velX)>0) &&
                    (((y - candidate.y)  * candidate.velY)>0);
        }
    }
}
