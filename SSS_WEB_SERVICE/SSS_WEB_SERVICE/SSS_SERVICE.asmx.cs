using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Services;
using MLApp;
using MathWorks.MATLAB.NET.Arrays;
using MathWorks.MATLAB.NET.Utility;
using myIntegrand;
using System;
using System.Net;
using System.Net.Sockets;

namespace SSS_WEB_SERVICE
{
	/// <summary>
	/// Summary description for SSS_SERVICE
	/// </summary>
	[WebService(Namespace = "http://tempuri.org/")]
	[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
	[System.ComponentModel.ToolboxItem(false)]
	// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
	// [System.Web.Script.Services.ScriptService]
	public class SSS_SERVICE : System.Web.Services.WebService
	{

		public string Matlab_Path = "C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/";
		//public MLApp.MLApp matlab = new MLApp.MLApp();


		public SSS_LINQ_DataContext linq = new SSS_LINQ_DataContext();



		[WebMethod]
		public Boolean INSERT_IMAGE(int user_ID, string image_Path)
		{

			Image image_Table = new Image();
			image_Table.User_ID = user_ID;
			image_Table.Image_Path = image_Path;
			linq.Images.InsertOnSubmit(image_Table);
			linq.SubmitChanges();

			return true;

		}




















		[WebMethod]
		public string  CONTACT_JAVA_SERVER()
		{


			UdpClient udpClient = new UdpClient();
			IPEndPoint ep = new IPEndPoint(IPAddress.Parse("192.168.0.137"), 9876);
			udpClient.Connect(ep);
			Byte[] senddata = System.Text.Encoding.UTF8.GetBytes("Hello World");
			udpClient.Send(senddata, senddata.Length);


			var data = udpClient.Receive(ref ep);
			string revData =  System.Text.Encoding.UTF8.GetString(data,0,data.Length);

			return revData;
		}
	}

}
