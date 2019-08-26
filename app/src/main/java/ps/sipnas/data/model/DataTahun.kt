package ps.sipnas.data.model

class DataTahun {
    val tahun: List<TahunItem>? = null

    data class TahunItem(
            val year: String? = ""
    ) {
        override fun toString(): String {
            return "Tahun $year"
        }
    }
}
