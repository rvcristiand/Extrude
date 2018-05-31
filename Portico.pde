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

  Scene scene() {
    return _scene;
  }

  void setScene(Scene scene) {
    _scene = scene;
  }

  PShape section() {
    return _section;
  }

  void setSection(PShape section) {
    _section = section;
  }

  Vector i() {
    return _i;
  }

  void setI(Vector i) {
    _i = i;
  }

  Vector j() {
    return _j;
  }

  void setJ(Vector j) {
    _j = j;
  }

  Vector localVector() {
    return Vector.subtract(j(), i());
  }

  float deltaXLocal() {
    return localVector().magnitude();
  }

  PShape extrude() {
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

  @Override
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
