package com.library.notification.repository;

import com.library.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByMemberId(Long memberId);
    
    List<Notification> findByStatus(Notification.NotificationStatus status);
    
    List<Notification> findByType(Notification.NotificationType type);
    
    @Query("SELECT n FROM Notification n WHERE n.status = 'PENDING' OR (n.status = 'RETRY' AND n.retryCount < 3)")
    List<Notification> findPendingNotifications();
    
    @Query("SELECT n FROM Notification n WHERE n.memberId = :memberId AND n.dateSent >= :fromDate ORDER BY n.dateSent DESC")
    List<Notification> findRecentNotificationsByMember(@Param("memberId") Long memberId, 
                                                      @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.status = 'SENT' AND n.dateSent >= :fromDate")
    Long countSentNotificationsSince(@Param("fromDate") LocalDateTime fromDate);
}
