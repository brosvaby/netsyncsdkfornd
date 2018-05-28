package com.inka.netsync.data.resource;

import android.content.Context;

/**
 * Created by birdgang on 2018. 1. 24..
 */
public interface ResourcesHelper {
    Context getContext ();
    String getStringResource (int resource);
    String[] getStringArrayResource (int resource);
}