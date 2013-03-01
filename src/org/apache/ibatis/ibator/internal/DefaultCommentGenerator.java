/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.ibator.internal;

import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.CompilationUnit;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.InnerClass;
import org.apache.ibatis.ibator.api.dom.java.InnerEnum;
import org.apache.ibatis.ibator.api.dom.java.JavaElement;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.xml.XmlElement;
import org.apache.ibatis.ibator.config.PropertyRegistry;
import org.apache.ibatis.ibator.internal.util.JavaBeansUtil;
import org.apache.ibatis.ibator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 * 
 */
public class DefaultCommentGenerator implements CommentGenerator {

	private Properties properties;
	private boolean suppressDate;

	public DefaultCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addFieldComment(Field, IntrospectedTable,
	 *      IntrospectedColumn)
	 */
	public void addFieldComment(Field field, FullyQualifiedTable table,
			String columnName) {
		StringBuilder sb = new StringBuilder();
		if (columnName != null && columnName.length() != 0) {
			field.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * "); //$NON-NLS-1$
			columnName = columnName.replaceAll("\n", "<br>\n\t * ");
			sb.append(columnName);
			field.addJavaDocLine(sb.toString());
			field.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addFieldComment(Field, IntrospectedTable)
	 */
	public void addFieldComment(Field field, FullyQualifiedTable table) {
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addClassComment(InnerClass,
	 *      IntrospectedTable)
	 */
	public void addClassComment(InnerClass innerClass,
			FullyQualifiedTable table, boolean markAsDoNotDelete) {
		StringBuilder sb = new StringBuilder();
		String remarks=table.getRemarks();
		if (remarks != null && remarks.length() != 0) {
			innerClass.addJavaDocLine("/**");
			sb.append(" * "+remarks.replaceAll("\n", "<br>\n * "));
			innerClass.addJavaDocLine(sb.toString());
			innerClass.addJavaDocLine(" */");
		}
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addEnumComment(InnerEnum, IntrospectedTable)
	 */
	public void addEnumComment(InnerEnum innerEnum, FullyQualifiedTable table) {
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addGetterComment(Method, IntrospectedTable,
	 *      IntrospectedColumn)
	 */
	public void addGetterComment(Method method, FullyQualifiedTable table,
			String columnName) {
		StringBuilder sb = new StringBuilder();
		if (columnName != null && columnName.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * @return ");
			columnName = columnName.replaceAll("\n", "<br>\n\t * ");
			sb.append(columnName);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */");
		}
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addSetterComment(Method, IntrospectedTable,
	 *      IntrospectedColumn)
	 */
	public void addSetterComment(Method method, FullyQualifiedTable table,
			IntrospectedColumn introspectedColumn) {
		StringBuilder sb = new StringBuilder();
		String columnName=introspectedColumn.getRemarks();
		if (columnName != null && columnName.length() != 0) {
			method.addJavaDocLine("/**"); //$NON-NLS-1$
			sb.append(" * @param "+JavaBeansUtil.getCamelCaseString(introspectedColumn.getActualColumnName(),false)+" "); //$NON-NLS-1$
			columnName = columnName.replaceAll("\n", "<br>\n\t * ");
			sb.append(columnName);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */"); //$NON-NLS-1$
		}
	}

	/**
	 * Method from the old version of the interface.
	 * 
	 * TODO - remove in release 1.2.3
	 * 
	 * @deprecated as of version 1.2.2.
	 * @see DefaultCommentGenerator#addGeneralMethodComment(Method,
	 *      IntrospectedTable)
	 */
	public void addGeneralMethodComment(Method method, FullyQualifiedTable table) {
	}

	public void addJavaFileComment(CompilationUnit compilationUnit) {
		// add no file level comments by default
		;
	}

	/**
	 * Adds a suitable comment to warn users that the element was generated, and
	 * when it was generated.
	 */
	public void addComment(XmlElement xmlElement) {
	}

	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
		;
	}

	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);

		suppressDate = StringUtility.isTrue(properties
				.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
	}

	/**
	 * This method adds the custom javadoc tag for ibator. You may do nothing if
	 * you do not wish to include the Javadoc tag - however, if you do not
	 * include the Javadoc tag then the Java merge capability of the eclipse
	 * plugin will break.
	 * 
	 * @param javaElement
	 *            the java element
	 */
	protected void addIbatorJavadocTag(JavaElement javaElement,
			boolean markAsDoNotDelete) {
	}

	/**
	 * This method returns a formated date string to include in the Javadoc tag
	 * and XML comments. You may return null if you do not want the date in
	 * these documentation elements.
	 * 
	 * @return a string representing the current timestamp, or null
	 */
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else {
			return new Date().toString();
		}
	}

	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable) {
		addClassComment(innerClass, introspectedTable.getFullyQualifiedTable(),
				false);
	}

	public void addEnumComment(InnerEnum innerEnum,
			IntrospectedTable introspectedTable) {
		addEnumComment(innerEnum, introspectedTable.getFullyQualifiedTable());
	}

	public void addFieldComment(Field field,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		addFieldComment(field, introspectedTable.getFullyQualifiedTable(),
				introspectedColumn.getRemarks());
	}

	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		addFieldComment(field, introspectedTable.getFullyQualifiedTable());
	}

	public void addGeneralMethodComment(Method method,
			IntrospectedTable introspectedTable) {
		addGeneralMethodComment(method,
				introspectedTable.getFullyQualifiedTable());
	}

	public void addGetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		addGetterComment(method, introspectedTable.getFullyQualifiedTable(),
				introspectedColumn.getRemarks());
	}

	public void addSetterComment(Method method,
			IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		addSetterComment(method, introspectedTable.getFullyQualifiedTable(),
				introspectedColumn);
	}

	public void addClassComment(InnerClass innerClass,
			IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		addClassComment(innerClass, introspectedTable.getFullyQualifiedTable(),
				markAsDoNotDelete);
	}
}
