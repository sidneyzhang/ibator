package org.apache.ibatis.ibator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaElement;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.internal.util.StringUtility;

/**
 * 把所有delete、update、insert返回为int类型的方法改为返回布尔值类型
 * <p>
 * 大部分都用不到返回的行数，多数情况只是关心是否执行成功
 *
 * @author QQ:34847009
 * @date 2010-12-6 上午10:34:45
 */
public class ChangeReturnPlugin extends IbatorPluginAdapter {

	private FullyQualifiedJavaType log4jLogger;
	private FullyQualifiedJavaType returnType;
	private FullyQualifiedJavaType result;
	private String resultUrl;
	/**
	 * 是否需要增加自定义类
	 */
	private boolean resultEnable = false;

	public ChangeReturnPlugin() {
		super();
		// 默认是log4j
		log4jLogger = new FullyQualifiedJavaType("org.apache.log4j.Logger");
	}

	@Override
	public boolean validate(List<String> warnings) {
		String temp = properties.getProperty("enableResult");
		if (StringUtility.stringHasValue(temp))
			resultEnable = StringUtility.isTrue(temp);
		if (resultEnable) {
			resultUrl = ibatorContext.getJavaModelGeneratorConfiguration()
					.getTargetPackage() + ".Result";
			result = new FullyQualifiedJavaType(resultUrl);
			returnType = result;
		} else {
			returnType = FullyQualifiedJavaType.getBooleanPrimitiveInstance();
		}
		return true;
	}

