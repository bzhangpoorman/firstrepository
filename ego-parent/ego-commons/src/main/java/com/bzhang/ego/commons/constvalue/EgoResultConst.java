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
