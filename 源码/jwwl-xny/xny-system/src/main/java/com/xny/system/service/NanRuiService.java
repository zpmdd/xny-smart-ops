package com.xny.system.service;

import com.xny.common.utils.R;
import com.xny.system.model.dto.DataRecieveDTO;


public interface NanRuiService {
    R<String> dataRecieve(DataRecieveDTO dto);
}
