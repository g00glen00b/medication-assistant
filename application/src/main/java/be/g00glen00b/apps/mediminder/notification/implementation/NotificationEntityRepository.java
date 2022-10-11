package be.g00glen00b.apps.mediminder.notification.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

interface NotificationEntityRepository extends JpaRepository<NotificationEntity, UUID> {
    @Query("""
        select n from NotificationEntity n
        where n.userId = ?1
        and n.active = true
    """)
    Page<NotificationEntity> findAllActiveByUserId(UUID userId, Pageable pageable);
    Optional<NotificationEntity> findByUserIdAndId(UUID userId, UUID id);
    Optional<NotificationEntity> findByUserIdAndReference(UUID userId, String reference);
}
