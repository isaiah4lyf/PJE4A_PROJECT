using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace SSS_WEB_APPLICATION
{
	public partial class UploadNewsFeed : System.Web.UI.Page
	{
		protected void Page_Load(object sender, EventArgs e)
		{

		}

		protected void Button1_Click(object sender, EventArgs e)
		{
			if (FileUpload1.HasFile && FileUpload2.HasFile)
			{
				SSS_SERVICE_LOCAL localService = new SSS_SERVICE_LOCAL();
				localService.UPLOAD_NEWS_FEED_IMAGE(title.Text.ToString(),Description.Text.ToString(), readMoreLink.Text.ToString(),FileUpload2.FileBytes,FileUpload2.FileName, FileUpload1.FileBytes, FileUpload1.FileName);
			}
			else
			{

			}
		}
	}
}