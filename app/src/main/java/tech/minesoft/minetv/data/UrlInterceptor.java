package tech.minesoft.minetv.data;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UrlInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl oldUrl = originalRequest.url();

        HttpUrl baseURL = null;
        //根据头信息中配置的value,来匹配新的base_url地址
        baseURL = HttpUrl.parse(RequestHelper.BASE_URLs[RequestHelper.urlIndex]);

        //重建新的HttpUrl，需要重新设置的url部分
        HttpUrl newHttpUrl = oldUrl.newBuilder()
                .scheme(baseURL.scheme())//http协议如：http或者https
                .host(baseURL.host())//主机地址
                .port(baseURL.port())//端口
                .build();

        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //获取处理后的新newRequest
        Request newRequest = builder.url(newHttpUrl).build();

        return chain.proceed(newRequest);
    }
}
