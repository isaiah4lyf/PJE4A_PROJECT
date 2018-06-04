using System;
using System.Collections.Generic;
using System.Linq;
using System.Timers;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_SERVICE
{
	public partial class Index : System.Web.UI.Page
	{
		private static System.Timers.Timer aTimer;

		protected void Page_Load(object sender, EventArgs e)
		{

		}
		protected void Button1_Click(object sender, EventArgs e)
		{

			string image_Path = FileUpload1.PostedFile.FileName;
			SSS_SERVICE service = new SSS_SERVICE();



			string res = service.PREDICT_USER(image_Path);
			User_Name.InnerText = res;
			img.InnerHtml = "<img style='border-radius:25px;width:50%'  class='img-fluid' src='" + image_Path + "' alt=''>";

		}

	}
}