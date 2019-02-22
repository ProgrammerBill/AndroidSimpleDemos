package com.example.jnidemo.NdkUtils;

public class NdkUtilsDynamic extends NdkUtilsBase {

    private static final String JnilibPath = "demo_jni_dynamic";
    static{
        System.loadLibrary(JnilibPath);
    }

    @Override
    public native String getTime();
}
