package cn.nicole.test;

public class JniTest{
    static {
        System.loadLibrary("gmp");
        System.loadLibrary("rabbitmq");
        System.loadLibrary("sm2");
    }
    public native static String getPA();
    public native static void sign(String e,String r,String s,String x,String y,String d,int t,int n,int p);
    public native static int verify(String r,String s,String e,String x,String y);
    public native static void messageInit();
    public native static void messageControl(String message);
    public native static String test(String a);
}