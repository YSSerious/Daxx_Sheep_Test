package com.paazl.scheduling;

import com.paazl.data.CurrentBalance;
import com.paazl.data.Sheep;
import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import com.paazl.util.CurrentBalanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Component
public class IncomeTask {

    private CurrentBalanceRepository currentBalanceRepository;
    private SheepRepository sheepRepository;

    private BigInteger amountPerSheep;

    @Autowired
    public IncomeTask(
    		CurrentBalanceRepository currentBalanceRepository,
    		SheepRepository sheepRepository,
    		@Value("${amount_per_sheep}") BigInteger amountPerSheep) {
		this.currentBalanceRepository = currentBalanceRepository;
		this.sheepRepository = sheepRepository;
		this.amountPerSheep = amountPerSheep;
	}

	@Scheduled(cron="${scheduling.current_balance_update_cron}")
    @Transactional
    public void updateCurrentBalance() {
        CurrentBalance currentBalance;
        if ((currentBalance = currentBalanceRepository.findFirstByOrderByTimestampDesc()) == null) return;

        List<Sheep> sheep = sheepRepository.findAllByStateNot(State.DEAD);

        currentBalanceRepository.save(CurrentBalanceUtils.addBalance(currentBalance, amountPerSheep.multiply(BigInteger.valueOf(sheep.size()))));
    }
}
