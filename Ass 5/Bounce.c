#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* author @ Linwei Yuan
 * This program convert the bounce class in java into a C program
 * Basically simulate and track a bouncing ball movement and print out data in terms of time, position and velocity
 */

#define g 9.8// MKS gravitational constant 9.8 m/s^2
#define Pi 3.141592654// To convert degrees to radians
#define Xinit 5.0 // initial ball location (X)
#define TICK 0.1 // Clock tick duration (sec)
#define ETHER 0.01 // If either Vx or Vy<ETHER STOP
#define m 1.0 // given m = 1.0 kg
#define k 0.0016 // given k = 0.0016
// define variables that will be used
#define false 0 // print if test true
#define true ! false

int main() {

    double time = 0.0;// set time start at 0
    double Vo;
    double theta;
    double loss;
    double bSize;

    printf("Enter the initial velocity of the ball in meters/second [0,100]: ");
    scanf("%lf", &Vo);
    printf("Enter the launch angle in degree [0,90]: ");
    scanf("%lf", &theta);
    printf("Enter energy loss parameter [0,1]: ");
    scanf("%lf", &loss);
    printf("Enter the radius of the ball in meters [0.1,5.0]: ");
    scanf("%lf", &bSize);

    // initialize Variables
    double Yinit = bSize; // initial ball location (Y)
    double Vt = g / (4 * Pi * bSize * bSize * k);
    double Vox = Vo * cos(theta * Pi / 180);
    double Voy = Vo * sin(theta * Pi / 180);
    double X, Y;
    double Xlast = Xinit, Ylast = Yinit;
    double Vx, Vy;
    double KEx = 0.5 * m * Vox * Vox, KEy = 0.5 * m * Voy * Voy;
    double XcollisionAdjust = 0; // time will be zero after each collision since I set time=0 below, to make the ball jump continuously, initialize a new variable
    double t = 0; // another variable to calculate total time
//Simulation loop
    while (true) {

        X = Vox * Vt / g * (1 - exp(-g * time / Vt)) + XcollisionAdjust; // after adding XcollisionAdjust, next jumping will start from where it lands
        Y = bSize + Vt / g * (Voy + Vt) * (1 - exp(-g * time / Vt)) - Vt * time;
        Vx = (X - Xlast) / TICK;
        Vy = (Y - Ylast) / TICK;
        Xlast = X;//give Xlast new X position
        Ylast = Y;//give Ylast new Y position
        if (Y < 0) Y = 0; // prevent the ball from being below Ground at the first contact with Ground
        time = time + TICK;

        // this if statement only happens when ball touches the ground
        if (Vy < 0 && Y <= bSize) {
            KEx = 0.5 * Vx * Vx * (1 - loss);
            KEy = 0.5 * Vy * Vy * (1 - loss);
            Vox = sqrt(2 * KEx);
            Voy = sqrt(2 * KEy);
            time = 0; // to prevent the next ball movement from starting below the ground
            XcollisionAdjust = Xlast; // give XcollisionAjust the X position of the landing ball, so that next while loop start from the last X position
        }

        if ((KEx <= ETHER) | (KEy <= ETHER)) break;

        if (time == 0.1) {
            Vx = Vox;
            Vy = Voy;
        } //eliminate the problem that at time == 0.1, the data is not compatible
        if (true)
            printf("t: %.2f   X: %.2f   Y: %.2f   Vx: %.2f   Vy:%.2f\n", t, Xinit + X, Y, Vx, Vy);
        t += TICK; //add after println so that data at t==0 can be printed
    }
    return 0;
}