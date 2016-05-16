package com.thalmic.android.sample.helloworld;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by MASTER on 16/05/2016.
 */
public class MyGLSurfaceView extends GLSurfaceView {
    private final OpenGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new OpenGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer((Renderer) mRenderer);
    }

}
