using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

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
		private string Matlab_Path = "C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/";
		private MLApp.MLApp matlab = new MLApp.MLApp();
		private SSS_LINQ_DataContext linq = new SSS_LINQ_DataContext();

		[WebMethod]
		public string INSERT_IMAGE(int user_ID, string image_Path)
		{
			
			Image image_Table = new Image();
			image_Table.User_ID = user_ID;
			image_Table.Image_Path = image_Path;
			linq.Images.InsertOnSubmit(image_Table);
			linq.SubmitChanges();

			return "true";
		}

		//remember to change image name to be an actual image using binary
		[WebMethod]
		public string UPDATE_TRAIN_DATA(int user_ID,string image_Name)
		{

			matlab.PutWorkspaceData("image_Path", "base", image_Name);
			matlab.PutWorkspaceData("user_ID", "base", user_ID);

			string ML_features_Database = Matlab_Path + "MATLAB_TRAIN_DATA/IMAGES_FEATURES.mat";
			matlab.PutWorkspaceData("path", "base", ML_features_Database);
			return matlab.Execute("run('C:/Users/isaia/PJE4A_PROJECT/SSS_WEB_SERVICE/SSS_WEB_SERVICE/Update_Train_Data.m')");
			//return matlab.Execute("new_File");

		}
	}
}
