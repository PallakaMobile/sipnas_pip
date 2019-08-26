package ps.sipnas.data.model

import android.os.Parcel
import android.os.Parcelable

class DataProfile {
    val data: Data? = null

    data class Data(
            val nip: String? = "",
            val foto: String? = "",
            val jabatan: String? = "",
            val golongan: String? = "",
            val galeri: List<GaleriItem>? = null,
            val jenisKelamin: String? = "",
            val email: String? = "",
            val namaLengkap: String? = "",
            val aktifitas: List<AktifitasItem>? = null
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                ArrayList<GaleriItem>().apply { source.readList(this, GaleriItem::class.java.classLoader) },
                source.readString(),
                source.readString(),
                source.readString(),
                source.createTypedArrayList(AktifitasItem.CREATOR)
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(nip)
            writeString(foto)
            writeString(jabatan)
            writeString(golongan)
            writeList(galeri)
            writeString(jenisKelamin)
            writeString(email)
            writeString(namaLengkap)
            writeTypedList(aktifitas)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
                override fun createFromParcel(source: Parcel): Data = Data(source)
                override fun newArray(size: Int): Array<Data?> = arrayOfNulls(size)
            }
        }
    }

    data class AktifitasItem(
            val id: String? = "",
            val idUser: String? = "",
            val content: String? = "",
            val hours: String? = "",
            val date: String? = ""
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(id)
            writeString(idUser)
            writeString(content)
            writeString(hours)
            writeString(date)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<AktifitasItem> = object : Parcelable.Creator<AktifitasItem> {
                override fun createFromParcel(source: Parcel): AktifitasItem = AktifitasItem(source)
                override fun newArray(size: Int): Array<AktifitasItem?> = arrayOfNulls(size)
            }
        }
    }

    data class GaleriItem(
            val imageUrl: String? = "",
            val id: String? = "",
            val ket: String? = ""
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(imageUrl)
            writeString(id)
            writeString(ket)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<GaleriItem> = object : Parcelable.Creator<GaleriItem> {
                override fun createFromParcel(source: Parcel): GaleriItem = GaleriItem(source)
                override fun newArray(size: Int): Array<GaleriItem?> = arrayOfNulls(size)
            }
        }
    }
}
