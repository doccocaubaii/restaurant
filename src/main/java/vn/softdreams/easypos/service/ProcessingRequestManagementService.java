package vn.softdreams.easypos.service;

import vn.softdreams.easypos.dto.bill.DeleteDishRequest;
import vn.softdreams.easypos.dto.processingRequest.ChangeProcessingStatus;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface ProcessingRequestManagementService {
    ResultDTO changeProcessingStatus(ChangeProcessingStatus req);

    ResultDTO deleteDish(DeleteDishRequest requests);
}
