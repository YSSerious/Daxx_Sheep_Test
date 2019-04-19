package com.paazl.rest;

import com.paazl.service.SheepStatusesDto;
import com.paazl.service.ShepherdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.math.BigInteger;

@Path("/shepherdmanager")
@Service
public class ShepherdResource {
    private ShepherdService service;

    @Autowired
    public ShepherdResource(ShepherdService service) {
		this.service = service;
	}

	@GET
    @Path("/balance")
    public BigInteger getBalance() {
        return service.getBalance();
    }

    @GET
    @Path("/sheepStatuses")
    public SheepStatusesDto getSheepStatuses() {
        return service.getSheepStatusses();
    }

    @POST
    @Path("/makeOrder")
    public ResponseEntity<String> makeOrder(int sheepsAmount) {
        return new ResponseEntity<>(service.orderNewSheep(sheepsAmount), HttpStatus.OK);
    }
}