package ps.sipnas.data.model

class DataUploadListSPJ {
    val data: List<Data>? = null

    data class Data(
            val biaya: String? = "",
            val rincian: String? = "",
            val idSpd: String? = "",
            val id: String? = ""
    ) {
        override fun toString(): String {
            return "$rincian"
        }
    }

}
