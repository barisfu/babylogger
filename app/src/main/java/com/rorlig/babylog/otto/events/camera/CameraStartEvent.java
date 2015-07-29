package com.rorlig.babylog.otto.events.camera;

import android.net.Uri;

/**
 * Created by rorlig on 6/18/15.
 */
public class CameraStartEvent {
    private Uri imageUri = null;

    public CameraStartEvent(Uri imageUri) {
        this.imageUri  = imageUri;
    }

    public CameraStartEvent(){

    }

    public Uri getImageUri() {
        return imageUri;
    }
}
