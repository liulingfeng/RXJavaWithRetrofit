package llf.rx.net;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import llf.rx.bean.CookBean;
import llf.rx.bean.GeneralResult;
import llf.rx.bean.WeatherBean;
import llf.rx.interfaces.ServiceCenter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by llf on 2016/8/4.
 * 封装的网络请求
 */
public class HttpUtil {
    public static final String BASE_URL = "http://apistore.baidu.com";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private ServiceCenter mServiceCenter;

    //构造方法私有
    private HttpUtil() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mServiceCenter = retrofit.create(ServiceCenter.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    //获取单例
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getWeather(Subscriber<WeatherBean> subscriber, Map<String,Object> map) {
       Observable observable = mServiceCenter.getData(map)
                .map(new Func1<GeneralResult<WeatherBean>, WeatherBean>() {

                    @Override
                    public WeatherBean call(GeneralResult<WeatherBean> retDataBeanGeneralResult) {
                        return retDataBeanGeneralResult.retData;
                    }
                });
        toSubscribe(observable,subscriber);
    }

    public void getCook(Subscriber<CookBean> subscriber,String name){
        Observable observable = mServiceCenter.getCook(name)
        .map(new Func1<GeneralResult<CookBean>, CookBean>() {

            @Override
            public CookBean call(GeneralResult<CookBean> retDataBeanGeneralResult) {
                return retDataBeanGeneralResult.retData;
            }
        });
        toSubscribe(observable,subscriber);
    }

    private void toSubscribe(Observable o, Subscriber s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
