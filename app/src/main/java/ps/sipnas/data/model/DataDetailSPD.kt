package ps.sipnas.data.model

data class DataDetailSPD(
	val angkutan: String? = "",
	val no: String? = "",
	val maksud: String? = "",
	val jabatan: String? = "",
	val tempatBerangkat: String? = "",
	val tempatTujuan: String? = "",
	val lamaPerjalanan: String? = "",
	val pembuatKomitmen: String? = "",
	val golongan: String? = "",
	val tingkatMenurutPeraturan:String?="",
	val mataAnggaran: String? = "",
	val pegawaiDiPerintahkan: String? = "",
	val tglBerangkat: String? = "",
	val tglKembali: String? = "",
	val instansi: String? = ""
)
