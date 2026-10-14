package com.example.proyectoaplicativo.data.remote.api

import com.example.proyectoaplicativo.data.remote.dto.ApiResponseDto
import com.example.proyectoaplicativo.data.remote.dto.ClientesDataDto
import com.example.proyectoaplicativo.data.remote.dto.InsertarTicketDto
import com.example.proyectoaplicativo.data.remote.dto.TicketDto
import com.example.proyectoaplicativo.data.remote.dto.TicketsDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketApiService {
    @GET("tickets")
    suspend fun getTickets(): ApiResponseDto<TicketsDataDto>

    @GET("tickets/{id}")
    suspend fun getTicketById(@Path("id") id: String): ApiResponseDto<TicketDto>

    @POST("tickets")
    suspend fun insertarTicket(@Body ticket: InsertarTicketDto): ApiResponseDto<TicketDto>

    @PUT("tickets/{id}")
    suspend fun actualizarTicket(
        @Path("id") id: String,
        @Body ticket: InsertarTicketDto
    ): ApiResponseDto<TicketDto>

    @DELETE("tickets/{id}")
    suspend fun eliminarTicket(@Path("id") id: String): Response<Unit>

    @GET("clientes")
    suspend fun getClientes(): ApiResponseDto<ClientesDataDto>
}