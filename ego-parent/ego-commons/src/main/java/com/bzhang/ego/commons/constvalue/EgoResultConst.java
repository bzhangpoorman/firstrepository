package com.bzhang.ego.commons.constvalue;

/**
 * 常量类
 * @author bzhang
 *
 */
public class EgoResultConst {
	/**
	 * EgoResult类的枚举值，包含状态码status及信息reason的值
	 * @author bzhang
	 *
	 */
	public enum EgoResultReason{
		OK_PARAMS(200,"可以使用"),
		ERROR_PARAMS_USERNAME(114,"用户名已存在"),
		ERROR_PARAMS_EMAIL(115,"邮箱已被注册"),
		ERROR_PARAMS_PHONE(113,"手机号已被注册"),
		
		OK_REGISTER(200,"注册成功"),
		ERROR_REGISTER(400,"注册失败,请校验数据"),
		
		OK_LOGOUT(200,"退出成功"),
		ERROR_LOGOUT(111,"退出失败"),
		
		OK_LOGIN(200,"用户登录成功"),
		ERROR_LOGIN(110,"用户名或密码错误"),
		
		OK_INSERT_PARAMS(200,"新增规格参数成功"),
		ERROR_EMPTY_PARAMDATA(109,"paramData不能为空"),
		ERROR_INSERT_PARAMS(109,"新增规格参数失败"),
		
		OK_SELECT_PARAMS(200,"规格参数查询成功"),
		ERROR_SELECT_PARAMS_NOTFOUND(108,"规格参数不存在"),
		
		OK_DELETE_PARAMS(200,"删除规格参数成功"),
		ERROR_DELETE_PARAMS(106,"删除规格参数失败"),
		ERROR_DELETE_PARAMS_ID_NULL(107,"删除规格参数ids不能为空"),
		
		OK_UPDATE_STATUS(200,"更新商品状态成功"),
		ERROR_UPDATE_STATUS(104,"更新商品状态失败"),
		
		OK_INSERT(200,"新增成功"),
		OK_UPDATE(200,"修改成功"),
		OK_DELETE(200,"删除成功"),
		ERROR_DELETE(116,"删除失败"),
		ERROR_UPDATE(117,"修改失败"),
		ERROR_PRICE(100,"商品价格不正确"),
		ERROR_TITLE(101,"商品标题为空"),
		ERROR_NUM(102,"商品库存不正确"),
		ERROR_INSERT(103,"商品新增失败"),
		ERROR_CID(105,"商品类目不能为空");
		
		private int code;
		private String value;
		private EgoResultReason(int code, String value) {
			this.code = code;
			this.value = value;
		}
		public int getCode() {
			return code;
		}
		public String getValue() {
			return value;
		}
		
	}
}
