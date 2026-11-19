package com.xny.system.service;


import com.xny.common.utils.R;
import com.xny.system.model.dto.LoginDTO;
import com.xny.system.model.vo.TokenVO;

import javax.servlet.http.HttpServletResponse;

public interface ILoginService {
    void captcha(HttpServletResponse response) throws Exception;

    R<TokenVO> sysUserLogin(LoginDTO dto);
}
