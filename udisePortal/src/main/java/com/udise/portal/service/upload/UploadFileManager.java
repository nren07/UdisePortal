package com.udise.portal.service.upload;
import com.udise.portal.entity.Job;
import com.udise.portal.service.Abstract.AbstractManager;
import com.udise.portal.vo.file_upload.UploadFileReqVo;
import com.udise.portal.vo.file_upload.UploadFileResVo;

public interface UploadFileManager extends AbstractManager {

//	public List<UploadFileVo> getAll(SearchCriteria searchCriteria);

	public UploadFileResVo save(Long id,UploadFileReqVo uploadFile);

	public Job update(UploadFileReqVo uploadFile);

	public Job getById(Long id);

}
