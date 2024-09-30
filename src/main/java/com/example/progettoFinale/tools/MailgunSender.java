package com.example.progettoFinale.tools;

import Epicode.organizzatoreEventi.entities.Utente;
import com.example.progettoFinale.entities.Dipendente;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {
    private String apiKey;
    private String domainKey;

    public MailgunSender(@Value("${mailgun.key}") String apiKey, @Value("${mailgun.domain}") String domainKey) {
        this.apiKey = apiKey;
        this.domainKey = domainKey;
    }

    public void sendRegistrationEmail(Dipendente ricevente) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainKey + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "eddy.turpo@gmail.com")
                .queryString("to", ricevente.getEmail())
                .queryString("subject", "Registrazione completata")
                .queryString("text", "Ciao ,grazie per esserti registrato")
                .asJson();
        System.out.println(response.getBody());
    }
}
