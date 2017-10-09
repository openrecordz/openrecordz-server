package shoppino.persistence.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import shoppino.domain.Notification;


/**
 * Simple repository interface to manage {@link Notification} instances.
 * 
 * @author Andrea Leo
 */ 
public interface NotificationRepository extends PagingAndSortingRepository<Notification,String>{

	
//	List<Notification> findByToAndRefId(String to, String refId);
//	
//	List<Notification> findByFromAndToAndRefId(String from, String to, String refId);
	
	Page<Notification> findAllByTenants(String tenant, Pageable pageable);
}
