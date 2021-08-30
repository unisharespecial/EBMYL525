#include <GL/glut.h>

void displayMe(void)
{
    glClear(GL_COLOR_BUFFER_BIT);
    
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	
	glColor3f(1.0, 0.4, 0.2);
	glBegin(GL_QUADS);
		glVertex3f(-0.5, 0.5, 0.0); // origin of the line
		glVertex3f(0.2, 0.8, 0.0); // ending point of the line
		glVertex3f(0.3, -0.8, 0.0); // ending point of the line
		glVertex3f(-0.3, -0.4, 0.0); // ending point of the line
	glEnd( );
	
	glColor4f(0.0, 0.4, 0.2, 0.5);	    
    glBegin(GL_POLYGON);
        glVertex3f(0.0, 0.0, 0.0);
        glVertex3f(-0.5, 0.0, 0.0);
        glVertex3f(-0.5, -0.5, 0.0);
        glVertex3f(0.0, -0.5, 0.0);
    glEnd();
    
    glFlush();
}

int main(int argc, char** argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutInitWindowSize(300, 300);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("Hello world :D");
    glutDisplayFunc(displayMe);
    glutMainLoop();
    return 0;
}
