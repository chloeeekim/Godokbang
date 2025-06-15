package chloe.godokbang.repository;

import chloe.godokbang.domain.Notification;
import chloe.godokbang.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByReceiver(Pageable pageable, User receiver);
}