	@Override
	public boolean daoImplementationGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		addLogger(topLevelClass);
		return true;
	}

	@Override
	public boolean daoInterfaceGenerated(Interface interfaze,
			IntrospectedTable introspectedTable) {
		if (resultEnable) {
			interfaze.addImportedType(result);
		}
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		if (resultEnable) {

			List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
			TopLevelClass topLevelClass = new TopLevelClass(result);
			topLevelClass.setVisibility(JavaVisibility.PUBLIC);
			addField(topLevelClass);
			addMethod(topLevelClass);
			GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass,
					ibatorContext.getJavaModelGeneratorConfiguration()
							.getTargetProject());
			files.add(file);
			return files;
		} else {
			return null;
		}
	}

	/**
	 * 添加字段
	 *
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass) {
		// 添加 success
		Field field = new Field();
		field.setName("success"); // 设置变量名
		field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance()); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "执行结果");
		topLevelClass.addField(field);
		// 设置结果
		field = new Field();
		field.setName("message"); // 设置变量名
		field.setType(FullyQualifiedJavaType.getStringInstance()); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "消息结果");
		topLevelClass.addField(field);
	}

	/**
	 * 添加方法
	 *
	 */
	protected void addMethod(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setSuccess");
		method.addParameter(new Parameter(FullyQualifiedJavaType
				.getBooleanPrimitiveInstance(), "success"));
		method.addBodyLine("this.success = success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType
				.getBooleanPrimitiveInstance());
		method.setName("isSuccess");
		method.addBodyLine("return success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setMessage");
		method.addParameter(new Parameter(FullyQualifiedJavaType
				.getStringInstance(), "message"));
		method.addBodyLine("this.message = message;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("getMessage");
		method.addBodyLine("return message;");
		topLevelClass.addMethod(method);
	}

	protected void addComment(JavaElement field, String comment) {
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		comment = comment.replaceAll("\n", "<br>\n\t * ");
		sb.append(comment);
		field.addJavaDocLine(sb.toString());
		field.addJavaDocLine(" */");
	}

	protected void addLogger(TopLevelClass topLevelClass) {
		topLevelClass.addImportedType(log4jLogger);
		// 添加日志
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("Logger.getLogger("
				+ topLevelClass.getType().getShortName() + ".class)"); // 设置值
		field.setName("logger"); // 设置变量名
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("Logger")); // 类型
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
		if (resultEnable) {
			topLevelClass.addImportedType(result);
			// 成功!
			field = new Field();
			field.setFinal(true);
			field.setInitializationString("\"成功!\""); // 设置值
			field.setName("SUCCESS"); // 设置变量名
			field.setStatic(true);
			field.setType(FullyQualifiedJavaType.getStringInstance()); // 类型
			field.setVisibility(JavaVisibility.PRIVATE);
			topLevelClass.addField(field);
			// 没有找到对应的记录!
			field = new Field();
			field.setFinal(true);
			field.setInitializationString("\"没有找到对应的记录!\""); // 设置值
			field.setName("NO_RECORD"); // 设置变量名
			field.setStatic(true);
			field.setType(FullyQualifiedJavaType.getStringInstance()); // 类型
			field.setVisibility(JavaVisibility.PRIVATE);
			topLevelClass.addField(field);
			// 系统繁忙!
			field = new Field();
			field.setFinal(true);
			field.setInitializationString("\"系统繁忙!\""); // 设置值
			field.setName("EXCEPTION"); // 设置变量名
			field.setStatic(true);
			field.setType(FullyQualifiedJavaType.getStringInstance()); // 类型
			field.setVisibility(JavaVisibility.PRIVATE);
			topLevelClass.addField(field);
		}
	}

	/**
	 * 增加方法内的相同部分
	 *
	 * @param method
	 *            方法
	 * @param tempString
	 *            执行的语句
	 */
	protected void addBody(Method method, String tempString) {
		if (resultEnable) {
			method.addBodyLine("Result result = new Result();");
			method.addBodyLine("try {");
			method.addBodyLine(tempString);
			method.addBodyLine("result.setSuccess(rows > 0 ? true : false);");
			method.addBodyLine("result.setMessage(rows > 0 ? SUCCESS : NO_RECORD);");
			method.addBodyLine("} catch (Exception e) {");
			method.addBodyLine("result.setSuccess(false);");
			method.addBodyLine("result.setMessage(EXCEPTION);");
			method.addBodyLine("logger.error(\"Exception e= \" + e);");
			method.addBodyLine("}");
			method.addBodyLine("return result;");
		} else {
			method.addBodyLine("boolean result=false;");
			method.addBodyLine("try {");
			method.addBodyLine(tempString);
			method.addBodyLine("result = rows > 0 ? true : false;");
			method.addBodyLine("} catch (Exception e) {");
			method.addBodyLine("logger.error(\"Exception e= \" + e);");
			method.addBodyLine("}");
			method.addBodyLine("return result;");
		}
	}

	@Override
	public boolean daoDeleteByExampleMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateDeleteByExample())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoDeleteByExampleMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateDeleteByExample()) {
			// 设置方法的返回类型
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			// 取掉rows
			String temp = list.get(0);
			// 去掉最后一行
			method.removeBodyLine(1);
			// 去掉倒数第二行
			method.removeBodyLine(0);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoDeleteByPrimaryKeyMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateDeleteByPrimaryKey())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoDeleteByPrimaryKeyMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(2);
			method.removeBodyLine(3);
			method.removeBodyLine(2);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoInsertMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateInsert())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoInsertMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateInsert()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(0);
			method.removeBodyLine(0);
			if (resultEnable) {
				method.addBodyLine("Result result = new Result();");
				method.addBodyLine("try {");
				method.addBodyLine(temp);
				method.addBodyLine("result.setSuccess(true);");
				method.addBodyLine("result.setMessage(SUCCESS);");
				method.addBodyLine("} catch (Exception e) {");
				method.addBodyLine("result.setSuccess(false);");
				method.addBodyLine("result.setMessage(EXCEPTION);");
				method.addBodyLine("logger.error(\"Exception e= \" + e);");
				method.addBodyLine("}");
				method.addBodyLine("return result;");
			} else {
				method.addBodyLine("boolean result=false;");
				method.addBodyLine("try {");
				method.addBodyLine(temp);
				method.addBodyLine("result = true;");
				method.addBodyLine("} catch (Exception e) {");
				method.addBodyLine("logger.error(\"Exception e= \" + e);");
				method.addBodyLine("}");
				method.addBodyLine("return result;");
			}
		}
		return true;
	}

	@Override
	public boolean daoInsertSelectiveMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateInsertSelective())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoInsertSelectiveMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateInsertSelective()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(0);
			method.removeBodyLine(0);
			if (resultEnable) {
				method.addBodyLine("Result result = new Result();");
				method.addBodyLine("try {");
				method.addBodyLine(temp);
				method.addBodyLine("result.setSuccess(true);");
				method.addBodyLine("result.setMessage(SUCCESS);");
				method.addBodyLine("} catch (Exception e) {");
				method.addBodyLine("result.setSuccess(false);");
				method.addBodyLine("result.setMessage(EXCEPTION);");
				method.addBodyLine("logger.error(\"Exception e= \" + e);");
				method.addBodyLine("}");
				method.addBodyLine("return result;");
			} else {
				method.addBodyLine("boolean result=false;");
				method.addBodyLine("try {");
				method.addBodyLine(temp);
				method.addBodyLine("result = true;");
				method.addBodyLine("} catch (Exception e) {");
				method.addBodyLine("logger.error(\"Exception e= \" + e);");
				method.addBodyLine("}");
				method.addBodyLine("return result;");
			}
		}
		return true;
	}

	@Override
	public boolean daoUpdateByExampleSelectiveMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleSelective())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByExampleSelectiveMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleSelective()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(1);
			method.removeBodyLine(2);
			method.removeBodyLine(1);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoUpdateByExampleWithBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByExampleWithBLOBsMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(1);
			method.removeBodyLine(2);
			method.removeBodyLine(1);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(1);
			method.removeBodyLine(2);
			method.removeBodyLine(1);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(0);
			method.removeBodyLine(1);
			method.removeBodyLine(0);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
			TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(0);
			method.removeBodyLine(1);
			method.removeBodyLine(0);
			addBody(method, temp);
		}
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
			Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules()
				.generateUpdateByPrimaryKeyWithoutBLOBs())
			method.setReturnType(returnType);
		return true;
	}

	@Override
	public boolean daoUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
			Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if (introspectedTable.getRules()
				.generateUpdateByPrimaryKeyWithoutBLOBs()) {
			method.setReturnType(returnType);
			List<String> list = method.getBodyLines();
			String temp = list.get(0);
			method.removeBodyLine(1);
			method.removeBodyLine(0);
			addBody(method, temp);
		}
		return true;
	}

}
