package com.commonsnet.hsiao.quartz.job;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.commonsnet.hsiao.utils.SendMail;


public  class PttTelnetJob  extends QuartzJobBean{
	TelnetClient telnet;
	InputStream inputStream;
	OutputStream outputStream;
	StringBuilder result;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		try {
			this.telnet = new TelnetClient();
			this.telnet.connect("ptt.cc", 23);
			this.telnet.setSoTimeout(5000);
			this.digCodeJobMetaData();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
       	  try {
       		  if(telnet.isConnected())
       			  telnet.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
	
    private synchronized void digCodeJobMetaData() throws Exception{
    	try{
            inputStream =  telnet.getInputStream();
            outputStream = telnet.getOutputStream();
       
            writeCommand("ptt_account","\r\n","ptt_pwd","\r\n"); // 輸入帳密畫面
            
            writeCommand("\r\n"); // 輸入帳密畫面後登入公告頁，任意鍵離開
           
            writeCommand("f","\r\n"); // 主目錄頁，選擇快速鍵F(我的最愛)
            
            writeCommand("5","\r\n,\r\n"); // 我的最愛目錄頁，選擇快速鍵5(CodeJob版)
            
            writeCommand("\r\n"); // (CodeJob版)公告頁，任意鍵離開
            
            
            StringBuffer parseContext = new StringBuffer();
          
            parseContext.append(result.toString());
           
            writeCommand("{PGUP}"); //上一頁
            parseContext.append(result.toString());
        
            
            String context =analyseCodeJobPageContext(parseContext.toString());
         
            SendMail.sendGmail("【"+new Date(System.currentTimeMillis()).toString()+"】 PoJen帶來PTT外包案件請笑納",context);
            System.out.println("自動讀取外包案件作業完成");
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    private String analyseCodeJobPageContext(String context ){
    	StringBuffer  bs = new StringBuffer();
    	
    	if(StringUtils.isEmpty(context)){
    		return "";
    	}
    	
    	try {
    		for(String s :StringUtils.substringsBetween(context, "□", "[1")){
    			
    			bs.append(s);
        		bs.append("\r\n");
        	}

        	/**
        	 * cursor line 
        	 */
//        	for(String s :StringUtils.substringsBetween(context, "● ", "[K")){
//        		bs.append(s);
//        		bs.append("\r\n");
//        	}
		} catch (Exception e) {
			return e.toString();
		}
    	return bs.toString();
    }
    
    private void writeCommand(String ... strings){
	      try {
	    	  for(String str : strings){
	    		  outputStream.write(str.getBytes());  
	    	  }
		      outputStream.flush();
		      readResponse(inputStream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    private void readResponse(InputStream instream) throws IOException {
    	 result = new StringBuilder();
    	BufferedInputStream bis = new BufferedInputStream(instream, 1024);
        while (bis.available() == 0){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        byte[] buffer = new byte[1024];
        while (bis.available() > 0){
            int count = inputStream.read(buffer);
            result.append(new String(buffer, 0, count));
        }
        IOUtils.write(result, System.out, "BIG5");
    }
}


