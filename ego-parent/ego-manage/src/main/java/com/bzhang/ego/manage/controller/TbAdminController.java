package com.bzhang.ego.manage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bzhang.ego.manage.service.TbAdminService;
import com.bzhang.ego.pojo.TbAdmin;

@Controller
public class TbAdminController {

	@Resource
	private TbAdminService tbAdminServiceImpl;
	
	@RequestMapping("login/check")
	public String logincheck(TbAdmin tbAdmin,HttpSession session) {
		boolean res = tbAdminServiceImpl.checkAdmin(tbAdmin);
		if (res) {
			session.setAttribute("admin", tbAdmin);
			return "redirect:/index";
		}else {
			return "redirect:/login";
		}
	}
	
}
