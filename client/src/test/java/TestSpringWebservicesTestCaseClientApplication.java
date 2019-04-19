import com.paazl.dto.SheepStatusesDto;
import com.paazl.gui.GuiInterface;
import com.paazl.rest.ShepherdClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TestSpringWebservicesTestCaseClientApplication {

    private static final String DEFAULT_PATH = "http://localhost:8080/rest/shepherdmanager";

    @Test
    public void testGetServerStatus(){
        GuiInterface guiInterface = mock(GuiInterface.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        BigInteger balance = BigInteger.valueOf(5000);
        SheepStatusesDto sheepsStatus = new SheepStatusesDto(10, 0);
        ShepherdClient shepherdClient = new ShepherdClient(guiInterface, restTemplate);
        when(restTemplate.getForObject(DEFAULT_PATH + "/balance", BigInteger.class)).thenReturn(balance);
        when(restTemplate.getForObject(DEFAULT_PATH + "/sheepStatuses", SheepStatusesDto.class)).thenReturn(sheepsStatus);
        assertEquals(shepherdClient.getServerStatus(), String.format("Balance: %d, number of sheep healthy and dead: [%d, %d]", balance, sheepsStatus.getNumberOfHealthySheep(), sheepsStatus.getNumberOfDeadSheep()));
    }

    @Test
    public void testMakeOrder(){
        int sheepsAmount = 20;
        GuiInterface guiInterface = mock(GuiInterface.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        ShepherdClient shepherdClient = new ShepherdClient(guiInterface, restTemplate);
        Map<String, String> map = new HashMap<>();
        map.put("body", String.format("In total %s sheep were ordered and added to your flock!", sheepsAmount));
        ResponseEntity response = new ResponseEntity(map, HttpStatus.OK);
        when(restTemplate.postForEntity(DEFAULT_PATH + "/makeOrder", sheepsAmount, Map.class)).thenReturn(response);
        assertEquals(shepherdClient.makeOrder(sheepsAmount), String.format("In total %s sheep were ordered and added to your flock!", sheepsAmount));
    }
}
