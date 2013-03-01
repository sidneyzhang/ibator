package org.apache.ibatis.ibator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.GeneratedJavaFile;
import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
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
 * 生成service类
 * 
 * @author QQ:34847009
 * @date 2010-12-6 上午10:34:45
 */
public class ServiceImplPlugin extends IbatorPluginAdapter {

    private FullyQualifiedJavaType log4jLogger;
    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType daoType;
    private FullyQualifiedJavaType interfaceType;
    private FullyQualifiedJavaType pojoType;
    private FullyQualifiedJavaType pojoCriteriaType;
    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType result;
    private String servicePack;
    private String serviceImplPack;
    private String project;
    private String pojoUrl;
    private String daoUrl;
    private String exampleName;

    private List<Method> methods;
    /**
     * 是否添加注解
     */
    private boolean enableAnnotation = true;

    /**
     * 是否添加result
     */
    private boolean addResult = false;
    private boolean enableInsert = true;
    private boolean enableupdateByExampleSelective = true;
    private boolean enableinsertSelective = true;
    private boolean enableUpdateByPrimaryKey = true;
    private boolean enableDeleteByPrimaryKey = true;
    private boolean enableDeleteByExample = true;
    private boolean enableUpdateByPrimaryKeySelective = true;
    private boolean enableUpdateByExample = true;

    public ServiceImplPlugin() {
	super();
	// 默认是log4j
	log4jLogger = new FullyQualifiedJavaType("org.apache.log4j.Logger");
	methods = new ArrayList<Method>();
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
	    IntrospectedTable introspectedTable) {
	this.exampleName = topLevelClass.getType().getShortName();
	return true;
    }

