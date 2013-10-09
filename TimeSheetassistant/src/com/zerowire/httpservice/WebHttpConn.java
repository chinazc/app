package com.zerowire.httpservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.zerowire.config.ConfigPara;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Web HTTP method connection
 * 
 * @author Jason.Wang 2013-07-07
 * Using APACHE HttpClient
 * {@link http://hc.apache.org/downloads.cgi}
 * 
 */
public class WebHttpConn {

	private boolean _debug = false;
	private int _timeOut = 30 * 1000;

	public void setDebug(boolean _debug) {
		this._debug = _debug;
	}

	public void setTimeOut(int _timeOut) {
		this._timeOut = _timeOut;
	}

	/**
	 * Http get object
	 * 
	 * @param senderUrlPath
	 * @param senderPara
	 * @return object
	 */
	public Object httpGetObject(String senderUrlPath,
			List<? extends NameValuePair> senderPara) {
		Object result = null;

		// Clip Code
		URL queryUrl = null;
		try {
			if (!senderUrlPath.startsWith("/"))
				senderUrlPath = "/" + senderUrlPath;

			if (senderPara != null && senderPara.size() > 0) {
				queryUrl = URIUtils.createURI("http", ConfigPara.hostIP, -1,
						senderUrlPath,
						URLEncodedUtils.format(senderPara, "UTF-8"), null)
						.toURL();
			} else {
				if (ConfigPara.hostIP.startsWith("http")) {
					queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
				} else {
					queryUrl = new URL("http://" + ConfigPara.hostIP
							+ senderUrlPath);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			HttpGet httpRequest = new HttpGet(queryUrl.toURI());
			HttpClient httpclient = new DefaultHttpClient();					
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					try {
						throw new Exception("网络异常"
								+ response.getStatusLine().getStatusCode());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (_debug) {
						Log.i(">>>", ">>> net issue code :"
								+ response.getStatusLine().getStatusCode());
					}
					return null;
				}

				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "utf-8"); // 默认编码
				if (_debug) {
					System.out.println(result);
				}
				// 无值返回字符串 null
				if (result.toString().length() == 0
						|| result.toString().equalsIgnoreCase("null")) {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	
	/**
	 *  Http Post Object with query
	 * @param senderUrlPath
	 * @param senderStringPara
	 * @return
	 */
	public Object httpPostObjectWithQuery(String senderUrlPath,
			String senderStringPara) {
		Object result = null;
		// Clip Code
		URL queryUrl = null;
		try {
			if (!senderUrlPath.startsWith("/"))
				senderUrlPath = "/" + senderUrlPath;
			if (ConfigPara.hostIP.startsWith("http")) {
				queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
			} else {
				queryUrl = new URL("http://" + ConfigPara.hostIP
						+ senderUrlPath);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 

		try {
			HttpPost httpRequest = new HttpPost(queryUrl.toURI());
			httpRequest.setHeader( "Charset", "UTF-8" );
            httpRequest.setHeader( "Content-Type", "Application/json" );
			httpRequest.setEntity(new StringEntity(senderStringPara));
			HttpClient httpclient = new DefaultHttpClient();
			  
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest, localContext);
//			ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
//	        String response1 = httpclient.execute(httpRequest,responseHandler); 
			if (response != null) {
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					try {
						throw new Exception("网络异常"
								+ response.getStatusLine().getStatusCode());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (_debug) {
						Log.i(">>>", ">>> net issue code :"
								+ response.getStatusLine().getStatusCode());
					}
					return null;
				}

				HttpEntity entityRetVal = response.getEntity();
				result = EntityUtils.toString(entityRetVal, "utf-8");
				if (_debug) {
					System.out.println(result);
				}
				// 无值返回字符串 null
				if (result.toString().length() == 0
						|| result.toString().equalsIgnoreCase("null")) {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
//	/**
//	 * Http post object
//	 * 
//	 * @param senderUrlPath
//	 * @param senderPara
//	 * @return object
//	 */
//	public Object httpPostObject(String senderUrlPath,
//			List<? extends NameValuePair> senderPara) {
//		Object result = null;
//
//		// Clip Code
//		URL queryUrl = null;
//		try {
//			if (!senderUrlPath.startsWith("/"))
//				senderUrlPath = "/" + senderUrlPath;
//
//			if (senderPara != null && senderPara.size() > 0) {
//				queryUrl = URIUtils.createURI("http", ConfigPara.hostIP, -1,
//						senderUrlPath,
//						URLEncodedUtils.format(senderPara, "UTF-8"), null)
//						.toURL();
//			} else {
//				if (ConfigPara.hostIP.startsWith("http")) {
//					queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
//				} else {
//					queryUrl = new URL("http://" + ConfigPara.hostIP
//							+ senderUrlPath);
//				}
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			URL url = queryUrl;
//			URI uri = queryUrl.toURI();
//			HttpPost httpRequest = new HttpPost(queryUrl.toURI());
//			HttpClient httpclient = new DefaultHttpClient();
//			
//			
//			
//			httpRequest.setHeader( "Charset", "UTF-8" );
// 
//			HttpContext localContext = new BasicHttpContext();  
//			
//			
//			
//			
//			httpRequest.setHeader("Content-Type", "application/json"); 
//			String aString = senderPara.toString().substring(10);
//			httpRequest.setEntity(new StringEntity(senderPara.toString().substring(10),"UTF-8")); 
//		    ResponseHandler<String> responseHandler = new BasicResponseHandler(); 
//	        String response1 = httpclient.execute(httpRequest,responseHandler); 
//	 
//	 
//	 
//			httpclient.getParams().setParameter(
//					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
//			httpclient.getParams().setParameter(
//					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
//			HttpResponse response = (HttpResponse) httpclient
//					.execute(httpRequest);
//			if (response != null) {
//				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//					try {
//						throw new Exception("网络异常"
//								+ response.getStatusLine().getStatusCode());
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (_debug) {
//						Log.i(">>>", ">>> net issue code :"
//								+ response.getStatusLine().getStatusCode());
//					}
//					return null;
//				}
//
//				HttpEntity entity = response.getEntity();
//				
//				result = EntityUtils.toString(entity, "utf-8");
//				if (_debug) {
//					System.out.println(result);
//				}
//				// 无值返回字符串 null
//				if (result.toString().length() == 0
//						|| result.toString().equalsIgnoreCase("null")) {
//					result = null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
	
	/**
	 * Http post object with binary
	 * 
	 * @param senderUrlPath
	 * @param senderStringPara
	 * @param senderBinaryPara
	 * @return object
	 */
	public Object httpPostObjectWithBinary(String senderUrlPath,
			List<? extends NameValuePair> senderStringPara, 
			List<? extends NameValuePair> senderBinaryPara) {
		Object result = null;

		// Clip Code
		URL queryUrl = null;
		try {
			if (!senderUrlPath.startsWith("/"))
				senderUrlPath = "/" + senderUrlPath;
			if (ConfigPara.hostIP.startsWith("http")) {
				queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
			} else {
				queryUrl = new URL("http://" + ConfigPara.hostIP
						+ senderUrlPath);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 

		try {
			HttpPost httpRequest = new HttpPost(queryUrl.toURI());
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (senderStringPara != null) {
				for(int i=0; i < senderStringPara.size(); i++) {  
					// Normal string data  
					entity.addPart(senderStringPara.get(i).getName(), new StringBody(senderStringPara.get(i).getValue()));  
		        }
			}
			if (senderBinaryPara != null) {
				for(int i=0; i < senderBinaryPara.size(); i++) {
					// Binary data
					entity.addPart(senderBinaryPara.get(i).getName(), new FileBody(new File(senderBinaryPara.get(i).getValue())));  
		        } 
			}
			
			httpRequest.setEntity(entity);
			HttpClient httpclient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();  
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest, localContext);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					try {
						throw new Exception("网络异常"
								+ response.getStatusLine().getStatusCode());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (_debug) {
						Log.i(">>>", ">>> net issue code :"
								+ response.getStatusLine().getStatusCode());
					}
					return null;
				}

				HttpEntity entityRetVal = response.getEntity();
				result = EntityUtils.toString(entityRetVal, "utf-8");
				if (_debug) {
					System.out.println(result);
				}
				// 无值返回字符串 null
				if (result.toString().length() == 0
						|| result.toString().equalsIgnoreCase("null")) {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	/**
	 * HTTP put object
	 * 
	 * @param senderUrlPath
	 * @param senderPara
	 * @return object
	 */
	public Object httpPutObject(String senderUrlPath,
			String senderStringPara) {
//		Object result = null;
//
//		// Clip Code
//		URL queryUrl = null;
//		try {
//			if (!senderUrlPath.startsWith("/"))
//				senderUrlPath = "/" + senderUrlPath;
//
//			if (senderPara != null && senderPara.size() > 0) {
//				queryUrl = URIUtils.createURI("http", ConfigPara.hostIP, -1,
//						senderUrlPath,
//						URLEncodedUtils.format(senderPara, "UTF-8"), null)
//						.toURL();
//			} else {
//				if (ConfigPara.hostIP.startsWith("http")) {
//					queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
//				} else {
//					queryUrl = new URL("http://" + ConfigPara.hostIP
//							+ senderUrlPath);
//				}
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		Object result = null;
		// Clip Code
		URL queryUrl = null;
		try {
			if (!senderUrlPath.startsWith("/"))
				senderUrlPath = "/" + senderUrlPath;
			if (ConfigPara.hostIP.startsWith("http")) {
				queryUrl = new URL(ConfigPara.hostIP + senderUrlPath);
			} else {
				queryUrl = new URL("http://" + ConfigPara.hostIP
						+ senderUrlPath);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		try {

//			HttpPut httpRequest = new HttpPut(queryUrl.toURI());
//			HttpClient httpclient = new DefaultHttpClient();
//			httpclient.getParams().setParameter(
//					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
//			httpclient.getParams().setParameter(
//					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
//			HttpResponse response = (HttpResponse) httpclient
//					.execute(httpRequest);
			
			
			
			HttpPut httpRequest = new HttpPut(queryUrl.toURI());
			httpRequest.setHeader( "Charset", "UTF-8" );
            httpRequest.setHeader( "Content-Type", "Application/json" );
			httpRequest.setEntity(new StringEntity(senderStringPara));
			HttpClient httpclient = new DefaultHttpClient();
			  
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, _timeOut); // 请求超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, _timeOut); // 读取超时
			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest, localContext);
			
			
			if (response != null) {
				int s =response.getStatusLine().getStatusCode();
				int b = HttpStatus.SC_OK;
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					try {
						throw new Exception("网络异常"
								+ response.getStatusLine().getStatusCode());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (_debug) {
						Log.i(">>>", ">>> net issue code :"
								+ response.getStatusLine().getStatusCode());
					}
					return null;
				}

				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "utf-8");
				if (_debug) {
					System.out.println(result);
				}
				// 无值返回字符串 null
				if (result.toString().length() == 0
						|| result.toString().equalsIgnoreCase("null")) {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从URL中取得Bitmap,并限制小于指定大小时才载入. 默认1024K
	 * 
	 * @param senderUrlPath
	 *            URL路径(不包括Host地址和頭)
	 * @return
	 */
	public Bitmap httpGetBitmapObject(String senderUrlPath) {
		int defaultLargestSize = 1024 * 1024; // 默认1024K
		URL imageUrl = null;
		Bitmap bitmap = null;
		try {
			if (ConfigPara.hostIP.startsWith("http")) {
				imageUrl = new URL(ConfigPara.hostIP + senderUrlPath);
			} else {
				imageUrl = new URL("http://" + ConfigPara.hostIP
						+ senderUrlPath);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpGet httpRequest = new HttpGet(imageUrl.toURI());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity buffHttpEntity = new BufferedHttpEntity(entity);
			if (buffHttpEntity.getContentLength() < defaultLargestSize) {
				InputStream is = buffHttpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			} else {
				bitmap = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
