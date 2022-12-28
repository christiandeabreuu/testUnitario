package br.com.zup.ezuppers.data.datasource.remote

import br.com.zup.ezuppers.data.model.CepResult
import br.com.zup.ezuppers.data.model.CitiesResult
import br.com.zup.ezuppers.data.model.StatesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface API {
    @GET("/api/v1/localidades/estados?orderBy=nome")
    suspend fun getStates(): StatesResult

    @GET("/api/v1/localidades/estados/{state}/municipios")
    suspend fun getCities(@Path("state") ufId:Int): CitiesResult

    @GET
    suspend fun getAddress(@Url url:String): CepResult
}