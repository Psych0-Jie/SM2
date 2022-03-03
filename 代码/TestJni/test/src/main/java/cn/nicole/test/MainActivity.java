package cn.nicole.test;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.encoders.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import static cn.nicole.test.GeneratePk10.byteMerger;
import static cn.nicole.test.GeneratePk10.genCSR;

public class MainActivity extends AppCompatActivity {

    private TextView tvSign;
    private Button btn1;
    private Button btnStart;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                tvSign.setText((String)msg.obj);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSign = findViewById(R.id.tvSign);

        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JniTest jniTest = new JniTest();
                        jniTest.messageInit();
                    }
                }).start();
            }
        });

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String x = null;
                        JniTest jniTest = new JniTest();
                        x = jniTest.getPA();
                        if(x != null){
                            Message msg = new Message();
                            msg.what = 100;
                            msg.obj = x;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });

    }

    public void test(){
        JniTest jniTest = new JniTest();
        String a = jniTest.test("nicole");
        tvSign.setText(a);
    }

    public void message(){
        JniTest jniTest = new JniTest();
        jniTest.messageControl("hello");
    }

    public void generateCSR(){
        System.out.println("start");
        try {
            String dn = "CN=dfg, OU=aert, O=45y, L=sdfg, ST=fg, C=CN";
            try {
                String pkStr = "AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAv/pInHHFzGAdhIRGDKOc2bjq9I3SUGIOIcMRwgMSpqEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIWXe67pEetAHBkEPY2Mi5B1TLu0+fH0z5gosfV21aUO";
                byte[] pkdata = Base64.decode(pkStr);
                byte[] x = new byte[32];
                byte[] y = new byte[32];
                System.arraycopy(pkdata, 36, x, 0, 32);
                System.arraycopy(pkdata, 36 + 32 + 32, y, 0, 32);
                byte[] data = byteMerger(x, y);
                genCSR(dn,  data);
            } catch (OperatorCreationException e) {
                e.printStackTrace();
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }

}
