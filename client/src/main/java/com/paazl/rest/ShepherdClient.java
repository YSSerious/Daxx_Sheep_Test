package com.paazl.rest;

import com.paazl.dto.SheepStatusesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.paazl.gui.GuiInterface;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Map;

@Component
public class ShepherdClient {

    /*
        TODO Use a Rest client to obtain the server status, so this client can be used to obtain that status.
        TODO Write unit tests.
     */

    private static final String DEFAULT_PATH = "http://localhost:8080/rest/shepherdmanager";
    private static final String SERVER_IS_NOT_RESPONDING = "Server currently is offline or you are a bankrupt";

    private RestTemplate restTemplate;

    @Autowired
    public ShepherdClient(GuiInterface guiInterface, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        guiInterface.addOrderRequestListener(i -> {
            guiInterface.addServerFeedback("Number of sheep to order: " + i);
            guiInterface.addServerFeedback(makeOrder(i));
        });
    }

    public String getServerStatus() {
        try {
            BigInteger balance = restTemplate.getForObject(DEFAULT_PATH + "/balance", BigInteger.class);
            SheepStatusesDto sheepsStatus = restTemplate.getForObject(DEFAULT_PATH + "/sheepStatuses", SheepStatusesDto.class);
            return parseServerStatus(balance, sheepsStatus.getNumberOfHealthySheep(), sheepsStatus.getNumberOfDeadSheep());
        } catch (ResourceAccessException e) {
            return SERVER_IS_NOT_RESPONDING;
        }
    }

    private String parseServerStatus(BigInteger balance, Integer aliveSheepsAmount, Integer deadSheepsAmount) {
        return String.format("Balance: %d, number of sheep healthy and dead: [%d, %d]", balance, aliveSheepsAmount, deadSheepsAmount);
    }

    public String makeOrder(int sheepsAmount) {
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(DEFAULT_PATH + "/makeOrder", sheepsAmount, Map.class);
            return response.getBody().get("body").toString();
        } catch (ResourceAccessException e) {
            return SERVER_IS_NOT_RESPONDING;
        }
    }
}
