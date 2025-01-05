package com.alura.convertidor.principal;

import com.alura.convertidor.principal.Conversiones;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaConvertidor {
    private static final String API_KEY = "57e10440419df6a14a7266c8";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient client;
    private final Gson gson;

    public ConsultaConvertidor(HttpClient client) {
        this.client = client;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Conversiones tipoDeCambio(String deMoneda, String aMoneda) throws IOException, InterruptedException {
        String url = BASE_URL + API_KEY + "/pair/" + deMoneda + "/" + aMoneda;
        URI direccion = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Validar el código de respuesta HTTP
        if (response.statusCode() != 200) {
            throw new IOException("Error en la API. Código de estado: " + response.statusCode());
        }

        // Convertir la respuesta JSON a objeto
        Conversiones conversion = gson.fromJson(response.body(), Conversiones.class);

        // Validar que la conversión fue exitosa
        if (!"success".equals(conversion.result())) {
            throw new RuntimeException("Error en la conversión: " + conversion.result());
        }

        // Validar que tenemos una tasa de conversión
        if (conversion.conversionRate() == null) {
            throw new RuntimeException("No se recibió una tasa de conversión válida");
        }

        return conversion;
    }
}
