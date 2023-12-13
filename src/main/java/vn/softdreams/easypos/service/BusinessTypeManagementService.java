package vn.softdreams.easypos.service;

import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface BusinessTypeManagementService {
    ResultDTO getAllTransactions(Integer comId, Integer type, String keyword);

    ResultDTO create(Integer comId, String name, Integer type);

    ResultDTO update(Integer comId, Integer id, String name, Integer type);

    ResultDTO insertDefault(List<String> codes, Integer type);
}
