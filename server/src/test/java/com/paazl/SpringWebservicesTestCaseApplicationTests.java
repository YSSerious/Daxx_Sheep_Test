package com.paazl;

import com.paazl.data.CurrentBalance;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import com.paazl.service.SheepStatusesDto;
import com.paazl.service.ShepherdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringWebservicesTestCaseApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public void testOrderNewSheep(){
		Integer priceOfNewSheep = 500;
		SheepRepository sheepRepository = mock(SheepRepository.class);

		CurrentBalanceRepository currentBalanceRepository = mock(CurrentBalanceRepository.class);
		when(currentBalanceRepository.findFirstByOrderByTimestampDesc()).thenReturn(null);
		ShepherdService shepherdService = new ShepherdService(sheepRepository, currentBalanceRepository, priceOfNewSheep);

		int nofSheepDesired = 10;
		int notEnoughBalance = 4900;
		assertTrue(shepherdService.orderNewSheep(nofSheepDesired).isEmpty());

		CurrentBalance currentBalance = new CurrentBalance();
		currentBalance.setBalance(BigInteger.valueOf(4900));
		when(currentBalanceRepository.findFirstByOrderByTimestampDesc()).thenReturn(currentBalance);
		assertEquals(shepherdService.orderNewSheep(nofSheepDesired), "You can't order " + nofSheepDesired + " sheeps. You have only " + notEnoughBalance + " money on balance!");

		currentBalance.setBalance(BigInteger.valueOf(5000));
		assertEquals(shepherdService.orderNewSheep(nofSheepDesired), "In total " + nofSheepDesired + " sheep were ordered and added to your flock!");

		ShepherdService shepherdServiceMock = mock(ShepherdService.class);
		doReturn(new SheepStatusesDto(nofSheepDesired, 0)).when(shepherdServiceMock).getSheepStatusses();
		assertEquals(10, shepherdServiceMock.getSheepStatusses().getNumberOfHealthySheep());
	}

}
