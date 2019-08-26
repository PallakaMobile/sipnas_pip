package ps.sipnas.data.rest

import io.reactivex.Flowable
import okhttp3.MultipartBody
import ps.sipnas.data.model.*
import retrofit2.Response
import retrofit2.http.*

//TODO interface Retrofit Cyclops API
interface SipnasAPI {
    @FormUrlEncoded
    @POST("oauth/token")
    fun login(@FieldMap headers: LinkedHashMap<String, String>): Flowable<Response<DataLogin>>

    @GET("api/spd/proses")
    fun proses(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataProses>>

    @GET("api/user")
    fun profile(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataProfile>>

    @Multipart
    @POST("api/image/profile/upload")
    fun profilePict(@HeaderMap headers: LinkedHashMap<String, String>, @Part photo: MultipartBody.Part): Flowable<Response<DataMessage>>

    @GET("api/spd/selesai")
    fun done(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataDone>>

    @GET("api/list/image/spj")
    fun imageSpj(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataImageSPJ>>

    @GET("api/list/image/perjalanan")
    fun imageKegiatan(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataImageKegiatan>>

    @POST("api/revoke")
    fun logout(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataMessage>>

    @GET("api/list/rincian")
    fun listUploadSPJ(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataUploadListSPJ>>

    @Multipart
    @POST("api/bukti/upload")
    fun uploadSPJKegiatan(@HeaderMap headers: LinkedHashMap<String, String>, @Part photo: MultipartBody.Part): Flowable<Response<DataUpload>>

    @GET("api/details/spd")
    fun detailSPD(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataDetailSPD>>

    @DELETE("api/image/{imageID}")
    fun deteleImage(@HeaderMap headers: LinkedHashMap<String, String>, @Path("imageID") imageID: String): Flowable<Response<DataMessage>>

    @GET("api/notification/all")
    fun notification(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataNotification>>

    @GET("api/statistik")
    fun statistik(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataStatistik>>

    @GET("api/statistik/tahun")
    fun tahun(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataTahun>>

    @POST("api/password/reset")
    fun resetPass(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataUpdatePass>>
}
