package wu.a.lib.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.util.Log;

public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, File file, String user,
			String price, String cate) {
		String charset = "utf-8";
		// PrintWriter out = null;
		String enctype = "multipart/form-data";

		BufferedReader in = null;
		DataOutputStream dos = null;
		String result = "";
		try {
			URL realUrl = new URL(url);

			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			conn.setRequestMethod("POST");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 设置通用的请求属性
			conn.setRequestProperty("Charset", charset);
			// conn.setRequestProperty("accept", "*/*");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.setRequestProperty("user-agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 自定义参数
			conn.setRequestProperty("combined", file.getName());
			conn.setRequestProperty("user", user);
			conn.setRequestProperty("price", price);
			conn.setRequestProperty("cate", cate);
			conn.setRequestProperty("enctype", enctype);

			Log.d("post", "set post");
			conn.connect();

			Log.d("post", "connect");
			// write file
			dos = new DataOutputStream(conn.getOutputStream());

			// for (File file : files) {
			// String fileBody = endline +
			// "Content-Disposition: form-data;name=\"Filedata\";filename=\"" +
			// file.getName() + "\"\r\n";
			// fileBody += "Content-Type: image/jpeg\r\n\r\n";
			// System.out.println(fileBody);
			// out.write(fileBody.getBytes());
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer, 0, 1024)) != -1) {
				dos.write(buffer, 0, len);
				Log.d("post_file", "post_file:" + len);
			}
			// dos.write("\r\n".getBytes());
			// }
			dos.flush();
			dos.close();
			// in = new BufferedReader(new
			// InputStreamReader(conn.getInputStream(), charset));
			// String tmp;
			// while ((tmp = in.readLine()) != null) {
			// Log.d("post_file","post_file:"+tmp);
			// System.out.println(tmp);
			// }
			//
			// in.close();
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void postFile(String url, File file, String themName,
			String userName, String price, String cate)
			throws FileNotFoundException {
		AjaxParams params = new AjaxParams();
		params.put("user", userName);
		params.put("price", price);
		params.put("cate", cate);
		params.put("name", themName);
		params.put("combined", file); // 上传文件

		FinalHttp fh = new FinalHttp();
		fh.post(url, params, new AjaxCallBack() {
			@Override
			public void onLoading(long count, long current) {
				Log.d("post", current + "/" + count);
			}

			public void onSuccess(String t) {
				Log.d("post", t == null ? "null" : t);
			}
		});
	}

	public static void httpDownload(String url, String dest) {
		FinalHttp fh = new FinalHttp();
		// 调用download方法开始下载
		HttpHandler handler = fh.download(url, // 这里是下载的路径
				dest, // 这是保存到本地的路径
				true,// true:断点续传 false:不断点续传（全新下载）
				new AjaxCallBack() {
					@Override
					public void onLoading(long count, long current) {
						Log.d("post", "下载进度：" + current + "/" + count);
					}

					public void onSuccess(File t) {
						Log.d("post", t == null ? "null" : t.getAbsoluteFile()
								.toString());
					}

				});

		// 调用stop()方法停止下载
		handler.stop();
	}
}