package com.example.picktheperfectwatermelon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

public class ResultActivity extends AppCompatActivity {

    private NumberProgressBar result_waterCon_Pro,result_sweetness_Pro,result_texture_Pro;
    private TextView result_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initView();
        Intent intent = getIntent();
        setData(intent.getIntExtra("waterCon",0),
                intent.getIntExtra("sweetness",0),
                intent.getIntExtra("texture",0));
    }

    private void setData(int waterCon,int sweetness,int texture){
        result_waterCon_Pro.setProgress(waterCon);
        result_sweetness_Pro.setProgress(sweetness);
        result_texture_Pro.setProgress(texture);

    }

    private void initView(){
        result_waterCon_Pro = (NumberProgressBar)findViewById(R.id.result_waterCon_Pro);
        result_sweetness_Pro = (NumberProgressBar)findViewById(R.id.result_sweetness_Pro);
        result_texture_Pro = (NumberProgressBar)findViewById(R.id.result_texture_Pro);
        result_tv = (TextView)findViewById(R.id.result_tv);

        result_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this,MainActivity.class));
                finish();
            }
        });
    }

}
