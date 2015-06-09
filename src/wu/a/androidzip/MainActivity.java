package wu.a.androidzip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import wu.a.lib.file.FileUtils;
import wu.a.lib.file.ZipUtils;
import wu.a.lib.utils.HttpRequest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
	public static final String TAG=MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	public void onClick(View v){
		File root =FileUtils.getSdcardForWrite();
		Log.d(TAG,root.getAbsolutePath());
		File[]files=root.listFiles();
		Log.d(TAG,"file count="+files.length);
		Log.d(TAG,getFileInfo());
		File at=new File(root,"meta-info.xml");
		if(!at.exists()){
			try {
				at.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File them_definal_a=new File(root,"them_definal_a");
		File zip=new File(root,"them_definal_a.zip");
		switch(v.getId()){
		case R.id.button1:
			zipFile(zip,them_definal_a.listFiles());
			break;
		case R.id.button2:
			Log.d("post","onclick");
			postFile(zip);
			break;
		}
	}
	
	public String getFileInfo(){
		StringBuilder sb=new StringBuilder();
		sb.append("FileUtils.getAvailableMemory();=");
		sb.append(FileUtils.formatFileSize(FileUtils.getAvailableMemory(this),false));
		sb.append('\n');
		
		sb.append("FileUtils.externalMemoryAvailable();=");
		sb.append(FileUtils.externalMemoryAvailable());
		sb.append('\n');

		sb.append("getSdcardForWrite()=");
		sb.append(FileUtils.getSdcardForWrite());
		sb.append('\n');
		
		sb.append("getSdcardForRead()=");
		sb.append(FileUtils.getSdcardForRead());
		sb.append('\n');
		
		sb.append("getSdcardSize()=");
		sb.append(FileUtils.formateFileSize(this,FileUtils.getSdcardSize()));
		sb.append('\n');
		
		sb.append("getSdcardFreeSize()=");
		sb.append(FileUtils.formateFileSize(this,FileUtils.getSdcardFreeSize()));
		sb.append('\n');
		
		sb.append("getSdcardAvailableSize()=");
		sb.append(FileUtils.formateFileSize(this,FileUtils.getSdcardAvailableSize()));
		sb.append('\n');
		
		sb.append("getAvailableInternalMemorySize()=");
		sb.append(FileUtils.formateFileSize(this,FileUtils.getAvailableInternalMemorySize()));
		sb.append('\n');
		
		sb.append("getTotalInternalMemorySize()=");
		sb.append(FileUtils.formateFileSize(this,FileUtils.getTotalInternalMemorySize()));
		sb.append('\n');
		
		sb.append("getAvailableExternalMemorySize()=");
		sb.append(FileUtils.formatFileSize(FileUtils.getAvailableExternalMemorySize(),false));
		sb.append('\n');
		
		return sb.toString();
	}
	
	private void zipFile(File des, File... resFiles){
		try {
			
			if(!des.exists()){
				des.createNewFile();
			}
			ZipUtils.zipFile(des, "文件压缩", resFiles);
//			ZipUtils.upZipFile(zip,root+"/zzzup");
//			ZipUtils.upZipSelectedFile(zip,root+"/zzzupb","a");
		} catch (ZipException e) {
			Log.d(TAG,e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG,e.getMessage());
			e.printStackTrace();
		}
	}
	private static final String localURL="http://192.168.1.247:9010/service/combineds/upload";
	private static final String SERVER_URL="http://ceshi001.noxus.cn:8084/service/combineds/upload";
	private void postFile(final File file){
				Log.d("post","run");
//				String sr=HttpRequest.sendPost("http://192.168.1.247:9010/service/combineds/upload/5418160f0cf2bc465a8363ba/2"/*localURL+"/1100/2"*/, file,"wjx","52aac39c0cf2a86c1c7e73fa","526f7fe70cf24fd8a418ed02");
				try {
					HttpRequest.postFile("http://192.168.1.247:9010/service/combineds/upload/5418160f0cf2bc465a8363ba/2"/*localURL+"/1100/2"*/, file,"defind_them","wjx","52aac39c0cf2a86c1c7e73fa","526f7fe70cf24fd8a418ed02");
				} catch (FileNotFoundException e) {
					Log.d("post",e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	
}
