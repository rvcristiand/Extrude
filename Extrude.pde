import frames.core.Graph;
import frames.core.Frame;
import frames.primitives.Quaternion;
import frames.primitives.Vector;
import frames.processing.Scene;
import frames.processing.Shape;


Scene scene;
PShape section;

int fillColorExtrude = color(0, 0, 255, 63);
int strokeExtrude = color(0, 0, 255);

Portico portico;

boolean extrude;

void settings() {
  size(600, 400, P3D);
}

void setup() {
  scene = new Scene(this);
  scene.setType(Graph.Type.ORTHOGRAPHIC);
  scene.setRightHanded();

  section = createShape();
  section.beginShape();
  section.fill(fillColorExtrude);
  section.stroke(strokeExtrude);
  section.vertex( 0.125,  0.225);
  section.vertex(-0.125,  0.225);
  section.vertex(-0.125, -0.225);
  section.vertex( 0.125, -0.225);
  section.endShape(CLOSE);

  portico = new Portico(scene, section, new Vector(2, 2, 0),
    new Vector(0, 10, 0));
}

void draw() {
  background(127);
  scene.drawAxes();
  scene.cast();
}

void mouseDragged() {
  if (mouseButton == LEFT) scene.mouseCAD(new Vector(0, 0, 1));
  if (mouseButton == CENTER) scene.mousePan();
}

void mouseWheel(MouseEvent event) {
  scene.scale(-event.getCount() * 20);
}

void keyPressed() {
  if (key == 'e') extrude = !extrude;
}
