#include <math.h>
#include <GL/glut.h>

#define CIRCLE_LIST 1

buildCircle() {
	GLint i;
	GLfloat cosine, sine;

	glNewList(CIRCLE_LIST, GL_COMPILE);
	glBegin(GL_POLYGON);
	for(i=0; i<100; i++){
	cosine = cos( 2*M_PI*i/100.0 );
	sine = sin( 2*M_PI*i/100.0 );
	glVertex2f( cosine, sine );
	}
	glEnd();
	glEndList();
}

void displayMe(void)
{

}

int main(int argc, char** argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutInitWindowSize(300, 300);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("Display List Example");
    glutDisplayFunc(displayMe);
    glutMainLoop();
    return 0;
}
