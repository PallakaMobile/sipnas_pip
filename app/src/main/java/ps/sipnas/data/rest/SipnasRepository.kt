package ps.sipnas.data.rest

import io.reactivex.Flowable
import okhttp3.MultipartBody
import ps.sipnas.data.model.*
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.HeaderMap
import retrofit2.http.Part
import retrofit2.http.Path

/**
 **********************************************
 * Created by ukie on 10/1/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */

interface SipnasRepository {
    fun login(@FieldMap headers: LinkedHashMap<String, String>): Flowable<Response<DataLogin>>
    fun proses(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataProses>>
    fun profile(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataProfile>>
    fun done(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataDone>>
    fun imageSPJ(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataImageSPJ>>
    fun imageKegiatan(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataImageKegiatan>>
    fun logout(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataMessage>>
    fun listUploadSPJ(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataUploadListSPJ>>
    fun uploadSPJKegiatan(@HeaderMap headers: LinkedHashMap<String, String>, @Part photo: MultipartBody.Part): Flowable<Response<DataUpload>>
    fun detailSPD(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataDetailSPD>>
    fun deleteImage(@HeaderMap headers: LinkedHashMap<String, String>, @Path("imageID") imageID: String): Flowable<Response<DataMessage>>
    fun notification(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataNotification>>
    fun profilPict(@HeaderMap headers: LinkedHashMap<String, String>, @Part photo: MultipartBody.Part): Flowable<Response<DataMessage>>
    fun statistik(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataStatistik>>
    fun tahun(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataTahun>>
    fun resetPass(@HeaderMap headers: LinkedHashMap<String, String>): Flowable<Response<DataUpdatePass>>
}

class SipnasRepositoryImpl(private val sipnasAPI: SipnasAPI) : SipnasRepository {

    override fun login(headers: LinkedHashMap<String, String>): Flowable<Response<DataLogin>> =
            sipnasAPI.login(headers)

    override fun proses(headers: LinkedHashMap<String, String>): Flowable<Response<DataProses>> =
            sipnasAPI.proses(headers)

    override fun profile(headers: LinkedHashMap<String, String>): Flowable<Response<DataProfile>> =
            sipnasAPI.profile(headers)

    override fun done(headers: LinkedHashMap<String, String>): Flowable<Response<DataDone>> =
            sipnasAPI.done(headers)

    override fun imageSPJ(headers: LinkedHashMap<String, String>): Flowable<Response<DataImageSPJ>> =
            sipnasAPI.imageSpj(headers)

    override fun imageKegiatan(headers: LinkedHashMap<String, String>): Flowable<Response<DataImageKegiatan>> =
            sipnasAPI.imageKegiatan(headers)

    override fun logout(headers: LinkedHashMap<String, String>): Flowable<Response<DataMessage>> =
            sipnasAPI.logout(headers)

    override fun listUploadSPJ(headers: LinkedHashMap<String, String>): Flowable<Response<DataUploadListSPJ>> =
            sipnasAPI.listUploadSPJ(headers)

    override fun uploadSPJKegiatan(headers: LinkedHashMap<String, String>, photo: MultipartBody.Part): Flowable<Response<DataUpload>> =
            sipnasAPI.uploadSPJKegiatan(headers, photo)

    override fun detailSPD(headers: LinkedHashMap<String, String>): Flowable<Response<DataDetailSPD>> =
            sipnasAPI.detailSPD(headers)

    override fun deleteImage(headers: LinkedHashMap<String, String>, imageID: String): Flowable<Response<DataMessage>> =
            sipnasAPI.deteleImage(headers, imageID)

    override fun notification(headers: LinkedHashMap<String, String>): Flowable<Response<DataNotification>> =
            sipnasAPI.notification(headers)

    override fun profilPict(headers: LinkedHashMap<String, String>, photo: MultipartBody.Part): Flowable<Response<DataMessage>> =
            sipnasAPI.profilePict(headers, photo)

    override fun statistik(headers: LinkedHashMap<String, String>): Flowable<Response<DataStatistik>> =
            sipnasAPI.statistik(headers)

    override fun tahun(headers: LinkedHashMap<String, String>): Flowable<Response<DataTahun>> =
            sipnasAPI.tahun(headers)

    override fun resetPass(headers: LinkedHashMap<String, String>): Flowable<Response<DataUpdatePass>> =
            sipnasAPI.resetPass(headers)
}