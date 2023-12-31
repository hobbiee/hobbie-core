package com.br.hobbie.modules.event.application;

import com.br.hobbie.modules.event.domain.entities.RequestStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ExpireJoinRequests implements RequestExpiration {

    private final EntityManager manager;

    /**
     * <h2>
     * Invalidate and expire join requests
     * </h2>
     *
     * <p>
     * There's a following situation where a join request should be expired:
     * </p>
     *
     * <ul>
     *     <li>
     *        The event starts and the request status is still PENDING
     *     </li>
     * </ul>
     */
    @Override
    @Transactional
    // execute a schedule task to run every 30 minutes to expire join requests
    @Scheduled(cron = "0 0/30 * * * *", zone = "America/Sao_Paulo")
    public void execute() {
        final boolean existsAnyPendingJoinRequest = manager.createQuery("""
                        SELECT COUNT(j)
                        FROM JoinRequest j
                        WHERE j.status = :status
                        """, Long.class)
                .setParameter("status", RequestStatus.PENDING)
                .getSingleResult() > 0;

        if (!existsAnyPendingJoinRequest) return;

        var nowWithTimezone = ZonedDateTime.now();
        final var query = """
                UPDATE join_request j
                SET j.status = 'EXPIRED'
                WHERE j.status = 'PENDING'
                AND j.event_id IN (
                    SELECT e.id
                    FROM event e
                    WHERE e.start_date <= :now
                );
                """;
        Query nativeQuery = manager.createNativeQuery(query);
        nativeQuery.setParameter("now", nowWithTimezone);
        nativeQuery.executeUpdate();
    }
}
