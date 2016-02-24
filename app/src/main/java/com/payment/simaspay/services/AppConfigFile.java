package com.payment.simaspay.services;



public class AppConfigFile {

	/**
	 * @Pramod J
	 */

	/**********************************************************************
	 * App Details *
	 **********************************************************************/


	public static String appOS = "Android";

	/**********************************************************************
	 * App Details *
	 **********************************************************************/
	public static String tcUrl = "http://www.uangku.co.id/tnc.html";

	/**********************************************************************
	 * KYC Details *
	 **********************************************************************/

	// Production
	public static String kycServerUrl = "https://uangku.smartfren.com/kycgateway/receive.aspx?";

	// UAT
	// public static String kycServerUrl =
	// "https://staging.dimo.co.id/kycgateway/receive.aspx?";

	// UAT-User Name
	// public static String userName = "albo.gitananda@gmail.com";

	// Production- User Name
	public static String userName = "help@uangku.co.id";

	/**********************************************************************
	 * MFS Sever Details *
	 **********************************************************************/

	// SmartFren Dev

	// public static String requestUrl =
	// "https://uangku.smartfren.com/dev_swebapi/sdynamic";
	// public static String webAPIUrlFiles =
	// "https://uangku.smartfren.com/dev_swebapi/sdynamic";
	// public static String webAPIUrl =
	// "https://uangku.smartfren.com/dev_swebapi/sdynamic";
	// public static String pfdDownLoadURL =
	// "https://uangku.smartfren.com/dev_swebapi/";
	// public static String promoImageURLPath = "https://uangku.smartfren.com/";

	// public static String requestUrl =
	// "https://uat.uangku.co.id/swebapi/sdynamic";
	// public static String webAPIUrlFiles =
	// "https://uat.uangku.co.id/swebapi/sdynamic";
	// public static String webAPIUrl =
	// "https://uat.uangku.co.id/swebapi/sdynamic";
	// public static String pfdDownLoadURL =
	// "https://uat.uangku.co.id/swebapi/";
	// public static String promoImageURLPath = "https://uat.uangku.co.id/";
	
	
//	UAT
		 /*public static String requestUrl="https://uat.uangku.co.id/swebapi/sdynamic"; 
		 public static String webAPIUrlFiles="http://uat.uangku.co.id/webapi/dynamic"; 
		 public static String webAPIUrl ="http://uat.uangku.co.id/webapi/dynamic"; 
		 public static String pfdDownLoadURL = "http://uat.uangku.co.id/webapi/"; 
		 public static String promoImageURLPath = "http://uat.uangku.co.id/";*/
	 /*public static String requestUrl="https://kyc.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String webAPIUrlFiles="https://kyc.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String webAPIUrl ="https://kyc.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String pfdDownLoadURL = "https://kyc.uangku.co.id/dev_swebapi/webapi/"; 
	 public static String promoImageURLPath = "https://kyc.uangku.co.id/";*/
	
	/*public static String requestUrl="https://uat.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String webAPIUrlFiles="https://uat.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String webAPIUrl ="https://uat.uangku.co.id/dev_swebapi/sdynamic"; 
	 public static String pfdDownLoadURL = "https://uat.uangku.co.id/dev_swebapi/"; 
	 public static String promoImageURLPath = "https://uat.uangku.co.id/";*/
	
	// SmartFren Production

	  /*public static String requestUrl="https://uangku.smartfren.com/swebapi30/sdynamic"; 
	  public static String webAPIUrlFiles ="https://uangku.smartfren.com/swebapi30/sdynamic"; 
	  public static String webAPIUrl = "https://uangku.smartfren.com/swebapi30/sdynamic"; 
	  public static String pfdDownLoadURL = "https://uangku.smartfren.com/swebapi30/";
	  public static String promoImageURLPath = "https://uangku.smartfren.com/";*/
	 

	// Staging

	/*
	 * public static String
	 * requestUrl="https://uangkustaging.uangku.co.id/webapi/sdynamic"; public
	 * static String webAPIUrlFiles
	 * ="http://uangkustaging.uangku.co.id/webapi/dynamic"; public static String
	 * webAPIUrl = "https://uangkustaging.uangku.co.id/webapi/sdynamic"; public
	 * static String pfdDownLoadURL =
	 * "http://uangkustaging.uangku.co.id/webapi/"; public static String
	 * promoImageURLPath = "http://uangkustaging.uangku.co.id/";
	 */
	// India server for Uangku RAND

	/*public static String requestUrl = "https://175.101.5.67:8443/webapi/sdynamic";
	public static String webAPIUrlFiles = "http://175.101.5.67:8080/webapi/dynamic";
	public static String webAPIUrl = "https://175.101.5.67:8443/webapi/sdynamic";
	public static String pfdDownLoadURL = "http://175.101.5.67:8080/webapi/";
	public static String promoImageURLPath = "http://175.101.5.67:8080/"; */
	
	
	// Multi Tenant QA Team

	public static String requestUrl = "https://175.101.5.70:8444/webapi/sdynamic";
	public static String webAPIUrlFiles = "https://175.101.5.70:8444/webapi/sdynamic";
	public static String webAPIUrl = "https://175.101.5.70:8444/webapi/sdynamic";
	public static String pfdDownLoadURL = "https://175.101.5.70:8444/webapi/";
	public static String promoImageURLPath = "http://175.101.5.70:8443/";
	
	
	// Multi Tenant Dev Team

		/*public static String requestUrl = "https://175.101.5.74:8443/webapi/sdynamic";
		public static String webAPIUrlFiles = "https://175.101.5.74:8443/webapi/sdynamic";
		public static String webAPIUrl = "https://175.101.5.74:8443/webapi/sdynamic";
		public static String pfdDownLoadURL = "https://175.101.5.74:8443/webapi/";
		public static String promoImageURLPath = "http://175.101.5.74:8080/";*/

	// Bala System.
	/*
	 * public static String requestUrl =
	 * "https://192.168.2.24:8443/webapi/sdynamic"; public static String
	 * webAPIUrlFiles = "http://192.168.2.24:8080/webapi/dynamic"; public static
	 * String webAPIUrl = "https://192.168.2.24:8443/webapi/sdynamic"; public
	 * static String pfdDownLoadURL = "http://192.168.2.24:8080/webapi/"; public
	 * static String promoImageURLPath = "http://192.168.2.24:8080/";
	 */

	// India server for Uangku Venkat

	/*
	 * public static String requestUrl =
	 * "https://192.168.2.19:8444/webapi/sdynamic"; public static String
	 * webAPIUrlFiles = "http://192.168.2.19:8081/webapi/dynamic"; public static
	 * String webAPIUrl = "https://192.168.2.19:8444/webapi/sdynamic"; public
	 * static String pfdDownLoadURL = "http://192.168.2.19:8081/webapi/"; public
	 * static String promoImageURLPath = "http://192.168.2.19:8081/";
	 */

	/**********************************************************************
	 * Flashiz Server Details *
	 **********************************************************************/

	// SandBox
	// public static String flashizURL =
	// FlashizUrlBuilder.BANK_SDK_SERVER_SANDBOX_URL;

//	 Production
//	 public static String flashizURL =
//	 FlashizUrlBuilder.BANK_SDK_SERVER_PROD_URL;

	// Dev
	// public static String flashizURL = "https://dev.flashiz.co.id";

	// UAT
	// public static String flashizURL = "https://uat.flashiz.co.id";

}
