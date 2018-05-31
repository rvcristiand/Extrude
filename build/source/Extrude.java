import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import frames.core.Graph; 
import frames.core.Frame; 
import frames.primitives.Quaternion; 
import frames.primitives.Vector; 
import frames.processing.Scene; 
import frames.processing.Shape; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Extrude extends PApplet {









Scene scene;
PShape section;

int fillColorExtrude = color(0, 0, 255, 63);
int strokeExtrude = color(0, 0, 255);
// PShape cubo;

Portico portico;

boolean extrude;

public void settings() {
  size(600, 400, P3D);
}

public void setup() {
  scene = new Scene(this);
  scene.setType(Graph.Type.ORTHOGRAPHIC);
  scene.setRightHanded();

  section = createShape();
  section.beginShape();
  section.fill(fillColorExtrude);
  section.stroke(strokeExtrude);
  section.vertex( 0.125f,  0.225f);
  section.vertex(-0.125f,  0.225f);
  section.vertex(-0.125f, -0.225f);
  section.vertex( 0.125f, -0.225f);
  section.endShape(CLOSE);

  portico = new Portico(scene, section, new Vector(2, 2, 0),
    new Vector(0, 10, 0));

  // cubo = extrude(section, 10);
}

public void draw() {
  background(127);
  scene.drawAxes();
  // shape(cubo, 25, 25);
  scene.cast();
}

public void mouseDragged() {
  if (mouseButton == LEFT) scene.mouseCAD(new Vector(0, 0, 1));
  if (mouseButton == CENTER) scene.mousePan();
}

// void mouseClicked() {
//   if (mouseButton == CENTER) scene.mousePan();
  // for (int i = 0; i < section.getVertexCount(); i ++) {
  //   println("Coords:", section.getVertex(i));
  // }
// }

public void mouseWheel(MouseEvent event) {
  scene.scale(-event.getCount() * 20);
}

public void keyPressed() {
  if (key == 'e') extrude = !extrude;
}
class Portico extends Shape {
  Scene _scene;
  PShape _section;
  Vector _i;
  Vector _j;

  Portico (Scene scene, PShape section, Vector i, Vector j) {
    super(scene);

    setSection(section);
    setI(i);
    setJ(j);

    setPosition(i);
    setRotation(new Quaternion(new Vector(1, 0, 0), localVector()));

    setPrecision(Frame.Precision.FIXED);
  }

  public Scene scene() {
    return _scene;
  }

  public void setScene(Scene scene) {
    _scene = scene;
  }

  public PShape section() {
    return _section;
  }

  public void setSection(PShape section) {
    _section = section;
  }

  public Vector i() {
    return _i;
  }

  public void setI(Vector i) {
    _i = i;
  }

  public Vector j() {
    return _j;
  }

  public void setJ(Vector j) {
    _j = j;
  }

  public Vector localVector() {
    return Vector.subtract(j(), i());
  }

  public float deltaXLocal() {
    return localVector().magnitude();
  }

  public PShape extrude() {
    PVector n;
    PShape extrude;

    extrude = createShape();
    extrude.beginShape(QUAD_STRIP);

    for (int i = 0; i < section().getVertexCount(); i++) {
      n = section().getVertex(i);

      extrude.vertex(n.x, n.y, 0);
      extrude.vertex(n.x, n.y, deltaXLocal());
    }

    n = section().getVertex(section().getVertexCount() - 1);

    extrude.vertex(n.x, n.y, 0);
    extrude.vertex(n.x, n.y, deltaXLocal());

    n = section().getVertex(0);

    extrude.vertex(n.x, n.y, 0);
    extrude.vertex(n.x, n.y, deltaXLocal());

    extrude.endShape();

    return extrude;
  }

  public @Override
  void setGraphics(PGraphics pGraphics) {

    // pGraphics.translate(0, 0, deltaX());

    pushMatrix();
    pGraphics.translate(deltaXLocal() / 2, 0, 0);
    scene.drawAxes(pGraphics, scene.radius() / 3);
    popMatrix();

    pGraphics.beginDraw();



    if (extrude) {
      pushMatrix();

      pushMatrix();
      pushStyle();
      pGraphics.fill(fillColorExtrude);
      pGraphics.stroke(strokeExtrude);
      pGraphics.rotateY(radians(90));
      pGraphics.rotateZ(radians(90));
      pGraphics.shape(extrude());
      popStyle();
      popMatrix();

      pushMatrix();
      pGraphics.rotateY(radians(90));
      pGraphics.rotateZ(radians(90));
      pGraphics.shape(section());
      popMatrix();

      pGraphics.translate(deltaXLocal(), 0, 0);

      pushMatrix();
      pGraphics.rotateY(radians(90));
      pGraphics.rotateZ(radians(90));
      pGraphics.shape(section());
      popMatrix();

      popMatrix();
    } else {
      pushStyle();
      pGraphics.stroke(255, 255, 0);
      pGraphics.strokeWeight(5);
      pGraphics.line(0, 0, 0, deltaXLocal(), 0, 0);
      popStyle();
    }

    pGraphics.endDraw();
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Extrude" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
