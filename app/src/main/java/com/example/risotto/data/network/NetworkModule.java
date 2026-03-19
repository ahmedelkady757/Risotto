package com.example.risotto.data.network;

import com.example.risotto.core.utils.AppLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkModule {

    // ── Constants ──

    public static final String BASE_URL         = "https://www.themealdb.com/api/json/v1/1/";
    private static final long  TIMEOUT_SECONDS  = 30L;

    // ── Singleton ──

    private static volatile NetworkModule instance;

    private final Retrofit retrofit;

    private NetworkModule() {
        OkHttpClient okHttpClient = buildOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public static NetworkModule getInstance() {
        if (instance == null) {
            synchronized (NetworkModule.class) {
                if (instance == null) {
                    instance = new NetworkModule();
                }
            }
        }
        return instance;
    }

    // ── Public API ──

    public Retrofit getRetrofit() {
        return retrofit;
    }

    // ── OkHttp builder ──

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(new RisottoInterceptor())
                .build();
    }

    // ── Custom logging interceptor ──

    private static final class RisottoInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // ── Log request ──
            String requestBodyStr = readRequestBody(request);
            AppLogger.logRequest(
                    request.method(),
                    request.url().toString(),
                    request.headers().toString().trim(),
                    requestBodyStr
            );

            // ── Proceed ──
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                AppLogger.logNetworkError(request.url().toString(), e);
                throw e;
            }

            // ── Log response ──
            String responseBodyStr = readResponseBody(response);
            AppLogger.logResponse(
                    response.code(),
                    request.url().toString(),
                    responseBodyStr
            );

            return response;
        }

        // ── Helpers ──

        private String readRequestBody(Request request) {
            if (request.body() == null) return "";
            try {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (IOException e) {
                AppLogger.e("RisottoInterceptor: failed to read request body", e);
                return "";
            }
        }


        private String readResponseBody(Response response) {
            ResponseBody body = response.body();
            if (body == null) return "";
            try {
                BufferedSource source = body.source();
                source.request(Long.MAX_VALUE); // force full buffer
                Buffer clone = source.getBuffer().clone();

                Charset charset = StandardCharsets.UTF_8;
                if (body.contentType() != null
                        && body.contentType().charset() != null) {
                    charset = body.contentType().charset(StandardCharsets.UTF_8);
                }

                String raw = clone.readString(charset);

                if (raw.length() > 4000) {
                    return raw.substring(0, 4000) + "\n… (truncated, full length="
                            + raw.length() + ")";
                }
                return raw;

            } catch (IOException e) {
                AppLogger.e("RisottoInterceptor: failed to read response body", e);
                return "";
            }
        }
    }
}