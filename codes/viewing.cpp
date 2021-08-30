#include <GL/glut.h>

void display() {

  glClear(GL_COLOR_BUFFER_BIT);

  glColor3f(1.0, 1.0, 1.0);
  glutWireTorus(0.5, 3, 20, 20);

  glBegin(GL_LINES);
    glColor3f(1, 0, 0); glVertex3f(0, 0, 0); glVertex3f(10, 0, 0);
    glColor3f(0, 1, 0); glVertex3f(0, 0, 0); glVertex3f(0, 10, 0);
    glColor3f(0, 0, 1); glVertex3f(0, 0, 0); glVertex3f(0, 0, 10);
  glEnd();

  glFlush();
}

void init() {

  glClearColor(0.0, 0.0, 0.0, 1.0);
  glColor3f(1.0, 1.0, 1.0);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
//  gluPerspective(60.0, 4/3, 1, 40);
	glOrtho(-10, 10, -10, 10, 1, 10);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  gluLookAt(5, 5, 5, 0, 5, 0, 0, 1, 0);
}

int main(int argc, char** argv) {
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
  glutInitWindowPosition(80, 80);
  glutInitWindowSize(800, 600);
  glutCreateWindow("A Simple Torus");
  glutDisplayFunc(display);
  init();
  glutMainLoop();
}
