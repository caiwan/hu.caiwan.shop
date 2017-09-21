package hu.caiwan.shop.tasks;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.caiwan.shop.persist.dao.VerificationTokenRepository;

@Service
@Transactional
public class PurgeTokensTask {
    @Autowired
    VerificationTokenRepository tokenRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        Date now = Date.from(Instant.now());
        tokenRepository.deleteByExpirationLessThan(now);
    }
}
