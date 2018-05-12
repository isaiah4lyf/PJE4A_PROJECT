using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_SERVICE
{
	public partial class Index : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{

		}
		protected void Button1_Click(object sender, EventArgs e)
		{
			//string image_Path = Server.MapPath(FileUpload1.FileName);
			string image_Path = FileUpload1.PostedFile.FileName;
			SSS_SERVICE service = new SSS_SERVICE();

			//Label1.Text = service.TRAIN_IMAGES_MODEL();
			//Label1.Text = service.INSERT_IMAGE(0, image_Path);
			Label1.Text = service.PREDICT_USER(image_Path);
		}
	}
}