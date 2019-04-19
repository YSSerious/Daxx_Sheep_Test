package com.paazl.service;

import com.paazl.data.CurrentBalance;
import com.paazl.data.Sheep;
import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import com.paazl.util.CurrentBalanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ShepherdService {
    private SheepRepository sheepRepository;
    private CurrentBalanceRepository currentBalanceRepository;

    @SuppressWarnings("unused")
	private Integer priceOfSheep;

    @Autowired
    public ShepherdService(
    		SheepRepository sheepRepository,
    		CurrentBalanceRepository currentBalanceRepository,
    		@Value("${price_of_new_sheep}") Integer priceOfSheep) {
		this.sheepRepository = sheepRepository;
		this.currentBalanceRepository = currentBalanceRepository;
		this.priceOfSheep = priceOfSheep;
	}

	public SheepStatusesDto getSheepStatusses() {
        List<Sheep> healthySheep = sheepRepository.findAllByState(State.HEALTHY);
        List<Sheep> deadSheep = sheepRepository.findAllByState(State.DEAD);

        return new SheepStatusesDto(
            healthySheep.size(),
            deadSheep.size()
        );
    }

    public BigInteger getBalance() {
        return currentBalanceRepository.findFirstByOrderByTimestampDesc().getBalance();
    }

    public Integer getDeadSheepsAmount() {
        return sheepRepository.findAllByState(State.DEAD).size();
    }

    @Transactional
    public synchronized String orderNewSheep(int nofSheepDesired) {
        // TODO Implement sheep ordering feature
    	// TODO Write unit tests
        CurrentBalance currentBalance;
        BigInteger orderCost = BigInteger.valueOf(nofSheepDesired * priceOfSheep);
        if ((currentBalance = currentBalanceRepository.findFirstByOrderByTimestampDesc()) == null)
            return "";
        if(currentBalance.getBalance().subtract(orderCost).signum() == -1){
            return String.format("You can't order %d sheeps. You have only %d money on balance!", nofSheepDesired, currentBalance.getBalance());
        }
        IntStream.rangeClosed(1, nofSheepDesired)
                .forEach(i -> sheepRepository.save(new Sheep()));

        currentBalanceRepository.save(CurrentBalanceUtils.subtractBalance(currentBalance, orderCost));
        return String.format("In total %s sheep were ordered and added to your flock!", nofSheepDesired);
    }
}