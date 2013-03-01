package org.apache.ibatis.ibator.plugins;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.GeneratedXmlFile;
import org.apache.ibatis.ibator.api.IbatorPluginAdapter;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.api.dom.xml.Attribute;
import org.apache.ibatis.ibator.api.dom.xml.Document;
import org.apache.ibatis.ibator.api.dom.xml.Element;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.generator.XmlConstants;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.AbstractXmlElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.DynamicSqlGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.ExampleWhereClauseElementGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.MysqlPaginationLimitGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.OraclePaginationHeadGenerator;
import org.apache.ibatis.ibator.generator.ibatis2.sqlmap.elements.OraclePaginationTailGenerator;

/**
 * 数据库分页插件
 * 把example by where的xml和分页的提取到公用的xml里面去。
 * @author QQ:34847009
 * @date 2010-10-21 下午09:33:48
 */
public class PaginationPlugin extends IbatorPluginAdapter {

	/** 数据库类型 */
	private String databaseType;
	/** 表 */
	private IntrospectedTable introspectedTable;

	@Override
	public boolean validate(List<String> warnings) {
		databaseType = ibatorContext.getJdbcConnectionConfiguration()
				.getDriverClass();
		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document,
			IntrospectedTable introspectedTable) {
		XmlElement element = document.getRootElement();
		List<Element> list = element.getElements();
		list.remove(1);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (databaseType.contains("oracle")) {
			XmlElement oracleHeadIncludeElement = new XmlElement("include");
			oracleHeadIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ OraclePaginationHeadGenerator.ORACLE_PAGINATION_HEAD));
			// 在第几个地方增加
			element.addElement(0, oracleHeadIncludeElement);

