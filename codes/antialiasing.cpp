#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <GL/glut.h>

#define WIDTH  800
#define HEIGHT 500

#define PI    3.14159265358979323846
#define points 512
#define XMIN  -15.0
#define XMAX   15.0
#define YMIN   0.0
#define YMAX   25.0

double pt[points];
double x[points];
double psi[points];
double V (double);

void drawStringTimesNewRoman (char *s){
  unsigned int i;
  for (i = 0; i < strlen (s); i++)
    glutBitmapCharacter ( GLUT_BITMAP_TIMES_ROMAN_24 , s[i]);
};

void drawStringHelvetica (char *s) {
  unsigned int i;
  for (i = 0; i < strlen (s); i++)
    glutBitmapCharacter (GLUT_BITMAP_HELVETICA_18, s[i]);
};

void psiDraw (double *psi, double *x, int index)  {
  double ratio1, ratio2;
  double r1, r2;
  double xs, ys;
  long i;
  long nx;

  glLineWidth (1);
  glPushAttrib (GL_LINE_BIT);

		if (index == 1)
		{
		r1 = r2 = 4.0; nx = 512;
		glColor3f (1.0, 0.0, 0.0);
		glLineStipple (3, 0xAAAA);
		}

       if (index == 2)
		{
		r1= 4.4; r2 = 0.6;  nx = 512;
		glColor3f (0.0, 1.0, 0.0);
		glLineStipple (3, 0x0101);
		}

	   if (index == 3)
	   {
	   r1= 2.0; r2 = 4.0;  nx = 512;
	   glColor3f (0.0, 0.0, 1.0);
	   glLineStipple (3, 0x1C47);
	   }

  ratio1 = r1 / (XMAX - XMIN);
  ratio2 = r2 / (YMAX - YMIN);
  glBegin (GL_LINE_STRIP);
  for (i = 0; i < nx; i++)
    {
      xs = ratio1 * (x[i] - XMIN) - 1.0;
      ys = ratio2 * (psi[2*i] - YMIN) - 1.0;
      if (ys > -1.0 && ys < 1.0)
	glVertex2d (xs, ys);
    };
  glEnd ();

};

void  reshape (int w, int h)
{
  glMatrixMode (GL_MODELVIEW);
  glLoadIdentity ();
  glViewport (0, 0, w, h);
  glMatrixMode (GL_PROJECTION);
  glLoadIdentity ();
  gluOrtho2D (-1.2, 1.2, -1.2, 1.2);
  glEnable (GL_LINE_SMOOTH);
  glEnable (GL_LINE_STIPPLE);
};

void  display (void)  {
  static char label[100];
  glClear (GL_COLOR_BUFFER_BIT);


};


double  V (double x)  {
  double tmp = 0.005;
  tmp *= pow (x - 2.0, 2.0);
  tmp *= pow (x + 2.0, 2.0);
  return tmp;
};

void myinit ()  {
  int i;
  double tmp;
  double xinc;
  double pinc;

  // wave function
  xinc = (XMAX - XMIN) / (double) (points - 1);
  tmp = XMIN;
  for (i = 0; i < points; i++)
    {
      x[i] = tmp;
      tmp += xinc;
    };

  pinc = 2.0 * PI / xinc / points;
  tmp = 0.0;
  for (i = 0; i < points / 2; i++)
    {
      psi[i] = tmp;
      tmp += pinc;
      psi[points - i - 1] = -tmp;
    };

  for (i = 0; i < points; i++)
    pt[i] = V (x[i]);
};

int main (int argc, char **argv)  {
  myinit();
  glutInit (&argc, argv);
  glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGBA);
  glutInitWindowPosition (5, 5);
  glutInitWindowSize (WIDTH, HEIGHT);
  glutCreateWindow ("Antialiasing Functions - Line Stippling - Character-Attribute Functions");
  glutDisplayFunc (display);
  glutReshapeFunc (reshape);
  glutMainLoop ();

  return 0;
};

