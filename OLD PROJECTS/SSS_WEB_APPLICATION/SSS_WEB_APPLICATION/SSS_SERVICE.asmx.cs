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

		[WebMethod]
		public string INSERT_IMAGE(int user_ID,string image_Path)
		{
			SSS_DATA_CLASSES_LINQDataContext linq = new SSS_DATA_CLASSES_LINQDataContext();
			Image image_Table = new Image();
			image_Table.User_ID = user_ID;
			image_Table.Image_Path = image_Path;
			linq.Images.InsertOnSubmit(image_Table);
			linq.SubmitChanges();

			return "true";
		}
	}
}
