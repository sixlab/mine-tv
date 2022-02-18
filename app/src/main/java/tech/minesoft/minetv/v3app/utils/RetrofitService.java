package tech.minesoft.minetv.v3app.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import tech.minesoft.minetv.v3app.vo.InitVo;
import tech.minesoft.minetv.v3app.vo.MovieListVo;

public interface RetrofitService {

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.26 Safari/537.36 Edg/85.0.564.13")
    @GET("init.json")
    Call<InitVo> init();


    /**
     *
     * ac=list&wd=1&pg=1&t=1&pg=1&h=1
     *
     * @param wd 搜索关键字
     * @param pg 页码
     *
     * @return
     */
    @GET("?ac=list")
    Call<MovieListVo> list(@Query("wd")String wd, @Query("pg")Integer pg);

    /**
     * ac=detail&pg=1&t=1&pg=1&h=1&ids=1
     *
     * @param wd 搜索关键字
     * @param pg 页码
     *
     * @return
     */
    @GET("?ac=detail")
    Call<MovieListVo> detail(@Query("wd")String wd, @Query("pg")Integer pg);


    /**
     * ac=detail&pg=1&t=1&pg=1&h=1
     *
     * @param ids 数据ID
     *
     * @return
     */
    @GET("?ac=detail")
    Call<MovieListVo> detail(@Query("ids") Integer ids);
}
