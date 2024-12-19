package genuary._2025;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static genuary._2025.parameters.Parameters.*;
import static genuary._2025.save.SaveUtil.saveSketch;

public class Genuary20 extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Genuary20.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        noFill();
        noLoop();
    }

    @Override
    public void draw() {
        ArrayList<Segment> segments = new ArrayList<>();
        for (int fortificationLevel = 1; fortificationLevel < NUMBER_OF_FORTIFICATIONS; fortificationLevel++) {
            float angle = -HALF_PI;
            int numberOfSides = fortificationLevel + 2;
            for (int i = 0; i < numberOfSides; i++) {
                segments.add(new Segment(
                        PVector.fromAngle(angle + TWO_PI * i / numberOfSides)
                                .mult(fortificationLevel * FORTIFICATION_RADIUS)
                                .add(width / 2f, height / 2f),
                        PVector.fromAngle(angle + TWO_PI * (i + 1) / numberOfSides)
                                .mult(fortificationLevel * FORTIFICATION_RADIUS)
                                .add(width / 2f, height / 2f)));
                segments.get(segments.size() - 1).render(this);
            }
        }

        for (int k = 0; k < NUMBER_OF_ITERATIONS; k++) {
            Segment origin = segments.get(floor(random(segments.size())));
            float maxValue = origin.lengthSq;
            for (int i = 0; i < NUMBER_OF_RANDOM_PICKS; i++) {
                Segment s = segments.get(floor(random(segments.size())));
                float value = s.lengthSq;
                if (value > maxValue) {
                    maxValue = value;
                    origin = s;
                }
            }

            float t = random(random(MIN_SEGMENT_OFFSET, .5f), random(.5f, MAX_SEGMENT_OFFSET));
            PVector point = new PVector(origin.a.x + t * (origin.b.x - origin.a.x),
                    origin.a.y + t * (origin.b.y - origin.a.y));
            float angle = atan2(origin.a.y - origin.b.y, origin.a.x - origin.b.x)
                    + floor(random(random(ANGLE_MIN_OFFSET, ANGLE_MID_OFFSET), random(ANGLE_MID_OFFSET, ANGLE_MAX_OFFSET)))
                    * PI / ANGLE_OFFSET_DIVISOR
                    + floor(random(2)) * PI;
            PVector end = new PVector(point.x, point.y);
            while (sq(end.x - width / 2f) + sq(end.y - height / 2f) < sq(CITY_RADIUS)) {
                end.add(PVector.fromAngle(angle));
            }


            Segment newSegment = new Segment(point, end);
            ArrayList<Segment> excluded = new ArrayList<>();
            excluded.add(origin);
            boolean hasChanged;
            do {
                hasChanged = false;
                for (Segment segment : segments) {
                    if (excluded.contains(segment)) continue;
                    PVector intersection = segment.getIntersectionPoint(newSegment);
                    if (intersection != null) {
                        newSegment.b = intersection;
                        hasChanged = true;
                        excluded.add(segment);
                        break;
                    }
                }
            } while (hasChanged);

            newSegment.lengthSq = sq(newSegment.a.x - newSegment.b.x) + sq(newSegment.a.y - newSegment.b.y);
            newSegment.render(this);
            segments.add(newSegment);
            segments.add(new Segment(origin.a, point));
            segments.add(new Segment(point, origin.b));
            segments.remove(origin);
        }

        for (Segment segment : segments) {
            segment.render(this);
        }

        saveSketch(this);
    }
}