package com.zerowire.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;

import com.zerowire.entity.PageBean;

public class XMLUtils {

	public static void toXml(List<PageBean> pages, String userId) {
		try {
			File xmlFile;
			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
				File dirctory = new File(Environment
						.getExternalStorageDirectory().getPath() + "/TimeSheet");
				if (!dirctory.exists()) {
					dirctory.mkdir();
				}
				xmlFile = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/TimeSheet/" + userId + ".xml");

			} else {
				File dirctory = new File("/TimeSheet");
				if (!dirctory.exists()) {
					dirctory.mkdir();
				}
				xmlFile = new File("/TimeSheet/" + userId + ".xml");
			}
			FileOutputStream outStream = new FileOutputStream(xmlFile);

			OutputStreamWriter outStreamWriter = new OutputStreamWriter(
					outStream, "UTF-8");
			BufferedWriter writer = new BufferedWriter(outStreamWriter);

			writeXML(pages, writer);

			writer.flush();

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String writeXML(List<PageBean> pages, Writer writer) {

		XmlSerializer serializer = Xml.newSerializer();

		try {

			serializer.setOutput(writer);

			serializer.startDocument("UTF-8", true);

			// 第一个参数为命名空间,如果不使用命名空间,可以设置为null

			serializer.startTag("", "pages");

			for (PageBean page : pages) {
				serializer.startTag("", "page");
				serializer.attribute("", "id", page.getId());

				serializer.startTag("", "rb1");
				serializer.text(page.getRb1());
				serializer.endTag("", "rb1");

				serializer.startTag("", "rb2");
				serializer.text(page.getRb2());
				serializer.endTag("", "rb2");

				serializer.startTag("", "date");
				serializer.text(page.getDate());
				serializer.endTag("", "date");

				serializer.endTag("", "page");

			}

			serializer.endTag("", "pages");

			serializer.endDocument();

			return writer.toString();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

//	public static List<Person> readXML(InputStream inStream) {
//
//		XmlPullParser parser = Xml.newPullParser();
//
//		try {
//
//			parser.setInput(inStream, "UTF-8");
//
//			int eventType = parser.getEventType();
//
//			Person currentPerson = null;
//
//			List<Person> persons = null;
//
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//
//				switch (eventType) {
//
//				case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
//
//					persons = new ArrayList<Person>();
//s
//					break;
//
//				case XmlPullParser.START_TAG:// 开始元素事件
//
//					String name = parser.getName();
//
//					if (name.equalsIgnoreCase("person")) {
//
//						currentPerson = new Person();
//
//						currentPerson.setId(new Integer(parser
//								.getAttributeValue(null, "id")));
//
//					} else if (currentPerson != null) {
//
//						if (name.equalsIgnoreCase("name")) {
//
//							currentPerson.setName(parser.nextText());// 如果后面是Text元素,即返回它的值
//
//						} else if (name.equalsIgnoreCase("age")) {
//
//							currentPerson.setAge(new Short(parser.nextText()));
//
//						}
//
//					}
//
//					break;
//
//				case XmlPullParser.END_TAG:// 结束元素事件
//
//					if (parser.getName().equalsIgnoreCase("person")
//							&& currentPerson != null) {
//
//						persons.add(currentPerson);
//
//						currentPerson = null;
//
//					}
//
//					break;
//
//				}
//
//				eventType = parser.next();
//
//			}
//
//			inStream.close();
//
//			return persons;
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//
//		return null;
//
//	}
}
