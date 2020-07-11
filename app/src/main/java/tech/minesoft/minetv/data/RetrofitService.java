package tech.minesoft.minetv.data;

import tech.minesoft.minetv.vo.VodListVo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

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
    Call<VodListVo> list(@Query("wd")String wd, @Query("pg")Integer pg);


    /**
     * ac=detail&pg=1&t=1&pg=1&h=1&ids=1
     *
     * @param wd 搜索关键字
     * @param pg 页码
     *
     * @return
     */
    @GET("?ac=detail")
    Call<VodListVo> detail(@Query("wd")String wd, @Query("pg")Integer pg);


    /**
     * ac=detail&pg=1&t=1&pg=1&h=1
     *
     * @param ids 数据ID
     *
     * @return
     */
    @GET("?ac=detail")
    Call<VodListVo> detail(@Query("ids")Integer ids);

}
