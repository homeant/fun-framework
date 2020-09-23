package xin.tianhui.cloud.domain;


public interface ITimestampEntity {
	Long getUpdatedTime();

	void setUpdatedTime(Long updatedTime);

	Long getCreatedTime();

	void setCreatedTime(Long createdTime);

	Long getDeletedTime();

	void setDeletedTime(Long deletedTime);
}
