package io.jpress.code.generator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.print.attribute.standard.DateTimeAtCompleted;

import net.sf.ehcache.store.disk.ods.AATreeSet;
import io.jpress.jp.utils.DateUtils;
import io.jpress.model.Attachment;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String flag=DateUtils.date("yyyy年MM月");
//		System.err.println(flag);
//		
//		String [] a={"2","34","56","34","23","45"};
//		BigDecimal totalAmount = new BigDecimal("0");
//		for (int i = 0; i < a.length; i++) {
//			totalAmount = totalAmount.add(new BigDecimal(a[i]));
//			System.err.println("totalAmount--->"+totalAmount);
//		}
//		System.err.println(totalAmount);


//		DecimalFormat decFormat = new DecimalFormat("#.0000");
//		NumberFormat percent = NumberFormat.getPercentInstance();
//		 percent.setMaximumFractionDigits(3);
//		BigDecimal taxff =new BigDecimal("555.5500");
//		BigDecimal taxoo = new BigDecimal("34");
////		BigDecimal tax=new BigDecimal("0");
//		BigDecimal tax=new BigDecimal(decFormat.format(taxoo)).divide(taxff,4);
//		percent.format(tax);
//		System.err.println(tax+"######"+percent.format(tax));

		/**
		 * map 测试
		 */
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("aa", "123");
//		map.put("bb", "1e3");
//		map.put("aa", "12@@43");
//		map.put("cc", "12##3");
//		System.err.println(map);
//		

		/**
		 *日期转换
		 */
//		String mdate="2016-06";
//		mdate=	mdate.replace("-","年")+"月";
//		
//		System.err.println(mdate);

//		String c ="2008-04-08"; //截止日期
//		String s ="2006-09-08"; //初始化日期
//		String d ="2007-03-03"; //当前日期
//		
//		s.substring(0, 4);
//		System.err.println(s.substring(0, 4)+"###"+s.substring(5, 7));
//		
//		int dly=Integer.parseInt(c.toString().substring(0, 4));
//		int dlm=Integer.parseInt(c.toString().substring(5, 7));
//		int dy=Integer.parseInt(s.toString().substring(0, 4));
//		int dm=Integer.parseInt(s.toString().substring(5, 7));
//		
//		int dcy=Integer.parseInt(d.substring(0, 4));
//		int dcm=Integer.parseInt(d.substring(5, 7));
//		
//		int totalMonth=(dly-dy)*12+(dlm-dm);
//		int dcmonth = (dcy-dy)*12+(dcm-dm);
//		System.err.println(dcmonth+"totalMonth-->"+totalMonth);
//		int times =totalMonth/3;
//		for(int i=1;i<=times;i++){
//			if(dcmonth==(i*3)){
//				System.err.println("交钱 交钱");
//				break;
//			}
//		}


		/**
		 * 日期比较大小
		 */

//		String myString = "2008-09-08";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
//		Date d;
//		try {
//			d = sdf.parse(myString);
//			boolean flag = d.before(new Date());
//			if(flag)
//			System.out.print("早于今天");
//			else
//			System.out.print("晚于今天");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


//		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//	        try {
//	        	long DateTime = System.currentTimeMillis(); 
//	            Date dt1 = df.parse("1995-11-12 15:21");
//	            Date dt2 = df.parse("1999-12-11 09:59");
//	            System.err.println(df+"&&&"+dt1);
//	            System.err.println(dt1.getTime()+ "#######"+ dt2.getTime()+"***"+DateTime);
//	            if (dt1.getTime() > dt2.getTime()) {
//	                System.out.println("dt1 在dt2前");
//	                
//	            } else if (dt1.getTime() < dt2.getTime()) {
//	                System.out.println("dt1在dt2后");
//	               
//	            } else {
//	            }
//	        } catch (Exception exception) {
//	            exception.printStackTrace();
//	        }

		/**
		 *
		 */
		List<Integer> aa = new ArrayList<Integer>();
		System.err.println(aa);
		System.err.println(aa.size() + "###");
		if (aa.size() > 0) {
			System.err.println(aa.size() + "###");
		}

		String[] a = {""};
		System.out.println(a.length + "**" + a + "***" + a[0]);
		if (a == null || a.length == 0 ) {
			System.out.println("33333");
		}

	}
}