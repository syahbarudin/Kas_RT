package id.kasrt.model

import com.google.gson.annotations.SerializedName

data class LaporanKeuangan(
    @SerializedName("sisa_saldo_maret")
    val sisaSaldomaret: String,
    @SerializedName("masuk")
    val masuk: String,
    @SerializedName("keluar")
    val keluar: String,
    @SerializedName("sisa_saldo_april")
    val sisaSaldoApril: String
)
