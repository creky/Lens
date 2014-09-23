package com.hh.lens.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import android.util.Log;

/**
 * HTTP方法调用类
 * 
 * @author HH
 * 
 */
/**
 * @Classname: ClientUtil
 * @Description:
 * @Author: hh
 * @Date: 2012-11-18 下午11:33:10
 * @Version:
 */
public class ClientUtil
{

	// 超时时间
	private static final int TIMEOUT = 8000;

	private static int retry = 3;

	public static String ENCODING = "utf-8";

	/**
	 * get 方法
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception
	{
		return get(url, null);
	}

	public static String get(String url, String cookie) throws Exception
	{
		return getString(url, cookie, null, null, null);
	}

	public static InputStream getStream(String url) throws Exception
	{
		return getStream(url, null);
	}

	public static InputStream getStream(String url, String cookie) throws Exception
	{
		return getInputStream(url, cookie, null, null, null);
	}
	
	/**
	 * 从网络读取文件并保存到本地
	 * 
	 * @Methodname: getAndSaveFile
	 * @Discription: 
	 * @param url 网络上文件地址
	 * @param cookie cookie
	 * @param basePath 基路径,以"/"结尾
	 * @param relativePath 相对路径，以"/"结尾
	 * @param name 指定文件名
	 * @return
	 * @throws Exception
	 * @Author HH
	 * @Time 2013-1-27 下午9:06:22
	 */
	public static String getAndSaveFile(String url,String cookie,String basePath,String relativePath,String name) throws Exception{
		InputStream in = getStream(url, cookie);
		String fileName = name;
		String fileExt = url.substring(url.lastIndexOf("."));
		if (StringUtils.isEmpty(fileName))
		{
			Log.d("要获取的URL为：{}",url);
			if(url.lastIndexOf("/")<url.lastIndexOf(".")){
				fileName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
			}else{
				fileName=url.substring(url.lastIndexOf("/")+1).replaceAll("/", "");
			}
		}

		if (!basePath.endsWith("/"))
		{
			basePath += "/";
		}
		if (!relativePath.endsWith("/"))
		{
			relativePath += "/";
		}

		File dir = new File(basePath + relativePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		String finalPath = relativePath + fileName + fileExt;
		File file = new File(basePath + finalPath);
		int i = 1;
		while (file.exists())
		{
			finalPath = relativePath + fileName + "(" + i + ")" + fileExt;
			file = new File(basePath + finalPath);
			i++;
		}
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
		{
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		in.close();
		return finalPath;
	}

	/**
	 * 获取请求内容
	 * 
	 * @Methodname: getString
	 * @Discription:
	 * @param url
	 *            请求地址
	 * @param cookie
	 *            请求Cookie
	 * @param method
	 *            请求方式，一般为 GET 或 POST，默认为GET
	 * @param parameter
	 *            Post方式的请求参数
	 * @param requestCharset
	 *            Post方式参数的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 * @Author HH
	 * @Time 2012-11-19 上午1:28:42
	 */
	private static String getString(String url, String cookie, String method, Map<String, Object> parameter,
			String requestCharset) throws Exception
	{
		URL theUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) theUrl.openConnection();
		conn.setReadTimeout(TIMEOUT);
		conn.setConnectTimeout(TIMEOUT);

		// 添加cookie
		if (cookie != null)
		{
			conn.setRequestProperty("Cookie", cookie);
		}
		addHeaders(conn, createHeaders(theUrl));

		// 设定请求方式
		if (StringUtils.isEmpty(method))
		{
			method = "GET";
		}
		conn.setRequestMethod(method.toUpperCase());

		// 如果为post方式请求，添加请求参数
		if ("POST".equalsIgnoreCase(method) && !StringUtils.isEmpty(parameter))
		{
			if (!StringUtils.isEmpty(requestCharset))
			{
				requestCharset = ENCODING;
			}
			conn.getOutputStream().write(getParameterString(parameter, requestCharset).getBytes());
		}

		// 取得响应流
		InputStream in = null;
		for (int i = 0; i <= retry; i++)
		{
			try
			{
				in = conn.getInputStream();
				break;
			}
			catch (SocketTimeoutException e)
			{
				if (i != retry)
				{
					Log.i("连接超时，正在重试第 {} 次……", i + 1+"");
				}
			}
		}

		if (in == null)
		{
			return "";
		}

		in = unCompress(in, conn.getContentEncoding());
		String charset = getCharset(conn.getContentType());
		Reader reader = new InputStreamReader(in, Charset.forName(charset));
		BufferedReader bf = new BufferedReader(reader);

		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = bf.readLine()) != null)
		{
			sb.append(line).append('\n');
		}
		bf.close();
		reader.close();
		in.close();
		conn.disconnect();
		return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : sb.toString();
	}

	/**
	 * 获取请求流数据
	 * 
	 * @Methodname: getStream
	 * @Discription:
	 * @param url
	 *            请求地址
	 * @param cookie
	 *            请求Cookie
	 * @param method
	 *            请求方式，一般为 GET 或 POST，默认为GET
	 * @param parameter
	 *            Post方式的请求参数
	 * @param requestCharset
	 *            Post方式参数的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 * @Author HH
	 * @Time 2012-11-19 下午7:01:43
	 */
	private static InputStream getInputStream(String url, String cookie, String method, Map<String, Object> parameter,
			String requestCharset) throws Exception
	{
		URL theUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) theUrl.openConnection();
		conn.setReadTimeout(TIMEOUT);
		conn.setConnectTimeout(TIMEOUT);

		// 添加cookie
		if (cookie != null)
		{
			conn.setRequestProperty("Cookie", cookie);
		}
		addHeaders(conn, createHeaders(theUrl));

		// 设定请求方式
		if (StringUtils.isEmpty(method))
		{
			method = "GET";
		}
		conn.setRequestMethod(method.toUpperCase());

		// 如果为post方式请求，添加请求参数
		if ("POST".equalsIgnoreCase(method) && !StringUtils.isEmpty(parameter))
		{
			if (!StringUtils.isEmpty(requestCharset))
			{
				requestCharset = ENCODING;
			}
			conn.getOutputStream().write(getParameterString(parameter, requestCharset).getBytes());
		}

		// 取得响应流
		InputStream in = null;
		for (int i = 0; i <= retry; i++)
		{
			try
			{
				in = conn.getInputStream();
				break;
			}
			catch (SocketTimeoutException e)
			{
				if (i != retry)
				{
					Log.i("连接超时，正在重试第 {} 次……", i + 1+"");
				}
			}
		}
		in = unCompress(in, conn.getContentEncoding());
		return in;
	}

	/**
	 * 构造POST方法提交的数据字符串
	 * 
	 * @Methodname: getParameterString
	 * @Discription:
	 * @param parameter
	 * @return
	 * @throws Exception
	 * @Author HH
	 * @Time 2012-11-19 上午1:13:20
	 */
	private static String getParameterString(Map<String, Object> parameter, String charset) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : parameter.entrySet())
		{
			sb.append(entry.getKey()).append('=');
			if (!StringUtils.isEmpty(entry.getValue()))
			{
				sb.append(URLEncoder.encode(String.valueOf(entry.getValue()), charset));
			}
			sb.append('&');
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获取内容编码 TODO
	 * 
	 * @Methodname: getCharset
	 * @Discription:
	 * @param contentType
	 * @return
	 * @Author HH
	 * @Time 2012-11-19 上午12:23:19
	 */
	private static String getCharset(String contentType)
	{
		if (contentType == null || "".equals(contentType.trim()))
		{
			return ENCODING;
		}
		String[] contArray = contentType.split(";");
		if (!StringUtils.isEmpty(contArray))
		{
			for (String items : contArray)
			{
				String[] item = items.split("=");
				if (item.length == 2 && "charset".equalsIgnoreCase(item[0]))
				{
					if (!StringUtils.isEmpty(item[1]))
					{
						return item[1];
					}
				}
			}
		}
		return ENCODING;
	}

	/**
	 * post 方法
	 * 
	 * @param url
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> parameter) throws Exception
	{
		return post(url, parameter, null);
	}

	/**
	 * post 方法
	 * 
	 * @param url
	 * @param parameter
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> parameter, String charset) throws Exception
	{
		return post(url, null, parameter, charset);
	}

	/**
	 * post 方法
	 * 
	 * @param url
	 * @param parameter
	 * @param charset
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, String cookie, Map<String, Object> parameter, String charset)
			throws Exception
	{
		return getString(url, cookie, "POST", parameter, charset);
	}

	public static InputStream getPostStream(String url, Map<String, Object> parameter) throws Exception
	{
		return getPostStream(url, null, parameter, null);
	}

	public static InputStream getPostStream(String url, Map<String, Object> parameter, String charset) throws Exception
	{
		return getPostStream(url, null, parameter, charset);
	}

	public static InputStream getPostStream(String url, String cookie, Map<String, Object> parameter, String charset)
			throws Exception
	{
		return getInputStream(url, cookie, "POST", parameter, charset);
	}

	/**
	 * 添加所有请求头
	 * 
	 * @Methodname: addHeaders
	 * @Discription:
	 * @param conn
	 * @param createHeaders
	 * @Author HH
	 * @Time 2012-11-18 下午11:55:00
	 */
	private static void addHeaders(URLConnection conn, Map<String, String> headers)
	{

		for (Map.Entry<String, String> entry : headers.entrySet())
		{
			conn.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 构造请求头
	 * 
	 * @Methodname: createHeaders
	 * @Discription:
	 * @param url
	 * @return
	 * @Author HH
	 * @Time 2012-11-18 下午11:33:21
	 */
	public static Map<String, String> createHeaders(URL url)
	{
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", url.getHost());
		header.put("Connection", "Keep-Alive");
		// header.put("Connection", "close");
		header.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.96 Safari/537.4");
		header.put("Content-type", "application/x-www-form-urlencoded");
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		header.put("Referer", url.getHost());
		header.put("Accept-Encoding", "gzip");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		header.put("Cache-Control", "max-age=0");
		return header;
	}

	/**
	 * 解压缩，支持gzip，deflate格式
	 * 
	 * @Methodname: unCompress
	 * @Discription:
	 * @param in
	 * @param encoding gzip或deflate
	 * @return
	 * @throws Exception
	 * @Author HH
	 * @Time 2012-11-18 下午11:37:25
	 */
	private static InputStream unCompress(InputStream in, String encoding) throws Exception
	{
		if (in == null)
		{
			return null;
		}
		if ("gzip".equalsIgnoreCase(encoding))
		{
			in = new GZIPInputStream(in);
		}
		return in;
	}

	public static void main(String[] args)
	{
		try
		{
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("aa", "中国人民");
			// map.put("fddd", "sssssss");
			// map.put("eee", 4444);
			// System.out.println(getParameterString(map, "utf-8"));

//			String res = ClientUtil.get("http://www.oschina.net");
			
			 String res=ClientUtil.getAndSaveFile("http://2a.zol-img.com.cn/product/45_800x600/934/ceGNEB5Qo4be.jpg", null, "d:\\zolpic", "ss", null);
			// res=HttpClientUtil.get("http://static.oschina.net/uploads/user/64/129733_50.jpg");
			// String url="http://www.rr12580.com:2010/admin/goods_so.action";
			// String
			// cookie="CNZZDATA2172344=cnzz_eid=70999058-1337766843-&ntime=1343285430&cnzz_a=7&retime=1343289948427&sin=&ltime=1343289948427&rtime=4; JSESSIONID=83FDB559BE97AAC89729C81C2034D5DB";
			// Map<String,String> pa=new HashMap<String, String>();
			// pa.put("key", "手机");
			// pa.put("sel", "0");
			// String res=post(url, pa,"gbk",cookie);
			System.out.println(res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
