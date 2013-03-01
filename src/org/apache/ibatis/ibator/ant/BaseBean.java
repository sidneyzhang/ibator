package org.apache.ibatis.ibator.ant;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 通过反射来生成tostring方法
 * 
 * @author QQ:34847009
 * @date 2010-11-4 下午02:58:42
 */
public class BaseBean {
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE, true, true);
	}
}
