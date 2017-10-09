package shoppino.notification.dao;

import java.util.List;

import shoppino.notification.domain.DeviceNotification;

public interface DeviceNotificationDAO {

	public void insert(DeviceNotification deviceNotification);
	
	public void update(DeviceNotification deviceNotification);
	
	public void delete(String id, String type);
	
	public void update(String oldRegId, String newRegId, String type, String tenant);
	
	public List<DeviceNotification> findByRegIdAndUsernameAndTypeAndTenant(String regId, String username, String type, String tenant);
	
	public List<DeviceNotification> findByRegIdAndTypeAndTenant(String regId, String type, String tenant);
	
	public List<DeviceNotification> findByUsernameAndTenant(String username, String tenant);
	
	public List<DeviceNotification> findByUsernameAndTypeAndTenant(String username, String type, String tenant);

	public List<DeviceNotification> findByTypeAndTenant(String type, String tenant);
	
	public void deleteAll();
}
