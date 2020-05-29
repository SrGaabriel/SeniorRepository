package dioray.datayy.thread;

import dioray.datayy.prototype.Team;
import dioray.datayy.repository.Repository;
import dioray.datayy.repository.team.TeamRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RaidExecutor {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final Repository<String, Team> teamRepository = TeamRepository.getInstance();

    public void execute() {
        executorService.scheduleAtFixedRate(() -> {
            for(Team team : teamRepository.getMap().values()) {
                if(!team.isRaiding()) continue;

                if(System.currentTimeMillis() >= team.getRemainingTime()) {

                }
            }
         }, 10, 10, TimeUnit.SECONDS);
    }

}
