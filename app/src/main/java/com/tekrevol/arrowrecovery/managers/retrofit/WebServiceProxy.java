package com.tekrevol.arrowrecovery.managers.retrofit;


import com.tekrevol.arrowrecovery.models.receiving_model.DataPriceModel;
import com.tekrevol.arrowrecovery.models.receiving_model.Product;
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel;
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by khanhamza on 09-Mar-17.
 */

public interface WebServiceProxy {


    @POST("api/v1/{path}")
    Call<Object> webServiceRequestAPIForJustObject(
            @Path(value = "path", encoded = true) String postfix,
            @Body RequestBody requestData
    );


    @DELETE("api/v1/{path}")
    Call<WebResponse<Object>> deleteAPIWebResponseAnyObject(
            @Path(value = "path", encoded = true) String postfix
    );

    @POST("api/v1/{path}")
    Call<WebResponse<Object>> postAPIWebResponseAnyObject(
            @Path(value = "path", encoded = true) String postfix,
            @Body RequestBody requestData
    );


    @Multipart
    @POST("api/v1/{path}")
    Call<WebResponse<Object>> postMultipartAPI(
            @Path(value = "path", encoded = true) String postfix,
            @Part ArrayList<MultipartBody.Part> body

    );


    @Multipart
    @POST("api/v1/{path}")
    Call<WebResponse<Object>> postMultipartWithSameKeyAPI(
            @Path(value = "path", encoded = true) String postfix,
            @Part ArrayList<MultipartBody.Part> body,
            @Part MultipartBody.Part[] attachment
    );


    /**
     * @param postfix
     * @param queryMap
     * @return
     */


    @GET("api/v1/{path}")
    Call<WebResponse<Object>> getAPIForWebresponseAnyObject(
            @Path(value = "path", encoded = true) String postfix,
            @QueryMap Map<String, Object> queryMap
    );

    @GET("JOHNMATT/{path}")
    Call<DataPriceModel> getAPIRhodiumPriceForWebresponseAnyObject(
            @Path(value = "path", encoded = true) String postfix,
            @QueryMap Map<String, Object> queryMap
    );

    @GET("api/v1/{path}")
    Observable<WebResponse<Object>> getProductsSearchByQuery(
            @Path(value = "path", encoded = true) String postfix,
            @QueryMap Map<String, Object> queryMap
    );
}