			XmlElement oracleTailIncludeElement = new XmlElement("include");
			oracleTailIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ OraclePaginationTailGenerator.ORACLE_PAGINATION_TAIL));
			// 在最后增加
			element.addElement(element.getElements().size(),
					oracleTailIncludeElement);
		} else if (databaseType.contains("mysql")) {
			XmlElement mysqlLimitIncludeElement = new XmlElement("include");
			mysqlLimitIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ MysqlPaginationLimitGenerator.MYSQL_PAGINATION_LIMIT));
			// 在最后增加
			element.addElement(element.getElements().size(),
					mysqlLimitIncludeElement);
		}

		return super.sqlMapExampleWhereClauseElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
			XmlElement element, IntrospectedTable introspectedTable) {
		if (databaseType.contains("oracle")) {
			XmlElement oracleHeadIncludeElement = new XmlElement("include");
			oracleHeadIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ OraclePaginationHeadGenerator.ORACLE_PAGINATION_HEAD));
			// 在第一个地方增加
			element.addElement(0, oracleHeadIncludeElement);

			XmlElement oracleTailIncludeElement = new XmlElement("include");
			oracleTailIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ OraclePaginationTailGenerator.ORACLE_PAGINATION_TAIL));
			// 在最后增加
			element.addElement(element.getElements().size(),
					oracleTailIncludeElement);
		} else if (databaseType.contains("mysql")) {
			XmlElement mysqlLimitIncludeElement = new XmlElement("include");
			mysqlLimitIncludeElement
					.addAttribute(new Attribute(
							"refid",
							XmlConstants.COMMON
									+ "."
									+ MysqlPaginationLimitGenerator.MYSQL_PAGINATION_LIMIT));
			// 在最后增加
			element.addElement(element.getElements().size(),
					mysqlLimitIncludeElement);
		}

		return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element,
				introspectedTable);
	}

	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		if (databaseType.contains("oracle")) {
			// 增加开始处
			// 增加oracleStart、oracleEnd、mysqlOffset、mysqlLength
			// add field, getter, setter for oracleStart clause
			Field field = new Field();
			field.setVisibility(JavaVisibility.PRIVATE);
			field.setType(FullyQualifiedJavaType.getInteger());
			field.setName("oracleStart"); //$NON-NLS-1$
			topLevelClass.addField(field);

			Method method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("setOracleStart"); //$NON-NLS-1$
			method.addParameter(new Parameter(FullyQualifiedJavaType
					.getInteger(), "oracleStart")); //$NON-NLS-1$
			method.addBodyLine("this.oracleStart = oracleStart;"); //$NON-NLS-1$
			addSetterComment(method, "开始记录数", "oracleStart");
			topLevelClass.addMethod(method);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(FullyQualifiedJavaType.getInteger());
			method.setName("getOracleStart"); //$NON-NLS-1$
			method.addBodyLine("return oracleStart;"); //$NON-NLS-1$
			topLevelClass.addMethod(method);
			// add field, getter, setter for oracleEnd clause
			field = new Field();
			field.setVisibility(JavaVisibility.PRIVATE);
			field.setType(FullyQualifiedJavaType.getInteger());
			field.setName("oracleEnd"); //$NON-NLS-1$
			topLevelClass.addField(field);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("setOracleEnd"); //$NON-NLS-1$
			method.addParameter(new Parameter(FullyQualifiedJavaType
					.getInteger(), "oracleEnd")); //$NON-NLS-1$
			method.addBodyLine("this.oracleEnd = oracleEnd;"); //$NON-NLS-1$
			addSetterComment(method, "结束记录数", "oracleEnd");
			topLevelClass.addMethod(method);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(FullyQualifiedJavaType.getInteger());
			method.setName("getOracleEnd"); //$NON-NLS-1$
			method.addBodyLine("return oracleEnd;"); //$NON-NLS-1$
			topLevelClass.addMethod(method);

		} else if (databaseType.contains("mysql")) {
			// add field, getter, setter for mysqlOffset clause
			Field field = new Field();
			field.setVisibility(JavaVisibility.PRIVATE);
			field.setType(FullyQualifiedJavaType.getInteger());
			field.setName("mysqlOffset"); //$NON-NLS-1$
			topLevelClass.addField(field);

			Method method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("setMysqlOffset"); //$NON-NLS-1$
			method.addParameter(new Parameter(FullyQualifiedJavaType
					.getInteger(), "mysqlOffset")); //$NON-NLS-1$
			method.addBodyLine("this.mysqlOffset = mysqlOffset;"); //$NON-NLS-1$
			addSetterComment(
					method,
					"指定返回记录行的偏移量<br> mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15",
					"mysqlOffset");
			topLevelClass.addMethod(method);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(FullyQualifiedJavaType.getInteger());
			method.setName("getMysqlOffset"); //$NON-NLS-1$
			method.addBodyLine("return mysqlOffset;"); //$NON-NLS-1$
			topLevelClass.addMethod(method);
			// add field, getter, setter for mysqlLength clause
			field = new Field();
			field.setVisibility(JavaVisibility.PRIVATE);
			field.setType(FullyQualifiedJavaType.getInteger());
			field.setName("mysqlLength"); //$NON-NLS-1$
			topLevelClass.addField(field);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setName("setMysqlLength"); //$NON-NLS-1$
			method.addParameter(new Parameter(FullyQualifiedJavaType
					.getInteger(), "mysqlLength")); //$NON-NLS-1$
			method.addBodyLine("this.mysqlLength = mysqlLength;"); //$NON-NLS-1$
			addSetterComment(
					method,
					"指定返回记录行的最大数目<br> mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15",
					"mysqlLength");
			topLevelClass.addMethod(method);

			method = new Method();
			method.setVisibility(JavaVisibility.PUBLIC);
			method.setReturnType(FullyQualifiedJavaType.getInteger());
			method.setName("getMysqlLength"); //$NON-NLS-1$
			method.addBodyLine("return mysqlLength;"); //$NON-NLS-1$
			topLevelClass.addMethod(method);
			// 增加结束处
		}

		return super.modelExampleClassGenerated(topLevelClass,
				introspectedTable);
	}

	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles() {

		Document document = new Document(
				XmlConstants.IBATIS2_SQL_MAP_PUBLIC_ID,
				XmlConstants.IBATIS2_SQL_MAP_SYSTEM_ID);
		XmlElement answer = new XmlElement("sqlMap"); //$NON-NLS-1$
		document.setRootElement(answer);
		answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
				XmlConstants.COMMON));

		AbstractXmlElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator();
		elementGenerator.setIbatorContext(ibatorContext);
		elementGenerator.setIntrospectedTable(introspectedTable);
		elementGenerator.addElements(answer);

		if (databaseType.contains("oracle")) {
			// 增加手写的尾sql
			AbstractXmlElementGenerator oraclePaginationHeadGenerator = new OraclePaginationHeadGenerator();
			oraclePaginationHeadGenerator.setIbatorContext(ibatorContext);
			oraclePaginationHeadGenerator.addElements(answer);
			// 增加手写的头sql
			AbstractXmlElementGenerator oraclePaginationTailGenerator = new OraclePaginationTailGenerator();
			oraclePaginationTailGenerator.setIbatorContext(ibatorContext);
			oraclePaginationTailGenerator.addElements(answer);
		} else if (databaseType.contains("mysql")) {
			// 增加mysql
			AbstractXmlElementGenerator mysqlPaginationLimitGenerator = new MysqlPaginationLimitGenerator();
			mysqlPaginationLimitGenerator.setIbatorContext(ibatorContext);
			mysqlPaginationLimitGenerator.addElements(answer);
		}
		// 增加动态sql
		elementGenerator = new DynamicSqlGenerator();
		elementGenerator.addElements(answer);

		GeneratedXmlFile gxf = new GeneratedXmlFile(document,
				properties.getProperty(
						"fileName", XmlConstants.COMMON + "_SqlMap.xml"), //$NON-NLS-1$ //$NON-NLS-2$
				ibatorContext.getSqlMapGeneratorConfiguration()
						.getTargetPackage(), //$NON-NLS-1$
				ibatorContext.getSqlMapGeneratorConfiguration()
						.getTargetProject(), //$NON-NLS-1$
				false);

		List<GeneratedXmlFile> files = new ArrayList<GeneratedXmlFile>(1);
		files.add(gxf);

		return files;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		this.introspectedTable = introspectedTable;
	}
}
