package com.bzhang.ego.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.CookieUtils;
import com.bzhang.ego.commons.utils.HttpClientUtil;
import com.bzhang.ego.commons.utils.JsonUtils;

/**
 * 用户登录验证拦截器
 * @author bzhang
 *
 */
public class LoginInterceptor implements HandlerInterceptor{
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception model)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView model)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 进入购物车前，用户是否登录验证
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
		
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		if (StringUtils.isNotBlank(token)) {
			String resJson = HttpClientUtil.doPost("http://passport.ego.com/user/token/"+token);
			EgoResult result = JsonUtils.jsonToPojo(resJson, EgoResult.class);
			if (result.getStatus()==200) {
				return true;
			}
		}
		String referer = request.getRequestURL().toString();
		System.out.println(referer);
		String num = request.getParameter("num");
		response.sendRedirect("http://passport.ego.com/user/showLogin?referer="+referer+"%3Fnum="+num);
		
		return false;
	}

}
