package com.example.jnidemo.NdkUtils;

public class NdkUtilsStatic extends NdkUtilsBase{

    private static final String JnilibPath = "demo_jni_static";
    static{
        System.loadLibrary(JnilibPath);
    }

    @Override
    public native String getTime();
}
