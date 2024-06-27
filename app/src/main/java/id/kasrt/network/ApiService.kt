package id.kasrt.network

import id.kasrt.model.CashflowItem
import id.kasrt.model.DataItem
import id.kasrt.model.LaporanKeuangan
import id.kasrt.model.ResponseUser

import retrofit2.Call

import retrofit2.http.GET

interface ApiService {
    @GET(".")
    fun getUsers(): Call<ResponseUser<List<DataItem>>>
    @GET(".")
    fun getLaporanKeuangan(): Call<ResponseUser<LaporanKeuangan>>
    @GET(".")
    fun getCashflow(): Call<ResponseUser<List<CashflowItem>>>



}