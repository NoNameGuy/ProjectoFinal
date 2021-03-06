package com.thalmic.android.sample.helloworld.auxiliary;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by MASTER on 16/05/2016.
 */
public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private Cube mCube = new Cube();
    private float mCubeRotation;

    private float orientationW;
    private float orientationX;
    private float orientationY;
    private float orientationZ;

    public void setOrientation (float orientationW, float  orientationX, float orientationY, float orientationZ){

        this.orientationW = orientationW;
        this.orientationX = orientationX;
        this.orientationY = orientationY;
        this.orientationZ = orientationZ;

    }



    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(-3.0f, 0.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(-2.0f, 2.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 3.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(2.0f, 2.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(3.0f, 0.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(2.0f, -2.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, -3.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        gl.glLoadIdentity();
        gl.glTranslatef(-2.0f, -2.0f, -10.0f);
        gl.glRotatef(orientationW * (float) (180 / Math.PI), orientationX * (float) (180 / Math.PI), orientationY * (float) (180 / Math.PI), orientationZ * (float) (180 / Math.PI));
        mCube.draw(gl);

        mCubeRotation -= 0.15f;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }



}
