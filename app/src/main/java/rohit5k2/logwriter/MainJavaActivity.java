package rohit5k2.logwriter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);

        final Button btn = findViewById(R.id.btn);
        btn.setBackgroundColor(0xFFFF0000);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Throwing a NPE to log
                throw new NullPointerException();
            }
        });
    }
}
