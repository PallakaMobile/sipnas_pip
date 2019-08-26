package ps.sipnas.data.model

class DataStatistik {
    val totalAnggaran: String? = ""
    val peringkat: String? = ""
    val statistik: List<StatistikItem>? = null
    val jmlKeberangkatan: String? = ""

    data class StatistikItem(
            val jumlahOrang: String? = "",
            val minggu: String? = "",
            val bulan: Int? = 0
    )
}
