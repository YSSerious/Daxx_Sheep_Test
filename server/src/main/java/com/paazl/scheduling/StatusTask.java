package com.paazl.scheduling;

import com.paazl.SpringWebservicesTestCaseApplication;
import com.paazl.data.Sheep;
import com.paazl.data.State;
import com.paazl.data.repositories.CurrentBalanceRepository;
import com.paazl.data.repositories.SheepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class StatusTask {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private SheepRepository sheepRepository;
    private CurrentBalanceRepository currentBalanceRepository;

    @Autowired
    public StatusTask(
    		SheepRepository sheepRepository,
    		CurrentBalanceRepository currentBalanceRepository) {
		this.sheepRepository = sheepRepository;
		this.currentBalanceRepository = currentBalanceRepository;
	}

	@Scheduled(cron="${scheduling.status_cron}")
    @Transactional
    public void checkSheepStates() {
        List<Sheep> aliveSheep = sheepRepository.findAllByState(State.HEALTHY);
        List<Sheep> deadSheep = sheepRepository.findAllByState(State.DEAD);

        if (checkSheepRemaining(aliveSheep)) return;

        log.info(
            "Balance: {}, number of sheep healthy and dead: [{}, {}]",
            currentBalanceRepository.findFirstByOrderByTimestampDesc().getBalance().toString(),
            aliveSheep.size(),
            deadSheep.size());
    }

    private boolean checkSheepRemaining(List<Sheep> alive) {
        if (alive.isEmpty()) {
            printExitStatement();
            SpringWebservicesTestCaseApplication.context.close();
            return true;
        }
        return false;
    }

    private void printExitStatement() {
        log.info("");
        log.info("*********************************************************");
        log.info("* You lost! All sheep died, the shepherd goes bankrupt! *");
        log.info("*********************************************************");
        log.info("");
    }
}
