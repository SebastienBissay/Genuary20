package genuary._2025;

import processing.core.PApplet;
import processing.core.PVector;

import static genuary._2025.parameters.Parameters.*;
import static genuary._2025.save.SaveUtil.saveSketch;

public class Genuary01 extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Genuary01.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.green());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        noFill();
        noLoop();
    }

    @Override
    public void draw() {
        for (float angle = 0; angle < TWO_PI; angle += 0.01f * PI) {
            PVector position = PVector.fromAngle(angle).mult(400).add(WIDTH / 2f, HEIGHT / 2f);
            line(position.x, position.y,
                    position.x + 200 * noise(NOISE_SCALE * position.x, NOISE_SCALE * position.y), position.y);
        }

        saveSketch(this);
    }
}
