package llf.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import llf.rx.bean.WeatherBean;
import llf.rx.net.HttpUtil;
import llf.rx.substribles.ProgressSubscriber;
import llf.rx.substribles.SubscriberOnNextListener;

public class MainActivity extends AppCompatActivity {
    private Map<String,Object> map;
    private SubscriberOnNextListener getWeatherNext = new SubscriberOnNextListener<WeatherBean>() {
        @Override
        public void onNext(WeatherBean result) {
            Toast.makeText(MainActivity.this,result.weather,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new HashMap<>();
        map.put("citypinyin","beijing");
    }

    public void getData(View v) {
        HttpUtil.getInstance().getWeather(new ProgressSubscriber(getWeatherNext, this),map);
    }
}
