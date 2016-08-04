package llf.rx.interfaces;

import java.util.Map;

import llf.rx.bean.CookBean;
import llf.rx.bean.GeneralResult;
import llf.rx.bean.WeatherBean;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by llf on 2016/8/4.
 * http://apistore.baidu.com/microservice/weather?citypinyin=beijing
 */
public interface ServiceCenter {
    @POST("/microservice/weather")
    Observable<GeneralResult<WeatherBean>> getData(@QueryMap Map<String,Object> map);

    @POST("/microservice/cook")
    Observable<GeneralResult<CookBean>> getCook(@Query("name") String name);
}
