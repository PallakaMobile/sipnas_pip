package ps.sipnas.data.model

class DataImageSPJ {
    val data: List<Data>? = null

    data class Data(
            val imageUrl: String? = "",
            val id: String? = "",
            val ket: String? = ""
    )
}
