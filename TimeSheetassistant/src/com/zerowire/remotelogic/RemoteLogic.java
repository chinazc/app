package com.zerowire.remotelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.zerowire.entity.EmployeeBean;
import com.zerowire.entity.FlyBackBean;
import com.zerowire.entity.LocationInfoBean;
import com.zerowire.entity.ProjectInfoBean;
import com.zerowire.global.Global;
import com.zerowire.httpservice.WebHttpConn;

/**
 * 逻辑单元
 * 
 * @author Jason.Wang 2013-07-08
 * 
 *         Using Google GSON
 * 
 */
public class RemoteLogic {
	private volatile static WebHttpConn _conn = null;

	private WebHttpConn Conn() {
		if (_conn == null) {
			synchronized (RemoteLogic.class) {
				if (_conn == null) {
					_conn = new WebHttpConn();
					_conn.setDebug(true);
					_conn.setTimeOut(10 * 1000);
				}
			}
		}
		return _conn;
	}

	/**
	 * 1. 登陆模块 - 登陆验证
	 * 
	 * @param context
	 * @param userID
	 * @param userPWD
	 * @return
	 */
	public boolean getServiceLogin(Context context, String userID,
			String userPWD) {
		String serviceURL = "/TimesheetAssistant-war/rest/login";
		boolean retVal = false;
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("username", userID);
		jsonPara.put("password", userPWD);
		jsonPara.put("hardware_id", Global.getHardwareNumber(context));
		jsonPara.put("hardware_type", Global.getHardwareModel(context));
		jsonPara.put("os_info", Global.getOperatingSysInfo(context));
		Gson gson = new Gson();
		String jsonParaString = gson.toJson(jsonPara);
		try {
			Object obj = Conn().httpPostObjectWithQuery(serviceURL, jsonParaString);
			
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * 2. 获取员工姓名,职位,签名,头像ID,背景ID信息
	 * 
	 * @param userIDs
	 *            ForEx. Single:1510 Multiply:1510,13803
	 * @return Employee bean array
	 */
	public EmployeeBean[] getServiceEmployee(String userIDs) {
		String serviceURL = "/TimesheetAssistant-war/rest/employee";
		EmployeeBean[] retVal = null;
		List<NameValuePair> para;
		if (userIDs == null || userIDs.length() == 0) {
			para = null;
		} else {
			para = new ArrayList<NameValuePair>();
			para.add(new BasicNameValuePair("employeeIds", userIDs));
		}

		try {
			Object obj = Conn().httpGetObject(serviceURL, para);
			if (obj != null) {
				JSONArray jsonObjArray = new JSONArray(obj.toString());
				if (jsonObjArray != null && jsonObjArray.length() > 0) {
					retVal = new EmployeeBean[jsonObjArray.length()];
					Gson gson = new Gson();
					for (int i = 0; i < jsonObjArray.length(); i++) {
						JSONObject mObj = jsonObjArray.getJSONObject(i);
						retVal[i] = (EmployeeBean) gson.fromJson(
								mObj.toString(), EmployeeBean.class);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * 3. 获取项目信息
	 * 根据员工ID获取员工所在项目列表
	 * 
	 * @param userID
	 *            用户编号
	 * @return
	 */
	public ProjectInfoBean[] getServiceProjects(String userID,String longitude,String latitude) {
		if (userID == null || userID.length() == 0)
			return null;

		String serviceURL = "/TimesheetAssistant-war/rest/project/" + userID;
		ProjectInfoBean[] retVal = null;
		
		
		Gson gson = new Gson();
		Map<String, String> jsonPara = new HashMap<String, String>();
		if (longitude != null && longitude.length() > 0) {
			jsonPara.put("longitude", longitude);
		}
		if (latitude != null && latitude.length() > 0) {
			jsonPara.put("latitude", latitude);
		}
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		String jsonParaString = gson.toJson(jsonPara);
		// searchInfo = {"expr":"上海","employee_id":"1150"}
		para.add(new BasicNameValuePair("locationInfo", jsonParaString));
		
		
		try {
			Object obj = Conn().httpGetObject(serviceURL, para);
			if (obj != null) {
				JSONArray jsonObjArray = new JSONArray(obj.toString());
				if (jsonObjArray != null && jsonObjArray.length() > 0) {
					retVal = new ProjectInfoBean[jsonObjArray.length()];
					
					for (int i = 0; i < jsonObjArray.length(); i++) {
						JSONObject mObj = jsonObjArray.getJSONObject(i);
						retVal[i] = (ProjectInfoBean) gson.fromJson(
								mObj.toString(), ProjectInfoBean.class);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 4. 根据项目名称或所在地，搜索员工所在的项目列表
	 * 
	 * @param searchKey
	 *            ForEx. 上海
	 * @param userID
	 *            用户编号
	 * @return
	 */
	public ProjectInfoBean[] getServiceProjectsSearch(String searchKey,
			String userID) {
		String serviceURL = "/TimesheetAssistant-war/rest/project/search";
		ProjectInfoBean[] retVal = null;
		Gson gson = new Gson();
		Map<String, String> jsonPara = new HashMap<String, String>();
		if (searchKey != null && searchKey.length() > 0) {
			jsonPara.put("expr", searchKey);
		}
		if (userID != null && userID.length() > 0) {
			jsonPara.put("employee_id", userID);
		}
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		String jsonParaString = gson.toJson(jsonPara);
		// searchInfo = {"expr":"上海","employee_id":"1150"}
		para.add(new BasicNameValuePair("searchInfo", jsonParaString));
		try {
			Object obj = Conn().httpGetObject(serviceURL, para);
			if (obj != null) {
				JSONArray jsonObjArray = new JSONArray(obj.toString());
				if (jsonObjArray != null && jsonObjArray.length() > 0) {
					retVal = new ProjectInfoBean[jsonObjArray.length()];

					for (int i = 0; i < jsonObjArray.length(); i++) {
						JSONObject mObj = jsonObjArray.getJSONObject(i);
						retVal[i] = (ProjectInfoBean) gson.fromJson(
								mObj.toString(), ProjectInfoBean.class);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 5. 项目地点——判断是否存在
	 * 
	 */
	public boolean getExistLocation(LocationInfoBean senderLocationInfoBean) {
		String serviceURL = "/TimesheetAssistant-war/rest/project/location/exist";
		boolean retVal = false;
		Gson gson = new Gson();
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("range", senderLocationInfoBean.getRange());
		jsonPara.put("longitude", senderLocationInfoBean.getLongitude());
		jsonPara.put("latitude", senderLocationInfoBean.getLatitude());
		jsonPara.put("project_id", senderLocationInfoBean.getProject_id());
		jsonPara.put("employee_id", senderLocationInfoBean.getEmployee_id());

		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("locationInfo", gson.toJson(jsonPara)));

		try {
			Object obj = Conn().httpGetObject(serviceURL, para);
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 6. 项目地点——创建
	 * 
	 * @param senderLocationInfoBean
	 * @return 返回 GUID
	 */
	public String getCreateLocation(LocationInfoBean senderLocationInfoBean) {
		String serviceURL = "/TimesheetAssistant-war/rest/project/location/create";
		String retVal = null;
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("longitude", senderLocationInfoBean
				.getLongitude()));
		para.add(new BasicNameValuePair("latitude", senderLocationInfoBean
				.getLatitude()));
		para.add(new BasicNameValuePair("project_id", senderLocationInfoBean
				.getProject_id()));
		para.add(new BasicNameValuePair("employee_id", senderLocationInfoBean
				.getEmployee_id()));
		
		
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("longitude", senderLocationInfoBean
				.getLongitude());
		jsonPara.put("latitude", senderLocationInfoBean
				.getLatitude());
		jsonPara.put("project_id", senderLocationInfoBean
				.getProject_id());
		jsonPara.put("employee_id", senderLocationInfoBean
				.getEmployee_id());
		Gson gson = new Gson();
		String jsonParaString = gson.toJson(jsonPara);
		try {		
			Object obj = Conn().httpPostObjectWithQuery(serviceURL, jsonParaString);
			if (obj != null) {
				retVal = obj.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 7. 项目地点——编辑
	 * 
	 * @param senderLocationInfoBean
	 * @return 返回 GUID
	 */
	/*
	public String getEditLocation(LocationInfoBean senderLocationInfoBean) {
		String serviceURL = "/TimesheetAssistant-war/rest/project/location/edit";
		String retVal = null;
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("range", senderLocationInfoBean
				.getRange()));
		para.add(new BasicNameValuePair("longitude", senderLocationInfoBean
				.getLongitude()));
		para.add(new BasicNameValuePair("latitude", senderLocationInfoBean
				.getLatitude()));
		para.add(new BasicNameValuePair("project_id", senderLocationInfoBean
				.getProject_id()));
		para.add(new BasicNameValuePair("employee_id", senderLocationInfoBean
				.getEmployee_id()));
		try {
			Object obj = Conn().httpPutObject(serviceURL, para);
			if (obj != null) {
				retVal = obj.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
*/

	/**
	 * 8. 上传头像图片
	 * 
	 * @param senderEmpId
	 * @param senderResId
	 * @param senderResName
	 * @param senderFilePath
	 * @return
	 */
	public boolean getUploadAvatarImage(String senderEmpId, String senderResId,
			String senderResName, String senderFilePath) {
		String serviceURL = "/TimesheetAssistant-war/rest/avatarimg";
		boolean retVal = false;
		List<NameValuePair> paraString = new ArrayList<NameValuePair>();
		paraString.add(new BasicNameValuePair("employeeId", senderEmpId));
		paraString.add(new BasicNameValuePair("resId", senderResId));
		paraString.add(new BasicNameValuePair("resName", senderResName));

		List<NameValuePair> paraBinary = new ArrayList<NameValuePair>();
		paraBinary.add(new BasicNameValuePair("fileData", senderFilePath));

		try {
			Object obj = Conn().httpPostObjectWithBinary(serviceURL,
					paraString, paraBinary);
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 9. 下载头像图片
	 * 
	 * @param senderResId
	 * @return
	 */
	public Bitmap getDownloadAvatarImage(String senderResId) {
		if (senderResId == null || senderResId.length() == 0)
			return null;
		String serviceURL = "/TimesheetAssistant-war/rest/avatarimg/"
				+ senderResId;
		Bitmap retVal = null;
		try {
			retVal = Conn().httpGetBitmapObject(serviceURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 10. 上传背景图片
	 * 
	 * @param senderEmpId
	 * @param senderResId
	 * @param senderResName
	 * @param senderFilePath
	 * @return
	 */
	public boolean getUploadBackgroudImage(String senderEmpId,
			String senderResId, String senderResName, String senderFilePath) {
		String serviceURL = "/TimesheetAssistant-war/rest/backgroundimg";
		boolean retVal = false;
		List<NameValuePair> paraString = new ArrayList<NameValuePair>();
		paraString.add(new BasicNameValuePair("employeeId", senderEmpId));
		paraString.add(new BasicNameValuePair("resId", senderResId));
		paraString.add(new BasicNameValuePair("resName", senderResName));

		List<NameValuePair> paraBinary = new ArrayList<NameValuePair>();
		paraBinary.add(new BasicNameValuePair("fileData", senderFilePath));

		try {
			Object obj = Conn().httpPostObjectWithBinary(serviceURL,
					paraString, paraBinary);
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 11. 下载背景图片
	 * 
	 * @param senderResId
	 * @return
	 */
	public Bitmap getDownloadBackgroudImage(String senderResId) {
		if (senderResId == null || senderResId.length() == 0)
			return null;
		String serviceURL = "/TimesheetAssistant-war/rest/backgroundimg/"
				+ senderResId;
		Bitmap retVal = null;
		try {
			retVal = Conn().httpGetBitmapObject(serviceURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	/**
	 * 12.客户端最新版本信息
	 * 
	 */
	
	public String getClientVersion(){
		String serviceURL = "/TimesheetAssistant-war/rest/softwareinfo/androidversion";
		Object obj = null;
		String retVal = null;
		try {
			obj = Conn().httpGetObject(serviceURL,null);
			if (obj != null) {
				JSONObject result = new JSONObject(obj.toString()); 
				retVal = (String) result.get("version");		
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
		
	}
	
	/**
	 * 13.根据项目ID获取项目相关address信息
	 * @param project_id
	 * @return
	 */
	public ProjectInfoBean[] getServiceProjectAddressInfo(String project_id){
		
		String serviceURL = "/TimesheetAssistant-war/rest/project/address"+project_id;
		ProjectInfoBean[] retVal = null;
		Gson gson = new Gson();
		
		try {
			Object obj = Conn().httpGetObject(serviceURL, null);
			if (obj != null) {
				JSONArray jsonObjArray = new JSONArray(obj.toString());
				if (jsonObjArray != null && jsonObjArray.length() > 0) {
					retVal = new ProjectInfoBean[jsonObjArray.length()];

					for (int i = 0; i < jsonObjArray.length(); i++) {
						JSONObject mObj = jsonObjArray.getJSONObject(i);
						retVal[i] = (ProjectInfoBean) gson.fromJson(
								mObj.toString(), ProjectInfoBean.class);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 14.是否有机票补助
	 * @param employeeId
	 * @param projectId
	 * @return
	 */
	
	public boolean getHasTecketAllowance(String employeeId ,String projectId){
		String serviceURL = "/TimesheetAssistant-war/rest/flyback/hasticket";
		
		boolean retVal = false;
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("employeeId", employeeId);
		jsonPara.put("projectId", projectId);
		Gson gson = new Gson();
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("ticketInfo", gson.toJson(jsonPara)));
		try {
			Object obj = Conn().httpGetObject(serviceURL, para);			
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}
	
	/**
	 * 15.flyback列表获取
	 * @param employeeId
	 * @param projectId
	 * @return
	 */
	public FlyBackBean[] getFlyTicketList(String employeeId , String projectId) {
		if (employeeId == null || employeeId.length() == 0
				||projectId == null || projectId.length() == 0)
			return null;

		String serviceURL = "/TimesheetAssistant-war/rest/flyback/ticketlist";
		FlyBackBean[] retVal = null;
		try {
			Object obj = Conn().httpGetObject(serviceURL, null);
			if (obj != null) {
				JSONArray jsonObjArray = new JSONArray(obj.toString());
				if (jsonObjArray != null && jsonObjArray.length() > 0) {
					retVal = new FlyBackBean[jsonObjArray.length()];
					Gson gson = new Gson();
					for (int i = 0; i < jsonObjArray.length(); i++) {
						JSONObject mObj = jsonObjArray.getJSONObject(i);
						retVal[i] = (FlyBackBean) gson.fromJson(
								mObj.toString(), FlyBackBean.class);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	

	/**
	 * 16.填写TimeSheet
	 * @param projectId
	 * @param resourceId
	 * @param addressId
	 * @param isOffBase
	 * @param ticketAllowance
	 * @param projectAllowanceFlag
	 * @param clientFeedInfo
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean getResultOfWriteTimeSheet(String projectId,String resourceId,String addressId,
			String isOffBase,String ticketAllowance,String projectAllowanceFlag,
			String clientFeedInfo,String longitude,String latitude) {
		String serviceURL = "/TimesheetAssistant-war/rest/resourcerecord/create";
		boolean retVal = false;
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("projectId", projectId);
		jsonPara.put("resourceId", resourceId);
		jsonPara.put("addressId",addressId);
		jsonPara.put("isOffBase", isOffBase);
		jsonPara.put("ticketAllowance", ticketAllowance);
		jsonPara.put("projectAllowanceFlag",projectAllowanceFlag);
		jsonPara.put("clientFeedInfo", clientFeedInfo);
		jsonPara.put("longitude", longitude);
		jsonPara.put("latitude",latitude);

		Gson gson = new Gson();
		String jsonParaString = gson.toJson(jsonPara);
		try {
			Object obj = Conn().httpPostObjectWithQuery(serviceURL, jsonParaString);
			
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
		
	}
	
	/**
	 * 17.查找附近同事
	 * @param latitude
	 * @param longitude
	 * @param employeeId
	 * @return
	 */
	public boolean getNearbyColleague(String latitude,String longitude,String employeeId){
        String serviceURL = "/TimesheetAssistant-war/rest/employee/nearby";
		
		boolean retVal = false;
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("latitude", latitude);
		jsonPara.put("longitude", longitude);
		jsonPara.put("employeeId", employeeId);
		Gson gson = new Gson();
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("locationInfo", gson.toJson(jsonPara)));
		try {
			Object obj = Conn().httpGetObject(serviceURL, para);			
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
		
	}
	
	/**
	 * 18.设置基本信息
	 * @param employeeId
	 * @param phoneNumber
	 * @param signature
	 * @return
	 */
	public boolean setEmployeeBaseInfo(String employeeId,String phoneNumber,String signature){
		String serviceURL = "/TimesheetAssistant-war/rest/employee/edit";
		boolean retVal = false;
//		List<NameValuePair> para = new ArrayList<NameValuePair>();
		Map<String, String> jsonPara = new HashMap<String, String>();
		jsonPara.put("employeeId", employeeId);
		jsonPara.put("phoneNumber", phoneNumber);
		jsonPara.put("signature", signature);
		Gson gson = new Gson();
		String jsonParaString = gson.toJson(jsonPara);
		try {
			Object obj = Conn().httpPutObject(serviceURL, jsonParaString);
			if (obj != null) {
				if (obj.toString().equalsIgnoreCase("true")) {
					retVal = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
}
