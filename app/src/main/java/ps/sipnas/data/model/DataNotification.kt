package ps.sipnas.data.model

class DataNotification {
    val data: List<Data>? = null

    data class Data(
            val hours: String? = "",
            val date: String? = "",
            val id: Int? = null,
            val idUser: Int? = null,
            val content: String? = ""
    )

}
