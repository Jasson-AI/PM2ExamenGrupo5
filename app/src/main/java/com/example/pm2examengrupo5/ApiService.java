package com.example.pm2examengrupo5;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PUT;

public interface ApiService {
    @GET("GetPersons.php")
    Call<List<Personas>> getPersons();

    @HTTP(method = "DELETE", path = "DeletePerson.php", hasBody = true)
    Call<Void> deletePerson(@Body Map<String, Integer> idMap);

    @PUT("UpdatePerson.php")
    Call<Void> updatePerson(@Body Personas persona);
}
