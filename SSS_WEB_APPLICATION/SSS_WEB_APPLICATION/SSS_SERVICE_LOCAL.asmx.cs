using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using SSS_WEB_APPLICATION.SSS_SERVICE;

namespace SSS_WEB_APPLICATION
{
		/// <summary>
	/// Summary description for SSS_SERVICE_LOCAL
	/// </summary>
	[WebService(Namespace = "http://tempuri.org/")]
	[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
	[System.ComponentModel.ToolboxItem(false)]
	// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
	[System.Web.Script.Services.ScriptService]
	public class SSS_SERVICE_LOCAL : System.Web.Services.WebService
	{
		private string ServerTrainDataUrl = "http://smartphonesecuritysystem.dedicated.co.za:8080/SSS_JAVA_SERVER/data/MATLAB_TRAIN_DATA/";

		private SSS_SERVICE.SSS_SERVICE remote_service = new SSS_SERVICE.SSS_SERVICE();
		[WebMethod]
		public List<Device_Coordinate> RETURN_DEVICE_COORDINATE_JS(string Device_Mac) {
			
			return remote_service.RETURN_DEVICE_COORDINATE_JS(Device_Mac).ToList();
		}
		[WebMethod]
		public string RING_DEVICE(string body, string From_num, string to_num)
		{
			return remote_service.SEND_SMS(body, From_num, to_num);
		}
		[WebMethod]
		public string STOP_RINGING_DEVICE(string body, string From_num, string to_num)
		{
			return remote_service.SEND_SMS(body, From_num, to_num);
		}
		[WebMethod]
		public string UPLOAD_NEWS_FEED_IMAGE(string title, string description, string readMoreLink, byte[] titleImageContents, string titleImageName, byte[] ImageContents, string ImageFilename)
		{
			remote_service.UPLOAD_NEWS_FEED_IMAGE(title, description, readMoreLink,titleImageContents,  titleImageName, ImageContents, ImageFilename);
			return "true";
		}
		[WebMethod]
		public string UPLOAD_NEWS_FEED_VIDEO(string title, string description, string readMoreLink, byte[] titleImageContents, string titleImageName, byte[] VideoContents, string VideoFilename)
		{
			remote_service.UPLOAD_NEWS_FEED_VIDEO(title, description, readMoreLink, titleImageContents, titleImageName, VideoContents, VideoFilename);
			return "true";
		}
		[WebMethod]
		public List<Prediction_Image> RETURN_PREDICTION_IMAGES(string User_ID,string User_Name)
		{
			List<Prediction_Image> images = remote_service.RETURN_PREDICTION_IMAGES_WEB(User_ID).ToList();
			for(int i = 0; i < images.Count; i++)
			{
				images.ElementAt(i).Image_Path = ServerTrainDataUrl + User_Name + "/MATLAB_PRED_DATA/" + images.ElementAt(i).Image_Path + ".jpg";
			}
			return images;
		}
		[WebMethod]
		public List<Image> RETURN_TRAINING_IMAGES(string User_ID, string User_Name)
		{
			List<Image> images = remote_service.RETURN_TRAINING_IMAGES_WEB(User_ID).ToList();
			for (int i = 0; i < images.Count; i++)
			{
				images.ElementAt(i).Image_Path = ServerTrainDataUrl + User_Name + "/" + images.ElementAt(i).Image_Path;
			}
			return images;
		}
		[WebMethod]
		public User LOGIN(string username, string password)
		{
			return remote_service.LOGIN_WEB(username, password);
		}
		[WebMethod]
		public User RETURN_USER_WITH_ID(string User_ID)
		{
			return remote_service.RETURN_USER_WITH_ID_WEB(User_ID);
		}
		[WebMethod]
		public Devices_Mac RETURN_DEVICE_WITH_USER_ID(string User_ID)
		{
			return remote_service.RETURN_DEVICE_WITH_USER_ID(User_ID);
		}
	}
}
