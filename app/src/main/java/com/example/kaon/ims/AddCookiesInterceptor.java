package com.example.kaon.ims;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {



    private DalgonaSharedPreferences mDsp;



    public AddCookiesInterceptor(Context context){

        mDsp = DalgonaSharedPreferences.getInstanceOf(context);

    }



    @Override

    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();

        HashSet<String> preferences = (HashSet) mDsp.getHashSet(DalgonaSharedPreferences.KEY_COOKIE, new HashSet<String>());

        for (String cookie : preferences) {

            builder.addHeader("Cookie", cookie);

            Log.e("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp

        }



        return chain.proceed(builder.build());

    }

}