package com.sundolls.epbackend.cronJob;

import com.sundolls.epbackend.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankScheduler {
    private final RankService rankService;

    @Scheduled(cron = "0 0 1 * * *")
    public void rankInit() {
        rankService.deleteAllCache();
        rankService.getSchoolRanking(15);
        rankService.getUserRanking(50);
    }
}
