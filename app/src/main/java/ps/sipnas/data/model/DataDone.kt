package ps.sipnas.data.model

class DataDone {
    val data: List<Data>? = null
    val meta: Meta? = null

    data class Data(
            val tanggalMulai: String? = "",
            val foto: String? = "",
            val rincianBiaya: List<RincianBiaya>? = null,
            val noSpt: String? = "",
            val totalBiaya: String? = "",
            val id: String? = "",
            val keberangkatan: String? = "",
            val durasi: String? = ""
    )

    data class RincianBiaya(
            val biaya: String? = "",
            val rincian: String? = "",
            val idSpd: String? = "",
            val id: String? = ""
    )

    data class Meta(
            val current_page: Int? = 0,
            val last_page: Int? = 0,
            val per_page: Int? = 0,
            val total: Int? = 0
    )
}
