package tech.minesoft.minetv.utils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.minesoft.minetv.vo.InitVo;
import tech.minesoft.minetv.vo.MovieListVo;

public interface RetrofitService {

    @GET("init.json")
    Call<InitVo> init();

    @GET("{version}.json")
    Call<Map> api(@Path("version") int version);


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
