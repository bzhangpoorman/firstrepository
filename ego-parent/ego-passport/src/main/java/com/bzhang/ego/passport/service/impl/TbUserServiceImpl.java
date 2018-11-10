package com.bzhang.ego.passport.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bzhang.ego.commons.constvalue.EgoResultConst.EgoResultReason;
import com.bzhang.ego.commons.pojo.EgoResult;
import com.bzhang.ego.commons.utils.JsonUtils;
import com.bzhang.ego.commons.utils.MD5Util;
import com.bzhang.ego.dubbo.service.TbUserDubboService;
import com.bzhang.ego.passport.service.TbUserService;
import com.bzhang.ego.pojo.TbUser;
import com.bzhang.ego.redis.dao.JedisDao;

@Service
public class TbUserServiceImpl implements TbUserService{
	
	@Reference
	private TbUserDubboService tbUserDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	
	@Value("${redis.user.key}")
	private String userKey;
	
	@Override
	public EgoResult checkUser(TbUser user,String uuid) {
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		TbUser resUser = tbUserDubboServiceImpl.selectByIdAndPassword(user);
		EgoResult egoResult= new EgoResult();
		if (resUser!=null) {
			//将uuid存到redis中
			String key= userKey+uuid;
			if (!jedisDaoImpl.exists(key)) {
				String value=JsonUtils.objectToJson(resUser);
				jedisDaoImpl.set(key, value);
				jedisDaoImpl.expire(key, 1800);
			}
			
			
			
			egoResult.setStatus(EgoResultReason.OK_LOGIN.getCode());
			egoResult.setMsg(EgoResultReason.OK_LOGIN.getValue());
			egoResult.setData(resUser);
			return egoResult;
		}
		
		return egoResult;
	}

	@Override
	public EgoResult getUser(String token) {
		EgoResult egoResult=new EgoResult();
		String key=userKey+token;
		if (jedisDaoImpl.exists(key)) {
			String value=jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(value)) {
				TbUser user = JsonUtils.jsonToPojo(value, TbUser.class);
				user.setPassword(null);
				egoResult.setStatus(EgoResultReason.OK_LOGIN.getCode());
				egoResult.setMsg(EgoResultReason.OK_LOGIN.getValue());
				egoResult.setData(user);
			}else {
				egoResult.setStatus(EgoResultReason.ERROR_LOGIN.getCode());
				egoResult.setMsg(EgoResultReason.ERROR_LOGIN.getValue());
			}
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_LOGIN.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_LOGIN.getValue());
		}
		
		return egoResult;
	}

	@Override
	public EgoResult logout(String token) {
		EgoResult egoResult = new EgoResult();
		String key= userKey+token;
		if (jedisDaoImpl.exists(key)) {
			String value=jedisDaoImpl.get(key);
			if (StringUtils.isNotBlank(value)) {
				jedisDaoImpl.del(key);
				egoResult.setStatus(EgoResultReason.OK_LOGOUT.getCode());
				egoResult.setMsg(EgoResultReason.OK_LOGOUT.getValue());
			}else {
				egoResult.setStatus(EgoResultReason.ERROR_LOGOUT.getCode());
				egoResult.setMsg(EgoResultReason.ERROR_LOGOUT.getValue());
			}
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_LOGOUT.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_LOGOUT.getValue());
		}
		
		
		return egoResult;
	}

	@Override
	public EgoResult checkParam(String param, int type) {
		EgoResult egoResult= new EgoResult();
		boolean res=false;
		if (type==1) {
			res = tbUserDubboServiceImpl.selectByUsername(param);
			if (res==false) {
				egoResult.setStatus(EgoResultReason.ERROR_PARAMS_USERNAME.getCode());
				egoResult.setMsg(EgoResultReason.ERROR_PARAMS_USERNAME.getValue());
				
			}
		}else if (type==2) {
			res = tbUserDubboServiceImpl.selectByPhone(param);
			if (res==false) {
				egoResult.setStatus(EgoResultReason.ERROR_PARAMS_PHONE.getCode());
				egoResult.setMsg(EgoResultReason.ERROR_PARAMS_PHONE.getValue());
				
			}
		}else {
			res = tbUserDubboServiceImpl.selectByEmail(param);
			if (res==false) {
				egoResult.setStatus(EgoResultReason.ERROR_PARAMS_EMAIL.getCode());
				egoResult.setMsg(EgoResultReason.ERROR_PARAMS_EMAIL.getValue());
				
			}
		}
		if (res==true) {
			egoResult.setStatus(EgoResultReason.OK_PARAMS.getCode());
			egoResult.setMsg(EgoResultReason.OK_PARAMS.getValue());
			
		}
		egoResult.setData(res);
		return egoResult;
	}

	@Override
	public EgoResult insertUser(TbUser user) {
		EgoResult egoResult=new EgoResult();
		if (!tbUserDubboServiceImpl.selectByUsername(user.getUsername())) {
			egoResult.setStatus(EgoResultReason.ERROR_REGISTER.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_REGISTER.getValue());
			return egoResult;
		}
		if (!tbUserDubboServiceImpl.selectByPhone(user.getPhone())) {
			egoResult.setStatus(EgoResultReason.ERROR_REGISTER.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_REGISTER.getValue());
			return egoResult;
		}
		if (!tbUserDubboServiceImpl.selectByEmail(user.getEmail())) {
			egoResult.setStatus(EgoResultReason.ERROR_REGISTER.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_REGISTER.getValue());
			return egoResult;
		}
		Date date = new Date();
		user.setUpdated(date);
		user.setCreated(date);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int i = tbUserDubboServiceImpl.insertUser(user);
		if (i==1) {
			egoResult.setStatus(EgoResultReason.OK_REGISTER.getCode());
			egoResult.setMsg(EgoResultReason.OK_REGISTER.getValue());
		}else {
			egoResult.setStatus(EgoResultReason.ERROR_REGISTER.getCode());
			egoResult.setMsg(EgoResultReason.ERROR_REGISTER.getValue());
		}
		return egoResult;
	}

}
