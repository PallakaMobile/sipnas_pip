package ps.sipnas.data.model

class DataProses {
    val data: Data? = null

    data class Data(
            val tanggalMulai: String? = "",
            val foto: String? = "",
            val rincianBiaya: List<RincianBiaya>? = null,
            val noSpt: String? = "",
            val totalBiaya: String? = "",
            val id: String? = "",
            val keberangkatan: String? = "",
            val durasi: String? = "",
            val statusUpload: String? = ""
    )

    data class RincianBiaya(
            val biaya: String? = "",
            val rincian: String? = "",
            val idSpd: String? = "",
            val id: String? = ""
    )
}