    @Override
    public boolean validate(List<String> warnings) {
	if (StringUtility.stringHasValue(properties
		.getProperty("enableAnnotation")))
	    enableAnnotation = StringUtility.isTrue(properties
		    .getProperty("enableAnnotation"));
	if (StringUtility.stringHasValue(properties.getProperty("addResult")))
	    addResult = StringUtility.isTrue(properties
		    .getProperty("addResult"));

	String enableInsert = properties.getProperty("enableInsert");
	String enableupdateByExampleSelective = properties
		.getProperty("enableupdateByExampleSelective");
	String enableinsertSelective = properties
		.getProperty("enableinsertSelective");
	String enableUpdateByPrimaryKey = properties
		.getProperty("enableUpdateByPrimaryKey");
	String enableDeleteByPrimaryKey = properties
		.getProperty("enableDeleteByPrimaryKey");
	String enableDeleteByExample = properties
		.getProperty("enableDeleteByExample");
	String enableUpdateByPrimaryKeySelective = properties
		.getProperty("enableUpdateByPrimaryKeySelective");
	String enableUpdateByExample = properties
		.getProperty("enableUpdateByExample");

	if (StringUtility.stringHasValue(enableInsert))
	    this.enableInsert = StringUtility.isTrue(enableInsert);
	if (StringUtility.stringHasValue(enableupdateByExampleSelective))
	    this.enableupdateByExampleSelective = StringUtility
		    .isTrue(enableupdateByExampleSelective);
	if (StringUtility.stringHasValue(enableinsertSelective))
	    this.enableinsertSelective = StringUtility
		    .isTrue(enableinsertSelective);
	if (StringUtility.stringHasValue(enableUpdateByPrimaryKey))
	    this.enableUpdateByPrimaryKey = StringUtility
		    .isTrue(enableUpdateByPrimaryKey);
	if (StringUtility.stringHasValue(enableDeleteByPrimaryKey))
	    this.enableDeleteByPrimaryKey = StringUtility
		    .isTrue(enableDeleteByPrimaryKey);
	if (StringUtility.stringHasValue(enableDeleteByExample))
	    this.enableDeleteByExample = StringUtility
		    .isTrue(enableDeleteByExample);
	if (StringUtility.stringHasValue(enableUpdateByPrimaryKeySelective))
	    this.enableUpdateByPrimaryKeySelective = StringUtility
		    .isTrue(enableUpdateByPrimaryKeySelective);
	if (StringUtility.stringHasValue(enableUpdateByExample))
	    this.enableUpdateByExample = StringUtility
		    .isTrue(enableUpdateByExample);

	servicePack = properties.getProperty("targetPackage");
	serviceImplPack = properties.getProperty("implementationPackage");
	project = properties.getProperty("targetProject");

	pojoUrl = ibatorContext.getJavaModelGeneratorConfiguration()
		.getTargetPackage();
	daoUrl = ibatorContext.getDaoGeneratorConfiguration()
		.getTargetPackage();
	if (addResult) {
	    result = new FullyQualifiedJavaType(ibatorContext
		    .getJavaModelGeneratorConfiguration().getTargetPackage()
		    + ".Result");
	}
	if (enableAnnotation) {
	    autowired = new FullyQualifiedJavaType(
		    "org.springframework.beans.factory.annotation.Autowired");
	    service = new FullyQualifiedJavaType(
		    "org.springframework.stereotype.Service");
	}
	return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
	    IntrospectedTable introspectedTable) {
	List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
	String table = introspectedTable.getBaseRecordType();
	String tableName = table.replaceAll(this.pojoUrl + ".", "");
	interfaceType = new FullyQualifiedJavaType(servicePack + "."
		+ tableName + "Service");
	daoType = new FullyQualifiedJavaType(daoUrl + "." + tableName + "DAO");
	serviceType = new FullyQualifiedJavaType(serviceImplPack + "."
		+ tableName + "ServiceImpl");
	pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);
	pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "."
		+ exampleName);
	listType = new FullyQualifiedJavaType("java.util.List");
	// 接口
	addService(introspectedTable, tableName, files);
	// 实现类
	addServiceImpl(introspectedTable, tableName, files);

	return files;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
	if (addResult) {
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
     * 添加接口
     * 
     * @param tableName
     * @param files
     */
    protected void addService(IntrospectedTable introspectedTable,
	    String tableName, List<GeneratedJavaFile> files) {
	Interface interface1 = new Interface(interfaceType);
	interface1.setVisibility(JavaVisibility.PUBLIC);

	interface1.addImportedType(pojoType);
	interface1.addImportedType(pojoCriteriaType);
	interface1.addImportedType(listType);

	// 添加方法

	Method method = countByExample(introspectedTable, tableName);
	method.removeAllBodyLines();
	interface1.addMethod(method);

	method = selectByPrimaryKey(introspectedTable, tableName);
	method.removeAllBodyLines();
	interface1.addMethod(method);

	method = selectByExample(introspectedTable, tableName);
	method.removeAllBodyLines();
	interface1.addMethod(method);
	if (enableDeleteByPrimaryKey) {

	    method = getOther("deleteByPrimaryKey", introspectedTable,
		    tableName, 2);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableUpdateByPrimaryKeySelective) {
	    interface1.addImportedType(result);
	    method = getOther("updateByPrimaryKeySelective", introspectedTable,
		    tableName, 1);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableUpdateByPrimaryKey) {
	    method = getOther("updateByPrimaryKey", introspectedTable,
		    tableName, 1);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableDeleteByExample) {
	    method = getOther("deleteByExample", introspectedTable, tableName,
		    3);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableupdateByExampleSelective) {
	    method = getOther("updateByExampleSelective", introspectedTable,
		    tableName, 4);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableUpdateByExample) {
	    method = getOther("updateByExample", introspectedTable, tableName,
		    4);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableInsert) {
	    method = getOtherInsert("insert", introspectedTable, tableName);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}
	if (enableinsertSelective) {
	    method = getOtherInsert("insertSelective", introspectedTable,
		    tableName);
	    method.removeAllBodyLines();
	    interface1.addMethod(method);
	}

	GeneratedJavaFile file = new GeneratedJavaFile(interface1, project);
	files.add(file);
    }

    /**
     * 添加实现类
     * 
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addServiceImpl(IntrospectedTable introspectedTable,
	    String tableName, List<GeneratedJavaFile> files) {
	TopLevelClass topLevelClass = new TopLevelClass(serviceType);
	topLevelClass.setVisibility(JavaVisibility.PUBLIC);
	// 设置实现的接口
	topLevelClass.addSuperInterface(interfaceType);
	topLevelClass.addImportedType(interfaceType);
	topLevelClass.addImportedType(pojoType);
	topLevelClass.addImportedType(pojoCriteriaType);
	topLevelClass.addImportedType(listType);

	if (enableAnnotation) {
	    topLevelClass.addAnnotation("@Service");
	    topLevelClass.addImportedType(service);
	}
	// 添加log
	addLogger(topLevelClass);
	// 添加引用dao
	addField(topLevelClass, tableName);
	// 添加方法
	topLevelClass.addMethod(countByExample(introspectedTable, tableName));
	topLevelClass
		.addMethod(selectByPrimaryKey(introspectedTable, tableName));
	topLevelClass.addMethod(selectByExample(introspectedTable, tableName));

	/**
	 * type 的意义 pojo 1 ;key 2 ;example 3 ;pojo+example 4
	 */
	if (enableDeleteByPrimaryKey) {
	    topLevelClass.addMethod(getOther("deleteByPrimaryKey",
		    introspectedTable, tableName, 2));
	}
	if (enableUpdateByPrimaryKeySelective) {
	    topLevelClass.addMethod(getOther("updateByPrimaryKeySelective",
		    introspectedTable, tableName, 1));
	}
	if (enableUpdateByPrimaryKey) {
	    topLevelClass.addMethod(getOther("updateByPrimaryKey",
		    introspectedTable, tableName, 1));
	}
	if (enableDeleteByExample) {
	    topLevelClass.addMethod(getOther("deleteByExample",
		    introspectedTable, tableName, 3));
	}
	if (enableupdateByExampleSelective) {
	    topLevelClass.addMethod(getOther("updateByExampleSelective",
		    introspectedTable, tableName, 4));
	}
	if (enableUpdateByExample) {
	    topLevelClass.addMethod(getOther("updateByExample",
		    introspectedTable, tableName, 4));
	}
	if (enableInsert) {
	    topLevelClass.addMethod(getOtherInsert("insert", introspectedTable,
		    tableName));
	}
	if (enableinsertSelective) {
	    topLevelClass.addMethod(getOtherInsert("insertSelective",
		    introspectedTable, tableName));
	}
	// 生成文件
	GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project);
	files.add(file);
    }

    /**
     * 添加字段
     * 
     * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass, String tableName) {
	// 添加 dao
	Field field = new Field();
	field.setName(toLowerCase(tableName) + "DAO"); // 设置变量名
	topLevelClass.addImportedType(daoType);
	field.setType(daoType); // 类型
	field.setVisibility(JavaVisibility.PRIVATE);
	if (enableAnnotation) {
	    field.addAnnotation("@Autowired");
	    topLevelClass.addImportedType(autowired);
	}
	topLevelClass.addField(field);
    }

    /**
     * 添加方法
     * 
     */
    protected Method selectByPrimaryKey(IntrospectedTable introspectedTable,
	    String tableName) {
	Method method = new Method();
	method.setName("selectByPrimaryKey");
	method.setReturnType(pojoType);
	if (introspectedTable.getRules().generatePrimaryKeyClass()) {
	    FullyQualifiedJavaType type = new FullyQualifiedJavaType(
		    introspectedTable.getPrimaryKeyType());
	    method.addParameter(new Parameter(type, "key"));
	} else {
	    for (IntrospectedColumn introspectedColumn : introspectedTable
		    .getPrimaryKeyColumns()) {
		FullyQualifiedJavaType type = introspectedColumn
			.getFullyQualifiedJavaType();
		method.addParameter(new Parameter(type, introspectedColumn
			.getJavaProperty()));
	    }
	}
	method.setVisibility(JavaVisibility.PUBLIC);
	StringBuilder sb = new StringBuilder();
	sb.append("return this.");
	sb.append(toLowerCase(tableName) + "DAO.");
	sb.append("selectByPrimaryKey");
	sb.append("(");
	for (IntrospectedColumn introspectedColumn : introspectedTable
		.getPrimaryKeyColumns()) {
	    sb.append(introspectedColumn.getJavaProperty());
	    sb.append(",");
	}
	sb.setLength(sb.length() - 1);
	sb.append(");");
	method.addBodyLine(sb.toString());
	return method;
    }

    /**
     * 添加方法
     * 
     */
    protected Method countByExample(IntrospectedTable introspectedTable,
	    String tableName) {
	Method method = new Method();
	method.setName("countByExample");
	method.setReturnType(FullyQualifiedJavaType.getIntInstance());
	method.addParameter(new Parameter(pojoCriteriaType, "example"));
	method.setVisibility(JavaVisibility.PUBLIC);
	StringBuilder sb = new StringBuilder();
	sb.append("return this.");
	sb.append(toLowerCase(tableName) + "DAO.");
	sb.append("countByExample");
	sb.append("(");
	sb.append("example");
	sb.append(");");
	method.addBodyLine(sb.toString());
	return method;
    }

    /**
     * 添加方法
     * 
     */
    protected Method selectByExample(IntrospectedTable introspectedTable,
	    String tableName) {
	Method method = new Method();
	method.setName("selectByExample");
	method.setReturnType(new FullyQualifiedJavaType("List<" + tableName
		+ ">"));
	method.addParameter(new Parameter(pojoCriteriaType, "example"));
	method.setVisibility(JavaVisibility.PUBLIC);
	StringBuilder sb = new StringBuilder();
	sb.append("return this.");
	sb.append(toLowerCase(tableName) + "DAO.");
	if (introspectedTable.hasBLOBColumns()) {
	    sb.append("selectByExampleWithoutBLOBs");
	} else {
	    sb.append("selectByExample");
	}
	sb.append("(");
	sb.append("example");
	sb.append(");");
	method.addBodyLine(sb.toString());
	return method;
    }

    /**
     * 添加方法
     * 
     */
    protected Method getOther(String methodName,
	    IntrospectedTable introspectedTable, String tableName, int type) {
	Method method = new Method();
	method.setName(methodName);
	method.setReturnType(result);
	String params = addParams(introspectedTable, method, type);
	method.setVisibility(JavaVisibility.PUBLIC);
	StringBuilder sb = new StringBuilder();
	method.addBodyLine("Result result = new Result();");
	method.addBodyLine("try {");
	sb.append("int rows = this.");
	sb.append(toLowerCase(tableName) + "DAO.");
	if (introspectedTable.hasBLOBColumns()
		&& (!"updateByPrimaryKeySelective".equals(methodName)
			&& !"deleteByPrimaryKey".equals(methodName)
			&& !"deleteByExample".equals(methodName) && !"updateByExampleSelective"
			    .equals(methodName))) {
	    sb.append(methodName + "WithoutBLOBs");
	} else {
	    sb.append(methodName);
	}
	sb.append("(");
	sb.append(params);
	sb.append(");");
	method.addBodyLine(sb.toString());
	method.addBodyLine("if (rows > 0) {");
	method.addBodyLine("result.setSuccess(true);");
	method.addBodyLine("result.setMessage(SUCCESS);");
	method.addBodyLine("} else {");
	method.addBodyLine("result.setSuccess(false);");
	method.addBodyLine("result.setMessage(NO_RECORD);");
	method.addBodyLine("logger.warn(NO_RECORD);");
	method.addBodyLine("}");
	method.addBodyLine("} catch (Exception e) {");
	method.addBodyLine("result.setSuccess(false);");
	method.addBodyLine("result.setMessage(EXCEPTION);");
	method.addBodyLine("logger.error(\"Exception e= \" + e);");
	method.addBodyLine("}");
	method.addBodyLine("return result;");
	return method;
    }

    /**
     * 添加方法
     * 
     */
    protected Method getOtherInsert(String methodName,
	    IntrospectedTable introspectedTable, String tableName) {
	Method method = new Method();
	method.setName(methodName);
	method.setReturnType(result);
	method.addParameter(new Parameter(pojoType, "record"));
	method.setVisibility(JavaVisibility.PUBLIC);
	StringBuilder sb = new StringBuilder();
	method.addBodyLine("Result result = new Result();");
	method.addBodyLine("try {");
	sb.append("this.");
	sb.append(toLowerCase(tableName) + "DAO.");
	sb.append(methodName);
	sb.append("(");
	sb.append("record");
	sb.append(");");
	method.addBodyLine(sb.toString());
	method.addBodyLine("result.setSuccess(true);");
	method.addBodyLine("result.setMessage(SUCCESS);");
	method.addBodyLine("} catch (Exception e) {");
	method.addBodyLine("result.setSuccess(false);");
	method.addBodyLine("result.setMessage(EXCEPTION);");
	method.addBodyLine("logger.error(\"Exception e= \" + e);");
	method.addBodyLine("}");
	method.addBodyLine("return result;");
	return method;
    }

    /**
     * type 的意义 pojo 1 key 2 example 3 pojo+example 4
     */
    protected String addParams(IntrospectedTable introspectedTable,
	    Method method, int type1) {
	switch (type1) {
	case 1:
	    method.addParameter(new Parameter(pojoType, "record"));
	    return "record";
	case 2:
	    if (introspectedTable.getRules().generatePrimaryKeyClass()) {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(
			introspectedTable.getPrimaryKeyType());
		method.addParameter(new Parameter(type, "key"));
	    } else {
		for (IntrospectedColumn introspectedColumn : introspectedTable
			.getPrimaryKeyColumns()) {
		    FullyQualifiedJavaType type = introspectedColumn
			    .getFullyQualifiedJavaType();
		    method.addParameter(new Parameter(type, introspectedColumn
			    .getJavaProperty()));
		}
	    }
	    StringBuffer sb = new StringBuffer();
	    for (IntrospectedColumn introspectedColumn : introspectedTable
		    .getPrimaryKeyColumns()) {
		sb.append(introspectedColumn.getJavaProperty());
		sb.append(",");
	    }
	    sb.setLength(sb.length() - 1);
	    return sb.toString();
	case 3:
	    method.addParameter(new Parameter(pojoCriteriaType, "example"));
	    return "example";
	case 4:

	    method.addParameter(0, new Parameter(pojoType, "record"));
	    method.addParameter(1, new Parameter(pojoCriteriaType, "example"));
	    return "record, example";
	default:
	    break;
	}
	return null;
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
	if (addResult) {
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

    @Override
    public boolean daoCountByExampleMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateCountByExample())
	    methods.add(method);
	return super.daoCountByExampleMethodGenerated(method, topLevelClass,
		introspectedTable);
    }

    @Override
    public boolean daoDeleteByExampleMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateDeleteByExample())
	    methods.add(method);
	return super.daoDeleteByExampleMethodGenerated(method, topLevelClass,
		introspectedTable);
    }

    @Override
    public boolean daoDeleteByPrimaryKeyMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateDeleteByPrimaryKey())
	    methods.add(method);
	return super.daoDeleteByPrimaryKeyMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoInsertMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateInsert())
	    methods.add(method);
	return super.daoInsertMethodGenerated(method, topLevelClass,
		introspectedTable);
    }

    @Override
    public boolean daoSelectByExampleWithBLOBsMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateSelectByExampleWithBLOBs())
	    methods.add(method);
	return super.daoSelectByExampleWithBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoSelectByExampleWithoutBLOBsMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateSelectByExampleWithoutBLOBs())
	    methods.add(method);
	return super.daoSelectByExampleWithoutBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoSelectByPrimaryKeyMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateSelectByPrimaryKey())
	    methods.add(method);
	return super.daoSelectByPrimaryKeyMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByExampleSelectiveMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateUpdateByExampleSelective())
	    methods.add(method);
	return super.daoUpdateByExampleSelectiveMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByExampleWithBLOBsMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateUpdateByExampleWithBLOBs())
	    methods.add(method);
	return super.daoUpdateByExampleWithBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs())
	    methods.add(method);
	return super.daoUpdateByExampleWithoutBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
	    methods.add(method);
	return super.daoUpdateByPrimaryKeySelectiveMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs())
	    methods.add(method);
	return super.daoUpdateByPrimaryKeyWithBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
	    Method method, TopLevelClass topLevelClass,
	    IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules()
		.generateUpdateByPrimaryKeyWithoutBLOBs())
	    methods.add(method);
	return super.daoUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method,
		topLevelClass, introspectedTable);
    }

    @Override
    public boolean daoInsertSelectiveMethodGenerated(Method method,
	    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
	if (introspectedTable.getRules().generateInsertSelective())
	    methods.add(method);
	return super.daoInsertSelectiveMethodGenerated(method, topLevelClass,
		introspectedTable);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
	methods.clear();
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

    /**
     * 添加方法
     * 
     */
    protected void addMethod(TopLevelClass topLevelClass, String tableName) {
	Method method2 = new Method();
	for (int i = 0; i < methods.size(); i++) {
	    Method method = new Method();
	    method2 = methods.get(i);
	    method = method2;
	    method.removeAllBodyLines();
	    method.removeAnnotation();
	    StringBuilder sb = new StringBuilder();
	    sb.append("return this.");
	    sb.append(toLowerCase(tableName) + "DAO.");
	    sb.append(method.getName());
	    sb.append("(");
	    List<Parameter> list = method.getParameters();
	    for (int j = 0; j < list.size(); j++) {
		sb.append(list.get(j).getName());
		sb.append(",");
	    }
	    sb.setLength(sb.length() - 1);
	    sb.append(");");
	    method.addBodyLine(sb.toString());
	    topLevelClass.addMethod(method);
	}
	methods.clear();
    }

    /**
     * BaseUsers to baseUsers
     * 
     * @param tableName
     * @return
     */
    protected String toLowerCase(String tableName) {
	StringBuilder sb = new StringBuilder(tableName);
	sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
	return sb.toString();
    }

}
